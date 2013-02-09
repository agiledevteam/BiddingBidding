package com.agileteam.biddingbidding;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	static final String AUCTION_ITEM_ID = "item-54321";
	private TextView textViewStatus;
	private View loginView;
	private View statusView;

	private ListView listView;
	private BidderListAdapter bidderList;

	private class BidderDisplayer implements BidderListener {
		@Override
		public void bidderStateChanged(Bidder bidder) {
			setProgressCircle(bidder);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);

		setProgressBarIndeterminateVisibility(false);

		loginView = findViewById(R.id.layout_login);
		statusView = findViewById(R.id.status);
		listView = (ListView) findViewById(R.id.list);
		bidderList = new BidderListAdapter(this);
		listView.setAdapter(bidderList);

		textViewStatus = (TextView) findViewById(R.id.textView_status);

		final Button buttonLogin = (Button) findViewById(R.id.button_login);
		buttonLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});
	}

	private String host() {
		return getStringFrom(R.id.editText_host);
	}

	private String id() {
		return getStringFrom(R.id.editText_id);
	}

	private String password() {
		return getStringFrom(R.id.editText_password);
	}

	private String getStringFrom(int resId) {
		EditText editText = (EditText) findViewById(resId);
		return editText.getText().toString();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private Bidder join(XMPPAuctionHouse house, String itemId)
			throws AuctionIsNotAvailable {
		Auction auction = house.auctionFor(itemId);
		Bidder bidder = new Bidder(auction);
		auction.addAuctionEventListener(bidder);
		bidder.addBidderListener(new UIThreadBidderListener(
				new BidderDisplayer()));
		bidder.join();
		return bidder;
	}

	private void setStatus(final String string) {
		textViewStatus.setText(string);
		Log.d("han", String.format("setStatus(%s)", string));
	}

	private void setProgressCircle(Bidder bidder) {
		if ((bidder.getState() == BidderState.BIDDING)
				|| (bidder.getState() == BidderState.JOINING)) {
			setProgressBarIndeterminateVisibility(true);
		} else {
			setProgressBarIndeterminateVisibility(false);
		}
	}

	private void setLoginViewEnable(boolean bSet) {
		findViewById(R.id.editText_host).setEnabled(bSet);
		findViewById(R.id.editText_id).setEnabled(bSet);
		findViewById(R.id.editText_password).setEnabled(bSet);
		findViewById(R.id.button_login).setEnabled(bSet);
	}

	private void login() {
		new JoinTask().execute(host(), id(), password());
	}

	class JoinTask extends AsyncTask<String, Void, JoinResult> {
		@Override
		protected void onPreExecute() {
			setStatus(getString(R.string.joining));
			setLoginViewEnable(false);
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected JoinResult doInBackground(String... params) {
			try {
				XMPPAuctionHouse house = XMPPAuctionHouse.login(params[0], params[1], params[2]);
				Bidder bidder = join(house, AUCTION_ITEM_ID);
				return JoinResult.ok(bidder);
			} catch (AuctionIsNotAvailable e) {
				return JoinResult.failed(getString(R.string.failed_to_join));
			} catch (AuctionIsNotAccessible e) {
				return JoinResult.failed(getString(R.string.failed_to_connect));
			} catch (AuctionLoginFailure e) {
				return JoinResult.failed(getString(R.string.failed_to_login));
			}
		}

		@Override
		protected void onPostExecute(JoinResult result) {
			if (result.ok) {
				bidderList.add(result.bidder);
				loginView.setVisibility(View.GONE);
				statusView.setVisibility(View.VISIBLE);
			} else {
				setStatus(result.message);
				setLoginViewEnable(true);
			}
			setProgressBarIndeterminateVisibility(false);
		}
	}

	private static class JoinResult {

		public boolean ok;
		public Bidder bidder;
		public String message;

		public static JoinResult ok(Bidder bidder) {
			JoinResult result = new JoinResult();
			result.ok = true;
			result.bidder = bidder;
			return result;
		}

		public static JoinResult failed(String message) {
			JoinResult result = new JoinResult();
			result.ok = false;
			result.message = message;
			return result;
		}

	}

	private class UIThreadBidderListener implements BidderListener {

		private BidderListener listener;

		public UIThreadBidderListener(BidderListener listener) {
			this.listener = listener;
		}

		@Override
		public void bidderStateChanged(final Bidder bidder) {
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					listener.bidderStateChanged(bidder);
				}
			});
		}

	}
}

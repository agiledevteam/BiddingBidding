package com.agileteam.biddingbidding;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

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
import android.widget.TextView;

public class MainActivity extends Activity {
	static final String AUCTION_ITEM_ID = "item-54321";
	private TextView textViewStatus;
	private View loginView;

	private Bidder bidder = null;

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

		loginView = (View) findViewById(R.id.layout_login);
		textViewStatus = (TextView) findViewById(R.id.textView_status);
		final Button buttonLogin = (Button) findViewById(R.id.button_login);
		setProgressBarIndeterminateVisibility(false);

		buttonLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new JoinTask().execute(host(), id(), password());
			}
		});

		final Button bidButton = (Button) findViewById(R.id.button_bid);
		bidButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bidder.bid();
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

	private Bidder join(String host, String id, String password) {
		ConnectionConfiguration config = new ConnectionConfiguration(host, 5222);
		XMPPConnection connection = new XMPPConnection(config);
		try {
			connection.connect();
			connection.login(id, password);
			Auction auction = new XMPPAuction(connection,
					MainActivity.AUCTION_ITEM_ID);
			Bidder bidder = new Bidder(auction);
			bidder.addBidderListener(new UIThreadBidderListener(
					new BidderDisplayer()));
			auction.addAuctionEventListener(bidder);
			auction.join();
			return bidder;
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return null;
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

	class JoinTask extends AsyncTask<String, Void, Bidder> {
		@Override
		protected void onPreExecute() {
			setStatus(getString(R.string.joining));
			setLoginViewEnable(false);
			setProgressBarIndeterminateVisibility(true);
		}

		@Override
		protected Bidder doInBackground(String... params) {
			Bidder bidder = join(params[0], params[1], params[2]);
			return bidder;
		}

		@Override
		protected void onPostExecute(Bidder bidder) {
			if (bidder != null) {
				BiddingItemView view = (BiddingItemView) findViewById(R.id.bidding_item_view);
				bidder.addBidderListener(new UIThreadBidderListener(view));

				MainActivity.this.bidder = bidder;
				bidder.setState(BidderState.JOINED);
				loginView.setVisibility(View.GONE);
			} else {
				setStatus(getString(R.string.failed_to_login));
				setLoginViewEnable(true);
			}
			setProgressBarIndeterminateVisibility(false);
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

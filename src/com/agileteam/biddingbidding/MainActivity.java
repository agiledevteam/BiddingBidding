package com.agileteam.biddingbidding;

import org.jivesoftware.smack.Chat;
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
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";
	private static final String AUCTION_ITEM_ID = "auction-item-54321";
	protected Chat chat;
	private Button buttonBid;
	private TextView textViewStatus;
	private TextView textViewCurrentPrice;
	private TextView textViewNextPrice;
	private ViewGroup baseLayoutView;
	private View loginView;

	private Bidder bidder = new Bidder(null, null);

	public class XMPPAuction implements Auction {

		private Chat chat;

		public XMPPAuction(Chat chat) {
			this.chat = chat;
		}

		@Override
		public void bid(int amount) {
			try {
				chat.sendMessage(String.format(BID_COMMAND_FORMAT, amount));
			} catch (XMPPException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);
		
		baseLayoutView = (ViewGroup)findViewById(R.id.layout_base);
		loginView = (View)findViewById(R.id.layout_login);
		
		buttonBid = (Button) findViewById(R.id.button_bid);
		textViewStatus = (TextView) findViewById(R.id.textView_status);
		textViewCurrentPrice = (TextView) findViewById(R.id.textView_currentPrice);
		textViewNextPrice = (TextView) findViewById(R.id.textView_nextPrice);
		final Button joinButton = (Button) findViewById(R.id.button_join_auction);
		setProgressBarIndeterminateVisibility(false);
		
		joinButton.setOnClickListener(new OnClickListener() {
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

	private void join(String host, String id, String password) {
		ConnectionConfiguration config = new ConnectionConfiguration(host, 5222);
		XMPPConnection connection = new XMPPConnection(config);
		try {
			connection.connect();
			connection.login(makeXMPPID(id), password);
			MainActivity.this.chat = connection.getChatManager().createChat(
					makeXMPPID(AUCTION_ITEM_ID), null);

			bidder = new Bidder(new BidderListener() {
				@Override
				public void bidderStateChanged(Bidder bidder) {
					switch (bidder.getState()) {
					case LOSING:
						setStatus(getString(R.string.losing), bidder);
						break;
					case JOINED:
						setStatus(getString(R.string.joined), bidder);
						break;
					case BIDDING:
						setStatus(getString(R.string.bidding), bidder);
						break;
					case WINNING:
						setStatus(getString(R.string.winning), bidder);
						break;
					case WON:
						setStatus(getString(R.string.won), bidder);
						break;
					case LOST:
						setStatus(getString(R.string.lost), bidder);
						break;
					default:
						throw new RuntimeException(
								"Defects - wrong BidderState");
					}
				}
			}, new XMPPAuction(chat));
			chat.addMessageListener(new AuctionMessageTranslator(id, bidder));
			chat.sendMessage(JOIN_COMMAND_FORMAT);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	private String makeXMPPID(String id) {
		return id + "@localhost";
	}

	private void setStatus(final String string, final Bidder bidder) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textViewStatus.setText(string);
				textViewCurrentPrice.setText(Integer.toString(bidder
						.getCurrentPrice()));
				textViewNextPrice.setText(Integer.toString(bidder
						.getNextPrice()));
				Log.d("han", String.format("setStatus(%s, %d, %d)", string,
						bidder.getCurrentPrice(), bidder.getNextPrice()));
				buttonBid.setEnabled(getString(R.string.losing).equals(string));
				setProgressCircle(bidder);
			}
		});
	}

	private void setProgressCircle(Bidder bidder) {
		if((bidder.getState() == BidderState.BIDDING) || (bidder.getState() == BidderState.JOINING)){
			setProgressBarIndeterminateVisibility(true);
		}else{
			setProgressBarIndeterminateVisibility(false);
		}
	}
	
	class JoinTask extends AsyncTask<String, Void, Void> {
		@Override
		protected void onPreExecute() {
			setStatus(getString(R.string.joining), bidder);
			baseLayoutView.removeView(loginView);
		}

		@Override
		protected Void doInBackground(String... params) {
			join(params[0], params[1], params[2]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			bidder.setState(BidderState.JOINED);
		}
	}

}

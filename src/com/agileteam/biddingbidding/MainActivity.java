package com.agileteam.biddingbidding;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity implements AuctionEventListener {
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";
	private static final String AUCTION_ITEM_ID = "auction-item-54321";
	protected Chat chat;
	private TextView textViewStatus;
	private TextView textViewCurrentPrice;
	private TextView textViewNextPrice;
	private int currentPrice;
	private int nextPrice;
	private boolean winning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textViewStatus = (TextView) findViewById(R.id.textView_status);
		textViewCurrentPrice = (TextView) findViewById(R.id.textView_currentPrice);
		textViewNextPrice = (TextView) findViewById(R.id.textView_nextPrice);
		final Button joinButton = (Button) findViewById(R.id.button_join_auction);
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
				bid();
			}
		});
	}

	protected void bid() {
		try {
			chat.sendMessage(String.format(BID_COMMAND_FORMAT, nextPrice));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		setStatus(getString(R.string.bidding));
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

	@Override
	public void currentPrice(int price, int increment, PriceSource priceSource) {
		this.currentPrice = price;
		this.nextPrice = price + increment;
		this.winning = priceSource == PriceSource.FromSelf;
		if (winning) {
			enableBid(false);
			setStatus(getString(R.string.winning));
		} else {
			enableBid(true);
			setStatus(getString(R.string.losing));
		}
	}

	private void enableBid(boolean b) {
		((Button) findViewById(R.id.button_bid)).setClickable(b);
	}

	@Override
	public void auctionClosed() {
		enableBid(false);

		if (winning) {
			setStatus(getString(R.string.won));
		} else {
			setStatus(getString(R.string.lost));
		}
	}

	private void join(String host, String id, String password) {
		final MessageListener listener = new AuctionMessageTranslator(id, this);
		ConnectionConfiguration config = new ConnectionConfiguration(host, 5222);
		XMPPConnection connection = new XMPPConnection(config);
		try {
			connection.connect();
			connection.login(makeXMPPID(id), password);
			MainActivity.this.chat = connection.getChatManager().createChat(
					makeXMPPID(AUCTION_ITEM_ID), listener);
			chat.sendMessage(JOIN_COMMAND_FORMAT);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	private String makeXMPPID(String id) {
		return id + "@localhost";
	}

	public void setStatus(final String string) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				textViewStatus.setText(string);
				textViewCurrentPrice.setText(Integer.toString(currentPrice));
				textViewNextPrice.setText(Integer.toString(nextPrice));
				Log.d("han", String.format("setStatus(%s, %d, %d)", string,
						currentPrice, nextPrice));
			}
		});
	}

	class JoinTask extends AsyncTask<String, Void, Void> {
		@Override
		protected void onPreExecute() {
			setStatus(getString(R.string.joining));
		}

		@Override
		protected Void doInBackground(String... params) {
			join(params[0], params[1], params[2]);
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			setStatus(getString(R.string.joined));
		}
	}

}

package com.agileteam.biddingbidding;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import com.agileteam.biddingbidding.AuctionEventListener.PriceSource;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";
	private static final String AUCTION_ITEM_ID = "auction-item-54321";
	protected Chat chat;
	private TextView textViewStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		textViewStatus = (TextView) findViewById(R.id.textView_status);
		final Button joinButton = (Button) findViewById(R.id.button_join_auction);
		joinButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new JoinTask().execute(host(), id(), password());
			}
		});
	}

	private String host() {
		return "localhost";
	}

	private String id() {
		return getStringFrom(R.id.editText_id);
	}

	private String password() {
		return getStringFrom(R.id.editText_id);
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
		AuctionEventListener auctionEventListener = new AuctionEventListener() {
			
			@Override
			public void currentPrice(int price, int increment, PriceSource priceSource) {
				setStatus(getString(R.string.losing));
			}
			
			@Override
			public void auctionClosed() {
				setStatus(getString(R.string.lost));
			}
		};
		
		final MessageListener listener = new AuctionMessageTranslator(id, auctionEventListener);

		ConnectionConfiguration config = new ConnectionConfiguration(
				host, 5222);
		XMPPConnection connection = new XMPPConnection(config);
		try {
			connection.connect();
			connection.login(makeXMPPID(host, id), password);
			MainActivity.this.chat = connection.getChatManager().createChat(
					makeXMPPID(host, AUCTION_ITEM_ID), listener);
			chat.sendMessage(JOIN_COMMAND_FORMAT);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	private String makeXMPPID(String host, String id) {
		return id + "@" + host;
	}

	public void setStatus(String string) {
		textViewStatus.setText(string);
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

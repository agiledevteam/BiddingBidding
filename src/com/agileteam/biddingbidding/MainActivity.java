package com.agileteam.biddingbidding;

import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {
	static final String AUCTION_ITEM_ID = "item-54321";
	private TextView textViewStatus;
	private View loginView;
	private View statusView;

	private EditText editTextHost;
	private EditText editTextId;
	
	private ListView listView;
	private BidderListAdapter bidderList;

	private String hostItems[] = new String[2];
	private String idItems[] = new String[16];
	private int idImages[] = new int[16];
	private int hostSelect = 0;
	private int idSelect = 0;
	
	private class BidderDisplayer implements BidderListener {
		@Override
		public void bidderStateChanged(Bidder bidder) {
			setProgressCircle(bidder);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initItems();
				
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.activity_main);

		setProgressBarIndeterminateVisibility(false);

		loginView = findViewById(R.id.layout_login);
		statusView = findViewById(R.id.status);
		listView = (ListView) findViewById(R.id.list);

		editTextHost = (EditText) findViewById(R.id.editText_host);
		editTextId = (EditText) findViewById(R.id.editText_id);
		
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

		final Button buttonHostSelect = (Button) findViewById(R.id.button_host_select);
		buttonHostSelect.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				DialogSelectHost();
			}
			
		});

		final Button buttonIdSelect = (Button) findViewById(R.id.button_id_select);
		buttonIdSelect.setOnClickListener(new OnClickListener(){
			
			@Override
			public void onClick(View v) {
				DialogSelectId();
			}
			
		});
	}

	private void initItems() {
		hostItems[0] = getResources().getString(R.string.prefixedlocalhost);
		hostItems[1] = getResources().getString(R.string.prefixedhost);
		
		idItems[0] = getResources().getString(R.string.id_alligator);
		idItems[1] = getResources().getString(R.string.id_buffalo);
		idItems[2] = getResources().getString(R.string.id_cheetah);
		idItems[3] = getResources().getString(R.string.id_deer);
		idItems[4] = getResources().getString(R.string.id_elephant);
		idItems[5] = getResources().getString(R.string.id_frog);
		idItems[6] = getResources().getString(R.string.id_gorilla);
		idItems[7] = getResources().getString(R.string.id_hippo);
		idItems[8] = getResources().getString(R.string.id_koala);
		idItems[9] = getResources().getString(R.string.id_lion);
		idItems[10] = getResources().getString(R.string.id_moose);
		idItems[11] = getResources().getString(R.string.id_panda);
		idItems[12] = getResources().getString(R.string.id_raccoon);
		idItems[13] = getResources().getString(R.string.id_snake);
		idItems[14] = getResources().getString(R.string.id_wolf);
		idItems[15] = getResources().getString(R.string.id_zebra);
		
		idImages[0] = R.raw.alligator;
		idImages[1] = R.raw.buffalo;
		idImages[2] = R.raw.cheetah;
		idImages[3] = R.raw.deer;
		idImages[4] = R.raw.elephant;
		idImages[5] = R.raw.frog;
		idImages[6] = R.raw.gorilla;
		idImages[7] = R.raw.hippo;
		idImages[8] = R.raw.koala;
		idImages[9] = R.raw.lion;
		idImages[10] = R.raw.moose;
		idImages[11] = R.raw.panda;
		idImages[12] = R.raw.raccoon;
		idImages[13] = R.raw.snake;
		idImages[14] = R.raw.wolf;
		idImages[15] = R.raw.zebra;
		
	}

	private void DialogSelectHost() {
		setHostSelect(0);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(R.string.host_select_dialog_title);
		alertDialogBuilder
				.setSingleChoiceItems(hostItems, 0,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								setHostSelect(whichButton);
							}
						})
				.setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						editTextHost.setText(hostItems[getHostSelect()]);
					}
				})
				.setNegativeButton(R.string.cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								setHostSelect(0);
							}
						});
		alertDialogBuilder.show();
	}
	
	private void DialogSelectId() {
		setIdSelect(0);
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle(R.string.id_select_dialog_title);
		alertDialogBuilder
		.setSingleChoiceItems(idItems, 0,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				setIdSelect(whichButton);
			}
		})
		.setPositiveButton(R.string.select, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				editTextId.setText(idItems[getIdSelect()].toLowerCase(Locale.US));
			}
		})
		.setNegativeButton(R.string.cancel,
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,
					int whichButton) {
				setIdSelect(0);
			}
		});
		alertDialogBuilder.show();
	}
	
	protected int getIdSelect() {
		return idSelect;
	}

	protected void setIdSelect(int whichButton) {
		idSelect = whichButton;
	}

	protected int getHostSelect() {
		return hostSelect;
	}

	protected void setHostSelect(int whichButton) {
		hostSelect  = whichButton;
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
		bidder.setIdImage(idImages[idSelect]);
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
				loginView.setVisibility(View.GONE);
				statusView.setVisibility(View.VISIBLE);
				bidderList.add(result.bidder);
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

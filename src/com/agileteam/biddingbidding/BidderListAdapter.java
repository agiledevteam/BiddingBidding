package com.agileteam.biddingbidding;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class BidderListAdapter extends ArrayAdapter<Bidder> implements
		BidderListener {

	private String priceFormat;
	private static Handler sHandler = new Handler();

	public BidderListAdapter(Context context) {
		super(context, R.layout.bidder_layout, R.id.text_item);
		priceFormat = context.getString(R.string.price_format);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = super.getView(position, convertView, parent);
		final Bidder bidder = getItem(position);

		TextView textItem = (TextView) view.findViewById(R.id.text_item);
		textItem.setText(bidder.getItemDescription());
		// ImageView imageItem = (ImageView) view.findViewById(R.id.image_item);
		TextView textPrice = (TextView) view.findViewById(R.id.text_price);
		textPrice.setText(formatPrice(bidder.getCurrentPrice()));

		Button buttonBid = (Button) view.findViewById(R.id.button_bid);
		if (bidder.getState() == BidderState.LOSING) {
			buttonBid.setText(formatPrice(bidder.getNextPrice()));
			buttonBid.setEnabled(true);
		} else {
			buttonBid.setText(getStateString(bidder.getState()));
			buttonBid.setEnabled(false);
		}

		buttonBid.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				bidder.bid();
			}
		});
		return view;
	}

	private String getStateString(BidderState state) {
		switch (state) {
		case LOSING:
			return getContext().getString(R.string.losing);
		case JOINED:
			return getContext().getString(R.string.joined);
		case BIDDING:
			return getContext().getString(R.string.bidding);
		case WINNING:
			return getContext().getString(R.string.winning);
		case WON:
			return getContext().getString(R.string.won);
		case LOST:
			return getContext().getString(R.string.lost);
		default:
			throw new RuntimeException("Defects - " + state.toString());
		}
	}

	private String formatPrice(int price) {
		return String.format(priceFormat, price);
	}

	@Override
	public void bidderStateChanged(Bidder bidder) {
		sHandler.post(new Runnable() {
			@Override
			public void run() {
				notifyDataSetChanged();
			}
		});
	}

	@Override
	public void add(Bidder bidder) {
		super.add(bidder);
		bidder.addBidderListener(this);
	}
}

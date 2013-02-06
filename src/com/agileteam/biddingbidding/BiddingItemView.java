package com.agileteam.biddingbidding;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BiddingItemView extends RelativeLayout implements BidderListener {

	private TextView textItem;
	private ImageView imageItem;
	private Button buttonBid;
	private TextView textPrice;
	private String priceFormat;

	public BiddingItemView(Context context) {
		this(context, null);
	}

	public BiddingItemView(Context context, AttributeSet attrSet) {
		this(context, attrSet, 0);
	}

	public BiddingItemView(Context context, AttributeSet attrSet, int defStyle) {
		super(context, attrSet, defStyle);
		inflate(context, R.layout.bidder_layout, this);
		textItem = (TextView) findViewById(R.id.text_item);
		textPrice = (TextView) findViewById(R.id.text_price);
		imageItem = (ImageView) findViewById(R.id.image_item);
		buttonBid = (Button) findViewById(R.id.button_bid);
		priceFormat = context.getString(R.string.price_format);
	}

	@Override
	public void bidderStateChanged(Bidder bidder) {
		textPrice.setText(formatPrice(bidder.getCurrentPrice()));
		if (bidder.getState() == BidderState.LOSING) {
			buttonBid.setText(formatPrice(bidder.getNextPrice()));
			buttonBid.setEnabled(true);
		} else {
			buttonBid.setText(getStateString(bidder.getState()));
			buttonBid.setEnabled(false);
		}
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
			throw new RuntimeException("Defects - wrong BidderState");
		}
	}

	private String formatPrice(int price) {
		return String.format(priceFormat, price);
	}

}

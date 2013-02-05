package com.agileteam.biddingbidding;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BiddingItemView extends RelativeLayout implements BidderListener {

	private TextView textItem;
	private ImageView imageItem;
	private Button buttonCommand;
	private TextView textPrice;

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
		buttonCommand = (Button) findViewById(R.id.button_command);
	}

	@Override
	public void bidderStateChanged(Bidder bidder) {
		textPrice.setText(format(bidder.getCurrentPrice()));
		if (bidder.getState() == BidderState.LOSING) {
			buttonCommand.setText(format(bidder.getNextPrice()));
			buttonCommand.setEnabled(true);
		} else {
			buttonCommand.setText(bidder.getState().toString());
			buttonCommand.setEnabled(false);
		}
	}

	private String format(int price) {
		return String.format("₩ %,d", price);
	}

}

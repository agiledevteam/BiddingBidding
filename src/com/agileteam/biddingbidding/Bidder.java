package com.agileteam.biddingbidding;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

public class Bidder implements AuctionEventListener {

	private Auction auction;
	private List<BidderListener> listeners = new ArrayList<BidderListener>();
	private int currentPrice;
	private int nextPrice;
	private BidderState state = BidderState.JOINING;
	private int bidderImage;

	public Bidder(Auction auction) {
		this.auction = auction;
	}

	@Override
	public void auctionClosed() {
		if (state == BidderState.WINNING) {
			setState(BidderState.WON);
		} else {
			setState(BidderState.LOST);
		}
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource priceSource) {
		Log.d("yskang", "currentPrice(" + price + ", " + increment + ", " + priceSource + ")");
		this.currentPrice = price;
		this.nextPrice = price + increment;
		if (priceSource == PriceSource.FromSelf) {
			setState(BidderState.WINNING);
		} else {
			setState(BidderState.LOSING);
		}
	}

	public int getCurrentPrice() {
		return currentPrice;
	}

	public int getNextPrice() {
		return nextPrice;
	}

	private void setState(BidderState state) {
		Log.d("yskang", "setState(" + state + ")");
		this.state = state;
		notifyChanged();
	}

	public BidderState getState() {
		return state;
	}

	public void join() throws AuctionIsNotAvailable {
		auction.join();
		setState(BidderState.LOSING);
	}

	public void bid() {
		auction.bid(nextPrice);
		currentPrice = nextPrice;
		setState(BidderState.BIDDING);
	}

	public void addBidderListener(BidderListener listener) {
		listeners.add(listener);
	}

	public void removeListener(BidderListener listener) {
		listeners.remove(listener);
	}

	private void notifyChanged() {
		for (BidderListener listener : listeners) {
			listener.bidderStateChanged(this);
		}
	}

	public String getItemDescription() {
		return "TDD for the Embedded C";
	}

	public int getIdImage() {
		return bidderImage;
	}
	
	public void setIdImage(int bidderImage){
		this.bidderImage = bidderImage;
	}
}

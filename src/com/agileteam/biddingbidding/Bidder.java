package com.agileteam.biddingbidding;

import java.util.ArrayList;
import java.util.List;

public class Bidder implements AuctionEventListener {

	private Auction auction;
	private List<BidderListener> listeners = new ArrayList<BidderListener>();
	private int currentPrice;
	private int nextPrice;
	private BidderState state = BidderState.JOINING;

	public Bidder(Auction auction) {
		this.auction = auction;
	}

	@Override
	public void auctionClosed() {
		if(state == BidderState.WINNING ){
			state = BidderState.WON;
		}else if(state == BidderState.LOSING){
			state = BidderState.LOST;
		}else if(state == BidderState.BIDDING){
			state = BidderState.LOST;
		}else if(state == BidderState.JOINED){
			state = BidderState.LOST;
		}else {
			throw new RuntimeException("Defect - Wrong BidderState");
		}
		notifyChanged();
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource priceSource) {
		this.currentPrice = price;
		this.nextPrice = price + increment;
		if (priceSource == PriceSource.FromSelf) {
			this.state = BidderState.WINNING;
		} else {
			this.state = BidderState.LOSING;
		}
		notifyChanged();
	}

	public int getCurrentPrice() {
		return currentPrice;
	}

	public int getNextPrice() {
		return nextPrice;
	}

	public void setState(BidderState state){
		this.state = state; 
		notifyChanged();
	}
	
	public BidderState getState() {
		return state;
	}

	public void bid() {
		currentPrice = nextPrice;
		state = BidderState.BIDDING;
		auction.bid(nextPrice);
		notifyChanged();
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

}

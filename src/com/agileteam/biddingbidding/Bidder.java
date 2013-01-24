package com.agileteam.biddingbidding;

public class Bidder implements AuctionEventListener {

	private Auction auction;
	private BidderListener listener;
	private int currentPrice;
	private int nextPrice;
	private BidderState state = BidderState.JOINED;

	public Bidder(BidderListener listener, Auction auction) {
		this.listener = listener;
		this.auction = auction;
	}

	@Override
	public void auctionClosed() {
		// TODO Auto-generated method stub

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
		listener.bidderStateChanged(this);
	}

	public int getCurrentPrice() {
		return currentPrice;
	}

	public int getNextPrice() {
		return nextPrice;
	}

	public BidderState getState() {
		return state;
	}

	public void bid() {
		// TODO Auto-generated method stub

	}

}

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
		listener.bidderStateChanged(this);
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
		currentPrice = nextPrice;
		state = BidderState.BIDDING;
		auction.bid(nextPrice);
		listener.bidderStateChanged(this);
	}

}

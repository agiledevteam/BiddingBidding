package com.agileteam.biddingbidding;

public interface AuctionEventListener {

	public enum PriceSource{
		FromSelf, FromOtherBidder;
	};
	
	public void auctionClosed();

	public void currentPrice(int price, int increment, PriceSource priceSource);
	
}

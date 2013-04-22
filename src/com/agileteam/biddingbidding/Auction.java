package com.agileteam.biddingbidding;

public interface Auction {
	void addAuctionEventListener(AuctionEventListener listener);

	void join() throws AuctionIsNotAvailable;

	void bid(int amount);
}

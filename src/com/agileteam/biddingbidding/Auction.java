package com.agileteam.biddingbidding;

public interface Auction {
	void addAuctionEventListener(AuctionEventListener listener);

	void join();

	void bid(int amount);
}

package com.agileteam.biddingbidding;

public class AuctionIsNotAvailable extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5906199499732041175L;

	public AuctionIsNotAvailable(String itemId) {
		super("Auction(" + itemId + ") is not available.");
	}
	
}

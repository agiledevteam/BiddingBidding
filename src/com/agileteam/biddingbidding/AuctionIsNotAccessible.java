package com.agileteam.biddingbidding;

public class AuctionIsNotAccessible extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1612669574197632132L;
	
	public AuctionIsNotAccessible(String host, Exception e) {
		super(host + " is not accessible.", e);
	}
}

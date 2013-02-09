package com.agileteam.biddingbidding;

import org.jivesoftware.smack.XMPPException;

public class AuctionLoginFailure extends Exception {

	public AuctionLoginFailure(XMPPException e) {
		super(e);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1402412839197822264L;

}

package com.agileteam.biddingbidding;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuctionHouse {
	private XMPPConnection connection;

	public XMPPAuctionHouse(XMPPConnection connection) {
		this.connection = connection;
	}

	public static XMPPAuctionHouse login(String host, String id, String password)
			throws AuctionIsNotAccessible, AuctionLoginFailure {
		ConnectionConfiguration config = new ConnectionConfiguration(host, 5222);
		XMPPConnection connection = new XMPPConnection(config);
		try {
			connection.connect();
		} catch (XMPPException e) {
			throw new AuctionIsNotAccessible(host, e);
		}
		try {
			connection.login(id, password);
		} catch (XMPPException e) {
			throw new AuctionLoginFailure(e);
		}
		return new XMPPAuctionHouse(connection);
	}

	public Auction auctionFor(String itemId) throws AuctionIsNotAvailable {
		return new XMPPAuction(connection, itemId);	
	}
}

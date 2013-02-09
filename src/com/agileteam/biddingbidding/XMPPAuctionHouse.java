package com.agileteam.biddingbidding;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuctionHouse {

	private XMPPConnection connection;

	public XMPPAuctionHouse(XMPPConnection connection) {
		this.connection = connection;
	}

	public static XMPPAuctionHouse login(String host, String id, String password) {
		ConnectionConfiguration config = new ConnectionConfiguration(host, 5222);
		XMPPConnection connection = new XMPPConnection(config);
		try {
			connection.connect();
			connection.login(id, password);
			return new XMPPAuctionHouse(connection);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Auction auctionFor(String itemId) {
		return new XMPPAuction(connection, itemId);
	}

}

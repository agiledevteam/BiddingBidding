package com.agileteam.biddingbidding;

import java.util.ArrayList;
import java.util.List;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

public class XMPPAuction implements Auction, AuctionEventListener {
	/**
	 * 
	 */
	private final List<AuctionEventListener> listeners = new ArrayList<AuctionEventListener>();
	private Chat chat;
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";

	public XMPPAuction(XMPPConnection connection, String itemId) {
		this.chat = connection.getChatManager().createChat(
				makeXMPPID(itemId),
				new AuctionMessageTranslator(getId(connection), this));
	}

	private String makeXMPPID(String id) {
		return id + "@localhost";
	}

	private String getId(XMPPConnection connection) {
		return connection.getUser().split("@")[0];
	}

	@Override
	public void bid(int amount) {
		try {
			chat.sendMessage(String.format(XMPPAuction.BID_COMMAND_FORMAT, amount));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addAuctionEventListener(AuctionEventListener listener) {
		listeners.add(listener);
	}

	@Override
	public void join() {
		try {
			chat.sendMessage(XMPPAuction.JOIN_COMMAND_FORMAT);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void auctionClosed() {
		for (AuctionEventListener listener : listeners)
			listener.auctionClosed();
	}

	@Override
	public void currentPrice(int price, int increment,
			PriceSource priceSource) {
		for (AuctionEventListener listener : listeners)
			listener.currentPrice(price, increment, priceSource);
	}
}
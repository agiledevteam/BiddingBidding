package com.agileteam.biddingbidding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;

import android.util.Log;

public class XMPPAuction implements Auction, AuctionEventListener {
	/**
	 * 
	 */
	private final List<AuctionEventListener> listeners = new ArrayList<AuctionEventListener>();
	private Chat chat;
	public static final String BID_COMMAND_FORMAT = "SOLVersion: 1.1; Command: BID; Price: %d;";
	public static final String JOIN_COMMAND_FORMAT = "SOLVersion: 1.1; Command: JOIN;";
	public static final String AUCTION_RESOURCE = "Auction";
	public static final String ITEM_ID_AS_LOGIN = "auction-%s";
	public static final String AUCTION_ID_FORMAT = ITEM_ID_AS_LOGIN + "@%s/"
			+ AUCTION_RESOURCE;
	private final String itemId;

	public XMPPAuction(final XMPPConnection connection, String itemId) {
		this.itemId = itemId;
		connection.getChatManager().addChatListener(new ChatManagerListener() {

			@Override
			public void chatCreated(Chat chat, boolean arg1) {
				Log.d("han", "chat created with: " + chat.getParticipant());
				chat.addMessageListener(new AuctionMessageTranslator(
						getId(connection), XMPPAuction.this));
			}
		});
		chat = connection.getChatManager().createChat(
				auctionId(itemId, connection), null);
	}

	private String auctionId(String itemId, XMPPConnection connection) {
		return String.format(AUCTION_ID_FORMAT, itemId,
				connection.getServiceName());
	}

	private String getId(XMPPConnection connection) {
		return connection.getUser().split("@")[0];
	}

	@Override
	public void bid(int amount) {
		try {
			chat.sendMessage(String.format(XMPPAuction.BID_COMMAND_FORMAT,
					amount));
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void addAuctionEventListener(AuctionEventListener listener) {
		listeners.add(listener);
	}

	@Override
	public void join() throws AuctionIsNotAvailable {
		final CountDownLatch joined = new CountDownLatch(1);
		MessageListener listener = new MessageListener() {
			@Override
			public void processMessage(Chat arg0, Message arg1) {
				joined.countDown();
			}
		};
		chat.addMessageListener(listener);
		try {
			chat.sendMessage(XMPPAuction.JOIN_COMMAND_FORMAT);
			if (!joined.await(2, TimeUnit.SECONDS)) {
				throw new AuctionIsNotAvailable(itemId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AuctionIsNotAvailable(itemId);
		}
	}

	@Override
	public void auctionClosed() {
		for (AuctionEventListener listener : listeners)
			listener.auctionClosed();
	}

	@Override
	public void currentPrice(int price, int increment, PriceSource priceSource) {
		for (AuctionEventListener listener : listeners)
			listener.currentPrice(price, increment, priceSource);
	}
}
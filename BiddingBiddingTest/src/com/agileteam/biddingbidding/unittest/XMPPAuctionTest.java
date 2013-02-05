package com.agileteam.biddingbidding.unittest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;

import com.agileteam.biddingbidding.Auction;
import com.agileteam.biddingbidding.AuctionEventListener;
import com.agileteam.biddingbidding.XMPPAuction;
import com.agileteam.biddingbidding.AuctionEventListener.PriceSource;
import com.agileteam.biddingbidding.test.FakeAuctionServer;

import junit.framework.TestCase;

public class XMPPAuctionTest extends TestCase {
	private ConnectionConfiguration config = new ConnectionConfiguration(
			"localhost", 5222);
	private XMPPConnection connection = new XMPPConnection(config);
	private FakeAuctionServer server = new FakeAuctionServer("item-54321");

	@Override
	public void setUp() throws Exception {
		server.startSellingItem();
		connection.connect();
		connection.login("bidder-1", "bidder");
	}

	public void testReceivesEventsFromAuctionServerAfterJoining()
			throws Exception {
		CountDownLatch auctionWasClosed = new CountDownLatch(1);
		Auction auction = new XMPPAuction(connection, server.getItemId());
		auction.addAuctionEventListener(auctionEventListener(auctionWasClosed));
		auction.join();
		server.hasReceivedJoinRequestFrom("bidder-1");
		server.announceClosed();
		assertTrue("should have been closed",
				auctionWasClosed.await(2, TimeUnit.SECONDS));
	}

	private AuctionEventListener auctionEventListener(
			final CountDownLatch auctionWasClosed) {
		return new AuctionEventListener() {

			@Override
			public void currentPrice(int price, int increment,
					PriceSource priceSource) {
			}

			@Override
			public void auctionClosed() {
				auctionWasClosed.countDown();
			}
		};

	}
}

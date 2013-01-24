package com.agileteam.biddingbidding.unittest;

import junit.framework.TestCase;

import org.jmock.Expectations;
import org.jmock.Mockery;

import com.agileteam.biddingbidding.Auction;
import com.agileteam.biddingbidding.AuctionEventListener.PriceSource;
import com.agileteam.biddingbidding.Bidder;
import com.agileteam.biddingbidding.BidderListener;
import com.agileteam.biddingbidding.BidderState;

public class BidderTest extends TestCase {
	private final Mockery context = new Mockery();
	private final BidderListener listener = context.mock(BidderListener.class);
	private final Auction auction = context.mock(Auction.class);
	private final Bidder bidder = new Bidder(listener, auction);

	@Override
	public void tearDown() {
		context.assertIsSatisfied();
	}

	private void assertBidder(Bidder bidder, int currentPrice, int nextPrice,
			BidderState state) {
		assertEquals(currentPrice, bidder.getCurrentPrice());
		assertEquals(nextPrice, bidder.getNextPrice());
		assertEquals(state, bidder.getState());
	}

	public void testBidderIsInitializedWithJoinedStateAndZeroPrice() {
		assertBidder(bidder, 0, 0, BidderState.JOINED);
	}

	public void testBidderNotifiesLosingWhenReceivedCurrentPriceFromOtherBidder() {
		context.checking(new Expectations() {
			{
				oneOf(listener).bidderStateChanged(bidder);
			}
		});
		bidder.currentPrice(100, 10, PriceSource.FromOtherBidder);
		assertBidder(bidder, 100, 110, BidderState.LOSING);
	}

	public void testBidderBecomesWinningWhenReceivedCurrentPriceFromItself() {
		context.checking(new Expectations() {
			{
				ignoring(listener);
			}
		});
		bidder.currentPrice(100, 10, PriceSource.FromSelf);
		assertBidder(bidder, 100, 110, BidderState.WINNING);
	}

	public void testBidderBecomesBiddingWhenBidding() {
		context.checking(new Expectations() {
			{
				atLeast(1).of(listener).bidderStateChanged(bidder);
				oneOf(auction).bid(110);
			}
		});
		bidder.currentPrice(100, 10, PriceSource.FromOtherBidder); // LOSING
		bidder.bid(); // button click
		assertBidder(bidder, 110, 110, BidderState.BIDDING);
	}
}

package com.agileteam.biddingbidding.test;

import android.test.ActivityInstrumentationTestCase2;

import com.agileteam.biddingbidding.MainActivity;
import com.jayway.android.robotium.solo.Solo;

public class BiddingBiddingEndToEndTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private final FakeAuctionServer auction = new FakeAuctionServer(
			"item-54321");
	private ApplicationRunner application;

	public Solo solo;

	public BiddingBiddingEndToEndTest() {
		super(MainActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		application = new ApplicationRunner(solo);
	}

	public void testSingleJoinLostWithoutBidding() throws Exception {
		auction.startSellingItem();
		application.joinAuction();
		application.showsJoined();
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.BIDDER_ID);
		auction.announceClosed();
		application.showsLost(0, 0);
	}

	public void testSingleJoinBiddingButLost() throws Exception {
		auction.startSellingItem();
		application.joinAuction();
		application.showsJoined();
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.BIDDER_ID);

		auction.reportPrice(2000, 100, "Other bidder");
		application.showsLosing(2000, 2100);
		application.bid();
		application.showsBidding(2100, 2100);

		auction.hasReceivedBid(2100, ApplicationRunner.BIDDER_ID);
		
		auction.announceClosed();
		application.showsLost(2100, 2100);
	}

	public void testSingleJoinBiddingWinningWon() throws Exception {
		auction.startSellingItem();
		application.joinAuction();
		application.showsJoined();
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.BIDDER_ID);

		auction.reportPrice(2000, 100, "Other bidder");
		application.showsLosing(2000, 2100);
		application.bid();
		application.showsBidding(2100, 2100);

		auction.hasReceivedBid(2100, ApplicationRunner.BIDDER_ID);
		auction.reportPrice(2100, 100, ApplicationRunner.BIDDER_ID);
		application.showsWinning(2100, 2200);

		auction.announceClosed();
		application.showsWon(2100, 2200);
	}

	public void testSingleJoinBiddingWinningLosingWiningWOn() throws Exception {
		auction.startSellingItem();
		application.joinAuction();
		application.showsJoined();
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.BIDDER_ID);

		auction.reportPrice(2000, 100, "Other bidder");
		application.showsLosing(2000, 2100);
		application.bid();
		application.showsBidding(2100, 2100);

		auction.hasReceivedBid(2100, ApplicationRunner.BIDDER_ID);
		auction.reportPrice(2100, 100, ApplicationRunner.BIDDER_ID);
		application.showsWinning(2100, 2200);

		auction.reportPrice(2200, 100, "Another bidder");
		application.showsLosing(2200, 2300);

		application.bid();
		auction.hasReceivedBid(2300, ApplicationRunner.BIDDER_ID);
		auction.reportPrice(2300, 100, ApplicationRunner.BIDDER_ID);
		application.showsWinning(2300, 2400);

		auction.announceClosed();
		application.showsWon(2300, 2400);
	}

	public void testFailToJoinIfServerIsNotStarted() throws Exception {
		application.joinAuction();
		application.showsFailedToJoin();
	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}
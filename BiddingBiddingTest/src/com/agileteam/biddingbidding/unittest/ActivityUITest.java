package com.agileteam.biddingbidding.unittest;

import com.agileteam.biddingbidding.MainActivity;
import com.agileteam.biddingbidding.test.ApplicationRunner;
import com.agileteam.biddingbidding.test.FakeAuctionServer;
import com.jayway.android.robotium.solo.Solo;

import android.test.ActivityInstrumentationTestCase2;

public class ActivityUITest extends ActivityInstrumentationTestCase2<MainActivity> {
	FakeAuctionServer auction = new FakeAuctionServer("item-54321");

	public ActivityUITest() {
		super(MainActivity.class);
	}

	public void testBidButtonInactiveWhenStateIsNotLosing() throws Exception {
		auction.startSellingItem();
		
		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.enterText(0, FakeAuctionServer.SERVER_IP_ADDRESS);
		solo.enterText(1, "sniper");
		solo.enterText(2, "sniper");
		solo.clickOnButton("Join Auction");
		
		assertTrue(solo.getButton("Bid!").isClickable());	// default state is losing and Clickable
		
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.BIDDER_ID);
		auction.reportPrice(2000, 100, ApplicationRunner.BIDDER_ID);
		assertFalse(solo.getButton("Bid!").isClickable());

		auction.reportPrice(2000, 100, "Other Bidder");
		assertTrue(solo.getButton("Bid!").isClickable());

		auction.announceClosed();
		assertFalse(solo.getButton("Bid!").isClickable());
	}
	
	public void testBidButtonActiveWhenLosing() throws Exception {
		auction.startSellingItem();
		
		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.enterText(0, FakeAuctionServer.SERVER_IP_ADDRESS);
		solo.enterText(1, "sniper");
		solo.enterText(2, "sniper");
		solo.clickOnButton("Join Auction");
			
		auction.hasReceivedJoinRequestFrom(ApplicationRunner.BIDDER_ID);
		auction.reportPrice(2000, 100, "JYP");

		assertTrue(solo.getButton("Bid!").isClickable());
	}
}

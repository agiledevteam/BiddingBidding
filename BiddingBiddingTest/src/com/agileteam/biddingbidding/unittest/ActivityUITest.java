package com.agileteam.biddingbidding.unittest;

import android.test.ActivityInstrumentationTestCase2;

import com.agileteam.biddingbidding.MainActivity;
import com.agileteam.biddingbidding.test.ApplicationRunner;
import com.agileteam.biddingbidding.test.FakeAuctionServer;
import com.jayway.android.robotium.solo.Solo;

public class ActivityUITest extends
		ActivityInstrumentationTestCase2<MainActivity> {
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

		assertFalse(solo.getButton("Bid!").isEnabled());

		auction.hasReceivedJoinRequestFrom(ApplicationRunner.BIDDER_ID);
		auction.reportPrice(2000, 100, ApplicationRunner.BIDDER_ID);
		assertFalse(solo.getButton("Bid!").isEnabled());

		auction.reportPrice(2000, 100, "Other Bidder");
		assertTrue(solo.getButton("Bid!").isEnabled());

		auction.announceClosed();
		assertFalse(solo.getButton("Bid!").isEnabled());
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

		assertTrue(solo.getButton("Bid!").isEnabled());
	}

	public void testKeepBidButtonActiveWhenDeviceRotate() throws Exception {
		auction.startSellingItem();

		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.enterText(0, FakeAuctionServer.SERVER_IP_ADDRESS);
		solo.enterText(1, "sniper");
		solo.enterText(2, "sniper");
		solo.clickOnButton("Join Auction");

		auction.hasReceivedJoinRequestFrom(ApplicationRunner.BIDDER_ID);
		auction.reportPrice(2000, 100, "JYP");

		assertTrue(solo.getButton("Bid!").isEnabled());

		solo.setActivityOrientation(Solo.LANDSCAPE);
		solo.setActivityOrientation(Solo.PORTRAIT);

		assertTrue(solo.getButton("Bid!").isEnabled());
	}
}

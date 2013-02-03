package com.agileteam.biddingbidding.unittest;

import java.util.ArrayList;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

import com.agileteam.biddingbidding.MainActivity;
import com.agileteam.biddingbidding.R;
import com.agileteam.biddingbidding.test.ApplicationRunner;
import com.agileteam.biddingbidding.test.FakeAuctionServer;
import com.jayway.android.robotium.solo.Solo;

public class ActivityUITest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	FakeAuctionServer auction = new FakeAuctionServer("item-54321");
	private Solo solo;

	public ActivityUITest() {
		super(MainActivity.class);
	}
	@Override
	public void setUp() {
		solo = new Solo(getInstrumentation(), getActivity());
	}

	public void testBidButtonInactiveWhenStateIsNotLosing() throws Exception {
		auction.startSellingItem();

		soloJoin(FakeAuctionServer.SERVER_IP_ADDRESS, "sniper", "sniper");

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

		soloJoin(FakeAuctionServer.SERVER_IP_ADDRESS, "sniper", "sniper");

		auction.hasReceivedJoinRequestFrom(ApplicationRunner.BIDDER_ID);
		auction.reportPrice(2000, 100, "JYP");

		assertTrue(solo.getButton("Bid!").isEnabled());
	}

	public void testKeepBidButtonActiveWhenDeviceRotate() throws Exception {
		auction.startSellingItem();

		soloJoin(FakeAuctionServer.SERVER_IP_ADDRESS, "sniper", "sniper");

		auction.hasReceivedJoinRequestFrom(ApplicationRunner.BIDDER_ID);
		auction.reportPrice(2000, 100, "JYP");

		assertTrue(solo.getButton("Bid!").isEnabled());

		solo.setActivityOrientation(Solo.LANDSCAPE);
		solo.setActivityOrientation(Solo.PORTRAIT);

		assertTrue(solo.getButton("Bid!").isEnabled());
	}
	
	public void testRemoveLoginViewAfterJoinClicked() throws Exception{
		ArrayList<View> views = new ArrayList<View>();
		View loginView = solo.getView(R.id.layout_login);

		auction.startSellingItem();
		
		soloJoin(FakeAuctionServer.SERVER_IP_ADDRESS, "sniper", "sniper");
		
		views = solo.getCurrentViews();
		assertEquals("Login layout found!", -1, views.indexOf(loginView));
	}
	
	private void soloJoin(String server, String bidderId, String password) {
		solo.enterText(0, server);
		solo.enterText(1, bidderId);
		solo.enterText(2, password);
		solo.clickOnButton("Join Auction");
	}
	
	@Override
	public void tearDown() {
		auction.stop();
		solo.finishOpenedActivities();
	}
}

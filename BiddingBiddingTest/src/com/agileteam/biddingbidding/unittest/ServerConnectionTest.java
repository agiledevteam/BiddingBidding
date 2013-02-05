package com.agileteam.biddingbidding.unittest;

import android.test.ActivityInstrumentationTestCase2;

import com.agileteam.biddingbidding.MainActivity;
import com.agileteam.biddingbidding.test.ApplicationRunner;
import com.agileteam.biddingbidding.test.FakeAuctionServer;
import com.jayway.android.robotium.solo.Solo;

public class ServerConnectionTest extends
		ActivityInstrumentationTestCase2<MainActivity> {
	private final FakeAuctionServer auction = new FakeAuctionServer(
			"item-54321");

	public ServerConnectionTest() {
		super(MainActivity.class);
	}

	public void testConnectWithIPAddress() throws Exception {
		auction.startSellingItem();

		Solo solo = new Solo(getInstrumentation(), getActivity());
		solo.enterText(0, "127.0.0.1");
		solo.enterText(1, ApplicationRunner.BIDDER_ID);
		solo.enterText(2, ApplicationRunner.BIDDER_PASSWORD);
		solo.clickOnButton("Login Auction");

		auction.hasReceivedJoinRequestFrom(ApplicationRunner.BIDDER_ID);
	}
}

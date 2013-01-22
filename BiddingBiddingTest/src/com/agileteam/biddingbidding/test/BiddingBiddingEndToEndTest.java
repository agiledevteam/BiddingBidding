package com.agileteam.biddingbidding.test;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;
import com.agileteam.biddingbidding.LoginActivity;

public class BiddingBiddingEndToEndTest extends
		ActivityInstrumentationTestCase2<LoginActivity> {
	private final FakeAuctionServer auction = new FakeAuctionServer(
			"item-54321");
	private ApplicationRunner application;

	public Solo solo;

	public BiddingBiddingEndToEndTest() {
		super(LoginActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		solo = new Solo(getInstrumentation(), getActivity());
		application = new ApplicationRunner(solo);
	}

	public void test_0_LoginAndActivityChange() throws Exception {
		application.loginToServer("sniper", "sniper");
		application.showsApplicationActivityHasChanged();
	}
	
//	public void test_1_SingleJoinLostWithoutBidding() throws Exception {
//		auction.startSellingItem();
//		application.startBiddingIn(auction);
//		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
//		auction.announceClosed();
//		application.showsSniperHasLostAuction();
//	}

//	public void test_2_SingleJoinBidAndLost() throws Exception {
//		auction.startSellingItem();
//		application.startBiddingIn(auction);
//		auction.hasReceivedJoinRequestFrom(ApplicationRunner.SNIPER_XMPP_ID);
//		auction.reportPrice(1000, 98, "other bidder");
//		application.hasShownSniperIsBidding(auction, 1000, 1098);
//		auction.hasReceivedBid(1098, ApplicationRunner.SNIPER_XMPP_ID);
//		auction.announceClosed();
//		application.showsSniperHasLostAuction();
//	}

	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
}
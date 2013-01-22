package com.agileteam.biddingbidding.test;

import com.jayway.android.robotium.solo.Solo;

public class ApplicationRunner {
	public static final String SNIPER_PASSWORD = "sniper";
	private static final String STATUS_JOINING = "joining";
	private static final String STATUS_LOST = "lost";
	private static final String STATUS_BIDDING = "bidding";
	private static final String STATUS_WINNING = "winning";
	public static final String SNIPER_XMPP_ID = "sniper";
	private static final String STATUS_WON = "won";
	private BiddingBiddingDriver driver;

	public ApplicationRunner(Solo solo) {
		driver = new BiddingBiddingDriver(solo);
	}

	public void startBiddingIn(final FakeAuctionServer auction) {
		final String itemId = auction.getItemId();
		driver.startBiddingFor(itemId);
		driver.showsSniperStatus(itemId, 0, 0, STATUS_JOINING);
	}

	public void showsSniperHasLostAuction() {
		driver.showsSniperStatus(STATUS_LOST);
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}

	public void hasShownSniperIsBidding(FakeAuctionServer auction,
			int lastPrice, int lastBid) {
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastBid,
				STATUS_BIDDING);
	}

	public void hasShownSniperIsWinning(FakeAuctionServer auction,
			int winningBid) {
		driver.showsSniperStatus(auction.getItemId(), winningBid, winningBid,
				STATUS_WINNING);
	}

	public void showsSniperHasWonAuction(FakeAuctionServer auction,
			int lastPrice) {
		driver.showsSniperStatus(auction.getItemId(), lastPrice, lastPrice,
				STATUS_WON);
	}

	public void loginToServer(String bidderId, String password) {
		driver.loginToserver(bidderId, password);
	}

	public void showsApplicationActivityHasChanged() {
		driver.showsApplicationActivityHasChanged();
	}

}

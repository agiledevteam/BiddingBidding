package com.agileteam.biddingbidding.test;

import com.jayway.android.robotium.solo.Solo;

public class ApplicationRunner {
	public static final String BIDDER_ID = "bidder-1";
	public static final String BIDDER_PASSWORD = "bidder";
	private static final String STATUS_JOINED = "Joined";
	private static final String STATUS_LOSING = "Losing";
	private static final String STATUS_BIDDING = "Bidding";
	private static final String STATUS_WINNING = "Winning";
	private static final String STATUS_LOST = "Lost";
	private static final String STATUS_WON = "Won";
	private BiddingBiddingDriver driver;

	public ApplicationRunner(Solo solo) {
		driver = new BiddingBiddingDriver(solo, 2000);
	}

	public void joinAuction() {
		driver.connectAndJoin(FakeAuctionServer.SERVER_IP_ADDRESS, BIDDER_ID,
				BIDDER_PASSWORD);
	}

	public void bid() {
		driver.bid();
	}

	public void showsLost(int currentPrice, int nextPrice) {
		driver.showsSniperStatus(STATUS_LOST, currentPrice);
	}

	public void showsJoined() {
		driver.showsSniperStatus(STATUS_JOINED, 0);
	}

	public void showsLosing(int currentPrice, int nextPrice) {
		driver.showsSniperStatus(currentPrice, nextPrice); // when losing, it shows next price
	}

	public void showsBidding(int currentPrice, int nextPrice) {
		driver.showsSniperStatus(STATUS_BIDDING, currentPrice);
	}

	public void showsWinning(int currentPrice, int nextPrice) {
		driver.showsSniperStatus(STATUS_WINNING, currentPrice);
	}

	public void showsWon(int currentPrice, int nextPrice) {
		driver.showsSniperStatus(STATUS_WON, currentPrice);
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}

	public void showsFailedToJoin() {
		driver.showStatus("Failed To Join");
	}

}

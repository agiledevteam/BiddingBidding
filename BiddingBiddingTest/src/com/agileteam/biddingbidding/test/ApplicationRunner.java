package com.agileteam.biddingbidding.test;

import com.jayway.android.robotium.solo.Solo;

public class ApplicationRunner {
	private static final String AUCTION_HOST = "localhost";
	public static final String BIDDER_ID = "sniper";
	public static final String BIDDER_PASSWORD = "sniper";
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
		driver.connectAndJoin(AUCTION_HOST, BIDDER_ID, BIDDER_PASSWORD);
	}

	public void bid() {
		driver.bid();
	}

	public void showsLost(int currentPrice, int nextPrice) {
		driver.showsSniperStatus(STATUS_LOST, currentPrice, nextPrice);
	}

	public void showsJoined() {
		driver.showsSniperStatus(STATUS_JOINED, 0, 0);
	}

	public void showsLosing(int currentPrice, int nextPrice) {
		driver.showsSniperStatus(STATUS_LOSING, currentPrice, nextPrice);
	}

	public void showsBidding(int currentPrice, int nextPrice) {
		driver.showsSniperStatus(STATUS_BIDDING, currentPrice, nextPrice);
	}

	public void showsWinning(int currentPrice, int nextPrice) {
		driver.showsSniperStatus(STATUS_WINNING, currentPrice, nextPrice);
	}

	public void showsWon(int currentPrice, int nextPrice) {
		driver.showsSniperStatus(STATUS_WON, currentPrice, nextPrice);
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}

}

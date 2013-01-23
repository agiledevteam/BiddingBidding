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

	public void showsLost() {
		driver.showsSniperStatus(STATUS_LOST);
	}

	public void showsJoined() {
		driver.showsSniperStatus(STATUS_JOINED);
	}

	public void showsLosing() {
		driver.showsSniperStatus(STATUS_LOSING);
	}

	public void showsBidding() {
		driver.showsSniperStatus(STATUS_BIDDING);
	}

	public void showsWinning() {
		driver.showsSniperStatus(STATUS_WINNING);
	}

	public void showsWon() {
		driver.showsSniperStatus(STATUS_WON);
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}

}

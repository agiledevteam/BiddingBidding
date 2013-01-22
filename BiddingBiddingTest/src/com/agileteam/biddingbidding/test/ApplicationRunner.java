package com.agileteam.biddingbidding.test;

import com.jayway.android.robotium.solo.Solo;

public class ApplicationRunner {
	public static final String BIDDER_ID = "sniper";
	public static final String BIDDER_PASSWORD = "sniper";
	private static final String STATUS_JOINED = "Joined";
	private static final String STATUS_LOST = "Lost";
	private BiddingBiddingDriver driver;

	public ApplicationRunner(Solo solo) {
		driver = new BiddingBiddingDriver(solo);
	}

	public void startBidding() {
		driver.startBidding(BIDDER_ID, BIDDER_PASSWORD);
	}

	public void showsLost() {
		driver.showsSniperStatus(STATUS_LOST);
	}

	public void showsJoined() {
		driver.showsSniperStatus(STATUS_JOINED);
	}

	public void stop() {
		if (driver != null) {
			driver.dispose();
		}
	}


}

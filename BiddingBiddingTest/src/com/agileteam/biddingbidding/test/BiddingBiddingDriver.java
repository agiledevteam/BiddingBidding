package com.agileteam.biddingbidding.test;

import static junit.framework.Assert.assertTrue;
import android.util.Log;

import com.agileteam.biddingbidding.MainActivity;
import com.jayway.android.robotium.solo.Solo;

public class BiddingBiddingDriver {

	private Solo solo;

	public BiddingBiddingDriver(Solo solo) {
		this.solo = solo;
	}

	public void showsSniperStatus(String status) {
		assertTrue("status does not match.", solo.waitForText(status, 1, 5000));
		Log.d("yskang", "Fake server receive message: " + status);
	}

	public void dispose() {

	}

	public void startBidding(String id, String password) {
		solo.enterText(0, id);
		solo.enterText(1, password);
		solo.clickOnButton("Join Auction");
	}

	public void showsSniperStatus(String itemId, int lastPrice, int lastBid,
			String status) {
		assertTrue("status does not match.", solo.waitForText(status));
		;
	}

	public void loginToserver(String bidderId, String password) {
		solo.enterText(0, bidderId);
		solo.enterText(1, password);
		solo.clickOnButton("Login");
	}

	public void showsApplicationActivityHasChanged() {
		solo.assertCurrentActivity("login fail", MainActivity.class);
	}

}

package com.agileteam.biddingbidding.test;

import android.util.Log;

import com.agileteam.biddingbidding.MainActivity;
import com.jayway.android.robotium.solo.Solo;

import static junit.framework.Assert.assertTrue;



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

	public void startBiddingFor(String itemId) {
		solo.enterText(0, itemId);
		solo.clickOnButton("Join Auction");
	}

	public void showsSniperStatus(String itemId, int lastPrice, int lastBid,
			String status) {
		assertTrue("status does not match.", solo.waitForText(itemId));;
		assertTrue("status does not match.", solo.searchText(Integer.toString(lastPrice)));;
		assertTrue("status does not match.", solo.searchText(Integer.toString(lastBid)));;
		assertTrue("status does not match.", solo.searchText(status));;
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

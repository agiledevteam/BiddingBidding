package com.agileteam.biddingbidding.test;

import android.app.Activity;
import android.widget.TextView;

import com.agileteam.biddingbidding.MainActivity;
import com.agileteam.biddingbidding.R;
import com.jayway.android.robotium.solo.Solo;
import com.objogate.wl.android.driver.AndroidDriver;

public class BiddingBiddingDriver extends AndroidDriver<Activity> {

	public BiddingBiddingDriver(Solo solo, int timeout) {
		super(solo, timeout);
	}

	public void showsSniperStatus(String status) {
		//assertTrue("status does not match.", solo.waitForText(status, 1, 5000));
		new AndroidDriver<TextView>(this, R.id.textView_status).is(containsAllStrings(status));
	}

	public void dispose() {

	}

	public void connectAndJoin(String id, String password) {
		solo.enterText(0, id);
		solo.enterText(1, password);
		solo.clickOnButton("Join Auction");
	}
	
	public void bid() {
		solo.clickOnButton("Bid!");
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

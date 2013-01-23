package com.agileteam.biddingbidding.test;

import android.app.Activity;
import android.widget.TextView;

import com.agileteam.biddingbidding.R;
import com.jayway.android.robotium.solo.Solo;
import com.objogate.wl.android.driver.AndroidDriver;

public class BiddingBiddingDriver extends AndroidDriver<Activity> {

	public BiddingBiddingDriver(Solo solo, int timeout) {
		super(solo, timeout);
	}

	public void showsSniperStatus(String status) {
		new AndroidDriver<TextView>(this, R.id.textView_status)
				.is(containsAllStrings(status));
	}

	public void dispose() {

	}

	public void connectAndJoin(String host, String id, String password) {
		solo.enterText(0, host);
		solo.enterText(1, id);
		solo.enterText(2, password);
		solo.clickOnButton("Join Auction");
	}

	public void bid() {
		solo.clickOnButton("Bid!");
	}
}

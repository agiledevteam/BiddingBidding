package com.agileteam.biddingbidding.test;

import static com.lge.android.wl.driver.ViewDriver.allText;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.hasItem;
import android.app.Activity;

import com.agileteam.biddingbidding.R;
import com.jayway.android.robotium.solo.Solo;
import com.lge.android.wl.driver.AndroidDriver;
import com.lge.android.wl.driver.ListViewDriver;

public class BiddingBiddingDriver extends AndroidDriver<Activity> {

	public BiddingBiddingDriver(Solo solo, int timeout) {
		super(solo, timeout);
	}

	public void showsSniperStatus(String status, int currentPrice) {
		new ListViewDriver(this, R.id.list).hasItem(
				allText(),
				allOf(hasItem(status), hasItem(price(currentPrice))));
	}
	
	public void showsSniperStatus(int currentPrice, int nextPrice) {
		new ListViewDriver(this, R.id.list).hasItem(
				allText(),
				allOf(hasItem(price(currentPrice)), hasItem(price(nextPrice))));
	}

	private String price(int price) {
		return String.format(solo.getString(R.string.price_format), price);
	}

	public void dispose() {

	}

	public void connectAndJoin(String host, String id, String password) {
		solo.enterText(0, host);
		solo.enterText(1, id);
		solo.enterText(2, password);
		solo.clickOnButton("Login Auction");
	}

	public void bid() {
		solo.clickOnButton(0);
	}

	public void showStatus(String string) {
		assertTrue(solo.waitForText(string));
	}
}

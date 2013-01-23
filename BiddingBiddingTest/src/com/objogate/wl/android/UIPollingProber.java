package com.objogate.wl.android;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import android.app.Activity;

import com.jayway.android.robotium.solo.Solo;
import com.objogate.wl.PollingProber;
import com.objogate.wl.Probe;
import com.objogate.wl.Prober;

public class UIPollingProber extends PollingProber implements Prober {

	private final Solo solo;

	public UIPollingProber(Solo solo, int timeoutMillis, int delayMillis) {
		super(timeoutMillis, delayMillis);
		this.solo = solo;
	}

	@Override
	protected void runProbe(final Probe probe) {
		try {
			invokeAndWait(new Runnable() {
				@Override
				public void run() {
					probe.probe();
				}
			});
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void invokeAndWait(final Runnable runnable)
			throws InterruptedException {
		final CountDownLatch latch = new CountDownLatch(1);

		Activity activity = solo.getCurrentActivity();
		activity.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				runnable.run();
				latch.countDown();
			}
		});

		latch.await(1, TimeUnit.SECONDS);
	}

}

package com.objogate.wl.android.driver;

import org.hamcrest.Matcher;

import android.view.View;

import com.jayway.android.robotium.solo.Solo;
import com.objogate.wl.Probe;
import com.objogate.wl.Prober;
import com.objogate.wl.android.Selector;
import com.objogate.wl.android.UIPollingProber;
import com.objogate.wl.android.matcher.ContainsAllStringsMatcher;
import com.objogate.wl.android.matcher.ViewEnabledMatcher;
import com.objogate.wl.android.matcher.ViewVisibleMatcher;
import com.objogate.wl.android.probe.ViewAssertionProbe;
import com.objogate.wl.android.probe.ViewSetEnabledProbe;

public class AndroidDriver<T> {

	protected final Solo solo;
	protected final Prober prober;
	protected final Selector<T> selector;

	public AndroidDriver(final Solo solo, int timeoutMillis) {
		this.solo = solo;
		this.prober = new UIPollingProber(solo, timeoutMillis, 100);
		this.selector = new ActivitySelector<T>(solo);
	}

	public AndroidDriver(final Solo solo, int timeoutMillis, int resId) {
		this.solo = solo;
		this.prober = new UIPollingProber(solo, timeoutMillis, 100);
		this.selector = new SingleViewSelector<T>(solo, resId);
	}

	@SuppressWarnings("rawtypes")
	public AndroidDriver(AndroidDriver parentDriver, final int resId) {
		this.solo = parentDriver.solo;
		this.prober = parentDriver.prober;
		this.selector = new SingleViewSelector<T>(solo, resId);
	}

	public void is(Matcher<? super T> criteria) {
		check(new ViewAssertionProbe<T>(selector, criteria));
	}

	public void check(Probe probe) {
		prober.check(probe);
	}

	protected View getView(int resId) {
		return solo.getView(resId);
	}

	protected ContainsAllStringsMatcher containsAllStrings(String... text) {
		return new ContainsAllStringsMatcher(solo, text);
	}

	public static Matcher<View> enabled() {
		return new ViewEnabledMatcher();
	}

	public static Matcher<View> visible() {
		return new ViewVisibleMatcher();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void setEnabled(boolean enabled) {
		check(new ViewSetEnabledProbe(selector, enabled));
	}
}
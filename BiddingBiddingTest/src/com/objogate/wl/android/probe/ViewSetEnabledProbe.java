package com.objogate.wl.android.probe;

import org.hamcrest.Description;

import android.view.View;

import com.objogate.wl.Probe;
import com.objogate.wl.android.Selector;

public class ViewSetEnabledProbe<T extends View> implements Probe {

	private Selector<T> selector;
	private boolean enabled;

	public ViewSetEnabledProbe(Selector<T> selector, boolean enabled) {
		this.selector = selector;
		this.enabled = enabled;
	}

	@Override
	public void describeTo(Description arg0) {
		selector.describeTo(arg0);
		arg0.appendText("set enabled to " + enabled);
	}

	@Override
	public void describeFailureTo(Description arg0) {
		selector.describeFailureTo(arg0);
	}

	@Override
	public boolean isSatisfied() {
		if (selector.isSatisfied())
			return selector.view().isEnabled() == enabled;
		return false;
	}

	@Override
	public void probe() {
		selector.probe();
		if (selector.isSatisfied())
			selector.view().setEnabled(enabled);
	}

}

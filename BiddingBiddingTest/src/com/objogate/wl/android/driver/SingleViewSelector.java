package com.objogate.wl.android.driver;

import org.hamcrest.Description;

import com.jayway.android.robotium.solo.Solo;
import com.objogate.wl.android.Selector;

public class SingleViewSelector<T> implements Selector<T> {

	private final Solo solo;
	private final int resId;
	private T found;

	public SingleViewSelector(Solo solo, int resId) {
		this.solo = solo;
		this.resId = resId;
	}

	@Override
	public void describeFailureTo(Description description) {
		description.appendText("View<" + resId + "> is not found.");
	}

	@Override
	public boolean isSatisfied() {
		return found != null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void probe() {
		found = (T) solo.getView(resId);
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("View<" + resId + ">");
	}

	@Override
	public T select() {
		return found;
	}

}

package com.objogate.wl.android.matcher;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import android.view.View;

public class ViewForcusedMatcher extends TypeSafeMatcher<View> {

	@Override
	public void describeTo(Description arg0) {
		arg0.appendText("forcused");
	}

	@Override
	protected boolean matchesSafely(View arg0) {
		return arg0.isFocused();
	}

}

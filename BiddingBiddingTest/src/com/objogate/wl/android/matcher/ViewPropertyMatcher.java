package com.objogate.wl.android.matcher;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import android.view.View;

import com.objogate.wl.Query;

public class ViewPropertyMatcher<P> extends TypeSafeMatcher<View> {

	private Query<View, P> query;
	private Matcher<? super P> matcher;

	public ViewPropertyMatcher(Query<View, P> query, Matcher<? super P> matcher2) {
		this.query = query;
		this.matcher = matcher2;
	}

	@Override
	public void describeTo(Description arg0) {
		arg0.appendDescriptionOf(query).appendDescriptionOf(matcher);
	}

	@Override
	protected void describeMismatchSafely(View item,
			Description desc) {
		matcher.describeMismatch(query.query(item), desc);
	}

	@Override
	protected boolean matchesSafely(View item) {
		return matcher.matches(query.query(item));
	}

}

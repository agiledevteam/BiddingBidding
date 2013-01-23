package com.objogate.wl.android.driver;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewDriver extends AndroidDriver<ListView> {

	@SuppressWarnings("rawtypes")
	public ListViewDriver(AndroidDriver parentDriver, int resId) {
		super(parentDriver, resId);
	}

	public void hasItem(Matcher<View> matcher) {
		is(new ListItemMatcher(matcher));
	}

	private class ListItemMatcher extends TypeSafeMatcher<ListView> {

		private Matcher<View> matcher;

		public ListItemMatcher(Matcher<View> matcher) {
			this.matcher = matcher;
		}

		@Override
		public void describeTo(Description description) {
			description.appendText("with item ").appendDescriptionOf(matcher);
		}

		@Override
		protected boolean matchesSafely(ListView list) {
			ListAdapter adapter = list.getAdapter();
			for (int i = 0; i < adapter.getCount(); i++) {
				View child = adapter.getView(i, null, list);
				if (matcher.matches(child)) {
					return true;
				}
			}
			return false;
		}
	}
}

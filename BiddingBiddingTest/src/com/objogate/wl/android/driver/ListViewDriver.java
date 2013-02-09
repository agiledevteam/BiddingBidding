package com.objogate.wl.android.driver;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.objogate.wl.Query;
import com.objogate.wl.android.matcher.ViewPropertyMatcher;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewDriver extends ViewDriver<ListView> {

	@SuppressWarnings("rawtypes")
	public ListViewDriver(AndroidDriver parentDriver, int resId) {
		super(parentDriver, resId);
	}

	public void hasItem(Matcher<View> matcher) {
		is(new ContainingAnItemMatcher(matcher));
	}

	public <P> void hasItem(Query<View, P> query, Matcher<? super P> matcher) {
		hasItem(new ViewPropertyMatcher<P>(query, matcher));
	}

	private class ContainingAnItemMatcher extends TypeSafeMatcher<ListView> {

		private Matcher<View> matcher;

		public ContainingAnItemMatcher(Matcher<View> matcher) {
			this.matcher = matcher;
		}

		@Override
		public void describeTo(Description description) {
			description.appendText("containing an item").appendDescriptionOf(matcher);
		}

		@Override
		protected void describeMismatchSafely(ListView list,
				Description desc) {
			ListAdapter adapter = list.getAdapter();
			if (adapter == null) {
				desc.appendText("\n Adapter is not set.");
				return;
			}
			desc.appendText(String.format(" has %d item(s)", list.getChildCount()));
			for (int i = 0; i < list.getChildCount(); i++) {
				View child = list.getChildAt(i);
				desc.appendText("\n * ");
				matcher.describeMismatch(child, desc);
			}
		}

		@Override
		protected boolean matchesSafely(ListView list) {
//			ListAdapter adapter = list.getAdapter();
//			if (adapter == null) {
//				return false;
//			}
//			for (int i = 0; i < adapter.getCount(); i++) {
//				View child = adapter.getView(i, null, list);
//				if (matcher.matches(child)) {
//					return true;
//				}
//			}
//			return false;
			for (int i=0; i<list.getChildCount(); i++) {
				View child = list.getChildAt(i);
				if (matcher.matches(child))
					return true;
			}
			return false;
		}
	}
}

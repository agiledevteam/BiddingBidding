package com.objogate.wl.android.probe;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

import android.view.View;

import com.objogate.wl.Probe;
import com.objogate.wl.Query;
import com.objogate.wl.android.Selector;

public class ViewPropertyAssertionProbe<V extends View, P> implements Probe {
	private final Selector<V> selector;
	private final Query<? super V, P> propertyValueQuery;
	private final Matcher<? super P> propertyValueMatcher;

	private P propertyValue;

	public ViewPropertyAssertionProbe(Selector<V> selector,
			Query<? super V, P> propertyValueQuery,
			Matcher<? super P> propertyValueMatcher) {
		this.selector = selector;
		this.propertyValueQuery = propertyValueQuery;
		this.propertyValueMatcher = propertyValueMatcher;
	}

	@Override
	public void describeTo(Description description) {
		description.appendDescriptionOf(selector)
				.appendText("\nand check that its ")
				.appendDescriptionOf(propertyValueQuery).appendText(" is ")
				.appendDescriptionOf(propertyValueMatcher);
	}

	@Override
	public void describeFailureTo(Description description) {
		if (selector.isSatisfied()) {
			selector.describeTo(description);
			description.appendText("\n    ")
					.appendDescriptionOf(propertyValueQuery)
					.appendText(" was ").appendValue(propertyValue);
		} else {
			selector.describeFailureTo(description);
		}
	}

	@Override
	public boolean isSatisfied() {
		return selector.isSatisfied()
				&& propertyValueMatcher.matches(propertyValue);
	}

	@Override
	public void probe() {
		selector.probe();
		if (selector.isSatisfied()) {
			propertyValue = propertyValueQuery.query(selector.view());
		}
	}

}

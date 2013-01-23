package com.objogate.wl.android.probe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.hamcrest.Description;

import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;
import com.objogate.wl.Probe;

public class HasItemWithText implements Probe {

	private final int resId;
	private final List<String> strings;
	private final Solo solo;

	private List<String> items = new ArrayList<String>();
	private boolean satisfied = false;

	public HasItemWithText(Solo solo, int resId, String... strings) {
		this.solo = solo;
		this.resId = resId;
		this.strings = Arrays.asList(strings);
	}

	@Override
	public void describeTo(Description description) {
		description.appendValueList("ListView which has an item with: \n",
				", ", "", strings);
	}

	@Override
	public void describeFailureTo(Description description) {
		if (items.isEmpty()) {
			description.appendText("ListView has no items.");
		} else {
			description.appendValueList("ListView has items: \n", "\n",
					"\n", items);
		}
	}

	@Override
	public boolean isSatisfied() {
		return satisfied;
	}

	@Override
	public void probe() {
		items.clear();

		ListView list = (ListView) solo.getView(resId);
		ListAdapter adapter = list.getAdapter();
		for (int i = 0; i < adapter.getCount(); i++) {
			List<String> childStrings = getAllItemText(list, i);
			if (childStrings.containsAll(strings)) {
				satisfied = true;
				return;
			}
			items.add("[" + i + "] "
					+ StringUtils.join(getAllItemText(list, i), ", "));
		}
	}

	private List<String> getAllItemText(final ListView list, final int pos) {
		ArrayList<String> text = new ArrayList<String>();
		ListAdapter adapter = list.getAdapter();
		View child = adapter.getView(pos, null, list);
		for (View subView : solo.getViews(child)) {
			if (subView instanceof TextView) {
				text.add(((TextView) subView).getText().toString());
			}
		}
		return text;
	}
}
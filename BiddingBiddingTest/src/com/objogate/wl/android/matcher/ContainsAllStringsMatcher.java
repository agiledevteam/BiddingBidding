package com.objogate.wl.android.matcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import android.view.View;
import android.widget.TextView;

import com.jayway.android.robotium.solo.Solo;

public class ContainsAllStringsMatcher extends TypeSafeMatcher<View> {
	private List<String> matchStrings;
	private Solo solo;

	public ContainsAllStringsMatcher(Solo solo, String[] text) {
		this.solo = solo;
		this.matchStrings = Arrays.asList(text);
	}

	@Override
	public void describeTo(Description description) {
		description.appendValueList("containing all strings:", ",", "",
				matchStrings);
	}

	@Override
	protected boolean matchesSafely(View itemView) {
		return getAllText(itemView).containsAll(matchStrings);
	}

	private List<String> getAllText(final View view) {
		List<String> text = new ArrayList<String>();
		for (View subView : solo.getViews(view)) {
			if (subView instanceof TextView) {
				text.add(((TextView) subView).getText().toString());
			}
		}
		return text;
	}
}
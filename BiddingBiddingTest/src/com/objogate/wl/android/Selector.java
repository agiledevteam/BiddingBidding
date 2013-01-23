package com.objogate.wl.android;

import com.objogate.wl.Probe;

public interface Selector<T> extends Probe {
	T select();
}
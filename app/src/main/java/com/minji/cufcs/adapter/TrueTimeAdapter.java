package com.minji.cufcs.adapter;

import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.base.MyBaseAdapter;
import com.minji.cufcs.bean.TreuTimeDate;
import com.minji.cufcs.holder.TreuTimeHolder;

import java.util.List;

public class TrueTimeAdapter extends MyBaseAdapter<TreuTimeDate> {

	public TrueTimeAdapter(List<TreuTimeDate> list) {
		super(list);
	}

	@Override
	public BaseHolder<TreuTimeDate> getHolder() {
		return new TreuTimeHolder();
	}

	@Override
	public List<TreuTimeDate> onLoadMore() {
		return null;
	}

	@Override
	public boolean hasMore() {
		return false;
	}
}

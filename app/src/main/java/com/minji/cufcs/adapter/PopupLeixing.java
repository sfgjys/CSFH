package com.minji.cufcs.adapter;

import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.base.MyBaseAdapter;
import com.minji.cufcs.holder.PopupLeixingHolder;

import java.util.List;

public class PopupLeixing extends MyBaseAdapter<String> {

	public PopupLeixing(List<String> list) {
		super(list);
	}

	@Override
	public BaseHolder getHolder() {
		return new PopupLeixingHolder();
	}

	@Override
	public List<String> onLoadMore() {
		return null;
	}

	@Override
	public boolean hasMore() {
		return false;
	}
}

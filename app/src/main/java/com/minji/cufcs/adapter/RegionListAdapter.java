package com.minji.cufcs.adapter;

import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.base.MyBaseAdapter;
import com.minji.cufcs.holder.RegionListHolder;

import java.util.List;

public class RegionListAdapter extends MyBaseAdapter<Object> {

	public RegionListAdapter(List<Object> list) {
		super(list);
	}

	@Override
	public BaseHolder<Object> getHolder() {
		return new RegionListHolder();
	}

	@Override
	public List<Object> onLoadMore() {
		return null;
	}

	@Override
	public boolean hasMore() {
		return false;
	}
}

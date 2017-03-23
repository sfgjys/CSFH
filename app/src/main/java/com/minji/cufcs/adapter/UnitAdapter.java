package com.minji.cufcs.adapter;

import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.base.MyBaseAdapter;
import com.minji.cufcs.bean.UnitDetails;
import com.minji.cufcs.holder.UnitHolder;

import java.util.List;

public class UnitAdapter extends MyBaseAdapter<UnitDetails> {

	public UnitAdapter(List<UnitDetails> list) {
		super(list);
	}

	@Override
	public BaseHolder<UnitDetails> getHolder() {
		return new UnitHolder();
	}

	@Override
	public List<UnitDetails> onLoadMore() {
		return null;
	}

	@Override
	public boolean hasMore() {
		return false;
	}
}

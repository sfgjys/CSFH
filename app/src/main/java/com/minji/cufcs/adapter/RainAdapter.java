package com.minji.cufcs.adapter;

import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.base.MyBaseAdapter;
import com.minji.cufcs.bean.RainDetails;
import com.minji.cufcs.holder.RainHolder;

import java.util.List;

public class RainAdapter extends MyBaseAdapter<RainDetails> {

	public RainAdapter(List<RainDetails> list) {
		super(list);
	}

	@Override
	public BaseHolder<RainDetails> getHolder() {
		return new RainHolder();
	}

	@Override
	public List<RainDetails> onLoadMore() {
		return null;
	}

	@Override
	public boolean hasMore() {
		return false;
	}
}

package com.minji.cufcs.adapter;

import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.base.MyBaseAdapter;
import com.minji.cufcs.bean.ValveDetails;
import com.minji.cufcs.holder.ValveHolder;

import java.util.List;

public class ValveAdapter extends MyBaseAdapter<ValveDetails> {

	public ValveAdapter(List<ValveDetails> list) {
		super(list);
	}

	@Override
	public BaseHolder<ValveDetails> getHolder() {
		// TODO Auto-generated method stub
		return new ValveHolder();
	}

	@Override
	public List<ValveDetails> onLoadMore() {
		return null;
	}

	@Override
	public boolean hasMore() {
		// TODO Auto-generated method stub
		return false;
	}
}

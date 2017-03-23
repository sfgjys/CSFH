package com.minji.cufcs.adapter;

import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.base.MyBaseAdapter;
import com.minji.cufcs.bean.WaterDetails;
import com.minji.cufcs.holder.WaterHolder;

import java.util.List;

public class WaterAdapter extends MyBaseAdapter<WaterDetails> {

	public WaterAdapter(List<WaterDetails> list) {
		super(list);
	}

	@Override
	public BaseHolder<WaterDetails> getHolder() {
		// TODO Auto-generated method stub
		return new WaterHolder();
	}

	@Override
	public List<WaterDetails> onLoadMore() {
		return null;
	}

	@Override
	public boolean hasMore() {
		// TODO Auto-generated method stub
		return false;
	}
}

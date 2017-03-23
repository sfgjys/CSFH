package com.minji.cufcs.adapter;

import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.base.MyBaseAdapter;
import com.minji.cufcs.holder.PollingHolder;

import java.util.ArrayList;
import java.util.List;

public class PollingAdapter extends MyBaseAdapter<String> {

	public PollingAdapter(List<String> list) {
		super(list);
	}

	@Override
	public BaseHolder getHolder() {
		return new PollingHolder();
	}

	@Override
	public boolean hasMore() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public List<String> onLoadMore() {

		ArrayList<String> arrayList = new ArrayList<String>();
		
		
		
		return arrayList;
	}

}

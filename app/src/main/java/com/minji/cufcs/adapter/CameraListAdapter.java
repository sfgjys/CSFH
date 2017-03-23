package com.minji.cufcs.adapter;

import com.hikvision.vmsnetsdk.CameraInfo;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.base.MyBaseAdapter;
import com.minji.cufcs.holder.CameraListHolder;

import java.util.List;

public class CameraListAdapter extends MyBaseAdapter<CameraInfo> {

	public CameraListAdapter(List<CameraInfo> list) {
		super(list);
	}

	@Override
	public BaseHolder<CameraInfo> getHolder() {
		return new CameraListHolder();
	}

	@Override
	public List<CameraInfo> onLoadMore() {
		return null;
	}

	@Override
	public boolean hasMore() {
		return false;
	}
}

package com.minji.cufcs.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.bean.ImplementAreaList.AreaSingle;
import com.minji.cufcs.uitls.ViewsUitls;

public class AreaHolder extends BaseHolder<AreaSingle> {

	private ImageView mIsOrNo;
	private TextView mTime;
	private TextView mHubName;

	@Override
	public View initView() {

		View view = ViewsUitls.inflate(R.layout.itme_implement);
		mIsOrNo = (ImageView) view.findViewById(R.id.iv_is_or_no);
		mHubName = (TextView) view.findViewById(R.id.tv_implement_hubname);
		mTime = (TextView) view.findViewById(R.id.tv_implement_make_time);

		return view;
	}

	@Override
	public void setRelfshData(AreaSingle mData, int postion) {

		if ("已实施未完成".equals(mData.getState())) {
			mIsOrNo.setImageResource(R.mipmap.already_implement);
		} else if ("未查看".equals(mData.getState())) {
			mIsOrNo.setImageResource(R.mipmap.no_implement);
		} else if ("已查看未实施".equals(mData.getState())) {
			mIsOrNo.setImageResource(R.mipmap.no_implement);
		} else {
			mIsOrNo.setImageResource(R.mipmap.no_implement);
		}

		mHubName.setText("枢纽名称: " + mData.getStationname());
		mTime.setText("制单时间: " + mData.getCreatetime());

	}

}

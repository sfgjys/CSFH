package com.minji.cufcs.holder;

import android.view.View;
import android.widget.TextView;

import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.bean.PollingSaveList.SaveSingle;
import com.minji.cufcs.uitls.ViewsUitls;

public class AlreadySaveHolder extends BaseHolder<SaveSingle> {


	private TextView mSaveDegree;
	private TextView mSaveHubName;

	@Override
	public View initView() {

		View view = ViewsUitls.inflate(R.layout.itme_save_polling);
		mSaveHubName = (TextView) view.findViewById(R.id.tv_save_polling_hubname);
		mSaveDegree = (TextView) view.findViewById(R.id.tv_save_polling_degree);

		return view;
	}

	@Override
	public void setRelfshData(SaveSingle mData, int postion) {

		mSaveHubName.setText("枢纽名 : "+mData.getStationname());
		String excepttime = mData.getExcepttime();
		String substring = excepttime.substring(0, 7);
		mSaveDegree.setText(substring+" : "+mData.getDegree());

	}

}

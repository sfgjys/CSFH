package com.minji.cufcs.holder;

import android.view.View;
import android.widget.TextView;

import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.bean.TreuTimeDate;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ViewsUitls;

public class TreuTimeHolder extends BaseHolder<TreuTimeDate> {

	private TextView superviseHubname;
	private TextView superviseInRiver;
	private TextView superviseOutRiver;
	private TextView superviseUnit;
	private TextView superviseUintOpen;

	@Override
	public View initView() {
		View view = ViewsUitls.inflate(R.layout.item_true_time_supervise);
		superviseHubname = (TextView) view
				.findViewById(R.id.tv_true_time_supervise_hubname);
		superviseInRiver = (TextView) view
				.findViewById(R.id.tv_true_time_supervise_inriver);
		superviseOutRiver = (TextView) view
				.findViewById(R.id.tv_true_time_supervise_outriver);
		superviseUnit = (TextView) view
				.findViewById(R.id.tv_true_time_supervise_unit);
		superviseUintOpen = (TextView) view
				.findViewById(R.id.tv_true_time_supervise_unit_open);

		return view;
	}

	@Override
	public void setRelfshData(TreuTimeDate mData, int postion) {

		if (!StringUtils.isEmpty(mData.getOutWaterLevel())) {
			superviseOutRiver.setText("外河水位 : " + mData.getOutWaterLevel()
					+ "m");
		} else {
			superviseOutRiver.setText("外河水位 : ---");
		}
		if (!StringUtils.isEmpty(mData.getInWaterLevel())) {
			superviseInRiver.setText("内河水位 : " + mData.getInWaterLevel() + "m");
		} else {
			superviseInRiver.setText("内河水位 : ---");
		}

		superviseHubname.setText(mData.getHubName());
		superviseUnit.setText("共有机组: " + mData.getAllUnit() + "台");

		if (!StringUtils.isEmpty(mData.getOpenUnit())) {
			superviseUintOpen.setText("已开启机组: " + mData.getOpenUnit());
		} else {
			superviseUintOpen.setText("暂无机组开启");
		}

	}

}

package com.minji.cufcs.holder;

import android.view.View;
import android.widget.TextView;

import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.bean.OperationDispatchingList;
import com.minji.cufcs.uitls.ViewsUitls;

public class OperationHolder extends BaseHolder<OperationDispatchingList> {

	private TextView schedulingType;
	private TextView enforcement;
	private TextView makingTime;
	private TextView makingPeople;

	@Override
	public View initView() {
		View view = ViewsUitls.inflate(R.layout.item_operation_dispatching);
		schedulingType = (TextView) view.findViewById(R.id.tv_scheduling_type);
		enforcement = (TextView) view.findViewById(R.id.tv_enforcement);
		makingTime = (TextView) view
				.findViewById(R.id.tv_decomposition_single_making_time);
		makingPeople = (TextView) view
				.findViewById(R.id.tv_decomposition_single_making_people);

		return view;
	}

	@Override
	public void setRelfshData(OperationDispatchingList mData, int postion) {

		schedulingType.setText("调度类型 : " + mData.getSchedulingType());
		enforcement.setText("执行部门 : " + mData.getEnforcement());
		makingTime.setText("分解单制单时间 : " + mData.getMakingTime());
		makingPeople.setText("分解单制单人 : " + mData.getMakingPeople());

	}

}

package com.minji.cufcs.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.hikvision.vmsnetsdk.RegionInfo;
import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.uitls.ViewsUitls;

public class RegionListHolder extends BaseHolder<Object> {

	private TextView textView;

	@Override
	public View initView() {
		textView = new TextView(ViewsUitls.getContext());
		textView.setTextSize(20);
		textView.setTextColor(Color.BLACK);
		textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
		textView.setPadding(ViewsUitls.dptopx(20), ViewsUitls.dptopx(20),
				ViewsUitls.dptopx(20), ViewsUitls.dptopx(20));
		textView.setBackgroundResource(R.drawable.selector_listview_item);
		return textView;
	}

	@Override
	public void setRelfshData(Object mData, int postion) {
	
		if (mData instanceof ControlUnitInfo) {
			textView.setText(((ControlUnitInfo) mData).getName());
		}

		if (mData instanceof RegionInfo) {
			textView.setText(((RegionInfo) mData).getName());
		}

		if (mData instanceof CameraInfo) {
			textView.setText(((CameraInfo) mData).getName());
		}
	}

}

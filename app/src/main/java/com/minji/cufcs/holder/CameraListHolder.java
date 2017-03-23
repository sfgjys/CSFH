package com.minji.cufcs.holder;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.hikvision.vmsnetsdk.CameraInfo;
import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.uitls.ViewsUitls;

public class CameraListHolder extends BaseHolder<CameraInfo> {

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
	public void setRelfshData(CameraInfo mData, int postion) {
	
		if (mData instanceof CameraInfo) {
			textView.setText(((CameraInfo) mData).getName());
		}else{
			textView.setText("数据集合出现异常");
		}
	}

}

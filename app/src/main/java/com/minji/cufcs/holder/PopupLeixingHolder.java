package com.minji.cufcs.holder;

import android.view.View;
import android.widget.TextView;

import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.uitls.TextViews;
import com.minji.cufcs.uitls.ViewsUitls;

public class PopupLeixingHolder extends BaseHolder<String> {

	private TextView view;

	@Override
	public View initView() {

		view = TextViews.getView(ViewsUitls.getContext());
		view.setTextSize(ViewsUitls.dptopx(10));
		view.setPadding(ViewsUitls.dptopx(6), ViewsUitls.dptopx(6),
				ViewsUitls.dptopx(6), ViewsUitls.dptopx(6));

		return view;
	}

	@Override
	public void setRelfshData(String mData, int postion) {
		view.setText(mData);
	}

}

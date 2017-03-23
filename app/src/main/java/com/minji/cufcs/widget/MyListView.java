package com.minji.cufcs.widget;

import android.content.Context;
import android.widget.ListView;

public class MyListView extends ListView {

	public MyListView(Context context) {
		super(context);
		initView();
	}

	private void initView() {
		//设置默认点击背景色为透明
		this.setSelector(android.R.color.transparent);
		//设置ListView默认的分割线
//		this.setDivider(null);
	}

}

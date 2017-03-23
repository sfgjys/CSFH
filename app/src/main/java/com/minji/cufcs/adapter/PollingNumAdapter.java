package com.minji.cufcs.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minji.cufcs.R;
import com.minji.cufcs.uitls.ViewsUitls;

public class PollingNumAdapter extends BaseAdapter {

	private String[] num={"第一次","第二次"};

	@Override
	public int getCount() {
		return num.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		if(convertView==null){
			convertView=ViewsUitls.inflate(R.layout.item_polling_popowindow);
		}
		TextView textView = (TextView) convertView.findViewById(R.id.tv_poppwindow);		
		textView.setText(num[position]);

		return convertView;
	}

}

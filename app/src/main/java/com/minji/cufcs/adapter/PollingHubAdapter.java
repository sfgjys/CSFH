package com.minji.cufcs.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minji.cufcs.R;
import com.minji.cufcs.bean.PollingHubName;
import com.minji.cufcs.uitls.ViewsUitls;

import java.util.List;

public class PollingHubAdapter extends BaseAdapter {

	private List<PollingHubName> hubNames;

	public PollingHubAdapter(List<PollingHubName> hubNames) {
		this.hubNames = hubNames;
	}

	@Override
	public int getCount() {
		return hubNames.size();
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
		textView.setText(hubNames.get(position).getName());

		return convertView;
	}

}

package com.minji.cufcs.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minji.cufcs.R;
import com.minji.cufcs.R.color;
import com.minji.cufcs.uitls.ViewsUitls;

public class PollingEighteenAdapter extends BaseAdapter {

	private String[] pollingPart;
	private String[] pollingContent;
	private boolean[] isSumbit;

	public PollingEighteenAdapter(boolean[] isSumbit, String[] pollingPart,
			String[] pollingContent) {
		this.isSumbit = isSumbit;
		this.pollingContent = pollingContent;
		this.pollingPart = pollingPart;
	}

	@Override
	public int getCount() {
		return pollingPart.length;
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

		if (convertView == null) {
			convertView = ViewsUitls.inflate(R.layout.item_eighteen);
		}
		TextView textView = (TextView) convertView
				.findViewById(R.id.tv_polling_part);
		textView.setText(pollingPart[position]);

		if (isSumbit[position]) {
			convertView.setBackgroundResource(color.white);
			textView.setTextColor(ViewsUitls.getContext().getResources().getColor(R.color.thin_gray));
		}else{
			convertView.setBackgroundResource(R.drawable.selector_eighteen_gridview_item);
			textView.setTextColor(Color.BLACK);
		}

		return convertView;
	}

}

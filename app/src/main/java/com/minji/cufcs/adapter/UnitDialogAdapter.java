package com.minji.cufcs.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.minji.cufcs.bean.ImplementUnit;
import com.minji.cufcs.uitls.ViewsUitls;

import java.util.List;

public class UnitDialogAdapter extends BaseAdapter {

	private List<ImplementUnit> unitNames;

	public UnitDialogAdapter(List<ImplementUnit> unitNames) {
		this.unitNames = unitNames;
	}

	@Override
	public int getCount() {
		return unitNames.size();
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
		ImplementUnit implementUnit = unitNames.get(position);
		String name = implementUnit.getName();
		if (convertView == null) {
			convertView = new TextView(ViewsUitls.getContext());
		}
		((TextView) convertView).setText(name);
		((TextView) convertView).setTextColor(Color.BLACK);
		((TextView) convertView)
				.setBackgroundColor(Color.parseColor("#FFFFFF"));
		((TextView) convertView).setPadding(ViewsUitls.dptopx(12),
				ViewsUitls.dptopx(4), ViewsUitls.dptopx(4),
				ViewsUitls.dptopx(4));

		return convertView;
	}

}

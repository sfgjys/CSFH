package com.minji.cufcs.adapter;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.minji.cufcs.R;
import com.minji.cufcs.bean.PollingPerson;
import com.minji.cufcs.uitls.ViewsUitls;

import java.util.ArrayList;
import java.util.List;

public class PollingPersonAdapter extends BaseAdapter {

	private List<PollingPerson> persons;

	public PollingPersonAdapter(List<PollingPerson> persons) {
		this.persons = persons;
	}

	@Override
	public int getCount() {
		return persons.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private List<Integer> list = new ArrayList<Integer>();;

	@Override
	public View getView( int position, View convertView, ViewGroup parent) {
		final Integer integer = Integer.valueOf(position);
		
		if (convertView == null) {
			convertView = ViewsUitls
					.inflate(R.layout.item_polling_dialog_persons);
		}
		CheckBox checkBox = (CheckBox) convertView
				.findViewById(R.id.tv_polling_dialog_check);
		checkBox.setText(persons.get(position).getDname() + " : "
				+ persons.get(position).getEname());
		checkBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (list.contains(integer)) {
					list.remove(integer);
				} else {
					list.add(integer);
				}
			}
		});

		if (list.contains(integer)) {
			checkBox.setChecked(true);
		} else {
			checkBox.setChecked(false);
		}
		return convertView;
	}
	
	public List<Integer> getToBeSelected(){
		return list;
	}

}

package com.minji.cufcs.adapter;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.minji.cufcs.bean.ViewScreenDetails;
import com.minji.cufcs.uitls.ViewsUitls;

import java.util.List;

public class ViewScreenAdapter extends BaseExpandableListAdapter {

	private List<ViewScreenDetails> groups;

	public ViewScreenAdapter(List<ViewScreenDetails> groups) {
		this.groups = groups;
	}

	// 设置组的个数
	@Override
	public int getGroupCount() {
		return groups.size();
	}

	// 设置孩子的个数
	@Override
	public int getChildrenCount(int groupPosition) {
		return groups.get(groupPosition).cameras.size();
	}

	// 根据组的位置获取的组的数据
	@Override
	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	// 根据组的位置和孩子的位置获取孩子的数据
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return groups.get(groupPosition).cameras.get(childPosition);
	}

	// 获取组的id
	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	// 获取孩子的id
	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	// 判断id是否稳定,如果你返回id,返回false,没有返回id,返回true
	@Override
	public boolean hasStableIds() {
		return false;
	}

	// 设置组的样式
	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
//		if (convertView == null) {
//			convertView = ViewsUitls.inflate(R.layout.itme_screen_father);
//		}
//		TextView textView = (android.widget.TextView) convertView
//				.findViewById(R.id.tv_screen_father);
//		textView.setText(groups.get(groupPosition).getName());
		
		TextView textView = new TextView(ViewsUitls.getContext());
		textView.setText(groups.get(groupPosition).getName());
		textView.setTextSize(20);
		textView.setTextColor(Color.BLACK);
		textView.setBackgroundColor(Color.parseColor("#FFFFFF"));
		textView.setPadding(ViewsUitls.dptopx(20), ViewsUitls.dptopx(20),ViewsUitls.dptopx(20), ViewsUitls.dptopx(20));
		
		
		return textView;
	}
	
	
	

	// 设置孩子的样式
	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
//		if (convertView == null) {
//			convertView = ViewsUitls.inflate(R.layout.itme_screen_child);
//		}
//		TextView textView = (android.widget.TextView) convertView
//				.findViewById(R.id.tv_screen_child);
//		textView.setText(groups.get(groupPosition).getCameras()
//				.get(childPosition).getName());
		
		TextView textView = new TextView(ViewsUitls.getContext());
		textView.setText(groups.get(groupPosition).getCameras()
				.get(childPosition).getName());
		textView.setTextSize(18);
		textView.setTextColor(Color.BLACK);
		textView.setPadding(ViewsUitls.dptopx(20), ViewsUitls.dptopx(20),ViewsUitls.dptopx(20), ViewsUitls.dptopx(20));
		textView.setBackgroundColor(Color.parseColor("#E6E6E6"));
		return textView;

	}

	// 设置孩子是否可以点击,false:表示不可点击,true:表示可以点击
	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}

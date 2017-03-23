package com.minji.cufcs.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.minji.cufcs.R;
import com.minji.cufcs.uitls.ViewsUitls;

public class HomePageAdapter extends BaseAdapter {

	private String[] name = { "水情雨情", "实时监测", "视频监控", "运行工况", "所长执行", "现场运行","巡视检查" };
	private int[] image = { R.mipmap.ico_23, R.mipmap.ico_26,
			R.mipmap.ico_34, R.mipmap.ico_31, R.mipmap.ico_36,
			R.mipmap.ico_35, R.mipmap.ico_30};

	@Override
	public int getCount() {
		return name.length;
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

		View view = View.inflate(ViewsUitls.getContext(),
				R.layout.item_gridview_homepage, null);
		ImageView imageView = (ImageView) view
				.findViewById(R.id.iv_gridview_itme);
		imageView.setImageResource(image[position]);
		TextView textView = (TextView) view.findViewById(R.id.tv_gridview_itme);
		textView.setText(name[position]);

		return view;
	}

}

package com.minji.cufcs.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.minji.cufcs.R;
import com.minji.cufcs.uitls.ViewsUitls;

import java.util.List;

public class HorizontalListViewAdapter extends BaseAdapter {

	private List<Bitmap> bitmaps;

	public HorizontalListViewAdapter(List<Bitmap> bitmaps) {
		this.bitmaps = bitmaps;
	}

	@Override
	public int getCount() {
		return bitmaps.size();
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
			convertView = ViewsUitls.inflate(R.layout.item_polling_entering);
		}
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.iv_add_polling);
		imageView.setImageBitmap(bitmaps.get(position));
		return convertView;
	}

}

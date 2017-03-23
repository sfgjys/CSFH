package com.minji.cufcs.holder;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.uitls.ViewsUitls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PollingHolder extends BaseHolder<String> {

	private ImageView mTags;
	private TextView mDanHao;
	private TextView mTime;
	private CheckBox mChexBox;

	private static Map<Integer, Boolean> isSelectPosition = new HashMap<Integer, Boolean>();

	private List<Integer> integers = new ArrayList<Integer>();

	private static List<String> list = new ArrayList<String>();

	private Integer integer;

	@Override
	public View initView() {

		View view = ViewsUitls.inflate(R.layout.item_me_polling);
		mTags = (ImageView) view.findViewById(R.id.iv_polling_tags);
		mDanHao = (TextView) view.findViewById(R.id.tv_polling_danhao);
		mTime = (TextView) view.findViewById(R.id.tv_polling_time);
		mChexBox = (CheckBox) view.findViewById(R.id.cb_polling);

		
		
		return view;
	}

	@Override
	public void setRelfshData(final String mData, final int position) {

		mChexBox.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				if (list.contains(mData)) {
					System.out.println("----------------------");
					list.remove(mData);
				} else {
					list.add(mData);
					System.out.println("++++++++++++++++++++++");
				}
				for (int i = 0; i < list.size(); i++) {
					System.out.println(list.get(i));
				}

			}
		});

		if (list.contains(mData)) {
			mChexBox.setChecked(true);
		} else {
			mChexBox.setChecked(false);
		}

		mDanHao.setText(mData);
	}

}

package com.minji.cufcs.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.bean.WaterDetails;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ViewsUitls;

public class WaterHolder extends BaseHolder<WaterDetails> {

	public String image[] = { "澡港河南枢纽", "老澡港河枢纽", "永汇河枢纽", "北塘河枢纽", "横塘河北枢纽",
			"横峰沟枢纽", "糜家塘枢纽", "横塘河南枢纽", "丁横河枢纽", "大运河东枢纽", "采菱港枢纽", "串新河枢纽",
			"南运河枢纽", "大运河西枢纽", "西界河闸站", "童子河闸站", };
	public int drawId[] = { R.mipmap.a1, R.mipmap.a2, R.mipmap.a3,
			R.mipmap.a4, R.mipmap.a5, R.mipmap.a6, R.mipmap.a7,
			R.mipmap.a8, R.mipmap.a9, R.mipmap.a10, R.mipmap.a11,
			R.mipmap.a12, R.mipmap.a13, R.mipmap.a14, R.mipmap.a15,
			R.mipmap.a16, };
	private TextView mShuNiu;
	private TextView mTime;
	private TextView mInRiver;
	private TextView mOutRiver;
	private TextView text;
	private ImageView hubImageView;

	@Override
	public View initView() {

		View view = ViewsUitls.inflate(R.layout.item_water);
		mShuNiu = (TextView) view.findViewById(R.id.tv_shuniu);
		mOutRiver = (TextView) view.findViewById(R.id.tv_out_river);
		mInRiver = (TextView) view.findViewById(R.id.tv_in_river);
		mTime = (TextView) view.findViewById(R.id.tv_time);
		hubImageView = (ImageView) view.findViewById(R.id.iv_water_hubname);

		return view;
	}

	@Override
	public void setRelfshData(WaterDetails mData, int postion) {
		String hubName = mData.getHubName();
		if (!StringUtils.isEmpty(hubName)) {
			mShuNiu.setText("枢纽名称 : " + hubName);
		} else {
			mShuNiu.setText("枢纽名称: ---" + hubName);
		}
		if (!StringUtils.isEmpty(mData.getOutWaterLevel())) {
			mOutRiver.setText("外河水位 : " + mData.getOutWaterLevel() + "m");
		} else {
			mOutRiver.setText("外河水位 : ---");
		}
		if (!StringUtils.isEmpty(mData.getInWaterLevel())) {
			mInRiver.setText("内河水位 : " + mData.getInWaterLevel() + "m");
		} else {
			mInRiver.setText("内河水位 : ---");
		}
		if (!StringUtils.isEmpty(mData.getCollectTime())) {
			mTime.setText("采集时间 : " + mData.getCollectTime());
		} else {
			mTime.setText("采集时间 : ---");
		}

		for (int i = 0; i < image.length; i++) {
			if (hubName.contains(image[i])) {
				hubImageView.setBackgroundResource(drawId[i]);
				return;
			} else {
				hubImageView
						.setBackgroundResource(R.mipmap.water_hubname_image);
			}
		}
	}

}

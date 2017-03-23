package com.minji.cufcs.holder;

import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.bean.RainDetails;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ViewsUitls;

public class RainHolder extends BaseHolder<RainDetails> {

	private TextView mRainNumber;
	private TextView mHubName;
	private ImageView mOperationState;
	private TextView mTime;
	private LinearLayout linearLayout;
	private ImageView leftImage;

	@Override
	public View initView() {

		View view = ViewsUitls.inflate(R.layout.item_workr);
		mHubName = (TextView) view.findViewById(R.id.tv_unit_up);
		mRainNumber = (TextView) view.findViewById(R.id.tv_unit_middle);
		mOperationState = (ImageView) view.findViewById(R.id.iv_unit_state);
		mTime = (TextView) view.findViewById(R.id.tv_unit_down);

		// 修改图片的宽高
		linearLayout = (LinearLayout) view.findViewById(R.id.ll_item_unit);
		leftImage = (ImageView) view.findViewById(R.id.iv_item_unit);
		ViewTreeObserver viewTreeObserver = linearLayout.getViewTreeObserver();
		viewTreeObserver
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						linearLayout.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						LayoutParams layoutParams = leftImage.getLayoutParams();
						layoutParams.width = linearLayout.getHeight();
						layoutParams.height = linearLayout.getHeight();
						leftImage.setLayoutParams(layoutParams);
					}
				});
		leftImage.setImageResource(R.mipmap.item_rain);
		mOperationState.setVisibility(View.INVISIBLE);
		return view;
	}

	@Override
	public void setRelfshData(RainDetails mData, int postion) {

		if (mData.getTotal() != 0) {
			mRainNumber.setText("降雨量: " + mData.getTotal() + "mm");
		} else {
			mRainNumber.setText("降雨量: ---");
		}
		if (!StringUtils.isEmpty(mData.getTime())) {
			mTime.setText("采集时间: " + mData.getTime());
		}else {
			mTime.setText("采集时间: ---");
		}
		if (!StringUtils.isEmpty(mData.getName())) {
			mHubName.setText("枢纽名称: " + mData.getName());
		}else {
			mHubName.setText("枢纽名称: ---");
		}

	}

}

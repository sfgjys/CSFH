package com.minji.cufcs.holder;

import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.bean.UnitDetails;
import com.minji.cufcs.uitls.ViewsUitls;

public class UnitHolder extends BaseHolder<UnitDetails> {

	private TextView mShuNiu;
	private TextView mTime;
	private ImageView mOperationState;
	private TextView mUnitNumber;
	private ImageView leftImage;
	private LinearLayout linearLayout;

	@Override
	public View initView() {

		View view = ViewsUitls.inflate(R.layout.item_workr);
		mShuNiu = (TextView) view.findViewById(R.id.tv_unit_up);
		mUnitNumber = (TextView) view.findViewById(R.id.tv_unit_middle);
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

		return view;
	}

	@Override
	public void setRelfshData(UnitDetails mData, int postion) {
		mShuNiu.setText("枢纽名称 : " + mData.getmHubName());
		mUnitNumber.setText("机组编号 : " + mData.getmUnitNumber());
		mTime.setText("采集时间 : " + mData.getmCollectTime());

		if ("运行".equals(mData.getmOperationState())) {
			mOperationState.setVisibility(View.VISIBLE);
			mOperationState.setImageResource(R.mipmap.state_open);
		} else if ("停止".equals(mData.getmOperationState())) {
			mOperationState.setVisibility(View.VISIBLE);
			mOperationState.setImageResource(R.mipmap.state_close);
		} else {
			mOperationState.setVisibility(View.INVISIBLE);
		}

	}

}

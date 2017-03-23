package com.minji.cufcs.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.minji.cufcs.IntentFields;
import com.minji.cufcs.R;
import com.minji.cufcs.widget.MyLinearLayout;

public abstract class BaseActivity extends FragmentActivity {
	private TextView mTvTitle;
	public ImageView mSetting;
	public Intent mIntentDate;
	public String mSTitle;
	public FrameLayout mContent;
	private ImageView mBack;
	private ImageView mSmallLogo;
	private FrameLayout mLoade;
	private MyLinearLayout linearLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_activity);

		mIntentDate = getIntent();
		mSTitle = mIntentDate.getStringExtra(IntentFields.ACTIVITY_TITLE);

		initView();

		setTtile();

		onCreateContent();

	}

	public abstract void onCreateContent();

	private void setTtile() {

		if (mSTitle == null || mSTitle.isEmpty()) {
			// 标题为空
			mTvTitle.setVisibility(View.GONE);
			Log.v(getClass().getName(), "标题为空");
		} else {
			mTvTitle.setText(mSTitle);
		}
	}

	private void initView() {
		linearLayout = (MyLinearLayout) findViewById(R.id.ll_text);

		mTvTitle = (TextView) findViewById(R.id.tv_title);
		mSetting = (ImageView) findViewById(R.id.iv_setting);
		mContent = (FrameLayout) findViewById(R.id.fl_content);
		mLoade = (FrameLayout) findViewById(R.id.fl_loading);

		mSmallLogo = (ImageView) findViewById(R.id.iv_small_logo);
		mBack = (ImageView) findViewById(R.id.iv_back);
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	public void showBack() {
		mSmallLogo.setVisibility(View.GONE);
		mBack.setVisibility(View.VISIBLE);
	}

	public void setSingnTtile(String title) {
		mTvTitle.setText(title);
	}

	public void makeToast(String text) {
		Toast.makeText(BaseApplication.getContext(), text, Toast.LENGTH_SHORT)
				.show();
	}

	/**
	 * 首先将一个xml布局打气压缩成一个View，在将该View添加到Framelayout中
	 */
	public View setContent(int id) {
		View inflate = View.inflate(BaseApplication.getContext(), id, null);
		mContent.addView(inflate);
		return inflate;
	}

	public void setContent(View view) {
		mContent.addView(view);
	}

	public void setLoadIsVisible(int visibility) {
		mLoade.setVisibility(visibility);
	}
	
	public void setIsInterrupt(boolean mIsInterrupt){
		linearLayout.setIsInterruptTouch(mIsInterrupt);
	}
}

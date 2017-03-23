package com.minji.cufcs.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyLinearLayout extends LinearLayout {

	private boolean mIsInterrupt;

	public MyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MyLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		mIsInterrupt = false;
	}

	public MyLinearLayout(Context context) {
		super(context);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mIsInterrupt) {
			return true;
		} else {
			return super.onInterceptTouchEvent(ev);
		}
	}

	public void setIsInterruptTouch(boolean mIsInterrupt) {
		this.mIsInterrupt = mIsInterrupt;
	}
}

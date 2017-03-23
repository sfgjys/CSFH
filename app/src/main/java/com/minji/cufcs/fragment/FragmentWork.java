package com.minji.cufcs.fragment;

import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.minji.cufcs.FragmentFactory;
import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.base.BasePagerAdapter;
import com.minji.cufcs.base.BaseViewPagerFragment;

public class FragmentWork extends BaseViewPagerFragment {

	@Override
	public void setupAdapter(BasePagerAdapter adapter) {

		adapter.setUpdate("机组");
		adapter.setUpdate("闸门");

	}

	@Override
	protected void onSubClassOnCreateView() {
		mPagerTabs.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				// 加载网络
				BaseFragment baseFragment = FragmentFactory.fragments[arg0];
				baseFragment.loadDataAndRefresh();
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}

	@Override
	protected String setDifferentiate() {
		return "Work";
	}

}

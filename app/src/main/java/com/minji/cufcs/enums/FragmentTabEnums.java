package com.minji.cufcs.enums;

import com.minji.cufcs.R;
import com.minji.cufcs.fragment.FragmentHomePage;
import com.minji.cufcs.fragment.FragmentSetting;

public enum FragmentTabEnums {

	HOMEPAGE(0, FragmentHomePage.class, "首页", R.drawable.selector_main_homepage), 
//	ME(1, FragmentMe.class, "我的", R.drawable.selector_main_me), 
	SETTING(1,FragmentSetting.class, "关于", R.drawable.selector_main_setting);

	private Class<?> mFragmentClass;
	private String mTabsFlg;
	private int mTabImageId;
	private int mNum;

	private FragmentTabEnums(int mNum, Class<?> mFragmentClass,
			String mTabsFlg, int mTabImageId) {
		this.mFragmentClass = mFragmentClass;
		this.mTabImageId = mTabImageId;
		this.mTabsFlg = mTabsFlg;
		this.mNum = mNum;
	}

	public Class<?> getmFragmentClass() {
		return mFragmentClass;
	}

	public void setmFragmentClass(Class<?> mFragmentClass) {
		this.mFragmentClass = mFragmentClass;
	}

	public String getmTabsFlg() {
		return mTabsFlg;
	}

	public void setmTabsFlg(String mTabsFlg) {
		this.mTabsFlg = mTabsFlg;
	}

	public int getmTabImageId() {
		return mTabImageId;
	}

	public void setmTabImageId(int mTabImageId) {
		this.mTabImageId = mTabImageId;
	}

}

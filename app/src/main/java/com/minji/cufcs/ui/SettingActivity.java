package com.minji.cufcs.ui;

import java.util.List;

import android.app.Activity;
import android.support.v4.app.FragmentTransaction;
import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseActivity;
import com.minji.cufcs.base.BaseApplication;
import com.minji.cufcs.fragment.FragmentSetting;

public class SettingActivity extends BaseActivity {

	private List<Activity> saveActicity;
	@Override
	public void onCreateContent() {
		
		if(mSTitle == null || mSTitle.isEmpty()){

		}

		saveActicity = BaseApplication.getSaveActicity();
		saveActicity.add(this);
		
		showBack();
		FragmentTransaction beginTransaction = getSupportFragmentManager()
				.beginTransaction();
		beginTransaction.replace(R.id.fl_content, new FragmentSetting());
		beginTransaction.commit();
	}

	@Override
	protected void onDestroy() {
		saveActicity.remove(this);
		super.onDestroy();
	}
	
}

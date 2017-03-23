package com.minji.cufcs.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseActivity;
import com.minji.cufcs.fragment.FragmentScreenSecond;
import com.minji.cufcs.fragment.FragmentSetting;

public class SecondScreenFragmentActivity extends BaseActivity {

	private int pResTypeIntent;
	private int pIdIntent;

	@Override
	public void onCreateContent() {

		showBack();
		pResTypeIntent = mIntentDate.getIntExtra("NextListResType", -1);
		pIdIntent = mIntentDate.getIntExtra("NextListId", -1);

		FragmentTransaction beginTransaction = getSupportFragmentManager()
				.beginTransaction();
		FragmentScreenSecond fragmentScreenSecond = new FragmentScreenSecond();

		Bundle bundle = new Bundle();
		bundle.putInt("pResTypeIntent", pResTypeIntent);
		bundle.putInt("pIdIntent", pIdIntent);
		fragmentScreenSecond.setArguments(bundle);
		
		beginTransaction.replace(R.id.fl_content, fragmentScreenSecond);
		beginTransaction.commit();

	}

	public int getpResTypeIntent() {
		return pResTypeIntent;
	}

	public int getpIdIntent() {
		return pIdIntent;
	}
}

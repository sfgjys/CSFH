package com.minji.cufcs.ui;

import android.os.Bundle;

import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseActivity;
import com.minji.cufcs.fragment.FragmentOperationDetail;
import com.minji.cufcs.observer.MySubject;

public class OperationDetailActivity extends BaseActivity {

	@Override
	public void onCreateContent() {

		showBack();

		Bundle bundle = new Bundle();
		bundle.putString("Id", mIntentDate.getStringExtra("Id"));
		bundle.putString("Departmentid",
				mIntentDate.getStringExtra("Departmentid"));
		bundle.putInt("position", mIntentDate.getIntExtra("position", -1));
		FragmentOperationDetail fragmentOperationDetail = new FragmentOperationDetail();
		fragmentOperationDetail.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fl_content, fragmentOperationDetail).commit();

	}

}

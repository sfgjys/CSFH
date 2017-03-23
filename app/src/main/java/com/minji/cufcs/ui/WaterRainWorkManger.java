package com.minji.cufcs.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;

import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseActivity;
import com.minji.cufcs.fragment.FragmentAlreadySave;
import com.minji.cufcs.fragment.FragmentImplement;
import com.minji.cufcs.fragment.FragmentOperation;
import com.minji.cufcs.fragment.FragmentPolling;
import com.minji.cufcs.fragment.FragmentRain;
import com.minji.cufcs.fragment.FragmentScreen;
import com.minji.cufcs.fragment.FragmentTrueTime;
import com.minji.cufcs.fragment.FragmentUnit;
import com.minji.cufcs.fragment.FragmentWater;
import com.minji.cufcs.fragment.FragmentWaterRain;
import com.minji.cufcs.fragment.FragmentWork;
import com.minji.cufcs.observer.MySubject;

public class WaterRainWorkManger extends BaseActivity {

	private static final int DATE_DIALOG_ID = 1;
	private static final int TIME_DIALOG_ID = 3;
	private FragmentAlreadySave fragmentAlreadySave;
	private FragmentTransaction beginTransaction;
	private FragmentPolling fragmentPolling;

	@Override
	public void onCreateContent() {

		showBack();

		int num = mIntentDate.getIntExtra("qufen", 7);
		beginTransaction = getSupportFragmentManager().beginTransaction();
		switch (num) {
		case 0:
			beginTransaction.replace(R.id.fl_content, new FragmentWaterRain());
			break;
		case 1:
			beginTransaction.replace(R.id.fl_content, new FragmentTrueTime());
			break;
		case 2:
			beginTransaction.replace(R.id.fl_content, new FragmentScreen());

			break;
		case 3:
			beginTransaction.replace(R.id.fl_content, new FragmentUnit());
			break;
		case 4:
			FragmentOperation fragmentOperation = new FragmentOperation();
			MySubject.getInstance().add(fragmentOperation);
			beginTransaction.replace(R.id.fl_content, fragmentOperation);
			break;
		case 5:
			beginTransaction.replace(R.id.fl_content, new FragmentImplement());
			break;
		case 6:
			fragmentAlreadySave = new FragmentAlreadySave();
			beginTransaction.replace(R.id.fl_content, fragmentAlreadySave);
			break;
		}

		beginTransaction.commit();
	}

	public void beginAddPolling() {
		fragmentPolling = new FragmentPolling();
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.fl_content, fragmentPolling);
		transaction.commit();
	}

	/** 为了FragmentPolling界面 */
	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DATE_DIALOG_ID:
			System.out.println("DATE:onCreateDialog");
			return new DatePickerDialog(this, fragmentPolling.mDateSetListener,
					fragmentPolling.mYear, fragmentPolling.mMonth,
					fragmentPolling.mDay);
		case TIME_DIALOG_ID:
			System.out.println("TIME:onCreateDialog");
			return new TimePickerDialog(this, fragmentPolling.mTimeSetListener,
					fragmentPolling.mHour, fragmentPolling.mMinute, true);
		}
		return null;

	}

	/** 为了FragmentPolling界面 */
	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			System.out.println("DATE:onPrepareDialog");
			((DatePickerDialog) dialog).updateDate(fragmentPolling.mYear,
					fragmentPolling.mMonth, fragmentPolling.mDay);
			break;
		case TIME_DIALOG_ID:
			System.out.println("TIME:onPrepareDialog");
			((TimePickerDialog) dialog).updateTime(fragmentPolling.mHour,
					fragmentPolling.mMinute);
			break;
		}
	}
}

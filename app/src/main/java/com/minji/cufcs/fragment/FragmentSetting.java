package com.minji.cufcs.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.format.Formatter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.IntentFields;
import com.minji.cufcs.R;
import com.minji.cufcs.SpFields;
import com.minji.cufcs.base.BaseApplication;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.ui.LoginActivity;
import com.minji.cufcs.ui.SettingActivity;
import com.minji.cufcs.ui.VersionsActivity;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.ViewsUitls;
import com.minji.cufcs.widget.SettingItem;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FragmentSetting extends Fragment implements OnClickListener {

	private View view;
	private SettingItem mCache;
	private SettingItem mInform;
//	private SettingItem mOut;
	private SettingItem mAboutUs;
	private SettingItem mVersions;
	private Button mLogOut;
	private AlertDialog alertDialog;
	private EditText ipAdrees;
	private EditText passWord;
	private EditText userName;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout_setting, null);

		initView();

		return view;

	}

	private void setCacheData() {

		File[] files = getFiles();
		long length = 0;
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			length = length + file.length();
		}
		String formatFileSize = Formatter.formatFileSize(
				ViewsUitls.getContext(), length);
		mCache.setCacheText(formatFileSize);
	}

	private File[] getFiles() {
		File files = new File(Environment.getExternalStorageDirectory(),
				"Cufcs/image");
		if (files.exists()) {
			File[] listFiles = files.listFiles();
			return listFiles;
		} else {
			return new File[] {};
		}

	}

	private void initView() {

		mCache = (SettingItem) view.findViewById(R.id.settingitem_cache);
		mAboutUs = (SettingItem) view.findViewById(R.id.settingitem_about_us);
		mVersions = (SettingItem) view.findViewById(R.id.settingitem_versions);
		mLogOut = (Button) view.findViewById(R.id.bt_logout);

		mLogOut.setOnClickListener(this);
		mCache.setOnClickListener(this);
		mAboutUs.setOnClickListener(this);
		mVersions.setOnClickListener(this);

		mInform = (SettingItem) view.findViewById(R.id.settingitem_inform);
//		mOut = (SettingItem) view.findViewById(R.id.settingitem_out);

		mInform.getSwitchImage().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 点击时先读取状态 此状态就是当前状态boolean
				boolean inform_warn = SharedPreferencesUtil.getboolean(
						ViewsUitls.getContext(), SpFields.INFORM_WARN, false);

				if (inform_warn) {
					// ture为开启状态，所以要关
					getActivity().stopService(
							BaseApplication.getIntentServicer());
				} else {
					// 与上面相反
					getActivity().startService(
							BaseApplication.getIntentServicer());
				}

				// 在设置
				mInform.setcSwitchAction(inform_warn);
				// 在存储状态
				SharedPreferencesUtil.saveboolean(ViewsUitls.getContext(),
						SpFields.INFORM_WARN, !inform_warn);
			}
		});

//		setSwitchOnclick(mOut, SpFields.OUT_INFORM);
	}

//	private void setSwitchOnclick(final SettingItem settingItem,
//			final String key) {
//
//		settingItem.getSwitchImage().setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				// 点击时先读取状态 此状态就是当前状态boolean
//				boolean inform_warn = SharedPreferencesUtil.getboolean(
//						ViewsUitls.getContext(), key, false);
//
//				if (inform_warn) {
//					// ture为开启状态，所以要关
//					getActivity().stopService(
//							BaseApplication.getIntentServicer());
//				} else {
//					// 与上面相反
//					getActivity().startService(
//							BaseApplication.getIntentServicer());
//				}
//
//				// 在设置
//				settingItem.setcSwitchAction(inform_warn);
//				// 在存储状态
//				SharedPreferencesUtil.saveboolean(ViewsUitls.getContext(), key,
//						!inform_warn);
//			}
//		});
//	}

	private void showReminderDialog(String content, int i) {
		alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setView(new EditText(ViewsUitls.getContext()));
		LayoutParams attributes = alertDialog.getWindow().getAttributes();// 获取对话框的属性集
		WindowManager m = getActivity().getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		attributes.width = (int) (d.getWidth() * 0.9);
		alertDialog.show();
		// 设置对话框中自定义内容
		Window window = alertDialog.getWindow();
		if (i == 0) {
			window.setContentView(R.layout.dialog_reminder);
			TextView reminderContents = (TextView) window
					.findViewById(R.id.tv_reminder_contents);
			reminderContents.setText(content);
			Button mCancel = (Button) window.findViewById(R.id.bt_cancel);
			Button mSure = (Button) window.findViewById(R.id.bt_make_sure);
			mCancel.setOnClickListener(this);
			mSure.setOnClickListener(this);
		}
		if (i == 1) {
			window.setContentView(R.layout.dialog_setting_screen);

			String ip = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), SpFields.SCREEN_IP, "");
			String user = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), SpFields.SCREEN_USERNAME, "");
			String pass = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), SpFields.SCREEN_PASSWORD, "");
			ipAdrees = (EditText) window.findViewById(R.id.et_ip_adrres);
			ipAdrees.setText(ip);
			passWord = (EditText) window.findViewById(R.id.et_setting_password);
			passWord.setText(pass);
			passWord.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			userName = (EditText) window.findViewById(R.id.et_setting_username);
			userName.setText(user);

			Button mCancel = (Button) window
					.findViewById(R.id.bt_setting_screen_cancel);
			Button mSure = (Button) window
					.findViewById(R.id.bt_make_setting_screen_sure);
			mCancel.setOnClickListener(this);
			mSure.setOnClickListener(this);
		}

	}

	private String middle;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.bt_make_setting_screen_sure:

			if (!StringUtils.isEmpty(passWord.getText().toString())
					&& !StringUtils.isEmpty(userName.getText().toString())
					&& !StringUtils.isEmpty(ipAdrees.getText().toString())) {
				SharedPreferencesUtil.saveStirng(ViewsUitls.getContext(),
						SpFields.SCREEN_IP, ipAdrees.getText().toString());
				SharedPreferencesUtil
						.saveStirng(ViewsUitls.getContext(),
								SpFields.SCREEN_USERNAME, userName.getText()
										.toString());
				SharedPreferencesUtil
						.saveStirng(ViewsUitls.getContext(),
								SpFields.SCREEN_PASSWORD, passWord.getText()
										.toString());
				alertDialog.cancel();
			} else {
				ToastUtil.showToast(ViewsUitls.getContext(), "不可为空");
			}
			break;

		case R.id.settingitem_cache:
			middle = "是否清除缓存";
			showReminderDialog(middle, 0);
			break;
		case R.id.settingitem_about_us:
			middle = "是否对流媒体进行设置？";
			showReminderDialog(middle, 0);
			break;
		case R.id.settingitem_versions:
			Intent intent = new Intent(ViewsUitls.getContext(),
					VersionsActivity.class);
			intent.putExtra(IntentFields.ACTIVITY_TITLE, "版本信息");
			getActivity().startActivity(intent);
			break;
		case R.id.bt_logout:
			ThreadManager.getInstance().execute(new Runnable() {
				@Override
				public void run() {
					HttpBasic httpBasic = new HttpBasic();
					List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
					try {
						String address = SharedPreferencesUtil.getString(
								ViewsUitls.getContext(), "address", "");
						String postBack = httpBasic.postBack(address
								+ ApiField.LOGOUT, list);
						System.out.println("postBack:  " + postBack);
						if (StringUtils.interentIsNormal(postBack)) {
							if ("{'result':true}".equals(postBack)) {
								ViewsUitls.runInMainThread(new Runnable() {
									@Override
									public void run() {
										// 判断是否开启服务
										boolean inform_warn = SharedPreferencesUtil.getboolean(
												ViewsUitls.getContext(),
												SpFields.INFORM_WARN, false);
										if (inform_warn) {
											getActivity()
													.stopService(
															BaseApplication
																	.getIntentServicer());
										}

										SharedPreferencesUtil.saveboolean(
												ViewsUitls.getContext(),
												SpFields.IS_AUTO_LOGIN, false);
										getActivity().startActivity(
												new Intent(ViewsUitls
														.getContext(),
														LoginActivity.class));
										List<Activity> saveActicity = BaseApplication
												.getSaveActicity();
										for (int i = 0; i < saveActicity.size(); i++) {
											Activity activity = saveActicity
													.get(i);
											activity.finish();
										}
									}
								});
							} else {
								ViewsUitls.runInMainThread(new Runnable() {
									@Override
									public void run() {
										ToastUtil.showToast(
												ViewsUitls.getContext(),
												"网络异常,无法登出");
									}
								});
							}
						} else {
							ViewsUitls.runInMainThread(new Runnable() {
								@Override
								public void run() {
									ToastUtil.showToast(
											ViewsUitls.getContext(),
											"网络异常,无法登出");
								}
							});
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			break;
		case R.id.bt_make_sure:
			alertDialog.cancel();

			if ("是否清除缓存".equals(middle)) {
				SettingActivity activity = (SettingActivity) getActivity();
				activity.setLoadIsVisible(View.VISIBLE);
				File files = new File(
						Environment.getExternalStorageDirectory(),
						"Cufcs/image");
				if (files.exists()) {
					String[] list = files.list();
					for (int i = 0; i < list.length; i++) {
						File file = new File(files, "/" + list[i]);
						file.delete();
					}
					setCacheData();
				}
				activity.setLoadIsVisible(View.GONE);
			}

			if ("是否对流媒体进行设置？".equals(middle)) {
				showReminderDialog("流媒体设置", 1);
			}

			break;

		case R.id.bt_cancel:
			alertDialog.cancel();
			break;
		case R.id.bt_setting_screen_cancel:
			alertDialog.cancel();
			break;

		}

	}

	/**
	 * 当界面可见时会调用下面的方法。其中setSwitchImage方法中会将此时开关的状态赋值给SettingItem控件的成员变量mIsOnOff
	 * 我们可以通过getSwitchState()方法获取。
	 * */
	@Override
	public void onStart() {
		super.onStart();

		setCacheData();

		boolean inform_warn = SharedPreferencesUtil.getboolean(
				ViewsUitls.getContext(), SpFields.INFORM_WARN, false);
//		boolean out_inform = SharedPreferencesUtil.getboolean(
//				ViewsUitls.getContext(), SpFields.OUT_INFORM, false);

		mInform.setSwitchImage(inform_warn);
//		mOut.setSwitchImage(out_inform);

	}

	@Override
	public void onResume() {
		setCacheData();
		super.onResume();
	}

}

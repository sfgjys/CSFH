package com.minji.cufcs;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TabHost.TabSpec;
import com.minji.cufcs.base.BaseActivity;
import com.minji.cufcs.base.BaseApplication;
import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.enums.FragmentTabEnums;
import com.minji.cufcs.fragment.FragmentHomePage;
import com.minji.cufcs.service.AlarmServicer;
import com.minji.cufcs.ui.SettingActivity;
import com.minji.cufcs.uitls.LogUtils;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.ViewsUitls;
import com.minji.cufcs.widget.MyFragmentTabHost;

public class MainActivity extends BaseActivity {

	private MyFragmentTabHost mFragmentTabs;
	private LayoutInflater layoutInflater;
	private FragmentTabEnums[] values;
	private View view;
	private List<Activity> saveActicity;

	@Override
	public void onCreateContent() {

		int getint = SharedPreferencesUtil.getint(ViewsUitls.getContext(),
				"initStart", 0);
		if (getint != 1) {
			SharedPreferencesUtil.saveStirng(ViewsUitls.getContext(),
					SpFields.SCREEN_IP, "http://223.112.181.214:8443");
			SharedPreferencesUtil.saveStirng(ViewsUitls.getContext(),
					SpFields.SCREEN_USERNAME, "admin");
			SharedPreferencesUtil.saveStirng(ViewsUitls.getContext(),
					SpFields.SCREEN_PASSWORD, "!QAZ2wsx");
			SharedPreferencesUtil.saveint(ViewsUitls.getContext(), "initStart",
					1);
			LogUtils.e("第一次登录");
		}

		saveActicity = BaseApplication.getSaveActicity();
		saveActicity.add(this);

		// view = setContent(R.layout.main_tab_layout);
		//
		// layoutInflater = LayoutInflater.from(this);

		// fl_content

		// 判断是否开启服务
		boolean inform_warn = SharedPreferencesUtil.getboolean(
				ViewsUitls.getContext(), SpFields.INFORM_WARN, false);
		if (inform_warn) {
			startService(BaseApplication.getIntentServicer());
		}

		FragmentTransaction beginTransaction = getSupportFragmentManager()
				.beginTransaction();
		beginTransaction.replace(R.id.fl_content, new FragmentHomePage());
		beginTransaction.commit();

		mSetting.setVisibility(View.VISIBLE);
		mSetting.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(ViewsUitls.getContext(),
						SettingActivity.class);
				intent.putExtra(IntentFields.ACTIVITY_TITLE, "设置");
				startActivity(intent);
			}
		});
	}

	private void createTab() {
		// 获取FragmentTab控件
		mFragmentTabs = (MyFragmentTabHost) view.findViewById(R.id.fth_tabs);
		// 参数三是导航列表上的内容
		mFragmentTabs.setup(this, getSupportFragmentManager(),
				R.id.fragemnt_content);
		values = FragmentTabEnums.values();
		int length = values.length;

		for (int i = 0; i < length; i++) {

			// 创建Tabs newTabSpec其作用是新new创建一个TabSpec对象 setIndicator设置具体控件样式
			TabSpec tabSpec = mFragmentTabs.newTabSpec(i + "").setIndicator(
					getTabItemView(i));
			// 添加Tabs到FragmentTab中

			mFragmentTabs.addTab(tabSpec, values[i].getmFragmentClass(), null);
			// 设置背景
			// mFragmentTabs.getTabWidget().getChildAt(i)
			// .setBackgroundColor(Color.WHITE);
			// 去除分割线
			mFragmentTabs.getTabWidget().setDividerDrawable(null);
		}

	}

	private View getTabItemView(int i) {
		View view = layoutInflater.inflate(R.layout.item_tab, null);
		ImageView imageView = (ImageView) view.findViewById(R.id.iv_tab);
		imageView.setBackgroundResource(values[i].getmTabImageId());
		return view;
	}

	@Override
	protected void onDestroy() {
		saveActicity.remove(this);

		// 判断是否开启服务
		boolean inform_warn = SharedPreferencesUtil.getboolean(
				ViewsUitls.getContext(), SpFields.INFORM_WARN, false);
		if (inform_warn) {
			stopService(BaseApplication.getIntentServicer());
		}

		super.onDestroy();
	}

}

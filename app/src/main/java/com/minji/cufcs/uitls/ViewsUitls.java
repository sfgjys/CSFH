package com.minji.cufcs.uitls;

import org.apache.http.client.HttpClient;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseApplication;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.ui.PollingEntering;

public class ViewsUitls {

	public static Context getContext() {
		return BaseApplication.getContext();
	}

	public static HttpClient getHttpClient() {
		return BaseApplication.getHttpClient();
	}

	// dip转px
	public static int dptopx(int dip) {
		float density = getContext().getResources().getDisplayMetrics().density;
		// px = dip * density
		// 3.3 3.8 3
		// 3.6 4.1 4
		return (int) (dip * density + 0.5);
	}

	/**
	 * xml 转成View对象
	 * 
	 * @param id
	 * @return
	 */
	public static View inflate(int id) {
		return View.inflate(getContext(), id, null);
	}

	/**
	 * 在主线程中执行任务 模仿runOut。。。。
	 * 
	 * @param task
	 */
	public static void runInMainThread(Runnable task) {
		if (BaseApplication.getMainThreadId() == android.os.Process.myTid()) {
			// 当前就是主线程，直接执行task
			task.run();
		} else {
			// 在子线程，post给主线程
			BaseApplication.getHanlder().post(task);
		}
	}

	// 版本名
	public static String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	// 版本号
	public static int getVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}

	private static PackageInfo getPackageInfo(Context context) {
		PackageInfo pi = null;
		try {
			PackageManager pm = context.getPackageManager();
			pi = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_CONFIGURATIONS);

			return pi;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pi;
	}

}

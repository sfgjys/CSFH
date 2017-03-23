package com.minji.cufcs.base;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

import com.hik.mcrsdk.MCRSDK;
import com.hik.mcrsdk.rtsp.RtspClient;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.service.AlarmServicer;

import org.apache.http.client.HttpClient;

import java.util.ArrayList;
import java.util.List;

public class BaseApplication extends Application {
	private static Context mContext;
	private static Handler mHandler;
	private static long mainThreadId;
	private static HttpClient httpClient;
	private static List<Activity> saveActicity;
	private static BaseApplication ins;
	private static Intent intentServicer;

	@Override
	public void onCreate() {
		// 在onCreate方法中初始化公共变量，Context，Handler，main Thread id
		super.onCreate();

		mContext = getApplicationContext();
		mHandler = new Handler();
		mainThreadId = android.os.Process.myTid();
		httpClient = HttpBasic.getHttpClient();
		saveActicity = new ArrayList<Activity>();

		// 将这个加入通知
		intentServicer = new Intent(mContext, AlarmServicer.class);

		ins = this;
		MCRSDK.init();
		RtspClient.initLib();
		MCRSDK.setPrint(1, null);

	}

	public static BaseApplication getIns() {
		return ins;
	}

	public static Intent getIntentServicer() {
		return intentServicer;
	}

	public static List<Activity> getSaveActicity() {
		return saveActicity;
	}

	// 返回OkHttpClient
	public static HttpClient getHttpClient() {
		return httpClient;
	}

	// 返回上下文
	public static Context getContext() {
		return mContext;
	}

	// 返回主线程的Handler
	public static Handler getHanlder() {
		return mHandler;
	}

	// 返回主线程的id
	public static long getMainThreadId() {
		return mainThreadId;
	}

}

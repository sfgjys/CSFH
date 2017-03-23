package com.minji.cufcs.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat.Builder;
import android.view.View;
import android.widget.RemoteViews;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.R;
import com.minji.cufcs.SpFields;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.observer.MySubject;
import com.minji.cufcs.ui.SettingActivity;
import com.minji.cufcs.ui.ShowAlarmActivity;
import com.minji.cufcs.uitls.BaseTools;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ViewsUitls;
import com.minji.cufcs.widget.MyMediaPlayer;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmServicer extends Service {

	private NotificationManager mNotificationManager;
	private MyMediaPlayer myMediaPlayer;
	private Timer timer;
	private TimerTask timerTask;
	private boolean isAlarm;
	private Builder mBuilder;
	private String postBack;
	private int isStart = 0;
	private ButtonBroadcastReceiver bReceiver;
	/** 通知栏按钮点击事件对应的ACTION */
	public final static String ACTION_BUTTON = "com.notifications.intent.action.ButtonClick";
	public final static String INTENT_BUTTONID_TAG = "ButtonId";
	public final static int BUTTON_CANCEL_ALARM = 1;
	private String msg;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		initNotification();

		AssetManager am = getAssets();// 获得该应用的AssetManager
		try {
			AssetFileDescriptor afd = am.openFd("alarm.mp3");
			myMediaPlayer = new MyMediaPlayer();
			MySubject.getInstance().add(myMediaPlayer);
			myMediaPlayer.setDataSource(afd.getFileDescriptor());
			myMediaPlayer.prepare(); // 准备
			myMediaPlayer.setLooping(true);
			// mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			// @Override
			// public void onCompletion(MediaPlayer mp) {
			// mediaPlayer.start();
			// // mediaPlayer.setLooping(true);
			// }
			// });
		} catch (IOException e) {
			e.printStackTrace();
		}

		// 0秒后开启定时器，定时循环间隔60秒
		timer = new Timer();
		timerTask = new TimerTask() {

			@Override
			public void run() {
				// TODO 请求一次网络看是否要报警
				HttpBasic httpBasic = new HttpBasic();
				List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
				try {
					String address = SharedPreferencesUtil.getString(
							ViewsUitls.getContext(), "address", "");
					postBack = httpBasic.postBack(address + ApiField.ALARM,
							list);
					System.out.println(address + ApiField.ALARM);
					System.out.println("postBack:  " + postBack);
					if (StringUtils.interentIsNormal(postBack)) {
						if (postBack.contains("false")) {
							isAlarm = false;
							msg = "暂无报警信息";
						} else if (postBack.contains("true")) {
							JSONObject jsonObject = new JSONObject(postBack);
							msg = jsonObject.optString("msg");
							isAlarm = true;
						}
					} else {
						// 网络请求失败
						isAlarm = false;
						msg = "报警信息,请求失败!";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// TODO 测试用
//				msg = "dasdasdasd";
//				isAlarm = true;
				// TODO 测试用

				if (isAlarm) {
					isStart++;
					myMediaPlayer.start();
				} else {
					if (isStart != 0) {
						myMediaPlayer.pause();
						isStart = 0;
					}
				}
				SharedPreferencesUtil.saveStirng(ViewsUitls.getContext(),
						SpFields.ALARM_MSG, msg);
				mBuilder.setContentText(msg);
				startNotification();
			}
		};
		timer.schedule(timerTask, 0, 10000);

		return super.onStartCommand(intent, flags, startId);
	}

	private void initNotification() {
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mBuilder = new Builder(this);
		// //PendingIntent 跳转动作
		Intent intent2 = new Intent(ViewsUitls.getContext(),
				ShowAlarmActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent2, 0);
		mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker("系统警报")
				.setContentTitle("系统警报")
				.setContentText("当报警声响起，请点击通知栏进入设置页面关闭")
				.setContentIntent(pendingIntent);
	}

	private void startNotification() {
		Notification mNotification = mBuilder.build();
		// 设置通知 消息 图标
		mNotification.icon = R.mipmap.ic_launcher;
		// 在通知栏上点击此通知后自动清除此通知
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;// FLAG_ONGOING_EVENT
																// 在顶部常驻，可以调用下面的清除方法去除
																// FLAG_AUTO_CANCEL
																// 点击和清理可以去调
		// 设置显示通知时的默认的发声、震动、Light效果
		// mNotification.defaults = Notification.DEFAULT_VIBRATE;
		// 设置发出消息的内容
		mNotification.tickerText = "系统报警";
		// 设置发出通知的时间
		mNotification.when = System.currentTimeMillis();
		// mNotification.flags = Notification.FLAG_AUTO_CANCEL;
		// //在通知栏上点击此通知后自动清除此通知
		// mNotification.setLatestEventInfo(this, "常驻测试",
		// "使用cancel()方法才可以把我去掉哦", null); //设置详细的信息 ,这个方法现在已经不用了
		mNotificationManager.notify(100, mNotification);
	}

	private void showButtonNotify() {
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Builder mBuilder = new Builder(this);

		RemoteViews mRemoteViews = new RemoteViews(getPackageName(),
				R.layout.layout_alarm_notification);
		mRemoteViews.setImageViewResource(R.id.iv_alarm_notification_icon,
				R.mipmap.cufc);
		// API3.0 以上的时候显示按钮，否则消失
		mRemoteViews.setTextViewText(R.id.tv_alarm_notification_title, "系统报警");
		mRemoteViews
				.setTextViewText(
						R.id.tv_alarm_notification_msg,
						"系统报警系统报警系统报警系统报警系统报警系统报警系统报警系统报警系统报警系统报警系统报警系统报警系统报警系统报警系统报警系统报警系统报警系统报警系统报警系统报警系统报警");
		// 如果版本号低于（3。0），那么不显示按钮
		if (BaseTools.getSystemVersion() <= 9) {
			mRemoteViews.setViewVisibility(
					R.id.ib_alarm_notification_cancel_alarm, View.GONE);
		} else {
			mRemoteViews.setViewVisibility(
					R.id.ib_alarm_notification_cancel_alarm, View.VISIBLE);
			mRemoteViews.setImageViewResource(
					R.id.ib_alarm_notification_cancel_alarm,
					R.mipmap.btn_play);
		}

		// 点击的事件处理
		Intent buttonIntent = new Intent(ACTION_BUTTON);
		/* 上一首按钮 */
		buttonIntent.putExtra(INTENT_BUTTONID_TAG, BUTTON_CANCEL_ALARM);
		// 这里加了广播，所及INTENT的必须用getBroadcast方法
		PendingIntent intent_prev = PendingIntent.getBroadcast(this, 1,
				buttonIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mRemoteViews.setOnClickPendingIntent(
				R.id.ib_alarm_notification_cancel_alarm, intent_prev);

		Intent intent2 = new Intent(ViewsUitls.getContext(),
				SettingActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent2, 0);
		mBuilder.setContent(mRemoteViews).setContentIntent(pendingIntent)
				.setWhen(System.currentTimeMillis())// 通知产生的时间，会在通知信息里显示
				.setTicker("正在播放").setPriority(Notification.PRIORITY_DEFAULT)// 设置该通知优先级
				.setOngoing(true).setSmallIcon(R.mipmap.cufc);
		Notification notify = mBuilder.build();
		notify.flags = Notification.FLAG_ONGOING_EVENT;
		// 会报错，还在找解决思路
		// notify.contentView = mRemoteViews;
		// notify.contentIntent = PendingIntent.getActivity(this, 0, new
		// Intent(), 0);
		mNotificationManager.notify(200, notify);
	}

	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {

		// if (bReceiver != null) {
		// unregisterReceiver(bReceiver);
		// }

		myMediaPlayer.stop();

		if (timer != null)
			timer.cancel();
		timer = null;
		timerTask = null;

		if (mNotificationManager != null) {
			mNotificationManager.cancel(100);
		}
		super.onDestroy();
	}

	/** 带按钮的通知栏点击广播接收 */
	public void initButtonReceiver() {
		bReceiver = new ButtonBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_BUTTON);
		registerReceiver(bReceiver, intentFilter);
	}

	public class ButtonBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (action.equals(ACTION_BUTTON)) {
				// 通过传递过来的ID判断按钮点击属性或者通过getResultCode()获得相应点击事件
				int buttonId = intent.getIntExtra(INTENT_BUTTONID_TAG, 0);
				switch (buttonId) {
				case BUTTON_CANCEL_ALARM:
					System.out
							.println("+++++++++============——————————————————————");
					break;
				default:
					break;
				}
			}
		}
	}

}

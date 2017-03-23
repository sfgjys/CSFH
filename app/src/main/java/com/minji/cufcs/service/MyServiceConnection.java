package com.minji.cufcs.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class MyServiceConnection implements ServiceConnection {

	private static volatile MyServiceConnection connection = null;

	public MyServiceConnection getConnection() {
		if (connection == null) {
			synchronized (MyServiceConnection.class) {
				if (connection == null) {
					connection = new MyServiceConnection();
				}
			}
		}
		return connection;
	}

	private MyServiceConnection() {
		super();
	}

	// 当服务连接成功后调用
	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {

	}

	@Override
	public void onServiceDisconnected(ComponentName name) {

	}

}

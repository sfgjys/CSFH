package com.minji.cufcs.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.widget.LinearLayout;
import com.hik.mcrsdk.rtsp.LiveInfo;
import com.hik.mcrsdk.rtsp.RtspClient;
import com.hikvision.vmsnetsdk.CameraInfo;
import com.hikvision.vmsnetsdk.CameraInfoEx;
import com.hikvision.vmsnetsdk.RealPlayURL;
import com.hikvision.vmsnetsdk.ServInfo;
import com.hikvision.vmsnetsdk.VMSNetSDK;
import com.hikvision.vmsnetsdk.netLayer.msp.deviceInfo.DeviceInfo;
import com.minji.cufcs.R;
import com.minji.cufcs.haikang.constans.Constants;
import com.minji.cufcs.haikang.data.Config;
import com.minji.cufcs.haikang.data.TempData;
import com.minji.cufcs.haikang.live.ConstantLive;
import com.minji.cufcs.haikang.live.LiveCallBack;
import com.minji.cufcs.haikang.live.LiveControl;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.uitls.DebugLog;
import com.minji.cufcs.uitls.LogUtils;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.ViewsUitls;

public class ScreenActivity extends Activity implements LiveCallBack {

	private SurfaceView mSurfaceView;

	private LinearLayout mLoad;
	private LinearLayout mError;

	private ServInfo mServInfo;

	private CameraInfo cameraInfo;

	private RealPlayURL mRealPlayURL;

	private String mCameraID;

	private CameraInfoEx cameraInfoEx;

	private VMSNetSDK mVmsNetSDK;

	private RtspClient mRtspHandle;

	private LiveControl mLiveControl;

	private final int GET_CAMERAINFO_NAME_PASSWORD = 1;

	private int DISTINGUISH_ERROR;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.arg1) {
			case GET_CAMERAINFO_NAME_PASSWORD:
				if (StringUtils.isEmpty(mName)
						|| StringUtils.isEmpty(mPassword)) {
					ToastUtil.showToast(ViewsUitls.getContext(), "网络异常,请稍后");
					showError();
					DISTINGUISH_ERROR = GET_CAMERAINFO_NAME_PASSWORD;
				} else {
					startLive();
				}
				break;
			case ConstantLive.RTSP_SUCCESS:
				LogUtils.e("启动取流成功");
				break;

			case ConstantLive.STOP_SUCCESS:
				LogUtils.e("停止成功");
				// ToastUtil.showToast(ViewsUitls.getContext(), "停止成功");
				break;

			case ConstantLive.START_OPEN_FAILED:
				LogUtils.e("开启播放库失败");
				showError();
				DISTINGUISH_ERROR = ConstantLive.START_OPEN_FAILED;
				break;

			case ConstantLive.PLAY_DISPLAY_SUCCESS:
				LogUtils.e("播放成功");
				// ToastUtil.showToast(ViewsUitls.getContext(), "播放成功");
				goneAll();
				break;

			case ConstantLive.RTSP_FAIL:
				LogUtils.e("RTSP链接失败");
				showError();
				DISTINGUISH_ERROR = ConstantLive.RTSP_FAIL;
				if (null != mLiveControl) {
					mLiveControl.stop();
				}
				break;

			case ConstantLive.GET_OSD_TIME_FAIL:
				ToastUtil.showToast(ViewsUitls.getContext(), "获取OSD时间失败");
				break;
			case ConstantLive.AUDIO_START_FAILED_NPLAY_STATE:
				ToastUtil.showToast(ViewsUitls.getContext(), "非播放状态不能开启音频");
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.layout_srceen_activity);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		initeActivity();
		initData();

	}

	private void startLive() {
		ThreadManager.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				String playUrl = getPlayUrl(0);
				mLiveControl.setLiveParams(playUrl, mName, mPassword);
				if (mLiveControl.LIVE_INIT == mLiveControl.getLiveState()) {
					mLiveControl.startLive(mSurfaceView);
				}

			}
		});
	}

	/**
	 * 初始化网络库和控制层对象
	 * 
	 * @since V1.0
	 */
	private void initData() {
		// 前面两个界面中已经存储的对象信息
		mServInfo = TempData.getIns().getLoginData();
		cameraInfo = TempData.getIns().getCameraInfo();

		mRealPlayURL = new RealPlayURL();

		mLiveControl = new LiveControl();
		mLiveControl.setLiveCallBack(this);

		mCameraID = cameraInfo.getId();
		cameraInfoEx = new CameraInfoEx();
		cameraInfoEx.setId(mCameraID);

		mVmsNetSDK = VMSNetSDK.getInstance();
		if (mVmsNetSDK == null) {
			Log.e(Constants.LOG_TAG, "mVmsNetSDK is null");
			return;
		}

		String serAddr = Config.getIns().getServerAddr();
		String sessionid = mServInfo.getSessionID();

		// 获取监控点详情方法
		getCameraDetailInfo(serAddr, sessionid);

		// liveCameraInfo.setParams(cameraInfoEx);
		// RTSP SDK
		mRtspHandle = RtspClient.getInstance();
		if (null == mRtspHandle) {
			Log.e(Constants.LOG_TAG, "initialize:"
					+ "RealPlay mRtspHandle is null!");
			return;
		}

	}

	private boolean getCameraDetailInfoResult;
	private String mDeviceID;
	private DeviceInfo deviceInfo;
	private boolean getDeviceInfoResult;
	private String mName;
	private String mPassword;
	private String mToken;

	/**
	 * 获取监控点详情方法
	 * 
	 * @param serAddr
	 *            服务器地址
	 * @param sessionid
	 *            会话ID
	 */
	private void getCameraDetailInfo(final String serAddr,
			final String sessionid) {
		ThreadManager.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				// 参数三四通过 cameraInfo.getId()方法 以及 创建CameraInfoEx对象并设置参数 获取
				// 参数四存储获取的信息
				getCameraDetailInfoResult = ScreenActivity.this.mVmsNetSDK
						.getCameraInfoEx(serAddr, sessionid, mCameraID,
								cameraInfoEx);
				Log.i(Constants.LOG_TAG, "result is :"
						+ getCameraDetailInfoResult);

				// 通过已经设置了参数的CameraInfoEx对象获取
				mDeviceID = cameraInfoEx.getDeviceId();

				Log.i(Constants.LOG_TAG, "mDeviceID is :" + mDeviceID);
				deviceInfo = new DeviceInfo();

				// 获取设备信息 参数三四参照上面 参数四存储获取的信息
				getDeviceInfoResult = ScreenActivity.this.mVmsNetSDK
						.getDeviceInfo(serAddr, sessionid, mDeviceID,
								deviceInfo);
				if (!getDeviceInfoResult || null == deviceInfo
						|| deviceInfo.equals("")) {
					// 获取信息失败则手动设置信息
					deviceInfo.setLoginName("admin");
					deviceInfo.setLoginPsw("12345");
				}

				// deviceInfo和cameraInfoEx两个对象存储了这一步获取的信息
				// 获取帐户密码
				mName = deviceInfo.getLoginName();
				mPassword = deviceInfo.getLoginPsw();

				Message msg = Message.obtain();
				msg.arg1 = GET_CAMERAINFO_NAME_PASSWORD;
				handler.sendMessage(msg);

			}
		});
	}

	/**
	 * 该方法是获取播放地址的，当mStreamType=2时，获取的是MAG，当mStreamType =1时获取的子码流，当mStreamType =
	 * 0时获取的是主码流 由于该方法中部分参数是监控点的属性，所以需要先获取监控点信息，具体获取监控点信息的方法见resourceActivity。
	 * 
	 * @param streamType
	 *            2、表示MAG取流方式；1、表示子码流取流方式；0、表示主码流取流方式；
	 * @return String 播放地址 ：2、表示返回的是MAG的播放地址;1、表示返回的是子码流的播放地址；0、表示返回的是主码流的播放地址。
	 * @since V1.0
	 */
	private String getPlayUrl(int streamType) {
		String url = "";

		if (mRealPlayURL == null) {
			return null;
		}

		// 获取播放Token
		mToken = ScreenActivity.this.mVmsNetSDK.getPlayToken(mServInfo
				.getSessionID());
		DebugLog.info(Constants.LOG_TAG, "mToken is :" + mToken);

		Log.d(Constants.LOG_TAG, "generateLiveUrl MagStreamSerAddr:"
				+ mServInfo.getMagServer().getMagStreamSerAddr());
		Log.d(Constants.LOG_TAG, "generateLiveUrl MagStreamSerPort:"
				+ mServInfo.getMagServer().getMagStreamSerPort());
		Log.d(Constants.LOG_TAG,
				"generateLiveUrl cameraId:" + cameraInfoEx.getId());
		Log.d(Constants.LOG_TAG, "generateLiveUrl token:" + mToken);
		Log.d(Constants.LOG_TAG, "generateLiveUrl streamType:" + streamType);
		Log.d(Constants.LOG_TAG,
				"generateLiveUrl appNetId:" + mServInfo.getAppNetId());
		Log.d(Constants.LOG_TAG,
				"generateLiveUrl deviceNetID:" + cameraInfoEx.getDeviceNetId());
		Log.d(Constants.LOG_TAG,
				"generateLiveUrl userAuthority:" + mServInfo.getUserAuthority());
		Log.d(Constants.LOG_TAG,
				"generateLiveUrl cascadeFlag:" + cameraInfoEx.getCascadeFlag());
		Log.d(Constants.LOG_TAG,
				"generateLiveUrl internet:" + mServInfo.isInternet());

		LiveInfo liveInfo = new LiveInfo();
		liveInfo.setMagIp(mServInfo.getMagServer().getMagStreamSerAddr());
		liveInfo.setMagPort(mServInfo.getMagServer().getMagStreamSerPort());
		liveInfo.setCameraIndexCode(cameraInfoEx.getId());
		liveInfo.setToken(mToken);
		// 转码不区分主子码流
		liveInfo.setStreamType(streamType);
		liveInfo.setMcuNetID(mServInfo.getAppNetId());
		liveInfo.setDeviceNetID(cameraInfoEx.getDeviceNetId());
		liveInfo.setiPriority(mServInfo.getUserAuthority());
		liveInfo.setCascadeFlag(cameraInfoEx.getCascadeFlag());

		if (deviceInfo != null) {
			if (cameraInfoEx.getCascadeFlag() == LiveInfo.CASCADE_TYPE_YES) {
				deviceInfo.setLoginName("admin");
				deviceInfo.setLoginPsw("12345");
			}
		}

		if (mServInfo.isInternet()) {
			liveInfo.setIsInternet(LiveInfo.NETWORK_TYPE_INTERNET);
			// 获取不转码地址
			liveInfo.setbTranscode(false);
			mRealPlayURL.setUrl1(mRtspHandle.generateLiveUrl(liveInfo));

			// 获取转码地址
			// 使用默认转码参数cif 128 15 h264 ps
			liveInfo.setbTranscode(true);
			mRealPlayURL.setUrl2(mRtspHandle.generateLiveUrl(liveInfo));
		} else {
			liveInfo.setIsInternet(LiveInfo.NETWORK_TYPE_LOCAL);
			liveInfo.setbTranscode(false);
			// 内网不转码
			mRealPlayURL.setUrl1(mRtspHandle.generateLiveUrl(liveInfo));
			mRealPlayURL.setUrl2("");
		}

		Log.d(Constants.LOG_TAG, "url1:" + mRealPlayURL.getUrl1());
		Log.d(Constants.LOG_TAG, "url2:" + mRealPlayURL.getUrl2());

		url = mRealPlayURL.getUrl1();
		if (streamType == 2 && mRealPlayURL.getUrl2() != null
				&& mRealPlayURL.getUrl2().length() > 0) {
			url = mRealPlayURL.getUrl2();
		}
		System.out.println("url:  " + url);
		Log.i(Constants.LOG_TAG, "mRTSPUrl" + url);

		return url;
	}

	private boolean initeActivity() {
		findViews();
		initSurface();
		return true;
	}

	private void initSurface() {
		mSurfaceView.getHolder().addCallback(new Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
			}

			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
					int width, int height) {
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {
				if (null != mLiveControl) {
					mLiveControl.stop();
				}
			}
		});
	}

	private void findViews() {
		mSurfaceView = (SurfaceView) findViewById(R.id.sv_player);
		mLoad = (LinearLayout) findViewById(R.id.ll_loading);
		mError = (LinearLayout) findViewById(R.id.ll_error);

		mError.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (DISTINGUISH_ERROR) {
				case GET_CAMERAINFO_NAME_PASSWORD:// 帐户密码获取失败
					showLoad();
					String serAddr = Config.getIns().getServerAddr();
					String sessionid = mServInfo.getSessionID();
					// 获取监控点详情方法
					getCameraDetailInfo(serAddr, sessionid);

					break;
				case ConstantLive.START_OPEN_FAILED:
					showLoad();
					mLiveControl.stop();
					startLive();
					break;
				case ConstantLive.RTSP_FAIL:// Rtsp链接失败
					showLoad();
					startLive();
					break;
				}
			}
		});
	}

	private void goneAll() {
		ViewsUitls.runInMainThread(new Runnable() {
			@Override
			public void run() {
				mLoad.setVisibility(View.GONE);
				mError.setVisibility(View.GONE);
			}
		});
	}

	private void showError() {
		ViewsUitls.runInMainThread(new Runnable() {
			@Override
			public void run() {
				mLoad.setVisibility(View.GONE);
				mError.setVisibility(View.VISIBLE);
			}
		});
	}

	private void showLoad() {
		ViewsUitls.runInMainThread(new Runnable() {
			@Override
			public void run() {
				mLoad.setVisibility(View.VISIBLE);
				mError.setVisibility(View.GONE);
			}
		});
	}

	@Override
	public void onMessageCallback(int message) {
		if (null != handler) {
			Message msg = Message.obtain();
			msg.arg1 = message;
			handler.sendMessage(msg);
		}
	}

	private int middle = 0;

	private Runnable stopRunnable;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if (middle == 0) {
				middle++;
				stopRunnable = new Runnable() {
					@Override
					public void run() {
						try {
							Thread.sleep(200);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						mLiveControl.stop();
						finish();
					}
				};
				ThreadManager.getInstance().execute(stopRunnable);
			}
			break;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		ThreadManager.getInstance().cancel(stopRunnable);

		super.onDestroy();
	}

	@Override
	protected void onStop() {
		mLiveControl.stop();
		super.onStop();
	}

	int onStart = 0;

	@Override
	protected void onStart() {

		if (onStart > 0) {
			showLoad();
			startLive();
		}
		onStart++;
		super.onStart();
	}
}

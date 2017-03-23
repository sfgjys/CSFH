package com.minji.cufcs.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.IntentFields;
import com.minji.cufcs.MainActivity;
import com.minji.cufcs.R;
import com.minji.cufcs.adapter.HomePageAdapter;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.http.download.AllotDownLoadRunnable;
import com.minji.cufcs.http.download.DownLoadResult;
import com.minji.cufcs.http.download.DownLoadThread;
import com.minji.cufcs.http.download.StartDownLoadThread;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.ui.WaterRainWorkManger;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.ViewsUitls;
import com.minji.cufcs.widget.numberprogressbar.NumberProgressBar;

import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FragmentHomePage extends Fragment implements OnItemClickListener,
		OnClickListener {

	private View view;
	private MainActivity mainActivity;
	private int interentVersionCode = 0;
	private int versionCode;
	private AlertDialog alertDialog;
	private AlertDialog downloadDialog;
	private TextView againDownLoad;
	private NumberProgressBar numberProgressBar;
	private AllotDownLoadRunnable downLoadRunnable;
	private Runnable downLoadThread;
	protected String fileName;

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.layout_homepage, null);

		GridView gridView = (GridView) view
				.findViewById(R.id.homepage_gridview);
		gridView.setSelector(android.R.color.transparent);
		gridView.setAdapter(new HomePageAdapter());
		gridView.setOnItemClickListener(this);

		mainActivity = (MainActivity) getActivity();

		// 自动更新功能======================================================================
		mainActivity.setLoadIsVisible(View.VISIBLE);
		mainActivity.setIsInterrupt(true);
		ThreadManager.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				// TODO 请求版本信息
				requestVersionCode();

				versionCode = ViewsUitls.getVersionCode(mainActivity);
				ViewsUitls.runInMainThread(new Runnable() {
					@Override
					public void run() {
						if (interentVersionCode != 0) {
							if (interentVersionCode > versionCode) {// 需要更新
								showUpdateDialog("App已有最新版本!是否进行更新?");
								mainActivity.setLoadIsVisible(View.GONE);
								mainActivity.setIsInterrupt(false);
							} else {// 不需要更新
								mainActivity.setLoadIsVisible(View.GONE);
								mainActivity.setIsInterrupt(false);
								ToastUtil.showToast(mainActivity, "APK已是最新版本!");
							}
						} else {
							mainActivity.setLoadIsVisible(View.GONE);
							mainActivity.setIsInterrupt(false);
							ToastUtil.showToast(mainActivity, "APK更新服务器正忙!");
						}
					}
				});
			}
		});
		// 自动更新功能======================================================================
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			startWaterRainWorkManger("水情雨情", position);
			break;
		case 1:
			startWaterRainWorkManger("实时监测", position);
			break;
		case 2:
			startWaterRainWorkManger("视频监控", position);
			break;
		case 3:
			startWaterRainWorkManger("运行工况", position);
			break;
		case 4:
			startWaterRainWorkManger("所长执行", position);
			break;
		case 5:
			startWaterRainWorkManger("现场运行", position);
			break;
		case 6:
			startWaterRainWorkManger("巡视检查", position);
			break;
		}

	}

	private void startWaterRainWorkManger(String title, int position) {
		Intent intent = new Intent(ViewsUitls.getContext(),
				WaterRainWorkManger.class);
		intent.putExtra(IntentFields.ACTIVITY_TITLE, title);
		intent.putExtra("qufen", position);
		getActivity().startActivity(intent);
	}

	// ======================================================================以下都是自动更新功能======================================================================
	protected void requestVersionCode() {
		HttpBasic httpBasic = new HttpBasic();
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		try {
			String address = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), "address", "");
			// result直接是数字
			String result = httpBasic.postBack(address
					+ ApiField.INTERENTVERSIONCODE, list);
			System.out.println(address + ApiField.INTERENTVERSIONCODE);
			System.out.println("result: " + result);
			if (StringUtils.interentIsNormal(result)) {
				interentVersionCode = Integer.parseInt(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showUpdateDialog(String content) {
		alertDialog = new AlertDialog.Builder(mainActivity).create();
		alertDialog.setView(new EditText(ViewsUitls.getContext()));
		// alertDialog.setCanceledOnTouchOutside(false);
		alertDialog.setCancelable(false);
		LayoutParams attributes = alertDialog.getWindow().getAttributes();// 获取对话框的属性集
		WindowManager m = mainActivity.getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		attributes.width = (int) (d.getWidth() * 0.9);
		alertDialog.show();
		// 设置对话框中自定义内容
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog_update);
		TextView textContents = (TextView) window
				.findViewById(R.id.tv_update_contents);
		textContents.setText(content);
		Button mCancel = (Button) window.findViewById(R.id.bt_update_cancel);
		Button mSure = (Button) window.findViewById(R.id.bt_update_make_sure);
		mCancel.setOnClickListener(this);
		mSure.setOnClickListener(this);

	}

	private void showDownloadDialog(String content) {
		downloadDialog = new AlertDialog.Builder(mainActivity).create();
		downloadDialog.setView(new EditText(ViewsUitls.getContext()));
		// alertDialog.setCanceledOnTouchOutside(false);
		downloadDialog.setCancelable(false);
		LayoutParams attributes = downloadDialog.getWindow().getAttributes();// 获取对话框的属性集
		WindowManager m = mainActivity.getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		attributes.width = (int) (d.getWidth() * 0.9);
		downloadDialog.show();
		// 设置对话框中自定义内容
		Window window = downloadDialog.getWindow();
		window.setContentView(R.layout.dialog_download);
		TextView textContents = (TextView) window
				.findViewById(R.id.tv_update_contents);
		textContents.setText(content);

		againDownLoad = (TextView) window.findViewById(R.id.iv_again_download);
		againDownLoad.setOnClickListener(this);
		ImageView closeDownLoad = (ImageView) window
				.findViewById(R.id.iv_download_close);
		closeDownLoad.setOnClickListener(this);
		numberProgressBar = (NumberProgressBar) window
				.findViewById(R.id.npb_update_numberbar);

		// 创建下载文件存储所在地
		File file = new File(Environment.getExternalStorageDirectory()
				.getPath(), "/Cufc/apk");
		file.mkdirs();

		// TODO 下载具体url
		downLoadRunnable = new AllotDownLoadRunnable(
				"http://223.112.181.214:7001/slfx/upload/soft/slfx.apk",
				"slfx.apk", file.getPath(), startDownLoadThread);
		ThreadManager.getInstance().execute(downLoadRunnable);

	}

	@Override
	public void onClick(View v) {

		// TODO onClick
		switch (v.getId()) {
		case R.id.bt_update_cancel:
			mainActivity.finish();
			break;
		case R.id.bt_update_make_sure:
			alertDialog.cancel();
			showDownloadDialog("正在下载最新版App!");
			break;
		case R.id.iv_again_download:
			numberProgressBar.setVisibility(View.VISIBLE);
			againDownLoad.setVisibility(View.GONE);
			ThreadManager.getInstance().cancel(downLoadRunnable);
			ThreadManager.getInstance().cancel(downLoadThread);
			ThreadManager.getInstance().execute(downLoadRunnable);
			break;
		case R.id.iv_download_close:
			// 完全关闭本包下的所有进程服务等
			// ActivityManager am = (ActivityManager)
			// getSystemService(Context.ACTIVITY_SERVICE);
			// am.killBackgroundProcesses(getPackageName());
			android.os.Process.killProcess(android.os.Process.myPid()); // 获取PID，目前获取自己的也只有该API，否则从/proc中自己的枚举其他进程吧，不过要说明的是，结束其他进程不一定有权限，不然就乱套了。
			System.exit(0); // 常规java、c#的标准退出法，返回值为0代表正常退出
			break;
		default:
			break;
		}

	}

	private void showAgainDown() {
		numberProgressBar.setVisibility(View.INVISIBLE);
		againDownLoad.setVisibility(View.VISIBLE);
	}

	private StartDownLoadThread startDownLoadThread = new StartDownLoadThread() {
		@Override
		public void linkFail() {
			ViewsUitls.runInMainThread(new Runnable() {
				@Override
				public void run() {
					ToastUtil.showToast(ViewsUitls.getContext(), "请求下载网络链接失败");
					showAgainDown();
				}
			});
		}

		@Override
		public void startDownLoadThread(int startIndex, int endIndex,
				String urlPath, String fileName) {
			System.out.println("线程id:" + "理论的位置:" + startIndex + "----"
					+ endIndex + " urlPath: " + urlPath + " fileName: "
					+ fileName);

			// 开启正式下载文件的Runnable
			downLoadThread = new DownLoadThread(startIndex, endIndex, urlPath,
					fileName, 1, downLoadResult);
			ThreadManager.getInstance().execute(downLoadThread);
			FragmentHomePage.this.fileName = fileName;

		}
	};

	private DownLoadResult downLoadResult = new DownLoadResult() {

		@Override
		public void linkFail() {
			ViewsUitls.runInMainThread(new Runnable() {
				@Override
				public void run() {
					ToastUtil.showToast(ViewsUitls.getContext(), "正式下载网络链接失败");
					showAgainDown();
				}
			});
		}

		@Override
		public void updateCurrent(final int pbCurrent) {
			System.out.println(pbCurrent);
			ViewsUitls.runInMainThread(new Runnable() {
				@Override
				public void run() {
					numberProgressBar.setProgress(pbCurrent);
				}
			});
		}

		@Override
		public void downLoadFinish(final String filePathName) {
			ViewsUitls.runInMainThread(new Runnable() {
				@Override
				public void run() {
					File file = new File(fileName + 0 + ".txt");
					file.delete();
					ToastUtil.showToast(ViewsUitls.getContext(), "下载完成");

					System.out.println(filePathName);
					downloadDialog.cancel();

					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(Uri.fromFile(new File(filePathName)),
							"application/vnd.android.package-archive");
					startActivity(intent);
					mainActivity.finish();
				}
			});
		}

	};

}

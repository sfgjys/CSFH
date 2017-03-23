package com.minji.cufcs.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.R;
import com.minji.cufcs.SpFields;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.observer.MySubject;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.ViewsUitls;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ShowAlarmActivity extends Activity implements OnClickListener {

	private String postBack;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_translucent);

		// TODO 显示报警信息

		String msg = SharedPreferencesUtil.getString(ViewsUitls.getContext(),
				SpFields.ALARM_MSG, "");
		TextView textView = (TextView) findViewById(R.id.tv_alarm_msg_tenslucent);
		Button button = (Button) findViewById(R.id.bt_tenslucent_alarm_stop);
		ImageView close = (ImageView) findViewById(R.id.iv_translucent_alarm_close);
		close.setOnClickListener(this);
		button.setOnClickListener(this);

		textView.setText(msg + "            ");
		button.setVisibility(View.GONE);

		if (!StringUtils.isEmpty(msg) && !msg.equals("报警信息,请求失败!")
				&& !msg.equals("暂无报警信息")) {
			textView.setText("  " + msg);
			button.setVisibility(View.VISIBLE);
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_translucent_alarm_close:
			finish();
			break;
		case R.id.bt_tenslucent_alarm_stop:
			// TODO 请求网络停止报警，成功则手动停止报警在finish，失败则啥也不干
			ThreadManager.getInstance().execute(new Runnable() {
				@Override
				public void run() {
					HttpBasic httpBasic = new HttpBasic();
					List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
					try {
						String address = SharedPreferencesUtil.getString(
								ViewsUitls.getContext(), "address", "");
						postBack = httpBasic.postBack(address
								+ ApiField.CANCELALARM, list);
						System.out.println("postBack:  " + postBack);
						ViewsUitls.runInMainThread(new Runnable() {
							@Override
							public void run() {
								if (StringUtils.interentIsNormal(postBack)) {
									if (postBack.contains("true")) {
										MySubject.getInstance().operation(3, 0);
										finish();
									} else if (postBack.contains("false")) {// 网络请求失败
										ToastUtil.showToast(
												ViewsUitls.getContext(),
												"停止报警失败");
									}
								} else {// 网络不正常
									ToastUtil.showToast(
											ViewsUitls.getContext(), "网络异常,停止报警失败");
								}
							}
						});
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});

			break;
		}
	}

}

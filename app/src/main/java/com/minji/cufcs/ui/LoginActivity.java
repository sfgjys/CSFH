package com.minji.cufcs.ui;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.minji.cufcs.ApiField;
import com.minji.cufcs.IntentFields;
import com.minji.cufcs.MainActivity;
import com.minji.cufcs.R;
import com.minji.cufcs.SpFields;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.sqlite.MySQLiteOpenHelper;
import com.minji.cufcs.uitls.DecoderUtil;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.ViewsUitls;

public class LoginActivity extends Activity implements OnClickListener {

	private EditText mEtPassWard;
	private EditText mEtUser;
	private CheckBox mCbIsAutoLogin;
	private Button mLogin;
	private boolean mIsAuto;
	private String mUser;
	private String mPassWord;
	private String mHistoryUser;
	private String mHistoryPassward;
	private String body;
	private MySQLiteOpenHelper mySQLiteOpenHelper;
	private SQLiteDatabase writableDatabase;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// 223.112.181.214:7001
		SharedPreferencesUtil.saveStirng(getApplicationContext(), "address",
				"http://" + "223.112.181.214:7001");

		mySQLiteOpenHelper = new MySQLiteOpenHelper(ViewsUitls.getContext());
		writableDatabase = mySQLiteOpenHelper.getWritableDatabase();

		getData();

		initView();
		
		// 自动登录
		if (mIsAuto) {
			login();
		}
	}




	private void getData() {
		mIsAuto = SharedPreferencesUtil.getboolean(ViewsUitls.getContext(),
				SpFields.IS_AUTO_LOGIN, false);
		// mHistoryPassward = SharedPreferencesUtil.getString(
		// ViewsUitls.getContext(), "mPassWord", "");
		/*
		 * 参数1:表名 参数2:要查询的字段 参数3:where表达式 参数4:替换?号的真实值 参数5:分组 null
		 * 参数6:having表达式null 参数7:排序规则 c_age desc
		 */
		Cursor cursor = writableDatabase.query("t_user",
				new String[] { "c_password" }, "c_pw>?", new String[] { "0" },
				null, null, null);
		while (cursor.moveToNext()) {
			mHistoryPassward = cursor.getString(0);
		}
		cursor.close();

		mHistoryPassward = DecoderUtil.decoder(mHistoryPassward);
		mHistoryUser = SharedPreferencesUtil.getString(ViewsUitls.getContext(),
				"mUser", "");
		mHistoryUser = DecoderUtil.decoder(mHistoryUser);
	}

	private void login() {
		getLoginInfo();
		if (!StringUtils.isEmpty(mPassWord) && !StringUtils.isEmpty(mUser)) {
			// 帐号密码有值
			// 对其进行验证
			verification();
		} else {
			// 帐号密码有空值
			ToastUtil.showToast(ViewsUitls.getContext(), "帐号密码不可以为空");
		}
	}

	private void verification() {
		// fengqingg 12345 "http://192.168.1.36:8080/slfx/login/verify.html"

		ThreadManager.getInstance().execute(new Runnable() {
			@Override
			public void run() {
				HttpBasic httpBasic = new HttpBasic();
				List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
				list.add(new BasicNameValuePair("username", mUser));
				list.add(new BasicNameValuePair("password", mPassWord));
				try {
					String address = SharedPreferencesUtil.getString(
							ViewsUitls.getContext(), "address", "");
					body = httpBasic.postBack(address + ApiField.LOGIN, list);
					System.out.println(address + ApiField.LOGIN);
					System.out.println(mUser + "---" + mPassWord);
					System.out.println(body);

					ViewsUitls.runInMainThread(new Runnable() {
						@Override
						public void run() {
							judgeBody();
						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void judgeBody() {
		if (!StringUtils.isEmpty(body)) {
			if (body.contains("true")) {
				// 帐号密码正确就存储--------------此处存储的是编码后的乱码，所以在获取的时候需要解码

				// SharedPreferencesUtil.saveStirng(ViewsUitls.getContext(),
				// "mPassWord", mPassWord);
				if (StringUtils.isEmpty(mHistoryPassward)) {// 插入数据
					ContentValues values = new ContentValues();
					values.put("c_password", mPassWord);
					values.put("c_pw", 1);
					writableDatabase.insert("t_user", null, values);
				} else {// 修改数据
					if (!mHistoryPassward.equals(mPassWord)) {
						ContentValues values = new ContentValues();
						values.put("c_password", mPassWord);
						writableDatabase.update("t_user", values, "c_pw>?",
								new String[] { "0" });
					}
				}
				writableDatabase.close();
				mySQLiteOpenHelper.close();

				SharedPreferencesUtil.saveStirng(ViewsUitls.getContext(),
						"mUser", mUser);
				SharedPreferencesUtil.saveboolean(ViewsUitls.getContext(),
						SpFields.IS_AUTO_LOGIN, mCbIsAutoLogin.isChecked());
				// 登陆成功
				Intent homePage = new Intent(ViewsUitls.getContext(),
						MainActivity.class);
				homePage.putExtra(IntentFields.ACTIVITY_TITLE, "城市防洪");
				startActivity(homePage);
				finish();
			} else if (body.contains("Error")) {
				ToastUtil.showToast(ViewsUitls.getContext(), "服务器异常，请稍候");
			} else if (body.contains("false")) {
				ToastUtil.showToast(ViewsUitls.getContext(), "帐号密码错误");
			}
		} else {
			ToastUtil.showToast(ViewsUitls.getContext(), "网络异常");
		}
	}

	/**
	 * 从EdiText中获取登录信息
	 * */
	private void getLoginInfo() {
		String mPassWord1 = mEtPassWard.getText().toString();
		String mUser1 = mEtUser.getText().toString();

		mUser = DecoderUtil.encoder(mUser1);
		mPassWord = DecoderUtil.encoder(mPassWord1);

	}

	private void initView() {
		mEtPassWard = (EditText) findViewById(R.id.et_passward);
		mEtUser = (EditText) findViewById(R.id.et_user);
		mCbIsAutoLogin = (CheckBox) findViewById(R.id.cb);
		mLogin = (Button) findViewById(R.id.bt_login);

		mCbIsAutoLogin.setChecked(mIsAuto);

		mEtPassWard.setInputType(InputType.TYPE_CLASS_TEXT
				| InputType.TYPE_TEXT_VARIATION_PASSWORD);

		// 根据历史记录来设置显示
		if (!mHistoryUser.isEmpty() && !mHistoryPassward.isEmpty()) {
			mEtUser.setText(mHistoryUser);
			mEtPassWard.setInputType(InputType.TYPE_CLASS_TEXT
					| InputType.TYPE_TEXT_VARIATION_PASSWORD);
			mEtPassWard.setText(mHistoryPassward);
		}

		// 设置按钮点击事件调用登录方法
		mLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				login();
			}
		});
		// 192.168.1.36:8080
		mLogin.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {

				String string = mEtUser.getText().toString();

				string = "223.112.181.214:7001";

				SharedPreferencesUtil.saveStirng(getApplicationContext(),
						"address", "http://" + string);
				// ToastUtil.showToast(getApplicationContext(), "修改完成");

				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {

	}
}

package com.minji.cufcs.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import com.minji.cufcs.ApiField;
import com.minji.cufcs.R;
import com.minji.cufcs.adapter.UnitDialogAdapter;
import com.minji.cufcs.base.BaseActivity;
import com.minji.cufcs.bean.AutonomouslyDate;
import com.minji.cufcs.bean.AutonomouslySaveUnitGson;
import com.minji.cufcs.bean.ImplementUnit;
import com.minji.cufcs.bean.SaveImplementThridData.Date;
import com.minji.cufcs.bean.SaveUnitGson;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.observer.MySubject;
import com.minji.cufcs.uitls.GsonTools;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.ViewsUitls;
import com.minji.cufcs.widget.PullDownItem;

public class ImplementActivity extends BaseActivity implements OnClickListener {

	private View setContent;
	private EditText mOpenOutRiver;
	private EditText mOpenInRiver;
	private EditText mRemark;
	private String startinlandlevel;
	private String startouterlevel;
	private String memo;
	private HttpBasic httpBasic;
	/** excuteid一种特殊id */
	private String excuteid;
	/** 枢纽id */
	private String stationid;
	/** 制单的唯一id */
	private String implementId;
	private String createGsonString;
	/** item条目的位数 已加1 */
	private int mPosition;
	/** 实施单的状态 */
	private String isImplement;
	/** rdsid一种特殊id */
	private String rdsid;
	/** 由于接口的错误，所以在自主中请求机组信息的id用此id */
	private String stationsid;

	private AlertDialog alertDialog;

	private List<ImplementUnit> unitNames = new ArrayList<ImplementUnit>();
	private Window window;
	private EditText mEdUnitName;
	private EditText mEdOpenTime;
	private EditText mEdCloseTime;
	private PopupWindow mUnitNamePopup;
	int mEdOpenTimetouch_flag = 0;
	int mEdCloseTimetouch_flag = 0;

	private String unitid; // 用与保存机组明细时,选择的机组id
	private String gsonString; // 保存机组时要用到的json数据

	/** 用于区分片区与自主 */
	private int areaOrautonomously;

	private String requestSaveThridResult;
	private String findUnits;
	private String saveUnitInformation;

	private int isSaveUnit = 0;// 用于判断机组是否已经录入

	private String saveClosTime;
	private String saveOpenTime;

	@Override
	public void onCreateContent() {

		showBack();

		initIntent();

		setContent = setContent(R.layout.layout_implement);
		initChildView();

		// 设置时间
		initCalendar();
		setDateTime();

		httpBasic = new HttpBasic();
	}

	private void initIntent() {

		implementId = mIntentDate.getStringExtra("implementId");
		stationid = mIntentDate.getStringExtra("stationid");
		stationsid = mIntentDate.getStringExtra("stationsid");
		excuteid = mIntentDate.getStringExtra("excuteid");

		startinlandlevel = mIntentDate.getStringExtra("startinlandlevel");
		startouterlevel = mIntentDate.getStringExtra("startouterlevel");
		memo = mIntentDate.getStringExtra("memo");
		isImplement = mIntentDate.getStringExtra("isImplement");
		rdsid = mIntentDate.getStringExtra("rdsid");
		mPosition = mIntentDate.getIntExtra("mPosition", 0);

		areaOrautonomously = mIntentDate.getIntExtra("areaOrautonomously", 0);

	}

	private void initChildView() {

		mOpenOutRiver = (EditText) setContent
				.findViewById(R.id.et_open_out_river);
		setTextViewText(mOpenOutRiver, startouterlevel);
		mOpenInRiver = (EditText) setContent
				.findViewById(R.id.et_open_in_river);
		setTextViewText(mOpenInRiver, startinlandlevel);
		mRemark = (EditText) setContent.findViewById(R.id.et_remark);
		setTextViewText(mRemark, memo);

		ImageView mAddUnit = (ImageView) setContent
				.findViewById(R.id.iv_add_unit_all);
		Button mImplementSave = (Button) setContent
				.findViewById(R.id.bt_implement_save);
		mAddUnit.setOnClickListener(this);
		mImplementSave.setOnClickListener(this);

	}

	private void setTextViewText(EditText textView, String string) {
		if (StringUtils.isEmpty(string)) {
			textView.setText("");
		} else {
			textView.setText(string);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_add_unit_all:

			if (!(unitNames.size() > 0)) {
				setLoadIsVisible(View.VISIBLE);
				ThreadManager.getInstance().execute(new Runnable() {
					@Override
					public void run() {
						inquireImplementUnit();
					}
				});
			} else {
				showPesonsCustomDialog();
			}
			break;
		case R.id.bt_implement_save:
			if (isSaveUnit > 0) {
				createSaveGson();// 在创建json数据时就区分了
				// TODO 最后结果暂时没有测试
				setLoadIsVisible(View.VISIBLE);
				ThreadManager.getInstance().execute(new Runnable() {
					@Override
					public void run() {
						saveImplementThrid();
					}
				});
			} else {
				ToastUtil.showToast(ViewsUitls.getContext(), "请先录入机组明细");
			}
			break;
		case R.id.bt_cancel:
			alertDialog.cancel();
			break;
		case R.id.bt_make_sure:

			saveClosTime = mEdCloseTime.getText().toString();
			saveOpenTime = mEdOpenTime.getText().toString();
			String unitName = mEdUnitName.getText().toString();
			if (!StringUtils.isEmpty(unitName)
					&& !StringUtils.isEmpty(saveOpenTime)
					&& !StringUtils.isEmpty(saveClosTime)) {
				if (gsonString == null) {
					if (areaOrautonomously == 1) {
						SaveUnitGson saveUnitGson = new SaveUnitGson(stationid,
								excuteid, rdsid);
						gsonString = GsonTools.createGsonString(saveUnitGson);
						System.out.println("111111gsonString: "+gsonString);
					}
					if (areaOrautonomously == 2) {
						System.out.println(stationsid);
						AutonomouslySaveUnitGson autonomouslySaveUnitGson = new AutonomouslySaveUnitGson(
								stationsid, excuteid, stationid);
						gsonString = GsonTools
								.createGsonString(autonomouslySaveUnitGson);
						System.out.println("222222gsonString: "+gsonString);
					}
				}
				setLoadIsVisible(View.VISIBLE);
				saveUnitInformation();

				alertDialog.cancel();
			} else {
				ToastUtil.showToast(ViewsUitls.getContext(), "明细不能为空");
			}
			break;
		}
	}

	private void saveUnitInformation() {
		ThreadManager.getInstance().execute(new Runnable() {

			@Override
			public void run() {
				String type = "";
				if ("已实施未完成".equals(isImplement)) {
					type = "2";
				} else if ("未查看".equals(isImplement)) {
					type = "1";
				} else if ("已查看未实施".equals(isImplement)) {
					type = "1";
				}
				List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
				list.add(new BasicNameValuePair("unitid", unitid));
				list.add(new BasicNameValuePair("btime", saveOpenTime));
				list.add(new BasicNameValuePair("etime", saveClosTime));
				list.add(new BasicNameValuePair("jsonStr", gsonString));
				list.add(new BasicNameValuePair("type", type));
				try {
					String address = SharedPreferencesUtil.getString(
							ViewsUitls.getContext(), "address", "");

					if (areaOrautonomously == 1) {
						saveUnitInformation = httpBasic.postBack(address
								+ ApiField.IMPLEMENTSAVEUNIT, list);
						System.out
								.println(address + ApiField.IMPLEMENTSAVEUNIT);
					}
					if (areaOrautonomously == 2) {
						saveUnitInformation = httpBasic
								.postBack(
										address
												+ ApiField.AUTONOMOUSLYIMPLEMENTSAVEUNIT,
										list);
						System.out.println(address
								+ ApiField.AUTONOMOUSLYIMPLEMENTSAVEUNIT);
					}
					System.out.println(saveUnitInformation);
					ViewsUitls.runInMainThread(new Runnable() {
						@Override
						public void run() {
							setLoadIsVisible(View.GONE);
							if (StringUtils
									.interentIsNormal(saveUnitInformation)) {
								if (saveUnitInformation.contains("true")) {
									isSaveUnit++;
									ToastUtil.showToast(
											ViewsUitls.getContext(), "机组明细录入成功");
								} else if (saveUnitInformation
										.contains("false")) {
									ToastUtil.showToast(
											ViewsUitls.getContext(), "机组明细录入失败");
								}
							} else {
								ToastUtil.showToast(ViewsUitls.getContext(),
										"网络异常,请稍后");
							}
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void inquireImplementUnit() {

		System.out.println("stationid: " + stationid);
		System.out.println("stationsid: " + stationsid);
		try {
			String address = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), "address", "");
			if (areaOrautonomously == 1) {
				List<BasicNameValuePair> list1 = new ArrayList<BasicNameValuePair>();
				list1.add(new BasicNameValuePair("sid", stationid));
				findUnits = httpBasic.postBack(address
						+ ApiField.INQUIREIMPLEMENTUNIT, list1);
			}
			if (areaOrautonomously == 2) {
				List<BasicNameValuePair> list2 = new ArrayList<BasicNameValuePair>();
				list2.add(new BasicNameValuePair("sid", stationsid));
				findUnits = httpBasic.postBack(address
						+ ApiField.AUTONOMOUSLYINQUIREIMPLEMENTUNIT, list2);
			}
			System.out.println(findUnits);
			if (StringUtils.interentIsNormal(findUnits)) {
				JSONArray jsonArray = new JSONArray(findUnits);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject optJSONObject = jsonArray.optJSONObject(i);
					unitNames.add(new ImplementUnit(optJSONObject
							.optString("id"), optJSONObject.optString("name")));
				}
				ViewsUitls.runInMainThread(new Runnable() {
					@Override
					public void run() {
						setLoadIsVisible(View.GONE);
						if (!(unitNames.size() > 0)) {
							ToastUtil.showToast(ViewsUitls.getContext(),
									"服务器异常,无机组信息");
						} else {
							showPesonsCustomDialog();
						}
					}
				});
			} else {
				ViewsUitls.runInMainThread(new Runnable() {
					@Override
					public void run() {
						setLoadIsVisible(View.GONE);
						ToastUtil.showToast(ViewsUitls.getContext(), "网络异常,请稍后");
					}
				});
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void showPesonsCustomDialog() {

		alertDialog = new AlertDialog.Builder(this).create();
		LayoutParams attributes = alertDialog.getWindow().getAttributes();// 获取对话框的属性集
		WindowManager m = this.getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		attributes.width = (int) (d.getWidth());
		// attributes.height = (int) (d.getHeight() * 0.5);
		alertDialog.show();
		// 设置对话框中自定义内容

		window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog_imlement_unit);

		initDialogView();

		Button mCancel = (Button) window.findViewById(R.id.bt_cancel);
		Button mSure = (Button) window.findViewById(R.id.bt_make_sure);
		mCancel.setOnClickListener(this);
		mSure.setOnClickListener(this);
	}

	private void initDialogView() {
		PullDownItem unitNmae = (PullDownItem) window
				.findViewById(R.id.pd_imlement_unit_name);
		mEdUnitName = unitNmae.getmEd();
		mEdUnitName.setOnTouchListener(new OnTouchListener() {
			int touch_flag = 0;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				touch_flag++;
				if (touch_flag == 2) {
					showPuppo();// 展示机组
					touch_flag = 0;
				}
				return false;
			}
		});
		PullDownItem openTime = (PullDownItem) window
				.findViewById(R.id.pd_imlement_unit_open_time);
		mEdOpenTime = openTime.getmEd();
		mEdOpenTime.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mEdOpenTimetouch_flag++;
				if (mEdOpenTimetouch_flag == 2) {
					// 弹出时间对话框
					Message msg = new Message();
					if (mEdOpenTime.equals((EditText) v)) {
						msg.what = SHOW_DATAPICK_Open;
					}
					dateandtimeHandler.sendMessage(msg);
				}
				return false;
			}
		});
		PullDownItem closeTime = (PullDownItem) window
				.findViewById(R.id.pd_imlement_unit_close_time);
		mEdCloseTime = closeTime.getmEd();
		mEdCloseTime.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mEdCloseTimetouch_flag++;
				if (mEdCloseTimetouch_flag == 2) {
					// 自己业务
					Message msg = new Message();
					if (mEdCloseTime.equals((EditText) v)) {
						msg.what = SHOW_DATAPICK_Close;
					}
					dateandtimeHandler.sendMessage(msg);
				}
				return false;
			}
		});
	}

	private void showPuppo() {
		View view = ViewsUitls.inflate(R.layout.layout_only_listview_puppo);
		ListView listView = (ListView) view.findViewById(R.id.lv_only);
		listView.setSelector(android.R.color.transparent);
		listView.setBackgroundResource(R.color.white);

		listView.setAdapter(new UnitDialogAdapter(unitNames));
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mEdUnitName.setText(unitNames.get(position).getName());
				unitid = unitNames.get(position).getId();
				mUnitNamePopup.dismiss();
			}
		});

		int height = mEdUnitName.getHeight();
		if (mUnitNamePopup == null) {
			mUnitNamePopup = new PopupWindow(view, mEdUnitName.getWidth(),
					4 * height, true);
			mUnitNamePopup.setBackgroundDrawable(new ColorDrawable());
		}
		mUnitNamePopup.showAsDropDown(mEdUnitName);
	}

	private void createSaveGson() {
		String remark = mRemark.getText().toString();
		if (areaOrautonomously == 1) {
			Date date = new Date(stationid, excuteid, rdsid, remark);
			createGsonString = GsonTools.createGsonString(date);
		}
		if (areaOrautonomously == 2) {
			AutonomouslyDate autonomouslyDate = new AutonomouslyDate(stationsid,
					excuteid, stationid, remark);
			createGsonString = GsonTools.createGsonString(autonomouslyDate);
		}
		System.out.println(createGsonString);
	}

	private void saveImplementThrid() {
		String type = "";
		if ("已实施未完成".equals(isImplement)) {
			type = "2";
		} else if ("未查看".equals(isImplement)) {
			type = "1";
		} else if ("已查看未实施".equals(isImplement)) {
			type = "1";
		}
		String openOutRiver = mOpenOutRiver.getText().toString();
		String openInRiver = mOpenInRiver.getText().toString();

		// 传递的字段也一样，就json数据的内容不同
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("jsonStr", createGsonString));
		list.add(new BasicNameValuePair("startouterlevel", openOutRiver));
		list.add(new BasicNameValuePair("startinlandlevel", openInRiver));
		list.add(new BasicNameValuePair("type", type));
		try {
			String address = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), "address", "");

			if (areaOrautonomously == 1) {
				requestSaveThridResult = httpBasic.postBack(address
						+ ApiField.SAVEIMPLEMENTTHRID, list);
				System.out.println(address + ApiField.SAVEIMPLEMENTTHRID);
			}
			if (areaOrautonomously == 2) {
				requestSaveThridResult = httpBasic.postBack(address
						+ ApiField.AUTONOMOUSLYSAVEIMPLEMENTTHRID, list);
				System.out.println(address
						+ ApiField.AUTONOMOUSLYSAVEIMPLEMENTTHRID);
			}

			System.out.println(requestSaveThridResult);
			ViewsUitls.runInMainThread(new Runnable() {
				@Override
				public void run() {
					setLoadIsVisible(View.GONE);
					if (StringUtils.interentIsNormal(requestSaveThridResult)) {
						if (requestSaveThridResult.contains("true")) {
							ToastUtil.showToast(ViewsUitls.getContext(), "保存成功");
							MySubject.getInstance().operation(mPosition,
									areaOrautonomously);
							finish();
						} else if (requestSaveThridResult.contains("false")) {
							ToastUtil.showToast(ViewsUitls.getContext(), "保存失败");
						}
					} else {
						ToastUtil.showToast(ViewsUitls.getContext(), "网络异常,请稍后");
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private int distinguishTime = 0;
	private static final int SHOW_DATAPICK_Open = 0;
	private static final int SHOW_DATAPICK_Close = 1;
	private static final int DATE_DIALOG_ID = 1;
	private static final int TIME_DIALOG_ID = 3;
	public int mYear;
	public int mMonth;
	public int mDay;
	public int mHour;
	public int mMinute;
	/**
	 * 处理日期和时间控件的Handler
	 */
	Handler dateandtimeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_DATAPICK_Open:
				mEdOpenTimetouch_flag = 0;
				distinguishTime = 1;
				showDialog(DATE_DIALOG_ID);
				break;
			case SHOW_DATAPICK_Close:
				mEdCloseTimetouch_flag = 0;
				distinguishTime = 2;
				showDialog(DATE_DIALOG_ID);
				break;
			}
		}
	};

	/**
	 * 更新日期显示
	 */
	private void updateDateDisplay() {
		compareTime();
		StringBuilder stringBuilder = new StringBuilder().append(mYear)
				.append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay)
				.append(" ").append((mHour < 10) ? "0" + mHour : mHour)
				.append(":").append((mMinute < 10) ? "0" + mMinute : mMinute);
		stringBuilder.append(":").append("00");

		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		if (distinguishTime == 1) {
			if (StringUtils.isEmpty(mEdCloseTime.getText().toString())) {
				mEdOpenTime.setText(stringBuilder);
			} else {// 要比较
				try {
					java.util.Date openTime = simpleDateFormat
							.parse(stringBuilder.toString());
					java.util.Date closeTime = simpleDateFormat
							.parse(mEdCloseTime.getText().toString());
					long time1 = closeTime.getTime();
					long time2 = openTime.getTime();
					if (time1 > time2) {// 关闭时间大于开启时间
						mEdOpenTime.setText(stringBuilder);
					} else {
						mEdOpenTime.setText(mEdCloseTime.getText().toString());
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		if (distinguishTime == 2) {
			if (StringUtils.isEmpty(mEdOpenTime.getText().toString())) {
				mEdCloseTime.setText(stringBuilder);// opentime为空直接设置
			} else {// 要比较
				try {
					java.util.Date closeTime = simpleDateFormat
							.parse(stringBuilder.toString());
					java.util.Date openTime = simpleDateFormat
							.parse(mEdOpenTime.getText().toString());
					long time1 = closeTime.getTime();
					long time2 = openTime.getTime();
					if (time1 > time2) {// 关闭时间大于开启时间
						mEdCloseTime.setText(stringBuilder);
					} else {
						mEdCloseTime.setText(mEdOpenTime.getText().toString());
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/** 比较大小，使得选择的时间不大于现在的时间 */
	private void compareTime() {
		final Calendar c = Calendar.getInstance();
		int nowYear = c.get(Calendar.YEAR);
		int nowMonth = c.get(Calendar.MONTH);
		int nowDay = c.get(Calendar.DAY_OF_MONTH);
		int nowHour = c.get(Calendar.HOUR_OF_DAY);
		int nowMinute = c.get(Calendar.MINUTE);

		if (nowYear <= mYear) {
			mYear = nowYear;
			if (nowMonth <= mMonth) {
				mMonth = nowMonth;
				if (nowDay <= mDay) {
					mDay = nowDay;
					if (nowHour <= mHour) {
						mHour = nowHour;
						if (nowMinute <= mMinute) {
							mMinute = nowMinute;
						}
					}
				}
			}
		}
	}

	/**
	 * 日期控件的事件
	 */
	public DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			mYear = year;
			mMonth = monthOfYear;
			mDay = dayOfMonth;
			updateDateDisplay();
			showDialog(TIME_DIALOG_ID);
		}
	};
	/**
	 * 时间控件事件
	 */
	public TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			mHour = hourOfDay;
			mMinute = minute;
			updateDateDisplay();
		}
	};

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {

		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, mHour, mMinute,
					true);
		}
		return null;

	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		case TIME_DIALOG_ID:
			((TimePickerDialog) dialog).updateTime(mHour, mMinute);
			break;
		}
	}

	/**
	 * 设置日期
	 */
	private void setDateTime() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		setTimeOfDay();
	}

	/**
	 * 设置时间
	 */
	private void setTimeOfDay() {
		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
	}

	private void initCalendar() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
	}

}

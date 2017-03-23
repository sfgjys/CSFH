package com.minji.cufcs.fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TimePicker;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.ContentPage.ResultState;
import com.minji.cufcs.IntentFields;
import com.minji.cufcs.R;
import com.minji.cufcs.adapter.PollingHubAdapter;
import com.minji.cufcs.adapter.PollingNumAdapter;
import com.minji.cufcs.adapter.PollingPersonAdapter;
import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.bean.PollingDegreeVerify;
import com.minji.cufcs.bean.PollingHubName;
import com.minji.cufcs.bean.PollingPerson;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.ui.PollingEighteen;
import com.minji.cufcs.uitls.GsonTools;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.ViewsUitls;
import com.minji.cufcs.widget.PullDownItem;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FragmentPolling extends BaseFragment implements OnClickListener {

	private EditText showDate = null;
	private ImageView pickDate = null;
	private Button mButtonNext;
	private ImageView mHubImage;
	private EditText mHubText;
	private ImageView mNumberImage;
	private EditText mNumberText;
	private ImageView mPersonImage;
	private EditText mPersonText;

	private static final int SHOW_DATAPICK = 0;

	private static final int DATE_DIALOG_ID = 1;
	private static final int TIME_DIALOG_ID = 3;

	public int mYear;
	public int mMonth;
	public int mDay;
	public int mHour;
	public int mMinute;

	private String mPollingHubname;
	private String mPollingPerson;
	private List<PollingHubName> hubNames;
	private List<PollingPerson> persons;
	private View view;
	private PopupWindow mHubNamePopup;
	private PopupWindow mPersonPopup;
	private PopupWindow mNumberPopup;
	private ListView mHubNamePopupList;
	private ListView mPersonPopupList;
	private ListView mNumberPopupList;

	private PollingDegreeVerify pollingDegreeVerify;
	private FrameLayout mVerifyLoad;
	private HttpBasic httpBasic;
	private AlertDialog alertDialog;

	@Override
	protected View onCreateSuccessView() {
		view = ViewsUitls.inflate(R.layout.layout_polling);

		// 隐藏新建的图标
//		WaterRainWorkManger activity = (WaterRainWorkManger) getActivity();
//		if (activity != null) {
//			ImageView mAddPolling = activity.mAddPolling;
//			mAddPolling.setVisibility(View.INVISIBLE);
//		}

		pollingDegreeVerify = new PollingDegreeVerify();

		initializeViews();

		// 设置时间
		initCalendar();
		setDateTime();

		// 设置枢纽人员次数三个下拉框
		setDropDown();
		return view;
	}

	private void setDropDown() {
		initListView();
		mHubImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDropDown1(mHubNamePopupList, mHubText);
			}
		});
		mPersonImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// showDropDown2(mPersonPopupList, mPersonText);
				showPesonsCustomDialog();
			}
		});
		mNumberImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDropDown4(mNumberPopupList, mNumberText);
			}
		});
	}

	/**
	 * 展示巡检人员多选对话框
	 * */
	private void showPesonsCustomDialog() {
		alertDialog = new AlertDialog.Builder(getActivity()).create();
		LayoutParams attributes = alertDialog.getWindow().getAttributes();// 获取对话框的属性集
		WindowManager m = getActivity().getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		attributes.width = (int) (d.getWidth() * 0.9);
//		attributes.height = (int) (d.getHeight() * 0.5);
		alertDialog.show();
		// 设置对话框中自定义内容
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog_polling_entering_frist_step);
		ListView listView = (ListView) window
				.findViewById(R.id.lv_polling_frist_step);
		pollingPersonAdapter = new PollingPersonAdapter(persons);
		listView.setAdapter(pollingPersonAdapter);
		listView.setVerticalScrollBarEnabled(false);

		Button mCancel = (Button) window.findViewById(R.id.bt_cancel);
		Button mSure = (Button) window.findViewById(R.id.bt_make_sure);
		mCancel.setOnClickListener(this);
		mSure.setOnClickListener(this);
	}

	private void initListView() {
		mHubNamePopupList = new ListView(ViewsUitls.getContext());
		setListViewAttr(mHubNamePopupList);
		mPersonPopupList = new ListView(ViewsUitls.getContext());
		setListViewAttr(mPersonPopupList);
		mNumberPopupList = new ListView(ViewsUitls.getContext());
		setListViewAttr(mNumberPopupList);
		mHubNamePopupList.setAdapter(new PollingHubAdapter(hubNames));
		mPersonPopupList.setAdapter(new PollingPersonAdapter(persons));
		mNumberPopupList.setAdapter(new PollingNumAdapter());
		mHubNamePopupList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mHubText.setText(hubNames.get(position).getName());
				pollingDegreeVerify.setSid(hubNames.get(position).getId());
				mHubNamePopup.dismiss();
			}
		});
		mPersonPopupList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mPersonText.setText(persons.get(position).getEname());
				mPersonPopup.dismiss();
			}
		});
		mNumberPopupList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == 0) {
					mNumberText.setText("第一次");
					pollingDegreeVerify.setDegree("0");
				} else {
					mNumberText.setText("第二次");
					pollingDegreeVerify.setDegree("1");
				}
				mNumberPopup.dismiss();
			}
		});
	}

	private void setListViewAttr(ListView listView) {
		listView.setBackgroundResource(R.color.white);
	}

	/**
	 * 具体展示popupwindow的方法
	 * */
	protected void showDropDown1(ListView listView, EditText editText) {
		int height = mHubText.getHeight();
		if (mHubNamePopup == null) {
			mHubNamePopup = new PopupWindow(listView, editText.getWidth(), 4*height,
					true);
			mHubNamePopup.setBackgroundDrawable(new ColorDrawable());
		}
		mHubNamePopup.showAsDropDown(editText);
	}

	protected void showDropDown2(ListView listView, EditText editText) {
		int height = mHubText.getHeight();
		if (mPersonPopup == null) {
			mPersonPopup = new PopupWindow(listView, editText.getWidth(), 2*height,
					true);
			mPersonPopup.setBackgroundDrawable(new ColorDrawable());
		}
		mPersonPopup.showAsDropDown(editText);
	}

	protected void showDropDown4(ListView listView, EditText editText) {
		int height = mHubText.getHeight();
		if (mNumberPopup == null) {
			mNumberPopup = new PopupWindow(listView, editText.getWidth(), 2*height,
					true);
			mNumberPopup.setBackgroundDrawable(new ColorDrawable());
		}
		mNumberPopup.showAsDropDown(editText);
	}

	@Override
	protected ResultState onLoad() {

		requestDate();
		parseJson();

		if (hubNames == null || persons == null) {
			// 网络请求后承载数据的集和为空说明网络请求失败
			return ResultState.STATE_ERROR;
		} else {
			if (hubNames.size() == 0 || persons.size() == 0) {
				// 网络请求后承载数据的集为和不为空但是没有数据说明网络请求成功，但不用显示数据
				return ResultState.STATE_EMPTY;
			} else {
				// 成功且有数据
				return ResultState.STATE_SUCCESS;
			}
		}
	}

	private void requestDate() {
		httpBasic = new HttpBasic();
		List<BasicNameValuePair> list1 = new ArrayList<BasicNameValuePair>();
		List<BasicNameValuePair> list2 = new ArrayList<BasicNameValuePair>();
		try {
			String address = SharedPreferencesUtil.getString(ViewsUitls.getContext(), "address", "");
			mPollingHubname = httpBasic.postBack(address+ApiField.POLLING_HUBNAME,
					list1);
			mPollingPerson = httpBasic.postBack(address+ApiField.POLLING_PERSON, list2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseJson() {
		try {
			if (StringUtils.isEmpty(mPollingHubname)
					|| StringUtils.isEmpty(mPollingPerson)
					|| "{'relogin':true}".equals(mPollingHubname)
					|| "{'relogin':true}".equals(mPollingPerson)
					|| "Error Response: ".equals(mPollingHubname)
					|| "Error Response: ".equals(mPollingPerson)) {
				System.out.println("请求网络后返回的数据有问题");
				hubNames = null;
				persons = null;
			} else {
				ArrayList<PollingHubName> arrayList = new ArrayList<PollingHubName>();
				ArrayList<PollingPerson> arrayList2 = new ArrayList<PollingPerson>();
				JSONArray arrhubNames = new JSONArray(mPollingHubname);
				for (int i = 0; i < arrhubNames.length(); i++) {
					JSONObject obj = arrhubNames.optJSONObject(i);
					PollingHubName info = new PollingHubName();
					info.setId(obj.optString("id"));
					info.setName(obj.optString("name"));
					arrayList.add(info);
				}
				JSONArray arrpersons = new JSONArray(mPollingPerson);
				for (int i = 0; i < arrpersons.length(); i++) {
					JSONObject obj = arrpersons.optJSONObject(i);
					PollingPerson info = new PollingPerson();
					info.setDname(obj.optString("dname"));
					info.setEname(obj.optString("ename"));
					info.setId(obj.optString("id"));
					info.setUsername(obj.optString("username"));
					arrayList2.add(info);
				}
				hubNames = arrayList;
				persons = arrayList2;

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onSubClassOnCreateView() {
		loadDataAndRefresh();
	}

	/**
	 * 初始化控件和UI视图
	 */
	private void initializeViews() {
		mVerifyLoad = (FrameLayout) view.findViewById(R.id.fl_loading);

		PullDownItem mItemDown = (PullDownItem) view
				.findViewById(R.id.pd_polling_time);
		PullDownItem mHubnameDown = (PullDownItem) view
				.findViewById(R.id.pd_polling_hubname);
		PullDownItem mNumberDown = (PullDownItem) view
				.findViewById(R.id.pd_polling_number);
		PullDownItem mPersonDown = (PullDownItem) view
				.findViewById(R.id.pd_polling_person);

		mHubImage = mHubnameDown.getmDown();
		mHubText = mHubnameDown.getmEd();

		mNumberImage = mNumberDown.getmDown();
		mNumberText = mNumberDown.getmEd();

		mPersonImage = mPersonDown.getmDown();
		mPersonText = mPersonDown.getmEd();

		showDate = mItemDown.getmEd();
		pickDate = mItemDown.getmDown();

		pickDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message msg = new Message();
				if (pickDate.equals((ImageView) v)) {
					msg.what = FragmentPolling.SHOW_DATAPICK;
				}
				FragmentPolling.this.dateandtimeHandler.sendMessage(msg);
			}
		});

		mButtonNext = (Button) view.findViewById(R.id.bt_polling_next);
		mButtonNext.setOnClickListener(this);

	}

	private void initCalendar() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);

		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
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

		updateDateDisplay();
	}

	/**
	 * 设置时间
	 */
	private void setTimeOfDay() {
		final Calendar c = Calendar.getInstance();
		mHour = c.get(Calendar.HOUR_OF_DAY);
		mMinute = c.get(Calendar.MINUTE);
	}

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
		showDate.setText(stringBuilder);
		stringBuilder.append(":").append("00");
		pollingDegreeVerify.setParttime(stringBuilder.toString());
	}

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
	 * 处理日期和时间控件的Handler
	 */
	Handler dateandtimeHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case FragmentPolling.SHOW_DATAPICK:
				getActivity().showDialog(DATE_DIALOG_ID);
				break;
			}
		}
	};
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
			getActivity().showDialog(TIME_DIALOG_ID);
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
	private PollingPersonAdapter pollingPersonAdapter;
	private String gsonString;
	private List<BasicNameValuePair> list;
	private String saveFourId;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_polling_next:
			if (StringUtils.isEmpty(showDate.getText().toString())
					|| StringUtils.isEmpty(mHubText.getText().toString())
					|| StringUtils.isEmpty(mNumberText.getText().toString())
					|| StringUtils.isEmpty(mPersonText.getText().toString())) {
				ToastUtil.showToast(ViewsUitls.getContext(), "参数不能为空");
			} else {
				mVerifyLoad.setVisibility(View.VISIBLE);
				// 验证的json数据在加两个唯一的数据
				pollingDegreeVerify.setId("0");
				pollingDegreeVerify.setState("0");
				gsonString = GsonTools.createGsonString(pollingDegreeVerify);
				ThreadManager.getInstance().execute(new Runnable() {
					@Override
					public void run() {
						interentVerify(gsonString);
					}
				});
			}
			break;
		case R.id.bt_cancel:
			alertDialog.cancel();
			break;
		case R.id.bt_make_sure:
			// 创建一个新的集合存储id
			ArrayList<String> arrayList = new ArrayList<String>();
			// 通过适配器获取被选中的checkbox的集合
			List<Integer> toBeSelected = pollingPersonAdapter.getToBeSelected();
			StringBuilder stringBuilder = new StringBuilder();
			int size = toBeSelected.size();
			for (int i = 0; i < size; i++) {
				// 遍历拼接字符串
				Integer integer = toBeSelected.get(i);// 获取被选中checkbox代表的数据在persons集合中所在的位置
				String ename = persons.get(integer.intValue()).getEname();
				stringBuilder.append(ename + ";");
				// 将被选中的人员对应的id放入json对象
				String id = persons.get(integer.intValue()).getId();
				arrayList.add(id);
			}
			pollingDegreeVerify.setPp(arrayList);
			arrayList = null;
			mPersonText.setText(stringBuilder.toString());
			alertDialog.cancel();

			break;
		}
	}

	private void interentVerify(String gsonString) {

		httpBasic = new HttpBasic();
		list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("jsonStr", gsonString));
		try {
			// 请求网络来验证次数
			String address = SharedPreferencesUtil.getString(ViewsUitls.getContext(), "address", "");
			final String isVerify = httpBasic.postBack(address+ApiField.POLLING_NUMBER,
					list);
			System.out.println("isVerify: " + isVerify);
			boolean interentIsNormal = StringUtils.interentIsNormal(isVerify);
			if (interentIsNormal) {// 正常
				if (isVerify.contains("true")) {
					// 在跳转前请求网络来保存四个参数的请求
					String saveFour = saveFour();
					System.out.println("saveFour: " + saveFour);
					if (!StringUtils.isEmpty(saveFour)
							&& saveFour.contains("id")) {
						saveFourId = saveFourId.substring(6, 42);
						ViewsUitls.runInMainThread(new Runnable() {
							@Override
							public void run() {
								Intent intent = new Intent(getActivity(),
										PollingEighteen.class);
								intent.putExtra(IntentFields.ACTIVITY_TITLE,
										"巡检内容");
								intent.putExtra(IntentFields.SAVEFOURID,
										saveFourId);
								getActivity().startActivity(intent);
								getActivity().finish();
							}
						});
					}
				} else {
					ViewsUitls.runInMainThread(new Runnable() {
						@Override
						public void run() {
							ToastUtil.showToast(ViewsUitls.getContext(),
									"本表单已存在，请修改本月巡视次数或巡视枢纽");
						}
					});

				}
			}else{
				ViewsUitls.runInMainThread(new Runnable() {
					@Override
					public void run() {
						ToastUtil.showToast(ViewsUitls.getContext(),
								"网络异常,请稍候");
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ViewsUitls.runInMainThread(new Runnable() {
			@Override
			public void run() {
				mVerifyLoad.setVisibility(View.GONE);
			}
		});
	}

	private String saveFour() {
		try {
			String address = SharedPreferencesUtil.getString(ViewsUitls.getContext(), "address", "");
			saveFourId = httpBasic.postBack(address+ApiField.POLLING_SAVE_FOUR, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return saveFourId;
	}
}

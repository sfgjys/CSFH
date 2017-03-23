package com.minji.cufcs.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseActivity;
import com.minji.cufcs.chart.AverageCubicTemperatureChart;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.ViewsUitls;

public class ChartActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout relativeLayout;
	private Button refresh;
	private GraphicalView cubeLineChartView;
	// 此为曲线图的曲线数以及其名称
	private String[] titles = new String[] { "内河水位", "外河水位" };
	private XYSeries inRiverSeries;
	private XYSeries outRiverSeries;
	private double[] xLabels;

	private double[] yInRiver;
	private double[] yOutRiver;
	private FrameLayout mFlChart;
	private View view;
	private EditText selectTime;
	private String mSelectTime;
	private String chartHubname;
	private Runnable requestCurveDate;

	private String stationId;
	private XYMultipleSeriesRenderer renderer;
	private double yMax;
	private double yMin;

	@Override
	public void onCreateContent() {
		showBack();

		chartHubname = mIntentDate.getStringExtra("chartHubname");
		stationId = mIntentDate.getStringExtra("stationId");

		view = setContent(R.layout.layout_chart);

		// 初始化控件
		initAllView();

		// 代码设定按钮的高
		amendButtonHeight();

		// 设置选择时间的前置准备
		initCalendar();
		// 设置选择时间
		setSelectTime();

		// 创建图表控件添加进布局
		addGraphicalView();
		
		StringBuilder stringBuilder = new StringBuilder().append(mYear)
				.append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay);
		selectTime.setText(stringBuilder.toString());
		getCurveData();
	}

	private static final int SELECT_TIME = 0;
	private int touch_flag = 0;
	private static final int DATE_DIALOG_ID = 1;
	public int mYear;
	public int mMonth;
	public int mDay;

	private void setSelectTime() {
		selectTime.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				touch_flag++;
				if (touch_flag == 2) {
					// 弹出时间对话框
					Message msg = new Message();
					if (selectTime.equals((EditText) v)) {
						msg.what = SELECT_TIME;
					}
					handler.sendMessage(msg);
				}

				return false;
			}
		});

	}

	private void addGraphicalView() {
		// 获取已经设置好界面的图表XYMultipleSeriesRenderer

		AverageCubicTemperatureChart averageCubicTemperatureChart = new AverageCubicTemperatureChart();

		renderer = averageCubicTemperatureChart.execute();
		renderer.setChartTitle(chartHubname);
		renderer.setChartTitleTextSize(ViewsUitls.dptopx(20));
		// TODO 是否显示节点数据
		// SimpleSeriesRenderer seriesrendererOutRiver
		// =renderer.getSeriesRendererAt(1);
		// seriesrendererOutRiver.setDisplayChartValues(true);
		// seriesrendererOutRiver.setDisplayChartValuesDistance(30);
		// seriesrendererOutRiver.setChartValuesTextSize(ViewsUitls.dptopx(5));
		// SimpleSeriesRenderer seriesrendererInRiver
		// =renderer.getSeriesRendererAt(0);
		// seriesrendererInRiver.setDisplayChartValues(true);
		// seriesrendererInRiver.setDisplayChartValuesDistance(30);
		// seriesrendererInRiver.setChartValuesTextSize(ViewsUitls.dptopx(5));

		// 创建包含了数据的对象XYMultipleSeriesDataset
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		inRiverSeries = new XYSeries(titles[0], 0);
		outRiverSeries = new XYSeries(titles[1], 0);
		dataset.addSeries(inRiverSeries);
		dataset.addSeries(outRiverSeries);
		// 最后一个参数就是设置曲线的平滑度
		cubeLineChartView = ChartFactory.getCubeLineChartView(
				ViewsUitls.getContext(), dataset, renderer, 0);
		mFlChart.addView(cubeLineChartView);
	}

	private void initAllView() {
		relativeLayout = (RelativeLayout) view
				.findViewById(R.id.rl_select_time);
		refresh = (Button) view.findViewById(R.id.bt_refresh);
		mFlChart = (FrameLayout) view.findViewById(R.id.fl_chart);
		selectTime = (EditText) view.findViewById(R.id.et_select_time);
		refresh.setOnClickListener(this);
	}

	private void addDateToXYSeries(XYSeries xySeries, double[] xLabels,
			double[] yTest) {
		if (xLabels != null && yTest != null) {
			for (int i = 0; i < xLabels.length; i++) {
				xySeries.add(xLabels[i], yTest[i]);
			}
		}
	}

	/** 修改按钮高度 */
	private void amendButtonHeight() {
		ViewTreeObserver viewTreeObserver = relativeLayout
				.getViewTreeObserver();
		viewTreeObserver
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						relativeLayout.getViewTreeObserver()
								.removeGlobalOnLayoutListener(this);
						LayoutParams layoutParams = refresh.getLayoutParams();
						layoutParams.height = relativeLayout.getHeight() - 2;
						refresh.setLayoutParams(layoutParams);
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_refresh:

			getCurveData();
			
			break;
		}
	}

	private void getCurveData() {
		mSelectTime = selectTime.getText().toString();
		if (!StringUtils.isEmpty(mSelectTime)) {
			requestCurveDate = new Runnable() {
				@Override
				public void run() {
					requestHistory();
				}
			};
			setIsInterrupt(true);
			setLoadIsVisible(View.VISIBLE);
			ThreadManager.getInstance().execute(requestCurveDate);
		} else {
			ToastUtil.showToast(ViewsUitls.getContext(), "请先选择日期");
		}		
	}

	private void requestHistory() {
		HttpBasic httpBasic = new HttpBasic();
		try {
			String address = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), "address", "");
			String url = address + ApiField.REQUESTHISTORYINFORMATION + "?day="
					+ mSelectTime + "&stationId=" + stationId;
			String postBack = httpBasic.getBack(url);
			System.out.println(postBack);
			if (StringUtils.interentIsNormal(postBack)) {
				JSONArray jsonArray = new JSONArray(postBack);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject optJSONObject = jsonArray.optJSONObject(i);
					if (optJSONObject.has("hours")) {
						JSONArray hours = optJSONObject.optJSONArray("hours");

						int length = hours.length();
						double[] middleX = new double[length];
						double[] middleYIn = new double[length];
						double[] middleYOut = new double[length];

						for (int j = 0; j < hours.length(); j++) {
							String hour = hours.optString(j);

							middleX[j] = Integer.valueOf(hour.substring(hour
									.length() - 2));
						}
						if (optJSONObject.has("ints")) {
							JSONArray ints = optJSONObject.optJSONArray("ints");
							for (int j = 0; j < ints.length(); j++) {
								middleYIn[j] = ints.optDouble(j);
							}
						}
						if (optJSONObject.has("outs")) {
							JSONArray outs = optJSONObject.optJSONArray("outs");
							for (int j = 0; j < outs.length(); j++) {
								middleYOut[j] = outs.optDouble(j);
							}
						}

						xLabels = middleX;
						yInRiver = middleYIn;
						yOutRiver = middleYOut;

						// TODO 找出最大值最小值
						double yInRiverMax = yInRiver[0];
						double yInRiverMin = yInRiver[0];
						for (int j = 0; j < yInRiver.length; j++) {
							yInRiver[j] = yInRiver[j];
							if (yInRiverMax <= yInRiver[j]) {
								yInRiverMax = yInRiver[j];
							}
							if (yInRiverMin >= yInRiver[j]) {
								yInRiverMin = yInRiver[j];
							}
						}
						System.out.println(yInRiverMax + "---" + yInRiverMin);
						double yOutRiverMax = yOutRiver[0];
						double yOutRiverMin = yOutRiver[0];
						for (int j = 0; j < yOutRiver.length; j++) {
							yOutRiver[j] = yOutRiver[j];
							if (yOutRiverMax <= yOutRiver[j]) {
								yOutRiverMax = yOutRiver[j];
							}
							if (yOutRiverMin >= yOutRiver[j]) {
								yOutRiverMin = yOutRiver[j];
							}
						}
						System.out.println(yOutRiverMax + "---" + yOutRiverMin);

						yMax = yInRiverMax > yOutRiverMax ? yInRiverMax
								: yOutRiverMax;
						yMin = yInRiverMin < yOutRiverMin ? yInRiverMin
								: yOutRiverMin;
						System.out.println(yMax + "---" + yMin);

					} else {
						xLabels = null;
						yInRiver = null;
						yOutRiver = null;
						ViewsUitls.runInMainThread(new Runnable() {
							@Override
							public void run() {
								ToastUtil.showToast(ViewsUitls.getContext(),
										"服务器无历史数据");
							}
						});
					}
				}
			} else {
				ViewsUitls.runInMainThread(new Runnable() {

					@Override
					public void run() {
						xLabels = null;
						yInRiver = null;
						yOutRiver = null;
						ToastUtil.showToast(ViewsUitls.getContext(), "网络异常,请稍后");
					}
				});
			}
			ViewsUitls.runInMainThread(new Runnable() {
				@Override
				public void run() {
					Message message = new Message();
					message.what = 1;
					handler.sendMessage(message);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 处理日期和时间控件的Handler
	 */
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SELECT_TIME:
				touch_flag = 0;
				showDialog(DATE_DIALOG_ID);
				break;
			case 1:
				inRiverSeries.clear();
				outRiverSeries.clear();
				addDateToXYSeries(inRiverSeries, xLabels, yInRiver);
				addDateToXYSeries(outRiverSeries, xLabels, yOutRiver);

				// TODO 在刷新图标前设置Y轴分隔数以及Y轴起始点终结点
				renderer.setYLabels(20);
				renderer.setYAxisMin(yMin-0.15);
				renderer.setYAxisMax(yMax+0.15);

				cubeLineChartView.repaint();
				setLoadIsVisible(View.GONE);
				setIsInterrupt(false);
				ThreadManager.getInstance().cancel(requestCurveDate);

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
		}
	};

	@Override
	@Deprecated
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, mDateSetListener, mYear, mMonth,
					mDay);
		}
		return null;
	}

	@Override
	protected void onPrepareDialog(int id, Dialog dialog) {
		switch (id) {
		case DATE_DIALOG_ID:
			((DatePickerDialog) dialog).updateDate(mYear, mMonth, mDay);
			break;
		}
	}

	/**
	 * 更新日期显示
	 */
	private void updateDateDisplay() {
		compareTime();
		StringBuilder stringBuilder = new StringBuilder().append(mYear)
				.append("-")
				.append((mMonth + 1) < 10 ? "0" + (mMonth + 1) : (mMonth + 1))
				.append("-").append((mDay < 10) ? "0" + mDay : mDay);
		selectTime.setText(stringBuilder);
	}

	/** 比较大小，使得选择的时间不大于现在的时间 */
	private void compareTime() {
		final Calendar c = Calendar.getInstance();
		int nowYear = c.get(Calendar.YEAR);
		int nowMonth = c.get(Calendar.MONTH);
		int nowDay = c.get(Calendar.DAY_OF_MONTH);

		if (nowYear <= mYear) {
			mYear = nowYear;
			if (nowMonth <= mMonth) {
				mMonth = nowMonth;
				if (nowDay <= mDay) {
					mDay = nowDay;
				}
			}
		}
	}

	/**
	 * 获取现在的日期,使得在弹出修改时间的对话框时，一开就显示对应的时间
	 */
	private void initCalendar() {
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
	}
}

package com.minji.cufcs.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.ContentPage.ResultState;
import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.bean.OperationDeatilStations;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.observer.MySubject;
import com.minji.cufcs.ui.OperationDetailActivity;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.ViewsUitls;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentOperationDetail extends BaseFragment {

	private String mId;
	private String mDepartmentid;
	private String postBack;
	private String diaoduString;
	private String fenjieString;
	private String fenjieBeizhuString;
	private List<OperationDeatilStations> deatilStations;
	private View inflate;
	private TextView noDate;
	private LinearLayout addStationsDetaild;
	private ArrayList<String> arrayList;
	private OperationDetailActivity detailActivity;
	private String executeResult;
	private Button execute;
	private HttpBasic httpBasic;
	private int position;

	private String qianfaBeizhuString;// ===============
	private String startTimeString;
	private String endTimeString;
	private String typeNameString;
	private String sendPeopleString;
	private String receiverPeopleString;
	private String receiverTimeString;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Bundle arguments = getArguments();
		mId = arguments.getString("Id");
		mDepartmentid = arguments.getString("Departmentid");
		position = arguments.getInt("position");

	}

	@Override
	protected void onSubClassOnCreateView() {
		loadDataAndRefresh();
	}

	@Override
	protected ResultState onLoad() {
		requestDate();
		parseJson();
		return chat(arrayList);
	}

	@Override
	protected View onCreateSuccessView() {

		detailActivity = (OperationDetailActivity) getActivity();

		initInflateView();

		showAddStationDetails();

		executeSetOnClick();

		return inflate;
	}

	private void executeSetOnClick() {
		execute = (Button) inflate
				.findViewById(R.id.bt_operation_dispatching_detail_execute);
		execute.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				detailActivity.setLoadIsVisible(View.VISIBLE);
				execute.setClickable(false);
				ThreadManager.getInstance().execute(new Runnable() {
					@Override
					public void run() {
						requestExecute();
						ViewsUitls.runInMainThread(new Runnable() {
							@Override
							public void run() {
								detailActivity.setLoadIsVisible(View.GONE);
								execute.setClickable(true);
								if (StringUtils.interentIsNormal(executeResult)) {
									try {
										JSONObject jsonObject = new JSONObject(
												executeResult);
										String optString = jsonObject
												.optString("result");

										if (optString.equals("true")) {
											// TODO 关闭本页面
											MySubject.getInstance().operation(
													position, 3);
											detailActivity.finish();
										} else {
											ToastUtil.showToast(getActivity(),
													"下发失败");
										}
									} catch (JSONException e) {
										e.printStackTrace();
									}

								} else {
									ToastUtil.showToast(getActivity(),
											"服务器正忙，请稍候");
								}
							}
						});
					}
				});

			}
		});
	}

	private void showAddStationDetails() {
		if (deatilStations.size() != 0) {
			noDate.setVisibility(View.GONE);
			for (int i = 0; i < deatilStations.size(); i++) {
				OperationDeatilStations operationDeatilStations = deatilStations
						.get(i);
				View view = ViewsUitls
						.inflate(R.layout.item_add_operation_stationsdetails);
				TextView stationName = (TextView) view
						.findViewById(R.id.tv_add_operation_stationsdetails_name);
				setTextOrNull(stationName, operationDeatilStations.getStationname());
				
				TextView runUnitNumber = (TextView) view
						.findViewById(R.id.tv_add_operation_stationsdetails_run);
				setTextOrNull(runUnitNumber, operationDeatilStations.getRuncount());
				
				TextView keepUnitNumber = (TextView) view
						.findViewById(R.id.tv_add_operation_stationsdetails_keep);
				setTextOrNull(keepUnitNumber, operationDeatilStations.getKeepcount());
				
				TextView valveState = (TextView) view
						.findViewById(R.id.tv_add_operation_stationsdetails_state);
				setTextOrNull(valveState, operationDeatilStations.getGatetype());
				
				addStationsDetaild.addView(view);
			}

		} else {
			noDate.setVisibility(View.VISIBLE);
		}
	}

	private void initInflateView() {
		inflate = ViewsUitls
				.inflate(R.layout.layout_operation_dispatching_detail);

		TextView startTime = (TextView) inflate
				.findViewById(R.id.tv_operation_dispatching_detail_starttime);
		setTextOrNull(startTime, startTimeString);

		TextView endTime = (TextView) inflate
				.findViewById(R.id.tv_operation_dispatching_detail_endtime);
		setTextOrNull(endTime, endTimeString);

		TextView typeName = (TextView) inflate
				.findViewById(R.id.tv_operation_dispatching_detail_typename);
		setTextOrNull(typeName, typeNameString);

		TextView sendPeople = (TextView) inflate
				.findViewById(R.id.tv_operation_dispatching_detail_sendpeople);
		setTextOrNull(sendPeople, sendPeopleString);

		TextView receiverPeople = (TextView) inflate
				.findViewById(R.id.tv_operation_dispatching_detail_receiverpeople);
		setTextOrNull(receiverPeople, receiverPeopleString);

		TextView receiverTime = (TextView) inflate
				.findViewById(R.id.tv_operation_dispatching_detail_receivertime);
		setTextOrNull(receiverTime, receiverTimeString);

		TextView diaodu = (TextView) inflate
				.findViewById(R.id.tv_operation_dispatching_detail_diaodu);
		setTextOrNull(diaodu, diaoduString);

		EditText fenjie = (EditText) inflate
				.findViewById(R.id.et_operation_dispatching_detail_fenjie);
		fenjie.setText(fenjieString);
		EditText fenjieBeizhu = (EditText) inflate
				.findViewById(R.id.et_operation_dispatching_detail_fenjie_beizhu);
		fenjieBeizhu.setText(fenjieBeizhuString);
		EditText qianfaBeizhu = (EditText) inflate
				.findViewById(R.id.et_operation_dispatching_detail_qianfa_beizhu);
		qianfaBeizhu.setText(qianfaBeizhuString);

		noDate = (TextView) inflate
				.findViewById(R.id.tv_operation_details_stationsdetaild_no_date);
		addStationsDetaild = (LinearLayout) inflate
				.findViewById(R.id.ll_operation_details_stationsdetaild_add);
	}

	private void setTextOrNull(TextView endTime, String text) {
		if (StringUtils.isEmpty(text)) {
			endTime.setText("---");
		} else {
			endTime.setText(text);
		}
	}

	private void requestExecute() {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("id", mId));
		list.add(new BasicNameValuePair("departmentid", mDepartmentid));
		try {
			String address = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), "address", "");
			executeResult = httpBasic.postBack(address
					+ ApiField.OPERATION_DISPATCHING_DETAIL_EXECUTE, list);
			System.out.println("executeResult:  " + executeResult);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void requestDate() {
		httpBasic = new HttpBasic();
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("id", mId));
		try {
			String address = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), "address", "");
			postBack = httpBasic.postBack(address
					+ ApiField.OPERATION_DISPATCHING_DETAIL, list);
			System.out.println("postBack:  " + postBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseJson() {
		try {
			if (StringUtils.interentIsNormal(postBack)) {
				// TODO 解析JSON数据
				JSONObject obj = new JSONObject(postBack);
				diaoduString = obj.optString("ddcode");
				fenjieString = obj.optString("fjcode");
				fenjieBeizhuString = obj.optString("memo");
				qianfaBeizhuString = obj.optString("rmemo");// ===============
				startTimeString = obj.optString("launchtime");
				endTimeString = obj.optString("launchtime");
				typeNameString = obj.optString("typeName");
				sendPeopleString = obj.optString("launcher");
				receiverPeopleString = obj.optString("receipter");
				receiverTimeString = obj.optString("receiptetime");

				String stationdetails = obj.optString("stationdetails");
				JSONArray jsonArray = new JSONArray(stationdetails);
				deatilStations = new ArrayList<OperationDeatilStations>();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					deatilStations.add(new OperationDeatilStations(jsonObject
							.optString("gatetype"), jsonObject
							.optString("keepcount"), jsonObject
							.optString("runcount"), jsonObject
							.optString("stationname")));
				}
				arrayList = new ArrayList<>();
				arrayList.add("为了显示界面");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}

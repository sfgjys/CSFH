package com.minji.cufcs.fragment;

import android.view.View;
import android.widget.ListView;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.ContentPage.ResultState;
import com.minji.cufcs.R;
import com.minji.cufcs.adapter.TrueTimeAdapter;
import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.bean.TreuTimeDate;
import com.minji.cufcs.bean.TrueTimeChild;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ViewsUitls;
import com.minji.cufcs.widget.OnRefreshListener;
import com.minji.cufcs.widget.PullToRefreshLayout;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentTrueTime extends BaseFragment<TreuTimeDate> implements
		OnRefreshListener {

	private PullToRefreshLayout pullToRefreshLayout;
	private List<TreuTimeDate> arrayList;
	private String postBack1;
	private String postBack2;
	private List<TreuTimeDate> lists = new ArrayList<TreuTimeDate>();
	private TrueTimeAdapter trueTimeAdapter;
	private ThreadManager threadManager;
	private Timer timer;
	private TimerTask timerTask;

	@Override
	protected View onCreateSuccessView() {
		View view = ViewsUitls.inflate(R.layout.layout_listview);

		pullToRefreshLayout = (PullToRefreshLayout) view
				.findViewById(R.id.ptrl_view);
		ListView listView = (ListView) view.findViewById(R.id.lv_content);

		pullToRefreshLayout.setOnRefreshListener(this);

		trueTimeAdapter = new TrueTimeAdapter(arrayList);

		listView.setAdapter(trueTimeAdapter);

		return view;
	}

	@Override
	protected ResultState onLoad() {

		// TODO 为了定时刷新 抽取请求网络和解析数据的方法
		requestDate();
		parseJson();

		return chat(arrayList);
	}

	private void requestDate() {
		HttpBasic httpBasic = new HttpBasic();
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();

		try {
			String address = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), "address", "");
			postBack1 = httpBasic.postBack(address + ApiField.WATERCURRENTLIST,
					list);
			System.out.println("postBack111:  " + postBack1);
			postBack2 = httpBasic.postBack(address
					+ ApiField.PUMPRUNCURRENTLIST, list);
			System.out.println("postBack222:  " + postBack2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseJson() {
		try {
			if (StringUtils.interentIsNormal(postBack1)
					&& StringUtils.interentIsNormal(postBack2)) {
				JSONArray arr1 = new JSONArray(postBack1);
				JSONArray arr2 = new JSONArray(postBack2);
				lists.clear();

				HashMap<String, TrueTimeChild> hashMap = new HashMap<String, TrueTimeChild>();

				for (int i = 0; i < arr2.length(); i++) {
					JSONObject obj = arr2.optJSONObject(i);
					String hubName = obj.optString("mHubName");
					String operationState = obj.optString("mOperationState");
					String valveNumber = obj.optString("mValveNumber");

					if (hashMap.get(hubName) == null) {// 第一次加入一个枢纽
						int allUnit = 0;
						if (!StringUtils.isEmpty(valveNumber)) {
							allUnit = 1;// 第一次加枢纽如果mValveNumber不为空则一定有一个机组
						}
						String openUnit = null;
						if (operationState.equals("运行")) {
							openUnit = valveNumber;
						}
						TrueTimeChild trueTimeChild = new TrueTimeChild(
								allUnit, openUnit);
						hashMap.put(hubName, trueTimeChild);
					} else {
						TrueTimeChild trueTimeChild = hashMap.get(hubName);
						int allUnit = trueTimeChild.getAllUnit();
						String getOpenUnit = trueTimeChild.getOpenUnit();
						String openUnit = null;
						if (!StringUtils.isEmpty(valveNumber)) {
							allUnit++;
						}
						if (operationState.equals("运行")) {
							if(!StringUtils.isEmpty(getOpenUnit)){
								openUnit = getOpenUnit + "," + valveNumber;
							}else{
								openUnit = valveNumber;
							}
						}else{
							openUnit=getOpenUnit;
						}
						trueTimeChild.setAllUnit(allUnit);
						trueTimeChild.setOpenUnit(openUnit);
						hashMap.put(hubName, trueTimeChild);
					}
				}

				for (int i = 0; i < arr1.length(); i++) {
					JSONObject obj = arr1.optJSONObject(i);
					TreuTimeDate info = new TreuTimeDate();
					String hubName = obj.optString("HubName");
					if (hubName.contains("北塘河枢纽")) {
						hubName = "北塘河枢纽";
					}
					TrueTimeChild trueTimeChild = hashMap.get(hubName);
					info.setHubName(hubName);
					info.setInWaterLevel(obj.optString("InWaterLevel"));
					info.setOutWaterLevel(obj.optString("OutWaterLevel"));
					if (trueTimeChild != null) {
						info.setAllUnit(trueTimeChild.getAllUnit() + "");
						info.setOpenUnit(trueTimeChild.getOpenUnit());
					}
					lists.add(info);
				}
				
				System.out.println();

				arrayList = lists;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onRefresh() {
		// TODO 下拉刷新
		threadManager = ThreadManager.getInstance();
		threadManager.execute(new Runnable() {
			@Override
			public void run() {
				requestDate();
				if (StringUtils.interentIsNormal(postBack1)
						&& StringUtils.interentIsNormal(postBack2)) {
					parseJson();
					ViewsUitls.runInMainThread(new Runnable() {
						@Override
						public void run() {
							trueTimeAdapter.notifyDataSetChanged();
							pullToRefreshLayout
									.refreshFinish(PullToRefreshLayout.REFRESH_SUCCEED);
						}
					});
				} else {
					ViewsUitls.runInMainThread(new Runnable() {
						@Override
						public void run() {
							pullToRefreshLayout
									.refreshFinish(PullToRefreshLayout.REFRESH_FAIL);
						}
					});
				}
			}
		});
	}

	@Override
	protected void onSubClassOnCreateView() {
		loadDataAndRefresh();
	}

	// TODO 在界面不可见时停止定时器
	@Override
	public void onStop() {
		stopTimer();
		super.onStop();
	}

	private void stopTimer() {
		if (timer != null)
			timer.cancel();
		timer = null;
		timerTask = null;
	}

	@Override
	public void onStart() {

		// TODO 5秒后开启定时器，定时循环间隔5秒
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				if (arrayList != null) {
					requestDate();
					if (StringUtils.interentIsNormal(postBack1)
							&& StringUtils.interentIsNormal(postBack2)) {
						parseJson();
						ViewsUitls.runInMainThread(new Runnable() {
							@Override
							public void run() {
								trueTimeAdapter.notifyDataSetChanged();
							}
						});
					}
					System.out.println("又一次" + this.getClass());
				}
			}
		};
		timer.schedule(timerTask, 5000, 5000);
		super.onResume();
	}
}

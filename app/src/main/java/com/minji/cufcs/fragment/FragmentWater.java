package com.minji.cufcs.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.ContentPage.ResultState;
import com.minji.cufcs.IntentFields;
import com.minji.cufcs.R;
import com.minji.cufcs.adapter.WaterAdapter;
import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.bean.WaterDetails;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.ui.ChartActivity;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentWater extends BaseFragment<WaterDetails> implements
		OnRefreshListener {

	private PullToRefreshLayout pullToRefreshLayout;
	private List<WaterDetails> arrayList;
	private String postBack;
	private List<WaterDetails> lists = new ArrayList<WaterDetails>();
	private WaterAdapter waterAdapter;
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

		waterAdapter = new WaterAdapter(arrayList);

		listView.setAdapter(waterAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(), ChartActivity.class);
				intent.putExtra(IntentFields.ACTIVITY_TITLE, "内/外河水位");
				intent.putExtra("chartHubname", lists.get(position)
						.getHubName());
				intent.putExtra("stationId", arrayList.get(position)
						.getStationid());
				getActivity().startActivity(intent);
			}
		});

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
			postBack = httpBasic.postBack(address + ApiField.WATERCURRENTLIST,
					list);
			System.out.println("postBack:  " + postBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseJson() {
		try {
			if (StringUtils.interentIsNormal(postBack)) {
				JSONArray arr = new JSONArray(postBack);
				lists.clear();
				for (int i = 0; i < arr.length(); i++) {
					JSONObject obj = arr.optJSONObject(i);
					WaterDetails info = new WaterDetails();
					info.setHubName(obj.optString("HubName"));
					info.setCollectTime(obj.optString("CollectTime"));
					info.setInWaterLevel(obj.optString("InWaterLevel"));
					info.setOutWaterLevel(obj.optString("OutWaterLevel"));
					info.setStationid(obj.optString("stationid"));
					lists.add(info);
				}

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
				if (postBack.contains("Error") || StringUtils.isEmpty(postBack)) {
					ViewsUitls.runInMainThread(new Runnable() {
						@Override
						public void run() {
							pullToRefreshLayout
									.refreshFinish(PullToRefreshLayout.REFRESH_FAIL);
						}
					});
				} else {
					parseJson();
					ViewsUitls.runInMainThread(new Runnable() {
						@Override
						public void run() {
							waterAdapter.notifyDataSetChanged();
							pullToRefreshLayout
									.refreshFinish(PullToRefreshLayout.REFRESH_SUCCEED);
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
					if (postBack.contains("Error")
							|| StringUtils.isEmpty(postBack)) {
					} else {
						parseJson();
						ViewsUitls.runInMainThread(new Runnable() {
							@Override
							public void run() {
								waterAdapter.notifyDataSetChanged();
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

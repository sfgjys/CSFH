package com.minji.cufcs.fragment;

import android.view.View;
import android.widget.ListView;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.ContentPage.ResultState;
import com.minji.cufcs.R;
import com.minji.cufcs.adapter.RainAdapter;
import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.bean.RainDetails;
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
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentRain extends BaseFragment<RainDetails> implements
		OnRefreshListener {

	private List<RainDetails> arrayList;
	private PullToRefreshLayout pullToRefreshLayout;
	private RainAdapter rainAdapter;
	private String postBack;
	private List<RainDetails> lists = new ArrayList<RainDetails>();
	private Runnable runnable;
	private Timer timer;
	private TimerTask timerTask;

	@Override
	protected View onCreateSuccessView() {
		View view = ViewsUitls.inflate(R.layout.layout_listview);

		pullToRefreshLayout = (PullToRefreshLayout) view
				.findViewById(R.id.ptrl_view);
		ListView listView = (ListView) view.findViewById(R.id.lv_content);

		pullToRefreshLayout.setOnRefreshListener(this);

		rainAdapter = new RainAdapter(arrayList);

		listView.setAdapter(rainAdapter);

		return view;
	}

	@Override
	protected ResultState onLoad() {

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
			postBack = httpBasic.postBack(address + ApiField.RAINCURRENTLIST,
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
					RainDetails info = new RainDetails();
					info.setName(obj.optString("name"));
					info.setTime(obj.optString("time"));
					info.setTotal(obj.optInt("total"));
					lists.add(info);
				}
				arrayList = lists;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onSubClassOnCreateView() {
		loadDataAndRefresh();
	}

	@Override
	public void onRefresh() {
		pullToRefreshLayout.refreshFinish(PullToRefreshLayout.REFRESH_FAIL);
		runnable = new Runnable() {
			@Override
			public void run() {
				requestDate();
				if (StringUtils.interentIsNormal(postBack)) {
					parseJson();
					ViewsUitls.runInMainThread(new Runnable() {
						@Override
						public void run() {
							rainAdapter.notifyDataSetChanged();
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
		};
		ThreadManager.getInstance().execute(runnable);
	}

	@Override
	public void onStop() {
		if (timer != null)
			timer.cancel();
		timer = null;
		timerTask = null;
		if (runnable != null) {
			ThreadManager.getInstance().cancel(runnable);
		}
		super.onDestroy();
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
								rainAdapter.notifyDataSetChanged();
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

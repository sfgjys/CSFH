package com.minji.cufcs.fragment;

import android.view.View;
import android.widget.ListView;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.ContentPage.ResultState;
import com.minji.cufcs.R;
import com.minji.cufcs.adapter.ValveAdapter;
import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.bean.ValveDetails;
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

@SuppressWarnings("deprecation")
public class FragmentValve extends BaseFragment<ValveDetails> implements
		OnRefreshListener {

	private PullToRefreshLayout pullToRefreshLayout;
	private List<ValveDetails> arrayList;
	private String postBack;
	private List<ValveDetails> lists = new ArrayList<ValveDetails>();
	private Timer timer;
	private TimerTask timerTask;
	private int text;
	private ValveAdapter valveAdapter;
	private ThreadManager threadManager;

	@Override
	protected View onCreateSuccessView() {
		View view = ViewsUitls.inflate(R.layout.layout_listview);

		pullToRefreshLayout = (PullToRefreshLayout) view
				.findViewById(R.id.ptrl_view);
		ListView listView = (ListView) view.findViewById(R.id.lv_content);

		pullToRefreshLayout.setOnRefreshListener(this);

		valveAdapter = new ValveAdapter(arrayList);

		listView.setAdapter(valveAdapter);

		// TODO 5秒后开启定时器，定时循环间隔5秒
		timer = new Timer();
		timerTask = new TimerTask() {
			@Override
			public void run() {
				requestDate();
				if (postBack.contains("Error") || StringUtils.isEmpty(postBack)) {
				} else {
					parseJson();
					ViewsUitls.runInMainThread(new Runnable() {
						@Override
						public void run() {
							valveAdapter.notifyDataSetChanged();
						}
					});
				}
				System.out.println("又一次" + this.getClass());
			}
		};
		timer.schedule(timerTask, 5000, 5000);

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
			String address = SharedPreferencesUtil.getString(ViewsUitls.getContext(), "address", "");
			postBack = httpBasic.postBack(address+ApiField.GATERUNCURENTLIST, list);
			 System.out.println("postBack:  " + postBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseJson() {
		try {
			JSONArray arr = new JSONArray(postBack);
			lists.clear();
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.optJSONObject(i);
				ValveDetails info = new ValveDetails();
				info.setmCollectTime(obj.optString("mCollectTime"));
				info.setmHubName(obj.optString("mHubName"));
				info.setmOperationState(obj.optString("mOperationState"));
				info.setmValveNumber(obj.optString("mValveNumber"));
				lists.add(info);
			}

			arrayList = lists;
			System.out.println();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onSubClassOnCreateView() {
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
							valveAdapter.notifyDataSetChanged();
							pullToRefreshLayout
									.refreshFinish(PullToRefreshLayout.REFRESH_SUCCEED);
						}
					});
				}
			}
		});
	}

	@Override
	public void onStop() {
		if (timer != null)
			timer.cancel();
		timer = null;
		timerTask = null;
		super.onStop();
	}

}

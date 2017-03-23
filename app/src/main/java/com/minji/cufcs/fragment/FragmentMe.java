package com.minji.cufcs.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.minji.cufcs.ContentPage.ResultState;
import com.minji.cufcs.IntentFields;
import com.minji.cufcs.R;
import com.minji.cufcs.adapter.PollingAdapter;
import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.ui.PollingEntering;
import com.minji.cufcs.uitls.ViewsUitls;
import com.minji.cufcs.widget.OnRefreshListener;
import com.minji.cufcs.widget.PullToRefreshLayout;

import java.util.ArrayList;

public class FragmentMe extends BaseFragment<String> implements
		OnRefreshListener {

	private ArrayList<String> arrayList;
	private PullToRefreshLayout pullToRefreshLayout;
	private PollingAdapter pollingAdapter;

	@Override
	protected ResultState onLoad() {

		arrayList = new ArrayList<String>();

		int x = 324453555;
		for (int i = 0; i < 20; i++) {

			x++;
			Integer valueOf = Integer.valueOf(x);

			arrayList.add(valueOf.toString());
		}

		return chat(arrayList);
	}

	@Override
	protected View onCreateSuccessView() {

		View view = ViewsUitls.inflate(R.layout.layout_listview);

		pullToRefreshLayout = (PullToRefreshLayout) view
				.findViewById(R.id.ptrl_view);
		ListView listView = (ListView) view.findViewById(R.id.lv_content);

		pullToRefreshLayout.setOnRefreshListener(this);

		pollingAdapter = new PollingAdapter(arrayList);

		listView.setAdapter(pollingAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(ViewsUitls.getContext(),
						PollingEntering.class);

				intent.putExtra(IntentFields.ACTIVITY_TITLE, "巡检录入");

				getActivity().startActivity(intent);

			}
		});

		return view;
	}

	@Override
	public void onRefresh() {

		HttpUtils http = new HttpUtils();
		http.send(HttpMethod.GET, "https://www.baidu.com",
				new RequestCallBack<String>() {

					@Override
					public void onFailure(HttpException arg0, String arg1) {
						System.out.println("2222222");
						pullToRefreshLayout
								.refreshFinish(PullToRefreshLayout.REFRESH_FAIL);
					}

					@Override
					public void onSuccess(ResponseInfo<String> arg0) {

						ArrayList<String> arrayList2 = new ArrayList<String>();
						int x = 324453555;
						for (int i = 0; i < 20; i++) {

							x--;
							Integer valueOf = Integer.valueOf(x);

							arrayList2.add(valueOf.toString());
						}

						arrayList.addAll(0, arrayList2);

						pollingAdapter.notifyDataSetChanged();

						System.out.println("111111111");
						pullToRefreshLayout
								.refreshFinish(PullToRefreshLayout.REFRESH_SUCCEED);
					}
				});

	}

	@Override
	protected void onSubClassOnCreateView() {
		loadDataAndRefresh();
	}

}

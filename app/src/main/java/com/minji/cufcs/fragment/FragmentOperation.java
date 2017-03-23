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
import com.minji.cufcs.adapter.OpterationAdapter;
import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.bean.OperationDispatchingList;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.observer.MySubject;
import com.minji.cufcs.observer.Observers;
import com.minji.cufcs.ui.OperationDetailActivity;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ViewsUitls;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentOperation extends BaseFragment<OperationDispatchingList>
		implements Observers {

	private String postBack;
	private List<OperationDispatchingList> lists = new ArrayList<OperationDispatchingList>();
	private List<OperationDispatchingList> arrayList;
	private OpterationAdapter opterationAdapter;

	@Override
	protected void onSubClassOnCreateView() {
		loadDataAndRefresh();
	}

	@Override
	protected ResultState onLoad() {
		requestDate(1);
		parseJson();
		return chat(arrayList);
	}

	@Override
	protected View onCreateSuccessView() {

		View view = ViewsUitls.inflate(R.layout.layout_operation_dispatching);
		ListView listView = (ListView) view
				.findViewById(R.id.lv_operation_dispatching);
		listView.setSelector(android.R.color.transparent);

		opterationAdapter = new OpterationAdapter(arrayList);

		listView.setAdapter(opterationAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position == arrayList.size()) {
					System.out.println("点击了加载更多条目");
					opterationAdapter.loadMore();
				} else {

					Intent intent = new Intent(getActivity(),
							OperationDetailActivity.class);
					intent.putExtra(IntentFields.ACTIVITY_TITLE, "执行单");
					intent.putExtra("Id", arrayList.get(position).getId());
					intent.putExtra("Departmentid", arrayList.get(position)
							.getDepartmentid());
					intent.putExtra("position", position);
					getActivity().startActivity(intent);
				}

			}
		});

		return view;
	}

	private void requestDate(int page) {
		System.out.println("+++++++++++page+++++++++++" + page);
		HttpBasic httpBasic = new HttpBasic();
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("typeDate", "0"));
		list.add(new BasicNameValuePair("page", "" + page));
		list.add(new BasicNameValuePair("rows", "10"));
		try {
			String address = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), "address", "");
			postBack = httpBasic.postBack(address
					+ ApiField.OPERATION_DISPATCHING, list);
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
				JSONArray arr = obj.optJSONArray("rows");
				lists.clear();
				for (int i = 0; i < arr.length(); i++) {
					JSONObject object = arr.optJSONObject(i);
					OperationDispatchingList operationDispatchingList = new OperationDispatchingList(
							object.optString("dispatchtype"),
							object.optString("name"),
							object.optString("createtime"),
							object.optString("creater"),
							object.optString("id"),
							object.optString("departmentid"));
					lists.add(operationDispatchingList);
				}
				arrayList = lists;
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(int mPosition, int areaOrautonomously) {
		if (areaOrautonomously == 3) {
			System.out.println(mPosition);
			arrayList.remove(mPosition);
			opterationAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onDestroy() {

		MySubject.getInstance().del(this);

		super.onDestroy();
	}

}

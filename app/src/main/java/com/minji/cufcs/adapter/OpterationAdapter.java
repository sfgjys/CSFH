package com.minji.cufcs.adapter;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.base.MyBaseAdapter;
import com.minji.cufcs.bean.OperationDispatchingList;
import com.minji.cufcs.holder.OperationHolder;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ViewsUitls;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OpterationAdapter extends MyBaseAdapter<OperationDispatchingList> {

	private HttpBasic httpBasic;
	private String postBack;
	private String mAreaList;
	private List<OperationDispatchingList> middleList = new ArrayList<OperationDispatchingList>();

	public OpterationAdapter(List<OperationDispatchingList> list) {
		super(list);
	}

	@Override
	public BaseHolder<OperationDispatchingList> getHolder() {
		return new OperationHolder();
	}

	@Override
	public List<OperationDispatchingList> onLoadMore() {
		int middle = getDataSize();
		System.out.println(middle + "middle");
		if (middle % 10 != 0) {
			return new ArrayList<OperationDispatchingList>();
		} else {
			int c = middle / 10 + 1;
			requestDate(c);
			if (StringUtils.interentIsNormal(postBack)) {
				parseJson();
				return middleList;
			} else {
				return null;
			}
		}
	}

	@Override
	public boolean hasMore() {
		return true;
	}

	// TODO 修改网络请求
	private void requestDate(int page) {
		System.out.println("+++++++++++page+++++++++++" + page);
		httpBasic = new HttpBasic();
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("typeDate", "0"));
		list.add(new BasicNameValuePair("page", "" + page));
		list.add(new BasicNameValuePair("rows", "10"));
		try {
			String address = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), "address", "");
			postBack = httpBasic.postBack(address
					+ ApiField.OPERATION_DISPATCHING, list);
			System.out.println(postBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO 修改数据解析
	private void parseJson() {
		try {
			middleList.clear();
			JSONObject obj = new JSONObject(postBack);
			JSONArray arr = obj.optJSONArray("rows");
			for (int i = 0; i < arr.length(); i++) {
				JSONObject object = arr.optJSONObject(i);
				OperationDispatchingList operationDispatchingList = new OperationDispatchingList(
						object.optString("dispatchtype"),
						object.optString("name"),
						object.optString("createtime"),
						object.optString("creater"), object.optString("id"),
						object.optString("departmentid"));
				middleList.add(operationDispatchingList);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

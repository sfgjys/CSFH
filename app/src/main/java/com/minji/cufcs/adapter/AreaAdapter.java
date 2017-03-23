package com.minji.cufcs.adapter;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.base.MyBaseAdapter;
import com.minji.cufcs.bean.ImplementAreaList;
import com.minji.cufcs.bean.ImplementAreaList.AreaSingle;
import com.minji.cufcs.holder.AreaHolder;
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

public class AreaAdapter extends MyBaseAdapter<AreaSingle> {

	private HttpBasic httpBasic;
	private String mAreaList;
	private List<AreaSingle> middleList = new ArrayList<AreaSingle>();

	public AreaAdapter(List<AreaSingle> list) {
		super(list);
	}

	@Override
	public BaseHolder<AreaSingle> getHolder() {
		return new AreaHolder();
	}

	@Override
	public List<AreaSingle> onLoadMore() {

		int middle = getDataSize();
		if (middle % 10 != 0) {
			return new ArrayList<AreaSingle>();
		} else {
			int c = middle / 10 + 1;
			requestDate(c);
			if (StringUtils.interentIsNormal(mAreaList)) {
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
		httpBasic = new HttpBasic();
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("typeDate", "0"));
		list.add(new BasicNameValuePair("page", "" + page));
		list.add(new BasicNameValuePair("rows", "10"));
		try {
			String address = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), "address", "");
			mAreaList = httpBasic.postBack(address + ApiField.AREALIST, list);
			System.out.println(mAreaList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO 修改数据解析
	private void parseJson() {
		try {
			middleList.clear();
			JSONObject obj = new JSONObject(mAreaList);
			ImplementAreaList implementAreaList = new ImplementAreaList();
			implementAreaList.setTotal(obj.optInt("total"));

			if (obj.has("rows")) {
				JSONArray rows = obj.optJSONArray("rows");
				for (int i = 0; i < rows.length(); i++) {
					JSONObject rowsObj = rows.optJSONObject(i);
					AreaSingle areaSingle = new ImplementAreaList.AreaSingle();
					areaSingle.setCreatetime(rowsObj.optString("createtime"));
					areaSingle.setId(rowsObj.optString("id"));
					areaSingle.setState(rowsObj.optString("state"));
					areaSingle.setStationid(rowsObj.optString("stationid"));
					areaSingle.setStationname(rowsObj.optString("stationname"));
					areaSingle.setRdsid(rowsObj.optString("rdsid"));
					areaSingle.setExcuteid(rowsObj.optString("excuteid"));
					middleList.add(areaSingle);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}

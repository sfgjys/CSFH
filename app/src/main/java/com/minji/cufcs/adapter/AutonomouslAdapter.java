package com.minji.cufcs.adapter;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.base.MyBaseAdapter;
import com.minji.cufcs.bean.AutonomouslListItem;
import com.minji.cufcs.bean.AutonomouslListItem.AutonomouslSingle;
import com.minji.cufcs.holder.AutonomouslHolder;
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

public class AutonomouslAdapter extends MyBaseAdapter<AutonomouslSingle> {

	private HttpBasic httpBasic;
	private String mAutonomouslList;
	private List<AutonomouslSingle> middleList = new ArrayList<AutonomouslSingle>();

	public AutonomouslAdapter(List<AutonomouslSingle> list) {
		super(list);
	}

	@Override
	public BaseHolder<AutonomouslSingle> getHolder() {
		return new AutonomouslHolder();
	}

	@Override
	public List<AutonomouslSingle> onLoadMore() {

		int middle = getDataSize();
		if (middle % 10 != 0) {
			return new ArrayList<AutonomouslSingle>();
		} else {
			int c = middle / 10 + 1;
			requestDate(c);
			if (StringUtils.interentIsNormal(mAutonomouslList)) {
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
			mAutonomouslList = httpBasic.postBack(address + ApiField.AUTONOMOUSLYLIST, list);
			System.out.println(mAutonomouslList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TODO 修改数据解析
	private void parseJson() {
		try {
			middleList.clear();
			JSONObject obj = new JSONObject(mAutonomouslList);
			AutonomouslListItem autonomouslListItem = new AutonomouslListItem();
			autonomouslListItem.setTotal(obj.optInt("total"));

			if (obj.has("rows")) {
				JSONArray rows = obj.optJSONArray("rows");
				for (int i = 0; i < rows.length(); i++) {
					JSONObject rowsObj = rows.optJSONObject(i);
					AutonomouslSingle autonomouslSingle = new AutonomouslListItem.AutonomouslSingle();
					autonomouslSingle.setCreatetime(rowsObj.optString("createtime"));
					autonomouslSingle.setId(rowsObj.optString("id"));
					autonomouslSingle.setState(rowsObj.optString("state"));
					autonomouslSingle.setStationid(rowsObj.optString("stationid"));
					autonomouslSingle.setExcuteid(rowsObj.optString("excuteid"));
					autonomouslSingle.setSid(rowsObj.optString("sid"));
					middleList.add(autonomouslSingle);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}

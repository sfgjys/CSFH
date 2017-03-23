package com.minji.cufcs.adapter;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.base.BaseHolder;
import com.minji.cufcs.base.MyBaseAdapter;
import com.minji.cufcs.bean.PollingSaveList;
import com.minji.cufcs.bean.PollingSaveList.SaveSingle;
import com.minji.cufcs.holder.AlreadySaveHolder;
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

public class AlreadySaveAdapter extends MyBaseAdapter<SaveSingle> {

	private HttpBasic httpBasic;
	private String mSaveList;
	private List<SaveSingle> middleList=new ArrayList<PollingSaveList.SaveSingle>();

	public AlreadySaveAdapter(List<SaveSingle> list) {
		super(list);
	}

	@Override
	public BaseHolder<SaveSingle> getHolder() {
		return new AlreadySaveHolder();
	}

	@Override
 	public List<SaveSingle> onLoadMore() {
		int middle = getDataSize();
		if (middle % 10 != 0) {
			return new ArrayList<SaveSingle>();
		} else {
			int c = middle / 10 + 1;
			requestDate(c);
			if (StringUtils.interentIsNormal(mSaveList)) {
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

	private void requestDate(int page) {
		httpBasic = new HttpBasic();
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("typeDate", "0"));
		list.add(new BasicNameValuePair("page", "" + page));
		list.add(new BasicNameValuePair("rows", "10"));
		try {
			String address = SharedPreferencesUtil.getString(ViewsUitls.getContext(), "address", "");
			mSaveList = httpBasic.postBack(address+ApiField.POLLING_QUERY_ALREADY_SAVE,
					list);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseJson() {
		try {
			middleList.clear();
			JSONObject obj = new JSONObject(mSaveList);
			PollingSaveList pollingSaveList = new PollingSaveList();
			pollingSaveList.setTotal(obj.optInt("total"));

			if (obj.has("rows")) {
				JSONArray rows = obj.optJSONArray("rows");
				for (int i = 0; i < rows.length(); i++) {
					JSONObject rowsObj = rows.optJSONObject(i);
					SaveSingle saveSingle = new PollingSaveList.SaveSingle();
					saveSingle.setCreaterusername(rowsObj
							.optString("createrusername"));
					saveSingle.setCreatetime(rowsObj.optString("createtime"));
					saveSingle.setDegree(rowsObj.optString("degree"));
					saveSingle.setDetailsid(rowsObj.optString("detailsid"));
					saveSingle.setExcepttime(rowsObj.optString("excepttime"));
					saveSingle.setId(rowsObj.optString("id"));
					saveSingle.setSid(rowsObj.optString("sid"));
					saveSingle.setStationname(rowsObj.optString("stationname"));
					middleList.add(saveSingle);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

package com.minji.cufcs.fragment;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.ContentPage.ResultState;
import com.minji.cufcs.IntentFields;
import com.minji.cufcs.R;
import com.minji.cufcs.adapter.AlreadySaveAdapter;
import com.minji.cufcs.base.BaseActivity;
import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.bean.PollingSaveList;
import com.minji.cufcs.bean.PollingSaveList.SaveSingle;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.ui.PollingEighteen;
import com.minji.cufcs.ui.WaterRainWorkManger;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ViewsUitls;
import com.minji.cufcs.widget.PullToRefreshLayout;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentAlreadySave extends BaseFragment<SaveSingle> implements
		OnClickListener {

	private List<SaveSingle> singleList;
	private String mSaveList;
	private PullToRefreshLayout pullToRefreshLayout;
	private AlreadySaveAdapter alreadySaveAdapter;
	private HttpBasic httpBasic;
	private WaterRainWorkManger waterRainWorkManger;
	private AlertDialog alertDialog;
	private String saveFourId;

	private ArrayList<SaveSingle> middleList = new ArrayList<SaveSingle>();

	@Override
	protected void onSubClassOnCreateView() {

		// 开启新增的Fragment
		// waterRainWorkManger = (WaterRainWorkManger) getActivity();
		// final ImageView mAddPolling = waterRainWorkManger.mAddPolling;
		// mAddPolling.setVisibility(View.VISIBLE);
		// mAddPolling.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// // TODO 修改增加图标
		// mAddPolling.setVisibility(View.INVISIBLE);
		//
		// waterRainWorkManger.beginAddPolling();
		// }
		// });

		loadDataAndRefresh();
	}

	@Override
	protected View onCreateSuccessView() {

		View view = ViewsUitls.inflate(R.layout.layout_only_listview);
		ListView listView = (ListView) view.findViewById(R.id.lv_only);
		listView.setSelector(android.R.color.transparent);
		listView.setBackgroundResource(R.color.white);

		alreadySaveAdapter = new AlreadySaveAdapter(singleList);

		listView.setAdapter(alreadySaveAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获取条目对应的数据的四项后id
				if (position == singleList.size()) {
					alreadySaveAdapter.loadMore();
				} else {
					saveFourId = singleList.get(position).getId();
					System.out.println(saveFourId);
					showReminderDialog("是否要对此表单,余下的未完成的进行编辑");
				}
			}
		});
		return view;
	}

	private void showReminderDialog(String content) {
		alertDialog = new AlertDialog.Builder(getActivity()).create();
		LayoutParams attributes = alertDialog.getWindow().getAttributes();// 获取对话框的属性集
		WindowManager m = getActivity().getWindowManager();
		Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
		attributes.width = (int) (d.getWidth() * 0.9);
		alertDialog.show();
		// 设置对话框中自定义内容
		Window window = alertDialog.getWindow();
		window.setContentView(R.layout.dialog_reminder);
		TextView reminderContents = (TextView) window
				.findViewById(R.id.tv_reminder_contents);
		reminderContents.setText(content);
		Button mCancel = (Button) window.findViewById(R.id.bt_cancel);
		Button mSure = (Button) window.findViewById(R.id.bt_make_sure);
		mCancel.setOnClickListener(this);
		mSure.setOnClickListener(this);
	}

	@Override
	protected ResultState onLoad() {

		requestDate(1);
		if (StringUtils.interentIsNormal(mSaveList)) {
			parseJson();
		}

		return chat(singleList);
	}

	private void requestDate(int page) {
		httpBasic = new HttpBasic();
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("typeDate", "0"));
		list.add(new BasicNameValuePair("page", "" + page));
		list.add(new BasicNameValuePair("rows", "10"));
		try {
			String address = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), "address", "");
			mSaveList = httpBasic.postBack(address
					+ ApiField.POLLING_QUERY_ALREADY_SAVE, list);
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
					SaveSingle saveSingle = new SaveSingle();
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
			if (singleList == null) {
				singleList = new ArrayList<SaveSingle>();
			}
			singleList.addAll(0, middleList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private String postBack;

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_cancel:
			alertDialog.cancel();
			break;
		case R.id.bt_make_sure:
			baseActivity = (BaseActivity) getActivity();
			baseActivity.setLoadIsVisible(View.VISIBLE);
			ThreadManager.getInstance().execute(new Runnable() {
				@Override
				public void run() {
					reuqesReminder();
					parseReminder();
				}
			});
			// 在开启线程请求网络后直接取消对话框,但加载界面显示
			alertDialog.cancel();
			break;
		}

	}

	private List<Integer> alreadyEditeNumber;
	private BaseActivity baseActivity;

	private void parseReminder() {
		if (StringUtils.interentIsNormal(postBack)) {
			alreadyEditeNumber = new ArrayList<Integer>();// 存储已经编辑的18项序列号
			try {
				JSONArray array = new JSONArray(postBack);
				for (int i = 0; i < array.length(); i++) {
					JSONObject opt = array.optJSONObject(i);
					String optString = opt.optString("enumid");
					if (opt.has("problemtype")) {
						Integer integer = Integer.valueOf(optString);
						alreadyEditeNumber.add(integer);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			dealWithResult();
		} else {
			// 请求网络失败
			ViewsUitls.runInMainThread(new Runnable() {
				@Override
				public void run() {
					baseActivity.setLoadIsVisible(View.GONE);
				}
			});
		}
	}

	private void reuqesReminder() {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("id", saveFourId));
		try {
			String address = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), "address", "");
			postBack = httpBasic.postBack(address
					+ ApiField.POLLING_ALREADY_EIGHTEEN, list);
			System.out.println(saveFourId);
			System.out.println(address + ApiField.POLLING_ALREADY_EIGHTEEN);
			System.out.println(postBack);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void dealWithResult() {
		ViewsUitls.runInMainThread(new Runnable() {
			@Override
			public void run() {
				alertDialog.cancel();
				// private List<Integer> alreadyEditeNumber; 要么有数据，要么没数据
				// 跳转到18项
				boolean[] isSumbit = { false, false, false, false, false,
						false, false, false, false, false, false, false, false,
						false, false, false, false, false };
				for (int i = 0; i < alreadyEditeNumber.size(); i++) {
					Integer integer = alreadyEditeNumber.get(i);
					isSumbit[integer.intValue() - 1] = true;
				}

				Intent intent = new Intent(getActivity(), PollingEighteen.class);
				intent.putExtra(IntentFields.ACTIVITY_TITLE, "巡检内容");
				intent.putExtra(IntentFields.SAVEFOURID, saveFourId);
				intent.putExtra(IntentFields.ISSUMBIT, isSumbit);

				// 跳转界面前隐藏加载界面
				baseActivity.setLoadIsVisible(View.GONE);

				getActivity().startActivity(intent);
				getActivity().finish();

			}
		});
	}

}

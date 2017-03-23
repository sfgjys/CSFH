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
import com.minji.cufcs.FragmentFactory;
import com.minji.cufcs.IntentFields;
import com.minji.cufcs.R;
import com.minji.cufcs.adapter.AutonomouslAdapter;
import com.minji.cufcs.base.BaseActivity;
import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.bean.AutonomouslListItem;
import com.minji.cufcs.bean.AutonomouslListItem.AutonomouslSingle;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.observer.MySubject;
import com.minji.cufcs.observer.Observers;
import com.minji.cufcs.ui.ImplementActivity;
import com.minji.cufcs.uitls.LogUtils;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.ViewsUitls;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentAutonomously extends BaseFragment<AutonomouslSingle>
		implements OnClickListener, Observers {

	private List<AutonomouslSingle> singleList;
	private String mAutonomouslList;
	private AutonomouslAdapter autonomouslAdapter;
	private HttpBasic httpBasic;
	private AlertDialog alertDialog;

	private String implementId = "";
	private String stationid = "";
	private String stationname = "";
	private String state = "";

	private ArrayList<AutonomouslSingle> middleList = new ArrayList<AutonomouslSingle>();

	private BaseActivity baseActivity;
	private String startinlandlevel;
	private String startouterlevel;
	private String memo;
	private String excuteid;
	private int mPosition;
	private String isImplement;
	private String stationsid;

	@Override
	protected void onSubClassOnCreateView() {
	}

	@Override
	protected View onCreateSuccessView() {

		baseActivity = (BaseActivity) getActivity();

		View view = ViewsUitls.inflate(R.layout.layout_only_listview);
		ListView listView = (ListView) view.findViewById(R.id.lv_only);
		listView.setSelector(android.R.color.transparent);
		listView.setBackgroundResource(R.color.white);

		autonomouslAdapter = new AutonomouslAdapter(singleList);
		listView.setAdapter(autonomouslAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 获取条目对应的数据的四项后id
				if (position == singleList.size()) {
					System.out.println("点击了加载更多条目");
					autonomouslAdapter.loadMore();
				} else {

					stationname = singleList.get(position).getSid(); // 用于页面标题

					state = singleList.get(position).getState(); // 用于判断是否要查看信息
					implementId = singleList.get(position).getId(); // 用于查看信息
					stationid = singleList.get(position).getStationid(); // 用于查看信息
					excuteid = singleList.get(position).getExcuteid();
					isImplement = singleList.get(position).getState();
					stationsid = singleList.get(position).getStationsid();
					mPosition = position + 1;

					System.out.println("stationsid: "+stationsid);
					
					
					showReminderDialog("是否开始具体实施 ?");
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
		if (StringUtils.interentIsNormal(mAutonomouslList)) {
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
			mAutonomouslList = httpBasic.postBack(address
					+ ApiField.AUTONOMOUSLYLIST, list);
			System.out.println(mAutonomouslList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
					AutonomouslSingle autonomouslSingle = new AutonomouslSingle();
					autonomouslSingle.setCreatetime(rowsObj
							.optString("createtime"));
					autonomouslSingle.setId(rowsObj.optString("id"));
					autonomouslSingle.setState(rowsObj.optString("state"));
					autonomouslSingle.setStationid(rowsObj
							.optString("stationid"));
					autonomouslSingle.setStationsid(rowsObj
							.optString("stationsid"));
					autonomouslSingle
							.setExcuteid(rowsObj.optString("excuteid"));
					autonomouslSingle.setSid(rowsObj.optString("sid"));
					middleList.add(autonomouslSingle);
				}
			}
			if (singleList == null) {
				singleList = new ArrayList<AutonomouslSingle>();
			}
			singleList.clear();
			singleList.addAll(0, middleList);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.bt_cancel:
			alertDialog.cancel();
			break;
		case R.id.bt_make_sure:

			if ("已实施未完成".equals(state)) {// 此处需要请求网络获取信息
				if (!StringUtils.isEmpty(stationid)) {// 枢纽id不为空
					alertDialog.cancel();

					baseActivity.setLoadIsVisible(View.VISIBLE);

					ThreadManager.getInstance().execute(new Runnable() {
						@Override
						public void run() {
							requestImformation();
						}
					});
				} else {
					LogUtils.e("具体实施跳转页面时请求信息的三个参数有为空的");
					ToastUtil.showToast(ViewsUitls.getContext(), "网络异常,请稍候");
				}

			} else {
				startinlandlevel = "";
				startouterlevel = "";
				memo = "";
				alertDialog.cancel();
				thisToImplementActivity();
			}

			break;
		}
	}

	private void requestImformation() {
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("stationid", stationid));
		try {
			String address = SharedPreferencesUtil.getString(
					ViewsUitls.getContext(), "address", "");
			String mImformation = httpBasic.postBack(address
					+ ApiField.AUTONOMOUSLYINFORMATION, list);
			System.out.println(mImformation);
			if (StringUtils.interentIsNormal(mImformation)) {
				System.out.println("mImformation: " + mImformation);
				JSONObject obj = new JSONObject(mImformation);
				startinlandlevel = obj.optString("startinlandlevel");
				startouterlevel = obj.optString("startouterlevel");
				memo = obj.optString("memo");
				ViewsUitls.runInMainThread(new Runnable() {
					@Override
					public void run() {
						baseActivity.setLoadIsVisible(View.GONE);
						System.out.println(startinlandlevel + startouterlevel
								+ memo);
						thisToImplementActivity();
					}
				});
			} else {// 网络请求失败
				ViewsUitls.runInMainThread(new Runnable() {
					@Override
					public void run() {
						baseActivity.setLoadIsVisible(View.GONE);
						ToastUtil.showToast(ViewsUitls.getContext(), "网络异常,请稍候");
					}
				});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void thisToImplementActivity() {
		Intent intent = new Intent(ViewsUitls.getContext(),
				ImplementActivity.class);

		intent.putExtra(IntentFields.ACTIVITY_TITLE, stationname); // 以枢纽名称为页面标题

		intent.putExtra("implementId", implementId); // 制单的唯一id
		intent.putExtra("stationid", stationid); // 枢纽id
		intent.putExtra("stationsid", stationsid); // 枢纽id
		intent.putExtra("excuteid", excuteid); // excuteid一种特殊id

		intent.putExtra("startinlandlevel", startinlandlevel); // 内河水位
		intent.putExtra("startouterlevel", startouterlevel); // 外河水位
		intent.putExtra("memo", memo); // 备注
		intent.putExtra("mPosition", mPosition); // item条目的位数 已加1
		intent.putExtra("isImplement", isImplement); // 实施单的状态

		intent.putExtra("areaOrautonomously", 2); // 区分片区与自主

		getActivity().startActivityForResult(intent, 1);
	}

	// 观察者模式，当保存成功时会传递一个保存的条目的序列号过来
	@Override
	public void update(int mPosition, int areaOrautonomously) {

		if (areaOrautonomously == 2) {
			singleList.get(mPosition - 1).setState("已实施未完成");
			autonomouslAdapter.notifyDataSetChanged();
		}

	}

	@Override
	public void onDestroy() {
		FragmentAutonomously autonomously = (FragmentAutonomously) FragmentFactory.fragments[1];

		MySubject.getInstance().del(autonomously);

		FragmentFactory.fragments[1] = null;

		super.onDestroy();
	}

}

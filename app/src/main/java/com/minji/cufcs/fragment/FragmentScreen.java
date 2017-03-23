package com.minji.cufcs.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hikvision.vmsnetsdk.ControlUnitInfo;
import com.hikvision.vmsnetsdk.RegionInfo;
import com.hikvision.vmsnetsdk.ServInfo;
import com.hikvision.vmsnetsdk.VMSNetSDK;
import com.minji.cufcs.ContentPage.ResultState;
import com.minji.cufcs.IntentFields;
import com.minji.cufcs.SpFields;
import com.minji.cufcs.adapter.RegionListAdapter;
import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.haikang.callback.MsgCallback;
import com.minji.cufcs.haikang.callback.MsgIds;
import com.minji.cufcs.haikang.constans.Constants;
import com.minji.cufcs.haikang.data.Config;
import com.minji.cufcs.haikang.data.TempData;
import com.minji.cufcs.haikang.resource.ResourceControl;
import com.minji.cufcs.ui.SecondScreenFragmentActivity;
import com.minji.cufcs.uitls.LogUtils;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.ViewsUitls;

import java.util.ArrayList;
import java.util.List;

public class FragmentScreen extends BaseFragment<Object> implements MsgCallback {

	private ServInfo servInfo;
	private String servAddr;
	private ResourceControl resourceControl;
	private int pType;
	private int pId;
	private List<Object> regionList;

	private RegionListAdapter regionListAdapter;
	private boolean isLoginSuccess;
	private String ip;
	private String user;
	private String pass;

	@Override
	protected void onSubClassOnCreateView() {
		pType = Constants.Resource.TYPE_UNKNOWN;
		pId = 0;
		loadDataAndRefresh();
	}

	@Override
	protected ResultState onLoad() {

		ip = SharedPreferencesUtil.getString(ViewsUitls.getContext(),
				SpFields.SCREEN_IP, "");
		user = SharedPreferencesUtil.getString(ViewsUitls.getContext(),
				SpFields.SCREEN_USERNAME, "");
		pass = SharedPreferencesUtil.getString(ViewsUitls.getContext(),
				SpFields.SCREEN_PASSWORD, "");

		if (!StringUtils.isEmpty(ip) && !StringUtils.isEmpty(user)
				&& !StringUtils.isEmpty(pass)) {
			login();

			if (isLoginSuccess) {
				requestDate();
				// 请求控制中心
				resourceControl.reqResList(pType, pId);
			}
		} else {
			ViewsUitls.runInMainThread(new Runnable() {
				@Override
				public void run() {
					ToastUtil.showToast(ViewsUitls.getContext(), "请先设置流媒体信息");
				}
			});
		}
		return chat(regionList);
	}

	private void login() {
		Config.getIns().setServerAddr(ip);
		servInfo = new ServInfo();
		servAddr = Config.getIns().getServerAddr();
		String macAddress = getMacAddr();
		// 登录方法 "admin" "!QAZ2wsx"
		isLoginSuccess = VMSNetSDK.getInstance().login(servAddr, user, pass,
				macAddress, servInfo);
		if (isLoginSuccess) {
			TempData.getInstance().setLoginData(servInfo);
		} else {
			LogUtils.e("登录失败,login方法返回false");
			ViewsUitls.runInMainThread(new Runnable() {
				@Override
				public void run() {
					ToastUtil.showToast(ViewsUitls.getContext(),
							"流媒体登录失败,请查看流媒体是否正确设置");
				}
			});
		}
	}

	private void requestDate() {
		resourceControl = new ResourceControl();
		resourceControl.setCallback(this);
	}

	@Override
	protected View onCreateSuccessView() {
		ListView listView = new ListView(ViewsUitls.getContext());
		listView.setSelector(android.R.color.transparent);
		listView.setDivider(new ColorDrawable(Color.GRAY));
		listView.setDividerHeight(1);
		regionListAdapter = new RegionListAdapter(regionList);
		listView.setAdapter(regionListAdapter);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Object object = regionListAdapter.getItem(position);
				int pIdIntent = 0;
				int pResTypeIntent = 0;
				if (object instanceof ControlUnitInfo) {
					ControlUnitInfo info = (ControlUnitInfo) object;
					pIdIntent = Integer.parseInt(info.getControlUnitID());
					pResTypeIntent = Constants.Resource.TYPE_CTRL_UNIT;
				}

				if (object instanceof RegionInfo) {
					RegionInfo info = (RegionInfo) object;
					pIdIntent = Integer.parseInt(info.getRegionID());
					pResTypeIntent = Constants.Resource.TYPE_REGION;
				}
				Intent intent = new Intent(ViewsUitls.getContext(),
						SecondScreenFragmentActivity.class);
				intent.putExtra("NextListId", pIdIntent);
				intent.putExtra("NextListResType", pResTypeIntent);
				intent.putExtra(IntentFields.ACTIVITY_TITLE, "视频");
				getActivity().startActivity(intent);

			}
		});

		return listView;
	}

	/**
	 * 获取登录设备mac地址
	 * 
	 * @return
	 */
	protected String getMacAddr() {
		Activity activity = getActivity();
		if (activity != null) {
			WifiManager wm = (WifiManager) activity
					.getSystemService(Context.WIFI_SERVICE);
			String mac = wm.getConnectionInfo().getMacAddress();
			return mac == null ? "" : mac;
		} else {
			return "";
		}

	}

	@Override
	public void onMsg(int msgId, Object data) {
		switch (msgId) {
		// 获取控制中心列表成功
		case MsgIds.GET_C_F_NONE_SUC:
			List<ControlUnitInfo> datas = (List<ControlUnitInfo>) data;
			if (datas == null || datas.isEmpty()) {
				regionList = new ArrayList<Object>();
				break;
			}
			ControlUnitInfo info = datas.get(0);
			pType = Constants.Resource.TYPE_CTRL_UNIT;
			pId = Integer.parseInt(info.getControlUnitID());
			resourceControl.reqResList(pType, pId);
			break;
		case MsgIds.GET_C_F_NONE_FAIL:
			break;
		// 从控制中心获取下级资源列表成功
		case MsgIds.GET_SUB_F_C_SUC:
			regionList = (List<Object>) data;
			break;
		// 从控制中心获取下级资源列表成失败
		case MsgIds.GET_SUB_F_C_FAIL:
			break;

		}

	}
}

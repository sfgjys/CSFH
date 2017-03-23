package com.minji.cufcs.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hikvision.vmsnetsdk.CameraInfo;
import com.minji.cufcs.ContentPage.ResultState;
import com.minji.cufcs.adapter.CameraListAdapter;
import com.minji.cufcs.base.BaseFragment;
import com.minji.cufcs.haikang.callback.MsgCallback;
import com.minji.cufcs.haikang.callback.MsgIds;
import com.minji.cufcs.haikang.data.TempData;
import com.minji.cufcs.haikang.resource.ResourceControl;
import com.minji.cufcs.ui.ScreenActivity;
import com.minji.cufcs.ui.SecondScreenFragmentActivity;
import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.ViewsUitls;

import java.util.List;

public class FragmentScreenSecond extends BaseFragment<CameraInfo> implements
		MsgCallback {

	private ResourceControl resourceControl;
	private List<CameraInfo> cameraInfos;
	private CameraListAdapter cameraListAdapter;

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		int pResTypeIntent = bundle.getInt("pResTypeIntent", -1);
		int pIdIntent = bundle.getInt("pIdIntent", -1);
	}

	@Override
	protected void onSubClassOnCreateView() {
		loadDataAndRefresh();
	}

	@Override
	protected View onCreateSuccessView() {
		ListView listView = new ListView(ViewsUitls.getContext());
		listView.setSelector(android.R.color.transparent);
		listView.setDivider(new ColorDrawable(Color.GRAY));
		listView.setDividerHeight(1);
		cameraListAdapter = new CameraListAdapter(cameraInfos);
		listView.setAdapter(cameraListAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Object object = cameraListAdapter.getItem(position);
				if (object instanceof CameraInfo) {
					TempData.getIns().setCameraInfo((CameraInfo) object);
					Intent intent = new Intent(ViewsUitls.getContext(),
							ScreenActivity.class);
					getActivity().startActivity(intent);
				} else {
					ToastUtil.showToast(ViewsUitls.getContext(), "数据异常,请稍候");
				}
			}
		});

		return listView;
	}

	@Override
	protected ResultState onLoad() {

		SecondScreenFragmentActivity activity = (SecondScreenFragmentActivity) getActivity();
		int pIdIntent = -1;
		int pResTypeIntent = -1;
		if (activity != null) {
			pIdIntent = activity.getpIdIntent();
			pResTypeIntent = activity.getpResTypeIntent();
		}
		resourceControl = new ResourceControl();
		resourceControl.setCallback(this);
		resourceControl.reqResList(pResTypeIntent, pIdIntent);

		return chat(cameraInfos);
	}

	@Override
	public void onMsg(int msgId, Object data) {
		switch (msgId) {
		// 从控制中心获取下级资源列表成功
		case MsgIds.GET_SUB_F_C_SUC:
			List<CameraInfo> datas = (List<CameraInfo>) data;
			cameraInfos = datas;
			break;
		// 从控制中心获取下级资源列表成失败
		case MsgIds.GET_SUB_F_C_FAIL:
			break;
		}
	}

}

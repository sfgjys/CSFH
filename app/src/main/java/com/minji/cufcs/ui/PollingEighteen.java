package com.minji.cufcs.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

import com.minji.cufcs.ApiField;
import com.minji.cufcs.IntentFields;
import com.minji.cufcs.R;
import com.minji.cufcs.adapter.PollingEighteenAdapter;
import com.minji.cufcs.base.BaseActivity;
import com.minji.cufcs.base.BaseApplication;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.ViewsUitls;

public class PollingEighteen extends BaseActivity {

	private boolean[] isSumbit = { false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false,
			false, false, false };
	private String[] pollingPart = { "内外河引河", "内外河护坡", "内外河翼墙", "翼墙后填土区",
			"泵站内外进水池", "导流墩", "泵站主厂房", "泵站站身", "变压器", "节制闸闸室", "节制闸启闭机", "交通桥",
			"消防器材", "高压室", "高压电容室", "低压室", "中控室", "其他", };
	private String[] pollingContent = { "水流平顺，无杂物，无水草，无威胁工程的漂浮物",
			"护坡完好，排水畅通，无塌陷，表面无开裂破损", "墙体完好，排水通畅，无塌陷，表面无开裂破损",
			"无雨淋沟，无裂缝，无塌陷，无渗漏，无滑坡", "水流平顺，无杂物，无水草", "墩体完好，无裂缝，无渗漏，观测标志完好",
			"墙面完好，门窗完好，伸缩缝完好，排水通畅，无破损，无渗漏", "工程设施完好，无裂缝，无渗漏，伸缩缝完好",
			"室温正常，风扇运行正常", "闸门无振动、倾斜，闸下流态正常，无漂浮物", "钢丝绳无断丝无明显变形，油缸无泄漏",
			"桥面完好，栏杆完好，观测标志完好，无裂缝，无渗漏", "灭火器压力正常，消防栓内水带，水栓完好",
			"柜体、开关完好，仪表显示正常，柜内照明正常，柜前后绝缘垫完好清洁",
			"柜体、开关完好，仪表显示正常，柜内照明正常，柜前后绝缘垫完好清洁",
			"柜体、开关完好，仪表显示正常，电容补偿正常，指示灯显示正常，柜前后绝缘垫完好清洁",
			"监控设备运行正常，数据显示正确，视频监控画面清晰", "" };
	private String[] pollingPartNumber = { "01", "02", "03", "04", "05", "06",
			"07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17",
			"18" };
	private View setContent;
	private GridView gridView;
	private PollingEighteenAdapter pollingEighteenAdapter;
	private String mSaveFourId;

	@Override
	public void onCreateContent() {
		showBack();

		setContent = setContent(R.layout.layout_pollingeighteen);
		mSaveFourId = mIntentDate.getStringExtra(IntentFields.SAVEFOURID);
		
		boolean[] booleanArrayExtra = mIntentDate
				.getBooleanArrayExtra(IntentFields.ISSUMBIT);
		if (booleanArrayExtra != null) {
			System.out.println("从以保存的过来的");
			isSumbit = booleanArrayExtra;
		}

		initGridView();

		initSubmit();

	}

	private void initSubmit() {
		Button mSubmit = (Button) setContent
				.findViewById(R.id.bt_polling_eight_subimt);
		mSubmit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				boolean isSubmit = true;
				for (int i = 0; i < isSumbit.length; i++) {
					if (isSumbit[i] == false) {
						// 只有有一个i为false，就将isSubmit变为false
						isSubmit = false;
					}
				}
				if (isSubmit) {
					// isSubmit还为true，就提交

					setLoadIsVisible(View.VISIBLE);// 开启线程前开启加载页面
					ThreadManager.getInstance().execute(new Runnable() {
						@Override
						public void run() {
							// 提交所有18项
							submitAll();
						}
					});
				} else {
					ToastUtil.showToast(getApplicationContext(), "请录入完18项，在提交");
				}
			}
		});

	}

	private void submitAll() {
		HttpBasic httpBasic = new HttpBasic();
		List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
		list.add(new BasicNameValuePair("id", mSaveFourId));
		try {
			String address = SharedPreferencesUtil.getString(ViewsUitls.getContext(), "address", "");
			final String postBack = httpBasic.postBack(address+ApiField.PATROLRECEIPT
					+ "mobilecommit.html", list);
			System.out.println(postBack);
			ViewsUitls.runInMainThread(new Runnable() {
				@Override
				public void run() {
					// 请求完了网络，对结果进行处理前隐藏加载界面
					PollingEighteen.this.setLoadIsVisible(View.GONE);
					if (StringUtils.interentIsNormal(postBack)) {
						// 连接网络正常
						if ("{'result':true}".equals(postBack)) {
							// 提交成功
							finish();
						} else {
							ToastUtil.showToast(ViewsUitls.getContext(),
									"提交失败,请稍后");
						}
					} else {
						ToastUtil.showToast(ViewsUitls.getContext(), "网络异常,请稍候");
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void initGridView() {
		gridView = (GridView) setContent.findViewById(R.id.gv_eighteen);
		pollingEighteenAdapter = new PollingEighteenAdapter(isSumbit,
				pollingPart, pollingContent);
		gridView.setAdapter(pollingEighteenAdapter);
		gridView.setSelector(android.R.color.transparent);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (!isSumbit[position]) {
					Intent intent = new Intent(ViewsUitls.getContext(),
							PollingEntering.class);
					intent.putExtra(IntentFields.ACTIVITY_TITLE,
							pollingPart[position]);
					intent.putExtra(IntentFields.ALREADY_SUBMIT, position + 1);
					intent.putExtra(IntentFields.POLLING_CONTENT,
							pollingContent[position]);
					intent.putExtra(IntentFields.MSAVEFOURID, mSaveFourId);
					intent.putExtra("number", pollingPartNumber[position]);
					startActivityForResult(intent, 0);
				}
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0) {// 代表是通过录入界面返回的情况下
			if (resultCode - 1 >= 0) {
				isSumbit[resultCode - 1] = true;
				pollingEighteenAdapter.notifyDataSetChanged();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

}

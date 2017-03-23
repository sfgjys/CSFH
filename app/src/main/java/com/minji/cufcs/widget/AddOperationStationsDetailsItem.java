package com.minji.cufcs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.minji.cufcs.R;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.ViewsUitls;

public class AddOperationStationsDetailsItem extends LinearLayout {

	private View view;
	private String stationName;
	private String runUnit;
	private String keepUnit;
	private String openOrClose;

	public AddOperationStationsDetailsItem(Context context) {
		super(context);
	}

	public AddOperationStationsDetailsItem(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public AddOperationStationsDetailsItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = View.inflate(ViewsUitls.getContext(),
				R.layout.item_add_operation_stationsdetails, this);

		TypedArray attributes = context.obtainStyledAttributes(attrs,
				R.styleable.OperationAddStationsDettails);
		// 通过R文件中的id来获取对应属性的值，要什么值，那方法就是get该值的类型,参数就是id值,给id是所要获取值的对应设置的名字，参数一是我们前面写的declare-styleable标签下的attr标签

		stationName = attributes
				.getString(R.styleable.OperationAddStationsDettails_add_stationsdettails_name);
		runUnit = attributes
				.getString(R.styleable.OperationAddStationsDettails_add_stationsdettails_run);
		keepUnit = attributes
				.getString(R.styleable.OperationAddStationsDettails_add_stationsdettails_keep);
		openOrClose = attributes
				.getString(R.styleable.OperationAddStationsDettails_add_stationsdettails_state);

		// 使用了本方法，最后必须调用下面的这个方法
		attributes.recycle();

		initView();

	}

	private void initView() {
		TextView stationName = (TextView) view
				.findViewById(R.id.tv_add_operation_stationsdetails_name);
		stationName.setText(this.stationName);
		TextView runUnitNumber = (TextView) view
				.findViewById(R.id.tv_add_operation_stationsdetails_run);
		runUnitNumber.setText(runUnit);
		TextView keepUnitNumber = (TextView) view
				.findViewById(R.id.tv_add_operation_stationsdetails_keep);
		keepUnitNumber.setText(keepUnit);
		TextView valveState = (TextView) view
				.findViewById(R.id.tv_add_operation_stationsdetails_state);
		valveState.setText(openOrClose);
	}

	
	

}

package com.minji.cufcs.widget;

import com.minji.cufcs.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class PullDownItem extends RelativeLayout {

	private View view;
	private String maioshutext;
	private int downimage;
	private TextView mMiaoShu;
	private ImageView mDown;
	private EditText mEd;

	public PullDownItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public PullDownItem(Context context) {
		super(context);
	}

	public PullDownItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = View.inflate(context, R.layout.drop_down, this);

		TypedArray attributes = context.obtainStyledAttributes(attrs,
				R.styleable.PullDownItem);
		// 通过R文件中的id来获取对应属性的值，要什么值，那方法就是get该值的类型,参数就是id值,给id是所要获取值的对应设置的名字，参数一是我们前面写的declare-styleable标签下的attr标签
		maioshutext = attributes
				.getString(R.styleable.PullDownItem_maioshutext);
		downimage = attributes.getResourceId(
				R.styleable.PullDownItem_downimage, 0);

		// 使用了本方法，最后必须调用下面的这个方法
		attributes.recycle();

		initView();
	}

	private void initView() {
		mMiaoShu = (TextView) view.findViewById(R.id.tv_drop_down);
		mDown = (ImageView) view.findViewById(R.id.iv_drop_down);
		mEd = (EditText) view.findViewById(R.id.et_drop_down);
		mMiaoShu.setText(maioshutext);
		mDown.setImageResource(downimage);
	}

	public ImageView getmDown() {
		return mDown;
	}

	public EditText getmEd() {
		return mEd;
	}
}

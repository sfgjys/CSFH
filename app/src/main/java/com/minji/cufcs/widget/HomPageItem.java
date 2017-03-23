package com.minji.cufcs.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.minji.cufcs.R;

public class HomPageItem extends RelativeLayout {

	private String num;
	private String miaoshu;
	private int tubiao;
	private int viewcolor;
	private View view;
	private TextView mNum;

	public HomPageItem(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public HomPageItem(Context context, AttributeSet attrs) {
		super(context, attrs);
		view = View.inflate(context, R.layout.layout_homepage_top2, this);

		TypedArray attributes = context.obtainStyledAttributes(attrs,
				R.styleable.HomPageItem);
		// 通过R文件中的id来获取对应属性的值，要什么值，那方法就是get该值的类型,参数就是id值,给id是所要获取值的对应设置的名字，参数一是我们前面写的declare-styleable标签下的attr标签

		num = attributes.getString(R.styleable.HomPageItem_num);
		miaoshu = attributes.getString(R.styleable.HomPageItem_miaoshu);

		tubiao = attributes.getResourceId(R.styleable.HomPageItem_tubiao, 0);
		System.out.println(tubiao);

		viewcolor = attributes.getInt(R.styleable.HomPageItem_viewcolor, 0);

		// 使用了本方法，最后必须调用下面的这个方法
		attributes.recycle();

		initView();

	}

	private void initView() {

		mNum = (TextView) view.findViewById(R.id.tv_homepage_green);

		mNum.setText(num);
		TextView mMiaoshu = (TextView) view
				.findViewById(R.id.tv_homepage_green_bottom);
		mMiaoshu.setText(miaoshu);

		ImageView imageView = (ImageView) view
				.findViewById(R.id.iv_homepage_green);
		imageView.setImageResource(tubiao);

		View view2 = view.findViewById(R.id.view_middle);
		view2.setBackgroundColor(viewcolor);
	}

	public HomPageItem(Context context) {
		super(context);
	}

	public void setNum(String num) {
		mNum.setText(num);
	}

}

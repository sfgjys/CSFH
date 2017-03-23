package com.minji.cufcs.uitls;

import android.content.Context;
import android.graphics.Color;
import android.widget.TextView;

public class TextViews {
	
	public static TextView getView(Context context){
		TextView textView = new TextView(context);
		textView.setText("456745");
		textView.setTextColor(Color.BLACK);
		return textView;
	}
}

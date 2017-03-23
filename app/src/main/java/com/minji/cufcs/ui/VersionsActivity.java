package com.minji.cufcs.ui;

import android.view.View;
import android.widget.TextView;
import com.minji.cufcs.R;
import com.minji.cufcs.base.BaseActivity;
import com.minji.cufcs.uitls.ViewsUitls;

public class VersionsActivity extends BaseActivity {

	@Override
	public void onCreateContent() {
		
		showBack();

		View setContent = setContent(R.layout.activity_versions);
		TextView versions = (TextView) setContent
				.findViewById(R.id.tv_versions);
		String versionName = ViewsUitls.getVersionName(ViewsUitls.getContext());
		versions.setText("版本号: " + versionName);
	}

}

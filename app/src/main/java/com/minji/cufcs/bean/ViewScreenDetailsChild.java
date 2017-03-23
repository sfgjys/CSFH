package com.minji.cufcs.bean;

import java.util.ArrayList;
import java.util.List;

public class ViewScreenDetailsChild {

	public String mHubName;
	public List<ChildInfo> rows=new ArrayList<ChildInfo>();

	
	public ViewScreenDetailsChild(String mHubName, List<ChildInfo> rows) {
		super();
		this.mHubName = mHubName;
		this.rows = rows;
	}

	public ViewScreenDetailsChild() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getmHubName() {
		return mHubName;
	}

	public void setmHubName(String mHubName) {
		this.mHubName = mHubName;
	}

	public List<ChildInfo> getRows() {
		return rows;
	}

	public void setRows(List<ChildInfo> rows) {
		this.rows = rows;
	}

	public static class ChildInfo {
		public String name;
		public String url;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public ChildInfo(String name, String url) {
			super();
			this.name = name;
			this.url = url;
		}

		public ChildInfo() {
			super();
			// TODO Auto-generated constructor stub
		}

	}

}

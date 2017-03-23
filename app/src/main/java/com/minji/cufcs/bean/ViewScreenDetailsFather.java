package com.minji.cufcs.bean;

import java.util.ArrayList;
import java.util.List;

public class ViewScreenDetailsFather {

	public int total;
	public List<ChildInfo> rows=new ArrayList<ChildInfo>();

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<ChildInfo> getRows() {
		return rows;
	}

	public void setRows(List<ChildInfo> rows) {
		this.rows = rows;
	}

	public static class ChildInfo {
		public String station_name;
		public String name;
		public String url;

		public String getStation_name() {
			return station_name;
		}

		public void setStation_name(String station_name) {
			this.station_name = station_name;
		}

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

	}

}

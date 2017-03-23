package com.minji.cufcs.bean;

import java.util.ArrayList;
import java.util.List;

public class PollingSaveList {

	public int total;
	public List<SaveSingle> rows=new ArrayList<SaveSingle>();

	public PollingSaveList() {
		super();
	}

	public PollingSaveList(int total, List<SaveSingle> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List<SaveSingle> getRows() {
		return rows;
	}

	public void setRows(List<SaveSingle> rows) {
		this.rows = rows;
	}

	public static class SaveSingle {

		public String createrusername;
		public String createtime;
		public String degree;
		public String detailsid;
		public String excepttime;
		public String id;
		public String sid;
		public String stationname;
		public String getCreaterusername() {
			return createrusername;
		}
		public void setCreaterusername(String createrusername) {
			this.createrusername = createrusername;
		}
		public String getCreatetime() {
			return createtime;
		}
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}
		public String getDegree() {
			return degree;
		}
		public void setDegree(String degree) {
			this.degree = degree;
		}
		public String getDetailsid() {
			return detailsid;
		}
		public void setDetailsid(String detailsid) {
			this.detailsid = detailsid;
		}
		public String getExcepttime() {
			return excepttime;
		}
		public void setExcepttime(String excepttime) {
			this.excepttime = excepttime;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getSid() {
			return sid;
		}
		public void setSid(String sid) {
			this.sid = sid;
		}
		public String getStationname() {
			return stationname;
		}
		public void setStationname(String stationname) {
			this.stationname = stationname;
		}
		public SaveSingle(String createrusername, String createtime,
				String degree, String detailsid, String excepttime, String id,
				String sid, String stationname) {
			super();
			this.createrusername = createrusername;
			this.createtime = createtime;
			this.degree = degree;
			this.detailsid = detailsid;
			this.excepttime = excepttime;
			this.id = id;
			this.sid = sid;
			this.stationname = stationname;
		}
		public SaveSingle() {
			super();
		}

	}

}

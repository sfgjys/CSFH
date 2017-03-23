package com.minji.cufcs.bean;

import java.util.ArrayList;
import java.util.List;

public class ImplementAreaList {

	public int total;
	public List<AreaSingle> rows = new ArrayList<AreaSingle>();

	public ImplementAreaList() {
		super();
	}

	public ImplementAreaList(int total, List<AreaSingle> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public int getTotal() {
		return total;
	}

	public List<AreaSingle> getRows() {
		return rows;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setRows(List<AreaSingle> rows) {
		this.rows = rows;
	}

	public static class AreaSingle {

		public String id;// 制单唯一id
		public String stationid;// 枢纽id
		public String createtime;// 制单时间
		public String state;// 制单状态
		public String stationname;// 枢纽名称
		public String rdsid;// 用于查询信息的id
		public String excuteid;// 用于保存机组或三个参数

		public AreaSingle() {
			super();
		}

		public AreaSingle(String id, String stationid, String createtime,
				String state, String stationname, String rdsid, String excuteid) {
			super();
			this.id = id;
			this.stationid = stationid;
			this.createtime = createtime;
			this.state = state;
			this.stationname = stationname;
			this.rdsid = rdsid;
			this.excuteid = excuteid;
		}

		public String getId() {
			return id;
		}

		public String getStationid() {
			return stationid;
		}

		public String getCreatetime() {
			return createtime;
		}

		public String getState() {
			return state;
		}

		public String getStationname() {
			return stationname;
		}

		public String getRdsid() {
			return rdsid;
		}

		public String getExcuteid() {
			return excuteid;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setStationid(String stationid) {
			this.stationid = stationid;
		}

		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}

		public void setState(String state) {
			this.state = state;
		}

		public void setStationname(String stationname) {
			this.stationname = stationname;
		}

		public void setRdsid(String rdsid) {
			this.rdsid = rdsid;
		}

		public void setExcuteid(String excuteid) {
			this.excuteid = excuteid;
		}
	}
}

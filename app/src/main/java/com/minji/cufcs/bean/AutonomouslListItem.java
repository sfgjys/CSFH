package com.minji.cufcs.bean;

import java.util.ArrayList;
import java.util.List;

public class AutonomouslListItem {

	public int total;
	public List<AutonomouslSingle> rows = new ArrayList<AutonomouslSingle>();

	public int getTotal() {
		return total;
	}

	public List<AutonomouslSingle> getRows() {
		return rows;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public void setRows(List<AutonomouslSingle> rows) {
		this.rows = rows;
	}

	public AutonomouslListItem() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AutonomouslListItem(int total, List<AutonomouslSingle> rows) {
		super();
		this.total = total;
		this.rows = rows;
	}

	public static class AutonomouslSingle {
		public String createtime;// 制单时间
		public String excuteid;// 特殊id
		public String id; // 唯一id
		public String sid;// 枢纽名城
		public String state; // 状态
		public String stationid; // 枢纽id
		public String stationsid; // 请求机组信息时的枢纽id

		public AutonomouslSingle(String createtime, String excuteid, String id,
				String sid, String state, String stationid,String stationsid) {
			super();
			this.createtime = createtime;
			this.excuteid = excuteid;
			this.id = id;
			this.sid = sid;
			this.state = state;
			this.stationid = stationid;
			this.stationsid = stationsid;
		}

		public AutonomouslSingle() {
			super();
			// TODO Auto-generated constructor stub
		}

		public String getStationsid() {
			return stationsid;
		}

		public void setStationsid(String stationsid) {
			this.stationsid = stationsid;
		}

		public String getCreatetime() {
			return createtime;
		}

		public String getExcuteid() {
			return excuteid;
		}

		public String getId() {
			return id;
		}

		public String getSid() {
			return sid;
		}

		public String getState() {
			return state;
		}

		public String getStationid() {
			return stationid;
		}

		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}

		public void setExcuteid(String excuteid) {
			this.excuteid = excuteid;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setSid(String sid) {
			this.sid = sid;
		}

		public void setState(String state) {
			this.state = state;
		}

		public void setStationid(String stationid) {
			this.stationid = stationid;
		}
	}
}

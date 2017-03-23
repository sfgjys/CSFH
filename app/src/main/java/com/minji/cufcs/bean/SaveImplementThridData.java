package com.minji.cufcs.bean;

public class SaveImplementThridData {

	public String startouterlevel;
	public String startinlandlevel;
	public Date jsonStr;
	public String type;

	public static class Date {
		public String sid;
		public String id;
		public String rdstationid;
		public String memo;

		public String getSid() {
			return sid;
		}

		public String getId() {
			return id;
		}

		public String getRdstationid() {
			return rdstationid;
		}

		public String getMemo() {
			return memo;
		}

		public void setSid(String sid) {
			this.sid = sid;
		}

		public void setId(String id) {
			this.id = id;
		}

		public void setRdstationid(String rdstationid) {
			this.rdstationid = rdstationid;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public Date() {
			super();
		}

		public Date(String sid, String id, String rdstationid, String memo) {
			super();
			this.sid = sid;
			this.id = id;
			this.rdstationid = rdstationid;
			this.memo = memo;

		}

	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public SaveImplementThridData() {
		super();
	}

	public SaveImplementThridData(String startouterlevel,
			String startinlandlevel, Date jsonStr, String type) {
		super();
		this.startouterlevel = startouterlevel;
		this.startinlandlevel = startinlandlevel;
		this.jsonStr = jsonStr;
		this.type = type;
	}

	public String getStartouterlevel() {
		return startouterlevel;
	}

	public String getStartinlandlevel() {
		return startinlandlevel;
	}

	public Date getJsonStr() {
		return jsonStr;
	}

	public void setStartouterlevel(String startouterlevel) {
		this.startouterlevel = startouterlevel;
	}

	public void setStartinlandlevel(String startinlandlevel) {
		this.startinlandlevel = startinlandlevel;
	}

	public void setJsonStr(Date jsonStr) {
		this.jsonStr = jsonStr;
	}

}

package com.minji.cufcs.bean;

public class SaveUnitGson {

	public String sid;
	public String id;
	public String rdstationid;

	public SaveUnitGson() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SaveUnitGson(String sid, String excuteid, String rdstationid) {
		super();
		this.sid = sid;
		this.id = excuteid;
		this.rdstationid = rdstationid;
	}

	public String getSid() {
		return sid;
	}

	public String getExcuteid() {
		return id;
	}

	public String getRdstationid() {
		return rdstationid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public void setExcuteid(String excuteid) {
		this.id = excuteid;
	}

	public void setRdstationid(String rdstationid) {
		this.rdstationid = rdstationid;
	}

}

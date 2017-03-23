package com.minji.cufcs.bean;

public class AutonomouslySaveUnitGson {

	public String sid;
	public String id;
	public String dispatchstationid;

	public AutonomouslySaveUnitGson() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AutonomouslySaveUnitGson(String stationid, String excuteid, String id) {
		super();
		this.sid = stationid;
		this.id = excuteid;
		this.dispatchstationid = id;
	}

	public String getSid() {
		return sid;
	}

	public String getExcuteid() {
		return id;
	}

	public String getRdstationid() {
		return dispatchstationid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	public void setExcuteid(String excuteid) {
		this.id = excuteid;
	}

	public void setRdstationid(String rdstationid) {
		this.dispatchstationid = rdstationid;
	}

}

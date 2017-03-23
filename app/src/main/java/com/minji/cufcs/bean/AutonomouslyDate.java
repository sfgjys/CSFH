package com.minji.cufcs.bean;

public class AutonomouslyDate {

	public String sid;
	public String id;
	public String dispatchstationid;
	public String memo;

	public String getSid() {
		return sid;
	}

	public String getId() {
		return id;
	}

	public String getRdstationid() {
		return dispatchstationid;
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
		this.dispatchstationid = rdstationid;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public AutonomouslyDate() {
		super();
	}

	public AutonomouslyDate(String sid, String id, String dispatchstationid, String memo) {
		super();
		this.sid = sid;
		this.id = id;
		this.dispatchstationid = dispatchstationid;
		this.memo = memo;

	}
}

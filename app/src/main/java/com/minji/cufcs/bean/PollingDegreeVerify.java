package com.minji.cufcs.bean;

import java.util.List;

public class PollingDegreeVerify {

	public String sid;
	public String parttime;
	public String degree;
	public List<String> pp;
	public String id;
	public String state;
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
	public String getParttime() {
		return parttime;
	}
	public void setParttime(String parttime) {
		this.parttime = parttime;
	}
	public String getDegree() {
		return degree;
	}
	public void setDegree(String degree) {
		this.degree = degree;
	}
	public List<String> getPp() {
		return pp;
	}
	public void setPp(List<String> pp) {
		this.pp = pp;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public PollingDegreeVerify() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PollingDegreeVerify(String sid, String parttime, String degree,
			List<String> pp, String id, String state) {
		super();
		this.sid = sid;
		this.parttime = parttime;
		this.degree = degree;
		this.pp = pp;
		this.id = id;
		this.state = state;
	}


}

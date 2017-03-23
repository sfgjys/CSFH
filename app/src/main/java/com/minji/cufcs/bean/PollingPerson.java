package com.minji.cufcs.bean;

public class PollingPerson {

	public String dname;
	public String ename;
	public String id;
	public String username;
	public PollingPerson() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PollingPerson(String dname, String ename, String id, String username) {
		super();
		this.dname = dname;
		this.ename = ename;
		this.id = id;
		this.username = username;
	}
	public String getDname() {
		return dname;
	}
	public void setDname(String dname) {
		this.dname = dname;
	}
	public String getEname() {
		return ename;
	}
	public void setEname(String ename) {
		this.ename = ename;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	
}

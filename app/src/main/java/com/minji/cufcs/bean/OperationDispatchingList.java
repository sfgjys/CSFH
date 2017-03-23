package com.minji.cufcs.bean;

public class OperationDispatchingList {

	private String schedulingType;
	private String enforcement;
	private String makingTime;
	private String makingPeople;
	private String id;
	private String departmentid;

	public OperationDispatchingList(String schedulingType, String enforcement,
			String makingTime, String makingPeople, String id,
			String departmentid) {
		super();
		this.schedulingType = schedulingType;
		this.enforcement = enforcement;
		this.makingTime = makingTime;
		this.makingPeople = makingPeople;
		this.id = id;
		this.departmentid = departmentid;
	}

	public String getSchedulingType() {
		return schedulingType;
	}

	public String getEnforcement() {
		return enforcement;
	}

	public String getMakingTime() {
		return makingTime;
	}

	public String getMakingPeople() {
		return makingPeople;
	}

	public String getId() {
		return id;
	}

	public String getDepartmentid() {
		return departmentid;
	}

	public void setSchedulingType(String schedulingType) {
		this.schedulingType = schedulingType;
	}

	public void setEnforcement(String enforcement) {
		this.enforcement = enforcement;
	}

	public void setMakingTime(String makingTime) {
		this.makingTime = makingTime;
	}

	public void setMakingPeople(String makingPeople) {
		this.makingPeople = makingPeople;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setDepartmentid(String departmentid) {
		this.departmentid = departmentid;
	}

}

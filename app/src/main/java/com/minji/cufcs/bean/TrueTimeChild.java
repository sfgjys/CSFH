package com.minji.cufcs.bean;

public class TrueTimeChild {

	public int AllUnit;
	public String OpenUnit;
	public TrueTimeChild() {
		super();
		// TODO Auto-generated constructor stub
	}
	public TrueTimeChild(int allUnit, String openUnit) {
		super();
		AllUnit = allUnit;
		OpenUnit = openUnit;
	}
	public int getAllUnit() {
		return AllUnit;
	}
	public String getOpenUnit() {
		return OpenUnit;
	}
	public void setAllUnit(int allUnit) {
		AllUnit = allUnit;
	}
	public void setOpenUnit(String openUnit) {
		OpenUnit = openUnit;
	}
	
}

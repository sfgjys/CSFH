package com.minji.cufcs.bean;

public class RainDetails {

	public String name;
	public String time;
	public int total;

	public RainDetails(String name, String time, int total) {
		super();
		this.name = name;
		this.time = time;
		this.total = total;
	}

	public RainDetails() {
		super();
	}

	public String getName() {
		return name;
	}

	public String getTime() {
		return time;
	}

	public int getTotal() {
		return total;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public void setTotal(int total) {
		this.total = total;
	}
}

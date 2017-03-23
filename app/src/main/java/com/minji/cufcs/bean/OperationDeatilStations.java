package com.minji.cufcs.bean;

public class OperationDeatilStations {

	private String gatetype;
	private String keepcount;
	private String runcount;
	private String stationname;

	public OperationDeatilStations(String gatetype, String keepcount,
			String runcount, String stationname) {
		super();
		this.gatetype = gatetype;
		this.keepcount = keepcount;
		this.runcount = runcount;
		this.stationname = stationname;
	}

	public String getGatetype() {
		return gatetype;
	}

	public String getKeepcount() {
		return keepcount;
	}

	public String getRuncount() {
		return runcount;
	}

	public String getStationname() {
		return stationname;
	}

	public void setGatetype(String gatetype) {
		this.gatetype = gatetype;
	}

	public void setKeepcount(String keepcount) {
		this.keepcount = keepcount;
	}

	public void setRuncount(String runcount) {
		this.runcount = runcount;
	}

	public void setStationname(String stationname) {
		this.stationname = stationname;
	}

}

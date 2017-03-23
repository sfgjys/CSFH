package com.minji.cufcs.bean;

public class WaterDetails {

	public String HubName;
	public String OutWaterLevel;
	public String InWaterLevel;
	public String CollectTime;
	public String stationid;

	public WaterDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	public WaterDetails(String hubName, String outWaterLevel,
			String inWaterLevel, String collectTime, String stationid) {
		super();
		HubName = hubName;
		OutWaterLevel = outWaterLevel;
		InWaterLevel = inWaterLevel;
		CollectTime = collectTime;
		this.stationid = stationid;
	}

	public String getHubName() {
		return HubName;
	}

	public String getOutWaterLevel() {
		return OutWaterLevel;
	}

	public String getInWaterLevel() {
		return InWaterLevel;
	}

	public String getCollectTime() {
		return CollectTime;
	}

	public String getStationid() {
		return stationid;
	}

	public void setHubName(String hubName) {
		HubName = hubName;
	}

	public void setOutWaterLevel(String outWaterLevel) {
		OutWaterLevel = outWaterLevel;
	}

	public void setInWaterLevel(String inWaterLevel) {
		InWaterLevel = inWaterLevel;
	}

	public void setCollectTime(String collectTime) {
		CollectTime = collectTime;
	}

	public void setStationid(String stationid) {
		this.stationid = stationid;
	}

}

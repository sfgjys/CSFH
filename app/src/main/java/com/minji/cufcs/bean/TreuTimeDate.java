package com.minji.cufcs.bean;

public class TreuTimeDate {

	public String HubName;
	public String OutWaterLevel;
	public String InWaterLevel;
	public String AllUnit;
	public String OpenUnit;

	public TreuTimeDate() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TreuTimeDate(String hubName, String outWaterLevel,
			String inWaterLevel, String allUnit, String openUnit) {
		super();
		HubName = hubName;
		OutWaterLevel = outWaterLevel;
		InWaterLevel = inWaterLevel;
		AllUnit = allUnit;
		OpenUnit = openUnit;
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

	public String getAllUnit() {
		return AllUnit;
	}

	public String getOpenUnit() {
		return OpenUnit;
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

	public void setAllUnit(String allUnit) {
		AllUnit = allUnit;
	}

	public void setOpenUnit(String openUnit) {
		OpenUnit = openUnit;
	}

}

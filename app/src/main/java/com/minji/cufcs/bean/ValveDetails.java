package com.minji.cufcs.bean;

public class ValveDetails {
	public String mHubName;
	public String mValveNumber;
	public String mOperationState;
	public String mCollectTime;

	public ValveDetails(String mHubName, String mValveNumber,
			String mOperationState, String mCollectTime, String mPositionState) {
		super();
		this.mHubName = mHubName;
		this.mValveNumber = mValveNumber;
		this.mOperationState = mOperationState;
		this.mCollectTime = mCollectTime;
	}

	public ValveDetails() {
		super();
	}

	public String getmHubName() {
		return mHubName;
	}

	public void setmHubName(String mHubName) {
		this.mHubName = mHubName;
	}

	public String getmValveNumber() {
		return mValveNumber;
	}

	public void setmValveNumber(String mValveNumber) {
		this.mValveNumber = mValveNumber;
	}

	public String getmOperationState() {
		return mOperationState;
	}

	public void setmOperationState(String mOperationState) {
		this.mOperationState = mOperationState;
	}

	public String getmCollectTime() {
		return mCollectTime;
	}

	public void setmCollectTime(String mCollectTime) {
		this.mCollectTime = mCollectTime;
	}

}

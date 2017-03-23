package com.minji.cufcs.bean;

public class UnitDetails {

	public String mHubName;
	public String mValveNumber;
	public String OperationState;
	public String CollectTime;

	public String TrueTimeFlow;

	public String getTrueTimeFlow() {
		return TrueTimeFlow;
	}

	public void setTrueTimeFlow(String trueTimeFlow) {
		TrueTimeFlow = trueTimeFlow;
	}

	public UnitDetails(String mHubName, String mUnitNumber,
			String mOperationState, String mCollectTime,String TrueTimeFlow) {
		super();
		this.mHubName = mHubName;
		this.mValveNumber = mUnitNumber;
		this.OperationState = mOperationState;
		this.CollectTime = mCollectTime;
		this.TrueTimeFlow=TrueTimeFlow;
	}

	public UnitDetails() {
		super();
	}

	public String getmHubName() {
		return mHubName;
	}

	public void setmHubName(String mHubName) {
		this.mHubName = mHubName;
	}

	public String getmUnitNumber() {
		return mValveNumber;
	}

	public void setmUnitNumber(String mUnitNumber) {
		this.mValveNumber = mUnitNumber;
	}

	public String getmOperationState() {
		return OperationState;
	}

	public void setmOperationState(String mOperationState) {
		this.OperationState = mOperationState;
	}

	public String getmCollectTime() {
		return CollectTime;
	}

	public void setmCollectTime(String mCollectTime) {
		this.CollectTime = mCollectTime;
	}

}

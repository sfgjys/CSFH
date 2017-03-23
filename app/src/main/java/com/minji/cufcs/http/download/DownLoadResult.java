package com.minji.cufcs.http.download;

public interface DownLoadResult {

	public void linkFail();

	public void updateCurrent(int pbCurrent);

	public void downLoadFinish(String filePathName);
}

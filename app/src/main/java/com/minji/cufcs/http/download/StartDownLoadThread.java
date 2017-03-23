package com.minji.cufcs.http.download;

public interface StartDownLoadThread {

	public void startDownLoadThread(int startIndex, int endIndex,
									String urlPath, String fileName);

	public void linkFail();
}

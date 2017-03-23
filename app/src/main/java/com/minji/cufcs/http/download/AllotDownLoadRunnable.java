package com.minji.cufcs.http.download;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.UtilSDCard;
import com.minji.cufcs.uitls.ViewsUitls;

import android.os.Environment;

/**
 * 该Runnable主要是请求获取下载文件的长度以便提前创建文件所在空间, 以及开启多个下载线程
 * */
public class AllotDownLoadRunnable implements Runnable {

	private String urlPath;
	/**
	 * 文件名加上格式
	 * */
	private String name;
	/**
	 * 下载文件存储地址
	 * */
	private String filePath;

	private StartDownLoadThread startDownLoadThread;

	public AllotDownLoadRunnable(String urlPath, String name, String filePath,
			StartDownLoadThread startDownLoadThread) {
		super();
		this.urlPath = urlPath;
		this.name = name;
		this.filePath = filePath;
		this.startDownLoadThread = startDownLoadThread;
	}

	@Override
	public void run() {
		System.out.println("AllotDownLoadRunnable");
		
		try {
			// 对下载网址进行URL封装
			URL url = new URL(urlPath);
			// 使用URL对象获取HttpURLConnection网络链接对象
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置发送get请求
			conn.setRequestMethod("GET");// get要求大写 默认就是get请求
			// 设置请求超时时间
			conn.setConnectTimeout(5000);
			// 获取服务器返回的状态码

			int code = conn.getResponseCode();
			System.out.println("code = " + code);
			// 如果code == 200 说明请求成功
			if (code == 200) {
				// 获取索要下在文件的长度
				int length = conn.getContentLength();
				System.out.println("length:" + length);

				String fileName = filePath + "/" + name;
				// 判断要下载的文件是否已经经过第一步的请求开辟出了存储下载文件的空间
				boolean fileIsExist = UtilSDCard.fileIsExists(fileName);
				File file = new File(fileName);
				if (!fileIsExist && length != 0) {// 文件不存在
					System.out.println("文件不存在");
					// 在客户端创建一个大小和服务器一模一样的文件 目的提前在客客户端把空间申请出来
					RandomAccessFile raf = new RandomAccessFile(fileName, "rw");
					// 设置文件的大小 但是无内容
					raf.setLength(length);
				} else {
					if (length != ((int) file.length()) && length != 0) {// 文件存在但长度不一样
						System.out.println("文件存在但长度不一样");

						// 这里不止要删除要下载的文件也要删除记录了下载位置的文件
						File files = new File(filePath);
						if (files.exists()) {
							String[] list = files.list();
							for (int i = 0; i < list.length; i++) {
								File deleteFile = new File(files, "/" + list[i]);
								deleteFile.delete();
							}
						}

						// 在客户端创建一个大小和服务器一模一样的文件 目的提前在客客户端把空间申请出来
						RandomAccessFile raf = new RandomAccessFile(fileName,
								"rw");
						// 设置文件的大小 但是无内容
						raf.setLength(length);
					}
				}

				// 算出线程下载的开始位置和结束位置
				int startIndex = 0;
				int endIndex = length - 1;// 总长度-1
				// DownLoadThread downLoadThread = new DownLoadThread(
				// startIndex, endIndex, i, urlPath, filePath + "/"
				// + name, threadCount);
				// downLoadThread.start();
				startDownLoadThread.startDownLoadThread(startIndex, endIndex,
						urlPath, fileName);
			} else {
				System.out.println("第一次请求下载文件的长度失败,code!=200");
				startDownLoadThread.linkFail();
			}

		} catch (Exception e) {
			startDownLoadThread.linkFail();
			System.out.println("请查看是否连接了网络");
		}
	}

}

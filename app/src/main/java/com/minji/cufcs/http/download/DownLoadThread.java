package com.minji.cufcs.http.download;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadThread implements Runnable {
	// 把每个线程下载的开始位置和结束位置传递过来
	private int threadId;
	private int startIndex;
	private int endIndex;

	private int pbMaxSize; // 进度条的最大值
	private int pbLastposition;// 代表上次下载的位置
	private String pathURL;
	private String filePathName;
	private DownLoadResult downLoadResult;
	private double middle = 0;

	public DownLoadThread(int startIndex, int endIndex, String pathURL,
			String filePathName, int threadCount, DownLoadResult downLoadResult) {
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.threadId = 0;
		this.pathURL = pathURL;
		this.filePathName = filePathName;
		this.downLoadResult = downLoadResult;
	}

	@Override
	public void run() {
		System.out.println("DownLoadThread");
		
		// 进行具体的去服务器下载数据
		try {
			// 算出进度条的最大值
			pbMaxSize = endIndex - startIndex;
			// 创建URL 对象指定我们要访问的 网址(路径)
			URL url = new URL(pathURL);
			// 拿到HttpURLConnection对象 用于发送或者接收数据
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			// 设置发送get请求
			conn.setRequestMethod("GET");// get要求大写 默认就是get请求
			// 设置请求超时时间
			conn.setConnectTimeout(5000);

			// 判断是否中断过 继续上次的位置继续下 该文件是分身文件
			File file = new File(filePathName + threadId + ".txt");
			if (file.exists() && file.length() > 0) {
				FileInputStream fis = new FileInputStream(file);
				BufferedReader bufr = new BufferedReader(new InputStreamReader(
						fis));
				String lastPositionn = bufr.readLine(); // 读取上次下载的时写入的具体位置数字

				// 上次进度条下载的位置
				pbLastposition = Integer.parseInt(lastPositionn);
				// 下次在下载的时候 要改变一下 startIndex位置
				startIndex = Integer.parseInt(lastPositionn);

				System.out.println("线程id:" + threadId + "真实开始位置:" + startIndex
						+ "----:" + endIndex);
				fis.close();
			}

			// 要设置一个请求头信息 告诉服务器每个线程下载的开始位置 和结束位置
			conn.setRequestProperty("Range", "bytes=" + startIndex + "-"
					+ endIndex);

			// 获取服务器返回的状态码
			int code = conn.getResponseCode();
			// 如果code == 200 说明请求成功 206代表请求部分数据成功
			if (code == 206) {
				// 获取服务器返回的数据 是以流的形式返回的 由于把流转换成字符串是一个非常常见的操作
				InputStream in = conn.getInputStream();
				// 获取一开始创建的空内容的文件对象 ，此对象将把获取的流数据按照开始与结束的位置在 文件对象中写入
				RandomAccessFile raf = new RandomAccessFile(filePathName, "rw");
				raf.seek(startIndex);// 设置写入的开始位置
				int len = -1;

				int total = 0; // 代表当前线程下载的大小
				byte[] buffer = new byte[1024 * 1024];// 1M
				while ((len = in.read(buffer)) != -1) {
					raf.write(buffer, 0, len);
					// 断点的逻辑 就是当前线程下载的位置 存起来 算出当前线程下载的位置
					total += len;
					// 当前线程下载的位置
					int currentThreadPosition = startIndex + total;
					// 把当前线程下载的位置 存起来 如果中断过 那么下次再下载的时候 就按照这个保存的位置继续下载
					RandomAccessFile raff = new RandomAccessFile(filePathName
							+ threadId + ".txt", "rwd");
					// 记录此时写入文件的数据所在长度位置
					raff.write(String.valueOf(currentThreadPosition).getBytes());
					raff.close();

					// 时时的更新进度条
					double max = pbMaxSize * 1.0;
					double current = (pbLastposition + total) * 1.0;
					double s = current / max;
					final int pbCurrent = (int) (s * 100);

					if (pbCurrent != middle) {
						downLoadResult.updateCurrent(pbCurrent);
					}
					middle = pbCurrent;

				}
				in.close();
				raf.close();// 关闭流
				System.out.println("线程id::" + threadId + "下载完毕了");

				downLoadResult.downLoadFinish(filePathName);

			} else {
				System.out.println("code!=206");
				downLoadResult.linkFail();
			}
		} catch (Exception e) {
			downLoadResult.linkFail();
			System.out.println("请查看是否连接了网络");
		}

	}
}

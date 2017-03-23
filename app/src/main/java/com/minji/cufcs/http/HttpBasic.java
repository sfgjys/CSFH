package com.minji.cufcs.http;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.lidroid.xutils.http.client.multipart.MultipartEntity;
import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.minji.cufcs.ApiField;
import com.minji.cufcs.sqlite.MySQLiteOpenHelper;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ViewsUitls;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/3/3.
 */

public class HttpBasic {

	private static HttpClient httpClient;

	public HttpBasic() {
		if (httpClient == null) {
			getHttpClient();
		}
	}

	public static HttpClient getHttpClient() {
		if (null == httpClient) {
			synchronized (HttpBasic.class) {
				if (null == httpClient) {
					HttpParams httpParams = new BasicHttpParams();
					HttpConnectionParams.setConnectionTimeout(httpParams,
							5 * 1000);
					HttpConnectionParams.setSoTimeout(httpParams, 5 * 1000);
					HttpConnectionParams.setSocketBufferSize(httpParams, 8192);

					String userAgent = "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2) Gecko/20100115 Firefox/3.6";
					httpParams.setParameter(ClientPNames.COOKIE_POLICY,
							CookiePolicy.BROWSER_COMPATIBILITY);

					HttpProtocolParams.setUserAgent(httpParams, userAgent);
					ConnPerRouteBean connPerRoute = new ConnPerRouteBean(100);
					ConnManagerParams.setMaxConnectionsPerRoute(httpParams,
							connPerRoute);
					ConnManagerParams.setMaxTotalConnections(httpParams, 200);
					ConnManagerParams.setTimeout(httpParams, 60000);
					SchemeRegistry schReg = new SchemeRegistry();
					schReg.register(new Scheme("http", PlainSocketFactory
							.getSocketFactory(), 80));
					schReg.register(new Scheme("https", SSLSocketFactory
							.getSocketFactory(), 443));

					ClientConnectionManager conMgr = new ThreadSafeClientConnManager(
							httpParams, schReg);
					httpClient = new DefaultHttpClient(conMgr, httpParams);
				}
			}
		}
		return httpClient;
	}

	public String getBack(String url) throws Exception {
		String html = "";
		HttpGet httpGet = new HttpGet(url);
		HttpClient client = getHttpClient();
		HttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpGet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (httpResponse == null) {
			return "null";
		} else {
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			System.out.println("statusCode: " + statusCode);
			if (statusCode == 200) {
				html = EntityUtils.toString(httpResponse.getEntity(),
						HTTP.UTF_8);
				if (html.contains("html") || html.equals("")) {
					Log.v("HttpBasic", "重新登录！");

					String password = getPassWord();
					String user = SharedPreferencesUtil.getString(
							ViewsUitls.getContext(), "mUser", "");

					if (!StringUtils.isEmpty(user)
							&& !StringUtils.isEmpty(password)) {
						HttpBasic httpBasic = new HttpBasic();
						List<BasicNameValuePair> list2 = new ArrayList<BasicNameValuePair>();
						list2.add(new BasicNameValuePair("username", user));
						list2.add(new BasicNameValuePair("password", password));
						String address = SharedPreferencesUtil.getString(
								ViewsUitls.getContext(), "address", "");
						String body = httpBasic.postBack(address
								+ ApiField.LOGIN, list2);
						System.out.println(body);
						Log.v("HttpBasic", "重新登录成功！");
						return "{'relogin':true}";
					}
				}

			} else {
				html = "Error Response: ";
			}
		}
		return html;
	}

	public String postBack(String url, List<BasicNameValuePair> list)
			throws Exception {
		String strResult = "";
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(new UrlEncodedFormEntity(list, HTTP.UTF_8));
		HttpClient client = getHttpClient();

		HttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (httpResponse == null) {
			return "null";
		} else {

			int statusCode = httpResponse.getStatusLine().getStatusCode();
			System.out.println("statusCode: " + statusCode);
			if (statusCode == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("strResult: " + strResult);
				if (strResult.contains("html") || strResult.equals("")) {
					Log.v("HttpBasic", "重新登录！");

					String password = getPassWord();
					String user = SharedPreferencesUtil.getString(
							ViewsUitls.getContext(), "mUser", "");

					if (!StringUtils.isEmpty(user)
							&& !StringUtils.isEmpty(password)) {
						HttpBasic httpBasic = new HttpBasic();
						List<BasicNameValuePair> list2 = new ArrayList<BasicNameValuePair>();
						list2.add(new BasicNameValuePair("username", user));
						list2.add(new BasicNameValuePair("password", password));
						String address = SharedPreferencesUtil.getString(
								ViewsUitls.getContext(), "address", "");
						String body = httpBasic.postBack(address
								+ ApiField.LOGIN, list2);
						System.out.println(body);
						Log.v("HttpBasic", "重新登录成功！");
						return "{'relogin':true}";
					}
				}

			} else {
				strResult = "Error Response: ";
				// + httpResponse.getStatusLine().toString();
				// System.out.println("111");
				// throw new Exception(strResult);
			}
		}
		return strResult;
	}

	/**
	 * 上传文件
	 *
	 * HttpPost post = new HttpPost("http://localhost:8080/action.jsp");
	 * FileBody fileBody = new FileBody(new File("/home/sendpix0.jpg"));
	 * StringBody stringBody = new StringBody("文件的描述");
	 * */
	public String postFile(String url, FileBody fileBody, StringBody stringBody)
			throws Exception {
		String strResult = "";
		HttpPost httpPost = new HttpPost(url);
		MultipartEntity entity = new MultipartEntity();
		entity.addPart("file", fileBody);
		entity.addPart("desc", stringBody);
		httpPost.setEntity(entity);
		HttpClient client = getHttpClient();

		HttpResponse httpResponse = null;
		try {
			httpResponse = client.execute(httpPost);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (httpResponse == null) {
			return "null";
		} else {

			int statusCode = httpResponse.getStatusLine().getStatusCode();
			System.out.println("statusCode: " + statusCode);
			if (statusCode == 200) {
				strResult = EntityUtils.toString(httpResponse.getEntity());
				if (strResult.contains("html")) {
					Log.v("HttpBasic", "重新登录！");

					String password = getPassWord();
					String user = SharedPreferencesUtil.getString(
							ViewsUitls.getContext(), "mUser", "");

					if (!StringUtils.isEmpty(user)
							&& !StringUtils.isEmpty(password)) {
						HttpBasic httpBasic = new HttpBasic();
						List<BasicNameValuePair> list2 = new ArrayList<BasicNameValuePair>();
						list2.add(new BasicNameValuePair("username", user));
						list2.add(new BasicNameValuePair("password", password));
						String address = SharedPreferencesUtil.getString(
								ViewsUitls.getContext(), "address", "");
						String body = httpBasic.postBack(address
								+ ApiField.LOGIN, list2);
						System.out.println(body);
						Log.v("HttpBasic", "重新登录成功！");
						return "{'relogin':true}";
					}
				}

			} else {
				strResult = "Error Response: ";
				// + httpResponse.getStatusLine().toString();
				// System.out.println("111");
				// throw new Exception(strResult);
			}
		}
		return strResult;
	}

	private String getPassWord() {

		MySQLiteOpenHelper mySQLiteOpenHelper = new MySQLiteOpenHelper(ViewsUitls.getContext());
		SQLiteDatabase writableDatabase = mySQLiteOpenHelper.getWritableDatabase();
		Cursor cursor = writableDatabase.query("t_user",
				new String[] { "c_password" }, "c_pw>?", new String[] { "0" },
				null, null, null);
		String password = null;
		while (cursor.moveToNext()) {
			password = cursor.getString(0);
		}
		cursor.close();
		writableDatabase.close();
		mySQLiteOpenHelper.close();
		return password;
	}
}
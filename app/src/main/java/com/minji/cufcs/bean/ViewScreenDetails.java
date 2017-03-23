package com.minji.cufcs.bean;

import java.util.ArrayList;
import java.util.List;

public class ViewScreenDetails {

	public String name;
	public String url;
	public String port;
	public String username;
	public String password;
	public List<ViewScreenDetail> cameras = new ArrayList<ViewScreenDetail>();

	public ViewScreenDetails() {
		super();
	}

	public ViewScreenDetails(String name, String url, String port,
			String username, String password, List<ViewScreenDetail> cameras) {
		super();
		this.name = name;
		this.url = url;
		this.port = port;
		this.username = username;
		this.password = password;
		this.cameras = cameras;
	}

	public String getName() {
		return name;
	}

	public String getUrl() {
		return url;
	}

	public String getPort() {
		return port;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public List<ViewScreenDetail> getCameras() {
		return cameras;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setCameras(List<ViewScreenDetail> cameras) {
		this.cameras = cameras;
	}

	public static class ViewScreenDetail {
		public String name;
		public String channel;

		public ViewScreenDetail(String name, String channel) {
			super();
			this.name = name;
			this.channel = channel;
		}

		public ViewScreenDetail() {
			super();
		}

		public String getName() {
			return name;
		}

		public String getChannel() {
			return channel;
		}

		public void setName(String name) {
			this.name = name;
		}

		public void setChannel(String channel) {
			this.channel = channel;
		}
	}
}

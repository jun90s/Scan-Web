package com.jun90.projects.scan.web.model;

import java.util.Date;

public class Token {
	
	private String key;
	private API api;
	private User user;
	private Date expires = new Date();
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public API getAPI() {
		return api;
	}

	public void setAPI(API api) {
		this.api = api;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
	}

}

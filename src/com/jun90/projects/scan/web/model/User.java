package com.jun90.projects.scan.web.model;

import java.util.Date;

public class User {
	
	private long phoneNumber;
	private int type = 0;
	private int loginCode = 0;
	private Date loginCodeExpires = new Date();
	
	public long getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getLoginCode() {
		return loginCode;
	}
	
	public void setLoginCode(int loginCode) {
		this.loginCode = loginCode;
	}
	
	public Date getLoginCodeExpires() {
		return loginCodeExpires;
	}
	
	public void setLoginCodeExpires(Date loginCodeExpires) {
		this.loginCodeExpires = loginCodeExpires;
	}
	
}

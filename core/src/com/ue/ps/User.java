package com.ue.ps;

public class User {
	
	private String userName;
	private String token;
	
	public User(String userName) {
		this.userName = userName;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public void setUserName(String name) {
		this.userName = name;
	}
}

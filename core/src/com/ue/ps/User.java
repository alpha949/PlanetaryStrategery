package com.ue.ps;

public class User {
	
	private String username;
	private String token;
	
	public User(String userName) {
		this.username = userName;
	}
	
	public String getUserName() {
		return this.username;
	}
	
	public void setUserName(String name) {
		this.username = name;
	}
	
	public User() {
		this.username = "";
	}
}

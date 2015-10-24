package com.vhi.hsm.model;

public class User {

	private String userName;

	private String email;

	private int societyId;
	
	private String name;

	public User() {
		super();
	}

	public User(String userName, String email, int societyId) {
		super();
		this.userName = userName;
		this.email = email;
		this.societyId = societyId;
	}

	public User(String userName, String email) {
		super();
		this.userName = userName;
		this.email = email;
	}


	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getSocietyId() {
		return societyId;
	}

	public void setSocietyId(int societyId) {
		this.societyId = societyId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}

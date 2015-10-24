package com.vhi.hsm.model;

public class Society {

	private int society_id;

	private String name;

	private String address;

	private String registrationNumber;

	private String registrationDate;

	public Society() {
		super();
	}

	public Society(int society_id, String name, String address, String registrationNumber, String registrationDate) {
		super();
		this.society_id = society_id;
		this.name = name;
		this.address = address;
		this.registrationNumber = registrationNumber;
		this.registrationDate = registrationDate;
	}

	public Society(String name, String address, String registrationNumber, String registrationDate) {
		super();
		this.name = name;
		this.address = address;
		this.registrationNumber = registrationNumber;
		this.registrationDate = registrationDate;
	}

	public int getSociety_id() {
		return society_id;
	}

	public void setSociety_id(int society_id) {
		this.society_id = society_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

}

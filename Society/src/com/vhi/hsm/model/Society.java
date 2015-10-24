package com.vhi.hsm.model;

import java.util.Map;

public class Society {

	private int societyId;

	private String name;

	private String address;

	private String registrationNumber;

	private String registrationDate;

	private Map<Integer, PropertyGroup> propertyGroupMap;

	private Map<Integer, PropertyType> propertyTypeMap;

	public Society() {
		super();
	}

	public Society(int society_id, String name, String address, String registrationNumber, String registrationDate) {
		super();
		this.societyId = society_id;
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

	public int getSocietyId() {
		return societyId;
	}

	public void setSocietyId(int society_id) {
		this.societyId = society_id;
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

	public Map<Integer, PropertyGroup> getPropertyGroupMap() {
		return propertyGroupMap;
	}

	public void setPropertyGroupMap(Map<Integer, PropertyGroup> propertyGroupMap) {
		this.propertyGroupMap = propertyGroupMap;
	}

	public Map<Integer, PropertyType> getPropertyTypeMap() {
		return propertyTypeMap;
	}

	public void setPropertyTypeMap(Map<Integer, PropertyType> propertyTypeMap) {
		this.propertyTypeMap = propertyTypeMap;
	}

}

package com.vhi.hsm.model;

import java.util.HashMap;

public class Society {

	private int societyId;

	private String name;

	private String address;

	private String registrationNumber;

	private String registrationDate;

	private HashMap<Integer, PropertyGroup> propertyGroups;

	private HashMap<Integer, PropertyType> propertyTypes;
	
	private HashMap<Integer, Wing> wings;
	
	private HashMap<Integer, FloorPlan> floorPlans;
	
	private HashMap<Integer, AssetType> assetTypes;

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

	public HashMap<Integer, PropertyGroup> getPropertyGroups() {
		return propertyGroups;
	}

	public void setPropertyGroups(HashMap<Integer, PropertyGroup> propertyGroups) {
		this.propertyGroups = propertyGroups;
	}

	public HashMap<Integer, PropertyType> getPropertyTypes() {
		return propertyTypes;
	}

	public void setPropertyTypes(HashMap<Integer, PropertyType> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}

	public HashMap<Integer, Wing> getWings() {
		return wings;
	}

	public void setWings(HashMap<Integer, Wing> wings) {
		this.wings = wings;
	}

	public HashMap<Integer, FloorPlan> getFloorPlans() {
		return floorPlans;
	}

	public void setFloorPlans(HashMap<Integer, FloorPlan> floorPlans) {
		this.floorPlans = floorPlans;
	}

	public HashMap<Integer, AssetType> getAssetTypes() {
		return assetTypes;
	}

	public void setAssetTypes(HashMap<Integer, AssetType> assetTypes) {
		this.assetTypes = assetTypes;
	}
	
}

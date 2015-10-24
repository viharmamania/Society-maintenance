package com.vhi.hsm.model;

import java.util.HashMap;

public class Wing {
	
	private int societyId;
	
	private int wingId;
	
	private String name;
	
	private int noOfFloors;
	
	private HashMap<Integer, Floor> floors;
	
	public Wing() {
		
	}

	public int getSocietyId() {
		return societyId;
	}

	public void setSocietyId(int societyId) {
		this.societyId = societyId;
	}

	public int getWingId() {
		return wingId;
	}

	public void setWingId(int wingId) {
		this.wingId = wingId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNoOfFloors() {
		return noOfFloors;
	}

	public void setNoOfFloors(int noOfFloors) {
		this.noOfFloors = noOfFloors;
	}

	public HashMap<Integer, Floor> getFloors() {
		return floors;
	}

	public void setFloors(HashMap<Integer, Floor> floors) {
		this.floors = floors;
	}	

}

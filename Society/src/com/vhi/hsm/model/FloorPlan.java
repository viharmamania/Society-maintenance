package com.vhi.hsm.model;

import java.util.HashMap;

public class FloorPlan {

	private int societyId;

	private int floorPlanId;

	private String desription;

	private int noOfProperty;

	private HashMap<Integer, FloorPlanDesign> floorPlanDesigns;

	public HashMap<Integer, FloorPlanDesign> getFloorPlanDesigns() {
		return floorPlanDesigns;
	}

	public void setFloorPlanDesigns(HashMap<Integer, FloorPlanDesign> floorPlanDesigns) {
		this.floorPlanDesigns = floorPlanDesigns;
	}

	public FloorPlan() {
		floorPlanDesigns = new HashMap<Integer, FloorPlanDesign>();
	}

	public int getSocietyId() {
		return societyId;
	}

	public void setSocietyId(int societyId) {
		this.societyId = societyId;
	}

	public int getFloorPlanId() {
		return floorPlanId;
	}

	public void setFloorPlanId(int floorPlanId) {
		this.floorPlanId = floorPlanId;
	}

	public String getDesription() {
		return desription;
	}

	public void setDesription(String desription) {
		this.desription = desription;
	}

	public int getNoOfProperty() {
		return noOfProperty;
	}

	public void setNoOfProperty(int noOfProperty) {
		this.noOfProperty = noOfProperty;
	}

}

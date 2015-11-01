package com.vhi.hsm.model;

import java.util.ArrayList;

public class Charge {

	private String description;

	private double amount;

	private double tempCharges;

	private boolean isCancelled;
	
	private ArrayList<Integer> assignedPropertyGroup;
	private ArrayList<Integer> assignedPropertyType;
	private ArrayList<Integer> assignedProperty;
	
	public Charge() {
		assignedProperty = new ArrayList<Integer>();
		assignedPropertyGroup = new ArrayList<Integer>();
		assignedPropertyType = new ArrayList<Integer>();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getTempCharges() {
		return tempCharges;
	}

	public void setTempCharges(double tempCharges) {
		this.tempCharges = tempCharges;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	
	public ArrayList<Integer> getAssignedPropertyGroup() {
		return assignedPropertyGroup;
	}

	public void setAssignedPropertyGroup(ArrayList<Integer> assignedPropertyGroup) {
		this.assignedPropertyGroup = assignedPropertyGroup;
	}

	public ArrayList<Integer> getAssignedPropertyType() {
		return assignedPropertyType;
	}

	public void setAssignedPropertyType(ArrayList<Integer> assignedPropertyType) {
		this.assignedPropertyType = assignedPropertyType;
	}

	public ArrayList<Integer> getAssignedProperty() {
		return assignedProperty;
	}

	public void setAssignedProperty(ArrayList<Integer> assignedProperty) {
		this.assignedProperty = assignedProperty;
	}

}

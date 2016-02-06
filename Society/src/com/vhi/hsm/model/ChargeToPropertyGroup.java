package com.vhi.hsm.model;

public class ChargeToPropertyGroup {
	int societyId;
	int chargeId;
	String propertyGroup;
	public int getSocietyId() {
		return societyId;
	}
	public void setSocietyId(int societyId) {
		this.societyId = societyId;
	}
	public int getChargeId() {
		return chargeId;
	}
	public void setChargeId(int chargeId) {
		this.chargeId = chargeId;
	}
	public String getPropertyGroup() {
		return propertyGroup;
	}
	public void setPropertyGroup(String propertyGroup) {
		this.propertyGroup = propertyGroup;
	}
}

package com.vhi.hsm.model;

import java.util.HashMap;

public class PropertyAsset {
	
	private int societyId;
	
	private String assetType;
	
	private int assetNumber;
	
	private String assetDetails;
	
	private boolean isCancelled;
	
	public PropertyAsset() {
		
	}

	public int getSocietyId() {
		return societyId;
	}

	public void setSocietyId(int societyId) {
		this.societyId = societyId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public int getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(int assetNumber) {
		this.assetNumber = assetNumber;
	}

	public String getAssetDetails() {
		return assetDetails;
	}

	public void setAssetDetails(String assetDetails) {
		this.assetDetails = assetDetails;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	
}

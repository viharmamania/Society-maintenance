package com.vhi.hsm.model;

public class Charge {

	private String description;

	private double amount;

	private double tempCharges;

	private boolean isCancelled;

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

}

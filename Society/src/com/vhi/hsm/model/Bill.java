package com.vhi.hsm.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.sql.Date;

public class Bill {
	
	private int billId;
	
	private int propertyId;
	
	private double amount;
	
	private Date billDate;
	
	private int paymentId;
	
	private String modifiedBy;
	
	private Timestamp lastModified;
	
	private boolean isCancelled;
	
	private ArrayList<Integer> assignedCharges;
	
	public Bill() {
		assignedCharges = new ArrayList<Integer>();
	}

	public int getBillId() {
		return billId;
	}

	public void setBillId(int billId) {
		this.billId = billId;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public Date getBillDate() {
		return billDate;
	}

	public void setBillDate(Date billDate) {
		this.billDate = billDate;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Timestamp getLastModified() {
		return lastModified;
	}

	public void setLastModified(Timestamp lastModified) {
		this.lastModified = lastModified;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public ArrayList<Integer> getAssignedCharges() {
		return assignedCharges;
	}

	public void setAssignedCharges(ArrayList<Integer> assignedCharges) {
		this.assignedCharges = assignedCharges;
	}

}

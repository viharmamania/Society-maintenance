package com.vhi.hsm.model;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class Bill {
	
	private int billId;
	
	private int societyId;
	
	private int propertyId;
	
	private double amount;
	
	private Date billDate;
	
	private int paymentId;
	
	private String modifiedBy;
	
	private Timestamp lastModified;
	
	private boolean isCancelled;
	
	private ArrayList<Integer> assignedCharges;
	
	private static PreparedStatement readStatement, chargeReadStatement, 
									insertStatement,
									updateStatement,
									deleteStatement;
	
	private final static Logger LOG = Logger.getLogger(Bill.class);
	
	private Bill() {
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
	
	public int getSocietyId() {
		return societyId;
	}

	public void setSocietyId(int societyId) {
		this.societyId = societyId;
	}

	public static Bill read(int billId) {
		Bill bill = null;
		
		if (readStatement == null) {
			readStatement = SQLiteManager.getPreparedStatement("SELECT * FROM " + Constants.Table.Bill.TABLE_NAME
					+ " WHERE " + Constants.Table.Bill.FieldName.BILL_ID + " = ?");
			
			chargeReadStatement = SQLiteManager.getPreparedStatement("SELECT * FROM " + Constants.Table.BillCharge.TABLE_NAME
					+ " WHERE " + Constants.Table.Bill.FieldName.BILL_ID + " = ?");
		}
		
		if (readStatement != null) {
			try {
				readStatement.clearParameters();
				readStatement.setInt(1, billId);
				ResultSet resultSet = readStatement.executeQuery();
				if (resultSet != null && resultSet.next()) {
					bill = new Bill();
					bill.billId = resultSet.getInt(Constants.Table.Bill.FieldName.BILL_ID);
					bill.societyId = resultSet.getInt(Constants.Table.Society.FieldName.SOCIETY_ID);
					bill.propertyId = resultSet.getInt(Constants.Table.Property.FieldName.PROPERTY_ID);
					bill.amount = resultSet.getDouble(Constants.Table.Bill.FieldName.AMOUNT);
					bill.billDate = resultSet.getDate(Constants.Table.Bill.FieldName.BILL_TIMESTAMP);
					bill.paymentId = resultSet.getInt(Constants.Table.Payment.FieldName.PAYMENT_ID);
					bill.modifiedBy = resultSet.getString(Constants.Table.Bill.FieldName.MODIFIED_BY);
					bill.lastModified = resultSet.getTimestamp(Constants.Table.Bill.FieldName.LAST_MODIFIED);
					bill.isCancelled = resultSet.getBoolean(Constants.Table.Bill.FieldName.IS_CANCELLED);
					
					if (chargeReadStatement != null) {
						chargeReadStatement.clearParameters();
						chargeReadStatement.setInt(1, billId);
						resultSet = chargeReadStatement.executeQuery();
						if (resultSet != null) {
							while (resultSet.next()) {
								bill.assignedCharges.add(new Integer(resultSet.getInt(Constants.Table.Charge.FieldName.CHARGE_ID)));
							}
						}
					}
				}
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
		}
		
		return bill;
	}
	
	public static boolean save(Bill bill, boolean insertEntry) {
		boolean result = false;
		
		if (bill != null) {
			if (insertEntry) {
				
				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement("INSERT INTO " + Constants.Table.Bill.TABLE_NAME
							+ "("
							+ Constants.Table.Society.FieldName.SOCIETY_ID + ", "
							+ Constants.Table.Property.FieldName.PROPERTY_ID + ", "
							+ Constants.Table.Bill.FieldName.AMOUNT + ", "
							+ Constants.Table.Payment.FieldName.PAYMENT_ID + ", "
							+ Constants.Table.Bill.FieldName.BILL_TIMESTAMP + ", "
							+ Constants.Table.Bill.FieldName.IS_CANCELLED + ", "
							+ Constants.Table.Bill.FieldName.MODIFIED_BY + ", "
							+ Constants.Table.Bill.FieldName.LAST_MODIFIED
							+ ")"
							+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
				}
				
				if (insertStatement != null) {
					try {
						insertStatement.clearParameters();
						insertStatement.setInt(1, bill.societyId);
						insertStatement.setInt(2, bill.propertyId);
						insertStatement.setDouble(3, bill.amount);
						insertStatement.setInt(4, bill.paymentId);
						insertStatement.setDate(5, bill.billDate);
						insertStatement.setBoolean(6, bill.isCancelled);
						insertStatement.setString(7, bill.modifiedBy);
						insertStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
						result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);
						if (result) {
							ResultSet generatedKeys = insertStatement.getGeneratedKeys();
							if (generatedKeys != null) {
								bill.billId = generatedKeys.getInt(1);
							}
						}
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}
				
			} else {
				
				if (updateStatement == null) {
					updateStatement = SQLiteManager.getPreparedStatement("UPDATE " + Constants.Table.Bill.TABLE_NAME
							+ " SET "
							+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?, "
							+ Constants.Table.Property.FieldName.PROPERTY_ID + " = ?, "
							+ Constants.Table.Bill.FieldName.AMOUNT + " = ?, "
							+ Constants.Table.Payment.FieldName.PAYMENT_ID + " = ?, "
							+ Constants.Table.Bill.FieldName.BILL_TIMESTAMP + " = ?, "
							+ Constants.Table.Bill.FieldName.IS_CANCELLED + " = ?, "
							+ Constants.Table.Bill.FieldName.MODIFIED_BY + " = ?, "
							+ Constants.Table.Bill.FieldName.LAST_MODIFIED + " = ?"
							+ " WHERE " + Constants.Table.Bill.FieldName.BILL_ID + " = ?");
				}
				
				if (updateStatement != null) {
					try {
						updateStatement.clearParameters();
						updateStatement.setInt(1, bill.societyId);
						updateStatement.setInt(2, bill.propertyId);
						updateStatement.setDouble(3, bill.amount);
						updateStatement.setInt(4, bill.paymentId);
						updateStatement.setDate(5, bill.billDate);
						updateStatement.setBoolean(6, bill.isCancelled);
						updateStatement.setString(7, bill.modifiedBy);
						updateStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
						updateStatement.setInt(9, bill.billId);
						result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}
				
			}
		}
		
		return result;
	}
	
	public static boolean delete(Bill bill) {
		boolean result = false;
		
		if (deleteStatement == null) {
			deleteStatement = SQLiteManager.getPreparedStatement("DELETE FROM " + Constants.Table.Bill.TABLE_NAME
					+ " WHERE " + Constants.Table.Bill.FieldName.BILL_ID + " = ?");
		}
		
		if (deleteStatement != null) {
			try {
				deleteStatement.clearParameters();
				deleteStatement.setInt(1, bill.billId);
				result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
		}
		
		return result;
	}
	
	public static Bill create() {
		Bill bill = new Bill();
		return bill;
	}
	
	public static ArrayList<Bill> getBillsFromResultSet(ResultSet resultSet) {
		ArrayList<Bill> bills = new ArrayList<Bill>();
		try {
			while (resultSet.next()) {
				Bill bill = new Bill();
				bill.billId = resultSet.getInt(Constants.Table.Bill.FieldName.BILL_ID);
				bill.societyId = resultSet.getInt(Constants.Table.Society.FieldName.SOCIETY_ID);
				bill.propertyId = resultSet.getInt(Constants.Table.Property.FieldName.PROPERTY_ID);
				bill.amount = resultSet.getDouble(Constants.Table.Bill.FieldName.AMOUNT);
				bill.billDate = resultSet.getDate(Constants.Table.Bill.FieldName.BILL_TIMESTAMP);
				bill.paymentId = resultSet.getInt(Constants.Table.Payment.FieldName.PAYMENT_ID);
				bill.modifiedBy = resultSet.getString(Constants.Table.Bill.FieldName.MODIFIED_BY);
				bill.lastModified = resultSet.getTimestamp(Constants.Table.Bill.FieldName.LAST_MODIFIED);
				bill.isCancelled = resultSet.getBoolean(Constants.Table.Bill.FieldName.IS_CANCELLED);
				bills.add(bill);
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		return bills;
	}

}

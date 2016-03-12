package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class BillCharge {

	private int billId;

	private int chargeId;

	private double amount;
	
	private Bill bill;

	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;
	private final static Logger LOG = Logger.getLogger(BillCharge.class);

	private BillCharge() {

	}

	public int getBillId() {
		return billId;
	}

	public void setBillId(int billId) {
		this.billId = billId;
	}

	public int getChargeId() {
		return chargeId;
	}

	public void setChargeId(int chargeId) {
		this.chargeId = chargeId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public void setBill(Bill bill) {
		this.bill = bill;
	}
	
	public Bill getBill() {
		return this.bill;
	}

	public static BillCharge read(int billId, int chargeId) {
		BillCharge billCharge = null;

		if (readStatement == null) {
			readStatement = SQLiteManager.getPreparedStatement("SELECT * FROM " + Constants.Table.BillCharge.TABLE_NAME
					+ " WHERE " + Constants.Table.Bill.FieldName.BILL_ID + " = ?" + " AND "
					+ Constants.Table.Charge.FieldName.CHARGE_ID + " = ?");
		}

		if (readStatement != null) {
			try {
				readStatement.clearParameters();
				readStatement.setInt(1, billId);
				readStatement.setInt(2, chargeId);
				ResultSet resultSet = readStatement.executeQuery();
				if (resultSet != null && resultSet.next()) {
					billCharge = new BillCharge();
					billCharge.billId = resultSet.getInt(Constants.Table.Bill.FieldName.BILL_ID);
					billCharge.chargeId = resultSet.getInt(Constants.Table.Charge.FieldName.CHARGE_ID);
					billCharge.amount = resultSet.getDouble(Constants.Table.BillCharge.FieldName.AMOUNT);
				}
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
		}

		return billCharge;
	}
	
	public static void saveAll() {
		try {
			if (insertStatement != null) {
				insertStatement.executeBatch();
				insertStatement.clearBatch();
			}
		} catch (SQLException e) {
			
		}
	}

	public static boolean save(BillCharge billCharge, boolean insertEntry) {
		boolean result = false;

		if (billCharge != null) {

			if (insertEntry) {
				
				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement("INSERT INTO " + Constants.Table.BillCharge.TABLE_NAME
							+ " VALUES (?, ?, ?)");
				}

				if (insertStatement != null) {
					try {
						insertStatement.setInt(1, billCharge.billId);
						insertStatement.setInt(2, billCharge.chargeId);
						insertStatement.setDouble(3, billCharge.amount);
						insertStatement.addBatch();
//						result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}

			} else {

				if (updateStatement == null) {
					updateStatement = SQLiteManager.getPreparedStatement("UPDATE " + Constants.Table.BillCharge.TABLE_NAME
							+ " SET " + Constants.Table.BillCharge.FieldName.AMOUNT + " = ?"
							+ " WHERE " + Constants.Table.Bill.FieldName.BILL_ID + " = ?"
							+ " AND " + Constants.Table.Charge.FieldName.CHARGE_ID + " = ?");
				}

				if (updateStatement != null) {
					try {
						updateStatement.clearParameters();
						updateStatement.setInt(2, billCharge.billId);
						updateStatement.setInt(3, billCharge.chargeId);
						updateStatement.setDouble(1, billCharge.amount);
						result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}

			}

		}

		return result;
	}

	public static boolean delete(BillCharge billCharge) {
		boolean result = false;
		
		if (deleteStatement == null) {
			deleteStatement = SQLiteManager.getPreparedStatement("DELETE FROM " + Constants.Table.BillCharge.TABLE_NAME
					+ " WHERE " + Constants.Table.Bill.FieldName.BILL_ID + " = ?"
					+ " AND " + Constants.Table.Charge.FieldName.CHARGE_ID + " = ?" );
		}
		
		if (deleteStatement != null) {
			try {
				deleteStatement.clearParameters();
				deleteStatement.setInt(1, billCharge.billId);
				deleteStatement.setInt(2, billCharge.chargeId);
				result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
		}
		
		return result;
	}

	public static BillCharge create(Bill bill, int chargeId) {
		BillCharge billCharge = new BillCharge();
		billCharge.billId = bill.getBillId();
		billCharge.chargeId = chargeId;
		billCharge.bill = bill;
		return billCharge;
	}

}

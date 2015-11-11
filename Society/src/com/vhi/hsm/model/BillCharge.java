package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class BillCharge {

	private int billId;

	private int chargeId;

	private double amount;

	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;

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

	public static BillCharge read(int billId, int chargeId) {
		BillCharge billCharge = null;

		if (readStatement == null) {
			readStatement = SQLiteManager.getPreparedStatement("SELECT * FROM " + Constants.Table.BillCharge.TABLE_NAME
					+ " WHERE " + Constants.Table.Bill.FieldName.BILL_ID + " = ?" + " AND "
					+ Constants.Table.Charge.FieldName.CHARGE_ID + " = ?");
		}

		if (readStatement != null) {
			try {
				readStatement.setInt(1, billId);
				readStatement.setInt(2, chargeId);
				ResultSet resultSet = readStatement.executeQuery();
				if (resultSet != null && resultSet.first()) {
					billCharge = new BillCharge();
					billCharge.billId = resultSet.getInt(Constants.Table.Bill.FieldName.BILL_ID);
					billCharge.chargeId = resultSet.getInt(Constants.Table.Charge.FieldName.CHARGE_ID);
					billCharge.amount = resultSet.getDouble(Constants.Table.BillCharge.FieldName.AMOUNT);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return billCharge;
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
					} catch (SQLException e) {
						e.printStackTrace();
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
						updateStatement.setInt(2, billCharge.billId);
						updateStatement.setInt(3, billCharge.chargeId);
						updateStatement.setDouble(1, billCharge.amount);
					} catch (SQLException e) {
						e.printStackTrace();
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
				deleteStatement.setInt(1, billCharge.billId);
				deleteStatement.setInt(2, billCharge.chargeId);
				result = deleteStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}

	public static BillCharge create(int billId, int chargeId) {
		BillCharge billCharge = new BillCharge();
		billCharge.billId = billId;
		billCharge.chargeId = chargeId;
		return billCharge;
	}

}

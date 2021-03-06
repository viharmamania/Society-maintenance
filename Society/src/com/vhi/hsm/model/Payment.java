package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;

import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;
import com.vhi.hsm.utils.Utility;

public class Payment {
	private final static Logger LOG = Logger.getLogger(Payment.class);

	private int paymentId;

	private int propertyId;

	private String modeOfPayment;

	private String transactionNumber;

	private Date paymentDate;

	private boolean isCancelled;

	private Date cancellationDate;

	private String remarks;

	private String modifiedBy;

	private String last_modified;

	private double amount;

	private String chequeNumber;

	private Date chequeDate;

	public Date getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(Date chequeDate) {
		this.chequeDate = chequeDate;
	}

	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;

	public Payment() {

	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}

	public int getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(int paymentId) {
		this.paymentId = paymentId;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public String getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transaction_number) {
		this.transactionNumber = transaction_number;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date payment_date) {
		this.paymentDate = payment_date;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public Date getCancellationDate() {
		return cancellationDate;
	}

	public void setCancellationDate(Date cancellationDate) {
		this.cancellationDate = cancellationDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getLast_modified() {
		return last_modified;
	}

	public void setLast_modified(String last_modified) {
		this.last_modified = last_modified;
	}
	
	@Override
	public String toString() {
		String message = "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(paymentDate);
		message = Integer.toString(cal.get(Calendar.DATE)) + " " + Utility.getMonthNameFromNumber(cal.get(Calendar.MONTH))
			+ ", " + Integer.toString(cal.get(Calendar.YEAR)) + ": " + amount;
		return message;
	}

	public static Payment create(int propertyId) {
		Payment payment = new Payment();
		payment.propertyId = propertyId;
		return payment;
	}

	public static boolean save(Payment payment, boolean insertEntry) {
		boolean result = false;

		if (insertEntry) {

			if (insertStatement == null) {
				insertStatement = SQLiteManager.getPreparedStatement("INSERT INTO " + Constants.Table.Payment.TABLE_NAME
						+ " ( " + Constants.Table.Property.FieldName.PROPERTY_ID + " , "
						+ Constants.Table.Payment.FieldName.MODE_OF_PAYMENT + " , "
						+ Constants.Table.Payment.FieldName.TRANSACTION_NUMBER + " , "
						+ Constants.Table.Payment.FieldName.REMARKS + " , "
						+ Constants.Table.Payment.FieldName.PAYMENT_DATE + " , "
						+ Constants.Table.Payment.FieldName.IS_CANCELLED + " , "
						+ Constants.Table.Payment.FieldName.MODIFIED_BY + " , "
						+ Constants.Table.Payment.FieldName.LAST_MODIFIED + " , "
						+ Constants.Table.Payment.FieldName.AMOUNT + " , "
						+ Constants.Table.Society.FieldName.SOCIETY_ID + " , "
						+ Constants.Table.Payment.FieldName.CHEQUE_NUMBER + " ) "
						+ " VALUES ( ? , ? , ? , ? , ? , ? , ? , ?, ?, ?, ? )");
			}

			if (insertStatement != null) {
				try {
					insertStatement.clearParameters();
					insertStatement.setInt(1, payment.propertyId);
					insertStatement.setString(2, payment.modeOfPayment);
					insertStatement.setString(3, payment.transactionNumber);
					insertStatement.setString(4, payment.remarks);
					insertStatement.setTimestamp(5, new Timestamp(payment.getPaymentDate().getTime()));
					insertStatement.setBoolean(6, false);
					insertStatement.setString(7, SystemManager.loggedInUser.getName());
					insertStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
					insertStatement.setDouble(9, payment.amount);
					insertStatement.setDouble(10, SystemManager.society.getSocietyId());
					insertStatement.setString(11, payment.chequeNumber);
					result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);
					if (result) {
						ResultSet generatedKeys = insertStatement.getGeneratedKeys();
						if (generatedKeys != null) {
							payment.paymentId = generatedKeys.getInt(1);
						}
					}
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}
			}

		} else {

			if (updateStatement == null) {
				updateStatement = SQLiteManager.getPreparedStatement("UPDATE " + Constants.Table.Payment.TABLE_NAME
						+ " SET " + Constants.Table.Property.FieldName.PROPERTY_ID + " = ? , "
						+ Constants.Table.Payment.FieldName.MODE_OF_PAYMENT + " = ? , "
						+ Constants.Table.Payment.FieldName.TRANSACTION_NUMBER + " = ? , "
						+ Constants.Table.Payment.FieldName.REMARKS + " = ? , "
						+ Constants.Table.Payment.FieldName.CANCELLATION_TIMESTAMP + " = ? , "
						+ Constants.Table.Payment.FieldName.IS_CANCELLED + " = ? , "
						+ Constants.Table.Payment.FieldName.MODIFIED_BY + " = ? , "
						+ Constants.Table.Payment.FieldName.LAST_MODIFIED + " = ? ,"
						+ Constants.Table.Payment.FieldName.AMOUNT + " = ?, "
						+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " WHERE "
						+ Constants.Table.Payment.FieldName.PAYMENT_ID + " = ?");
			}

			if (updateStatement != null) {
				try {
					updateStatement.clearParameters();
					updateStatement.setInt(1, payment.propertyId);
					updateStatement.setString(2, payment.modeOfPayment);
					updateStatement.setString(3, payment.transactionNumber);
					updateStatement.setString(4, payment.remarks);
					updateStatement.setTimestamp(5, new Timestamp(payment.cancellationDate.getTime()));
					updateStatement.setBoolean(6, payment.isCancelled);
					updateStatement.setString(7, payment.modifiedBy);
					updateStatement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
					updateStatement.setDouble(9, payment.amount);
					updateStatement.setInt(10, SystemManager.society.getSocietyId());
					updateStatement.setInt(11, payment.paymentId);
					result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}
			}

		}

		return result;
	}

	public static boolean delete(Payment payment) {
		boolean result = false;

		if (deleteStatement == null) {
			deleteStatement = SQLiteManager.getPreparedStatement("DELETE FROM " + Constants.Table.Payment.TABLE_NAME
					+ " WHERE " + Constants.Table.Payment.FieldName.PAYMENT_ID + " = ?");
		}

		if (deleteStatement != null) {
			try {
				deleteStatement.clearParameters();
				deleteStatement.setInt(1, payment.paymentId);
				result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
		}

		return result;
	}

	public static Payment read(int paymentId) {
		Payment payment = null;

		if (readStatement == null) {
			readStatement = SQLiteManager.getPreparedStatement("SELECT * FROM " + Constants.Table.Payment.TABLE_NAME
					+ " WHERE " + Constants.Table.Payment.FieldName.PAYMENT_ID + " = ?");
		}

		if (readStatement != null) {
			try {
				readStatement.clearParameters();
				readStatement.setInt(1, paymentId);
				ResultSet resultSet = readStatement.executeQuery();
				ArrayList<Payment> payments = getPaymentsFromResultSet(resultSet);
				if (payments != null && payments.size() == 1) {
					payment = payments.get(0);
				}
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
		}

		return payment;
	}
	
	public static ArrayList<Payment> getPaymentsFromResultSet(ResultSet resultSet) {
		ArrayList<Payment> payments = new ArrayList<Payment>();
		
		if (resultSet != null) {
			try {
				while (resultSet.next()) {
					Payment payment = new Payment();
					payment.paymentId = resultSet.getInt(Constants.Table.Payment.FieldName.PAYMENT_ID);;
					payment.propertyId = resultSet.getInt(Constants.Table.Property.FieldName.PROPERTY_ID);
					payment.modeOfPayment = resultSet.getString(Constants.Table.Payment.FieldName.MODE_OF_PAYMENT);
					payment.transactionNumber = resultSet
							.getString(Constants.Table.Payment.FieldName.TRANSACTION_NUMBER);
					payment.remarks = resultSet.getString(Constants.Table.Payment.FieldName.REMARKS);
					payment.isCancelled = resultSet.getBoolean(Constants.Table.Payment.FieldName.IS_CANCELLED);
					if (payment.isCancelled) {
						payment.cancellationDate = new Date(resultSet
								.getTimestamp(Constants.Table.Payment.FieldName.CANCELLATION_TIMESTAMP).getTime());

					}
					payment.modifiedBy = resultSet.getString(Constants.Table.Payment.FieldName.MODIFIED_BY);
					payment.last_modified = resultSet.getString(Constants.Table.Payment.FieldName.LAST_MODIFIED);
					payment.amount = resultSet.getDouble(Constants.Table.Payment.FieldName.AMOUNT);
					payment.paymentDate = new Date(resultSet
							.getTimestamp(Constants.Table.Payment.FieldName.PAYMENT_DATE).getTime());
					payments.add(payment);
				}
			} catch (SQLException e) {
				LOG.error(e.getStackTrace());
			}
		}
		
		return payments;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}

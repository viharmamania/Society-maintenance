package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class Fine {

	private int societyId;

	private double fineLow;

	private double fineHigh;

	private double percentageCharge;

	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;
	
	private static ArrayList<Fine> fineList;
	private final static Logger LOG = Logger.getLogger(Fine.class);

	private Fine() {

	}

	public int getSocietyId() {
		return societyId;
	}

	public void setSocietyId(int societyId) {
		this.societyId = societyId;
	}

	public double getFineLow() {
		return fineLow;
	}

	public void setFineLow(double fineLow) {
		this.fineLow = fineLow;
	}

	public double getFineHigh() {
		return fineHigh;
	}

	public void setFineHigh(double fineHigh) {
		this.fineHigh = fineHigh;
	}

	public double getPercentageCharge() {
		return percentageCharge;
	}

	public void setPercentageCharge(double percentageCharge) {
		this.percentageCharge = percentageCharge;
	}

	public static Fine create(int societyId) {
		Fine fine = new Fine();
		fine.societyId = societyId;
		return fine;
	}

	public static boolean save(Fine fine, boolean insertEntry) {
		boolean result = false;

		if (fine != null) {

			if (insertEntry) {

				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement("INSERT INTO "
							+ Constants.Table.Fine.TABLE_NAME + " ( " + Constants.Table.Society.FieldName.SOCIETY_ID
							+ " , " + Constants.Table.Fine.FieldName.FINE_LOW + " , "
							+ Constants.Table.Fine.FieldName.FINE_HIGH + " , "
							+ Constants.Table.Fine.FieldName.PERCENTAGE_CHARGE + " ) " + " VALUES (?, ?, ?, ?)");
				}

				if (insertStatement != null) {
					try {
						insertStatement.clearParameters();
						insertStatement.setInt(1, fine.societyId);
						insertStatement.setDouble(2, fine.fineLow);
						insertStatement.setDouble(3, fine.fineHigh);
						insertStatement.setDouble(4, fine.percentageCharge);
						result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}

			} else {

				if (updateStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement("UPDATE"
							+ Constants.Table.Fine.TABLE_NAME + " SET "
							+ Constants.Table.Fine.FieldName.PERCENTAGE_CHARGE + " = ? " 
							+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ? "
							+ " AND " + Constants.Table.Fine.FieldName.FINE_LOW + " = ? "
							+ " AND " + Constants.Table.Fine.FieldName.FINE_HIGH + " = ? ");
				}

				if (updateStatement != null) {
					try {
						updateStatement.clearParameters();
						updateStatement.setDouble(1, fine.percentageCharge);
						updateStatement.setInt(2, fine.societyId);
						updateStatement.setDouble(3, fine.fineLow);
						updateStatement.setDouble(4, fine.fineHigh);
						result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}

			}

		}
		
		if (result) {
			getFineList(fine.societyId, true);
		}

		return result;
	}
	
	public static boolean delete(Fine fine) {
		boolean result = false;
		if (fine != null) {
			
			if (deleteStatement == null) {
				deleteStatement = SQLiteManager.getPreparedStatement("DELETE FROM " + Constants.DB_NAME
						+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ? "
						+ " AND " + Constants.Table.Fine.FieldName.FINE_LOW + " = ? "
						+ " AND " + Constants.Table.Fine.FieldName.FINE_HIGH + " = ? ");
			}
			
			if (deleteStatement != null) {
				try {
					deleteStatement.clearParameters();
					deleteStatement.setInt(1, fine.societyId);
					deleteStatement.setDouble(2, fine.fineLow);
					deleteStatement.setDouble(3, fine.fineHigh);
					result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}
			}
			
		}
		
		if (result) {
			getFineList(fine.societyId, true);
		}
		
		return result;
	}
	
	private static void getFineList(int societyId, boolean refreshList) {
		
		if (fineList == null) {
			fineList = new ArrayList<Fine>();
		}
		
		if (fineList.size() == 0 || refreshList) {
			fineList.clear();
			
			if (readStatement == null) {
				readStatement = SQLiteManager.getPreparedStatement("SELECT * FROM " + Constants.Table.Fine.TABLE_NAME
						+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?");
			}
			
			if (readStatement != null) {
				try {
					readStatement.clearParameters();
					readStatement.setInt(1, societyId);
					ResultSet resultSet = readStatement.executeQuery();
					if (resultSet != null) {
						while (resultSet.next()) {
							Fine fine = new Fine();
							fine.societyId = societyId;
							fine.fineHigh = resultSet.getDouble(Constants.Table.Fine.FieldName.FINE_HIGH);
							fine.fineLow = resultSet.getDouble(Constants.Table.Fine.FieldName.FINE_LOW);
							fine.percentageCharge = resultSet.getDouble(Constants.Table.Fine.FieldName.PERCENTAGE_CHARGE);
							fineList.add(fine);
						}
					}
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}
			}
			
		}
		
	}
	
	public static double getFineAmount(int societyId, double amount) {
		double percentage = 0;
		getFineList(societyId, false);
		for (Fine fine : fineList) {
			if (fine.fineLow == 0 && fine.fineHigh >= amount) {
				percentage = fine.percentageCharge;
				break;
			} else if (fine.fineLow <= amount && fine.fineHigh == 0) {
				percentage = fine.percentageCharge;
				break;
			} else if (fine.fineLow <= amount && fine.fineHigh >= amount) {
				percentage = fine.percentageCharge;
				break;
			}
		}
		percentage *= amount;
		return percentage;
	}

}

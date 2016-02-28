package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class Charge {
	private final static Logger LOG = Logger.getLogger(Charge.class);
	private int societyId;

	private int chargeId;

	private String description;

	private double amount;

	private boolean tempCharges;

	private boolean isdefault;

	public boolean isdefault() {
		return isdefault;
	}

	public void setdefault(boolean isdefault) {
		this.isdefault = isdefault;
	}

	private boolean isCancelled;

	private ArrayList<String> assignedPropertyGroup;
	private ArrayList<String> assignedPropertyType;
	private ArrayList<Integer> assignedProperty;

	private static HashMap<Integer, HashMap<Integer, Charge>> chargeMap;
	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement,
			readPropertyGroup, readPropertyType, readProperty;

	private Charge() {
		assignedProperty = new ArrayList<Integer>();
		assignedPropertyGroup = new ArrayList<String>();
		assignedPropertyType = new ArrayList<String>();
	}

	public int getChargeId() {
		return chargeId;
	}

	public void setChargeId(int chargeId) {
		this.chargeId = chargeId;
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

	public boolean isTempCharges() {
		return tempCharges;
	}

	public void setTempCharges(boolean tempCharges) {
		this.tempCharges = tempCharges;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public ArrayList<String> getAssignedPropertyGroup() {
		return assignedPropertyGroup;
	}

	public void setAssignedPropertyGroup(ArrayList<String> assignedPropertyGroup) {
		this.assignedPropertyGroup = assignedPropertyGroup;
	}

	public ArrayList<String> getAssignedPropertyType() {
		return assignedPropertyType;
	}

	public void setAssignedPropertyType(ArrayList<String> assignedPropertyType) {
		this.assignedPropertyType = assignedPropertyType;
	}

	public ArrayList<Integer> getAssignedProperty() {
		return assignedProperty;
	}

	public void setAssignedProperty(ArrayList<Integer> assignedProperty) {
		this.assignedProperty = assignedProperty;
	}

	@Override
	public String toString() {
		return this.description;
	}

	public static Charge read(int societyId, int chargeId) {

		Charge charge = null;
		HashMap<Integer, Charge> societyCharges;

		if (chargeMap == null) {
			chargeMap = new HashMap<Integer, HashMap<Integer, Charge>>();
		}

		societyCharges = chargeMap.get(societyId);
		if (societyCharges == null) {
			societyCharges = new HashMap<Integer, Charge>();
		}

		charge = societyCharges.get(chargeId);
		if (charge == null) {

			if (readStatement == null) {
				readStatement = SQLiteManager.getPreparedStatement("SELECT * FROM " + Constants.Table.Charge.TABLE_NAME
						+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
						+ Constants.Table.Charge.FieldName.CHARGE_ID + " = ?");

				readPropertyGroup = SQLiteManager
						.getPreparedStatement("SELECT * FROM " + Constants.Table.ChargeToPropertyGroup.TABLE_NAME
								+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
								+ Constants.Table.Charge.FieldName.CHARGE_ID + " = ?");

				readPropertyType = SQLiteManager
						.getPreparedStatement("SELECT * FROM " + Constants.Table.ChargeToPropertyType.TABLE_NAME
								+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
								+ Constants.Table.Charge.FieldName.CHARGE_ID + " = ?");

				readProperty = SQLiteManager
						.getPreparedStatement("SELECT * FROM " + Constants.Table.ChargeToProperty.TABLE_NAME + " WHERE "
								+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
								+ Constants.Table.Charge.FieldName.CHARGE_ID + " = ?");
			}

			if (readStatement != null) {
				try {
					readStatement.clearParameters();
					readStatement.setInt(1, societyId);
					readStatement.setInt(2, chargeId);
					ResultSet resultSet = readStatement.executeQuery();
					if (resultSet != null && resultSet.next()) {
						charge = new Charge();
						charge.societyId = resultSet.getInt(Constants.Table.Society.FieldName.SOCIETY_ID);
						charge.chargeId = resultSet.getInt(Constants.Table.Charge.FieldName.CHARGE_ID);
						charge.amount = resultSet.getDouble(Constants.Table.Charge.FieldName.AMOUNT);
						charge.description = resultSet.getString(Constants.Table.Charge.FieldName.DESCRIPTION);
						charge.isCancelled = resultSet.getBoolean(Constants.Table.Charge.FieldName.IS_CANCELLED);
						charge.tempCharges = resultSet.getBoolean(Constants.Table.Charge.FieldName.TEMP_CHARGE);
						charge.isdefault = resultSet.getBoolean(Constants.Table.Charge.FieldName.IS_DEFAULT);

						if (readProperty != null) {
							readProperty.clearParameters();
							readProperty.setInt(1, societyId);
							readProperty.setInt(2, chargeId);
							resultSet = readProperty.executeQuery();
							if (resultSet != null) {
								while (resultSet.next()) {
									charge.assignedProperty
											.add(resultSet.getInt(Constants.Table.Property.FieldName.PROPERTY_ID));
								}
							}
						}

						if (readPropertyGroup != null) {
							readPropertyGroup.clearParameters();
							readPropertyGroup.setInt(1, societyId);
							readPropertyGroup.setInt(2, chargeId);
							resultSet = readPropertyGroup.executeQuery();
							if (resultSet != null) {
								while (resultSet.next()) {
									charge.assignedPropertyGroup.add(resultSet
											.getString(Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP));
								}
							}
						}

						if (readPropertyType != null) {
							readPropertyType.clearParameters();
							readPropertyType.setInt(1, societyId);
							readPropertyType.setInt(2, chargeId);
							resultSet = readPropertyType.executeQuery();
							if (resultSet != null) {
								while (resultSet.next()) {
									charge.assignedPropertyType.add(
											resultSet.getString(Constants.Table.PropertyType.FieldName.PROPERTY_TYPE));
								}
							}
						}

						societyCharges.put(chargeId, charge);
						chargeMap.put(SystemManager.society.getSocietyId(), societyCharges);
					}
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}
			}

		}

		return charge;
	}

	public static boolean save(Charge charge, boolean insertEntry) {
		boolean result = false;

		if (charge != null) {

			if (insertEntry) {

				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement("INSERT INTO "
							+ Constants.Table.Charge.TABLE_NAME + "(" + Constants.Table.Society.FieldName.SOCIETY_ID
							+ ", " + Constants.Table.Charge.FieldName.CHARGE_ID + ", "
							+ Constants.Table.Charge.FieldName.DESCRIPTION + ", "
							+ Constants.Table.Charge.FieldName.AMOUNT + ", "
							+ Constants.Table.Charge.FieldName.TEMP_CHARGE + ", "
							+ Constants.Table.Charge.FieldName.IS_CANCELLED + ", "
							+ Constants.Table.Charge.FieldName.IS_DEFAULT + ")" + " VALUES (?, ?, ?, ?, ?, ?, ?) ");
				}

				if (insertStatement != null) {
					try {

						if (charge.chargeId == -1) {
							charge.chargeId = getNewChargeId(charge.societyId);
						}

						insertStatement.clearParameters();
						insertStatement.setInt(1, charge.societyId);
						insertStatement.setInt(2, charge.chargeId);
						insertStatement.setString(3, charge.description);
						insertStatement.setDouble(4, charge.amount);
						insertStatement.setBoolean(5, charge.tempCharges);
						insertStatement.setBoolean(6, charge.isCancelled);
						insertStatement.setBoolean(7, charge.isdefault);
						result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}

			} else {

				if (updateStatement == null) {
					updateStatement = SQLiteManager.getPreparedStatement("UPDATE " + Constants.Table.Charge.TABLE_NAME
							+ " SET " + Constants.Table.Charge.FieldName.DESCRIPTION + " = ?, "
							+ Constants.Table.Charge.FieldName.AMOUNT + " = ?, "
							+ Constants.Table.Charge.FieldName.TEMP_CHARGE + " = ?, "
							+ Constants.Table.Charge.FieldName.IS_CANCELLED + " = ?, "
							+ Constants.Table.Charge.FieldName.IS_DEFAULT + " = ?" + " WHERE "
							+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
							+ Constants.Table.Charge.FieldName.CHARGE_ID + " = ?");
				}

				if (updateStatement != null) {
					try {
						updateStatement.clearParameters();
						updateStatement.setString(1, charge.description);
						updateStatement.setDouble(2, charge.amount);
						updateStatement.setBoolean(3, charge.tempCharges);
						updateStatement.setBoolean(4, charge.isCancelled);
						updateStatement.setBoolean(5, charge.isdefault);
						updateStatement.setInt(6, charge.societyId);
						updateStatement.setInt(7, charge.chargeId);
						result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}

			}

		}

		if (result) {

			if (chargeMap == null) {
				chargeMap = new HashMap<Integer, HashMap<Integer, Charge>>();
			}

			HashMap<Integer, Charge> societyCharges = chargeMap.get(charge.societyId);
			if (societyCharges == null) {
				societyCharges = new HashMap<Integer, Charge>();
			}

			societyCharges.put(charge.chargeId, charge);
			chargeMap.put(SystemManager.society.getSocietyId(), societyCharges);

		}

		for (Integer i : charge.assignedProperty) {

			SQLiteManager.executeUpdate("INSERT OR REPLACE INTO " + Constants.Table.ChargeToProperty.TABLE_NAME
					+ " VALUES (" + SystemManager.society.getSocietyId() + ", " + charge.chargeId + ", " + i.toString()
					+ ")");

		}

		for (String i : charge.assignedPropertyType) {

			SQLiteManager.executeUpdate(
					"INSERT OR REPLACE INTO " + Constants.Table.ChargeToPropertyType.TABLE_NAME + " VALUES ("
							+ SystemManager.society.getSocietyId() + ", " + charge.chargeId + ", " + "'" + i + "\')");

		}

		for (String i : charge.assignedPropertyGroup) {

			SQLiteManager.executeUpdate(
					"INSERT OR REPLACE INTO " + Constants.Table.ChargeToPropertyGroup.TABLE_NAME + " VALUES ("
							+ SystemManager.society.getSocietyId() + ", " + charge.chargeId + ", " + "'" + i + "\')");

		}
		return result;
	}

	public static boolean delete(Charge charge) {

		boolean result = false;

		if (deleteStatement == null) {
			deleteStatement = SQLiteManager.getPreparedStatement("DELETE FROM " + Constants.Table.Charge.TABLE_NAME
					+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
					+ Constants.Table.Charge.FieldName.CHARGE_ID + " = ?");
		}

		if (deleteStatement != null) {
			try {
				deleteStatement.clearParameters();
				deleteStatement.setInt(1, charge.societyId);
				deleteStatement.setInt(2, charge.chargeId);
				result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
		}

		if (result) {
			if (chargeMap != null) {
				HashMap<Integer, Charge> societyCharges = chargeMap.get(charge.societyId);
				if (societyCharges != null) {
					societyCharges.remove(charge.chargeId);
				}
			}
		}

		try {
			SQLiteManager.executeQuery("DELETE FROM " + Constants.Table.ChargeToProperty.TABLE_NAME + " WHERE "
					+ Constants.Table.Society.FieldName.SOCIETY_ID + " = " + charge.societyId + " AND "
					+ Constants.Table.Charge.FieldName.CHARGE_ID + " = " + charge.chargeId);

			SQLiteManager.executeQuery("DELETE FROM " + Constants.Table.ChargeToPropertyGroup.TABLE_NAME + " WHERE "
					+ Constants.Table.Society.FieldName.SOCIETY_ID + " = " + charge.societyId + " AND "
					+ Constants.Table.Charge.FieldName.CHARGE_ID + " = " + charge.chargeId);

			SQLiteManager.executeQuery("DELETE FROM " + Constants.Table.ChargeToPropertyType.TABLE_NAME + " WHERE "
					+ Constants.Table.Society.FieldName.SOCIETY_ID + " = " + charge.societyId + " AND "
					+ Constants.Table.Charge.FieldName.CHARGE_ID + " = " + charge.chargeId);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return result;
	}

	public static Charge create(int societyId) {
		Charge charge = new Charge();
		charge.societyId = societyId;
		charge.chargeId = -1;
		return charge;
	}

	public static Charge getFineCharge() {
		// TODO Auto-generated method stub
		return null;
	}

	public static ArrayList<Charge> getAllCharge(int societyId) {
		ArrayList<Charge> list = new ArrayList<Charge>();

		try {
			ResultSet resultSet = SQLiteManager.executeQuery("SELECT * FROM " + Constants.Table.Charge.TABLE_NAME
					+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = " + societyId);
			if (resultSet != null) {
				Charge charge;

				while (resultSet.next()) {
					charge = null;
					charge = read(societyId, resultSet.getInt(Constants.Table.Charge.FieldName.CHARGE_ID));
					if (charge != null) {
						list.add(charge);
					}
				}

			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		return list;
	}

	public static int getNewChargeId(int societyId) {
		int newId = -2;
		try {
			ResultSet idMax = SQLiteManager.executeQuery("SELECT MAX(" + Constants.Table.Charge.FieldName.CHARGE_ID
					+ ") max_id FROM " + Constants.Table.Charge.TABLE_NAME + " WHERE "
					+ Constants.Table.Society.FieldName.SOCIETY_ID + " = " + societyId);
			if (idMax.next()) {
				newId = idMax.getInt("max_id");
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		newId++;
		System.out.println(newId);
		return newId;
	}

}

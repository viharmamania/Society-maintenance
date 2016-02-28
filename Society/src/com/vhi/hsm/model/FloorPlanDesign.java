package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class FloorPlanDesign {
	
	private final static Logger LOG = Logger.getLogger(FloorPlanDesign.class);

	private int societyId;

	private int floorPlanId;

	private int propertyNumber;

	private String propertyGroup;

	private String propertyType;

	private static HashMap<Integer, HashMap<Integer, HashMap<Integer, FloorPlanDesign>>> floorPlanDesignMap;

	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;

	private FloorPlanDesign() {

	}

	public int getSocietyId() {
		return societyId;
	}

	public void setSocietyId(int societyId) {
		this.societyId = societyId;
	}

	public int getFloorPlanId() {
		return floorPlanId;
	}

	public void setFloorPlanId(int floorPlanId) {
		this.floorPlanId = floorPlanId;
	}

	public int getPropertyNumber() {
		return propertyNumber;
	}

	public void setPropertyNumber(int propertyNumber) {
		this.propertyNumber = propertyNumber;
	}

	public String getPropertyGroup() {
		return propertyGroup;
	}

	public void setPropertyGroup(String propertyGroup) {
		this.propertyGroup = propertyGroup;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public static FloorPlanDesign read(int societyId, int floorPlanId, int propertyNumber) {
		FloorPlanDesign floorPlanDesign = null;

		if (floorPlanDesignMap == null) {
			floorPlanDesignMap = new HashMap<Integer, HashMap<Integer, HashMap<Integer, FloorPlanDesign>>>();
		}

		HashMap<Integer, HashMap<Integer, FloorPlanDesign>> societyFloorPlanDesign = floorPlanDesignMap.get(societyId);
		if (societyFloorPlanDesign == null) {
			societyFloorPlanDesign = new HashMap<Integer, HashMap<Integer, FloorPlanDesign>>();
			floorPlanDesignMap.put(societyId, societyFloorPlanDesign);
		}

		HashMap<Integer, FloorPlanDesign> floorsPlanDesign = societyFloorPlanDesign.get(floorPlanId);
		if (floorsPlanDesign == null) {
			floorsPlanDesign = new HashMap<Integer, FloorPlanDesign>();
			societyFloorPlanDesign.put(floorPlanId, floorsPlanDesign);
		}

		floorPlanDesign = floorsPlanDesign.get(propertyNumber);
		if (floorPlanDesign == null) {

			if (readStatement == null) {
				readStatement = SQLiteManager
						.getPreparedStatement("SELECT * FROM " + Constants.Table.FloorPlanDesign.TABLE_NAME + " WHERE "
								+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
								+ Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " = ?" + " AND "
								+ Constants.Table.FloorPlanDesign.FieldName.PROPERTY_NUMBER + " = ?");
			}

			if (readStatement != null) {
				try {
					readStatement.clearParameters();
					readStatement.setInt(1, societyId);
					readStatement.setInt(2, floorPlanId);
					readStatement.setInt(3, propertyNumber);
					ResultSet resultSet = readStatement.executeQuery();
					if (resultSet != null && resultSet.next()) {
						floorPlanDesign = new FloorPlanDesign();
						floorPlanDesign.setSocietyId(societyId);
						floorPlanDesign.setFloorPlanId(floorPlanId);
						floorPlanDesign.setPropertyNumber(propertyNumber);
						floorPlanDesign.setPropertyGroup(
								resultSet.getString(Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP));
						floorPlanDesign.setPropertyType(
								resultSet.getString(Constants.Table.PropertyType.FieldName.PROPERTY_TYPE));
						floorsPlanDesign.put(propertyNumber, floorPlanDesign);
					}
				} catch (SQLException e) {
					LOG.error(e.getMessage());;
				}
			}

		}

		return floorPlanDesign;
	}

	public static boolean save(FloorPlanDesign floorPlanDesign, boolean insertEntry) {
		boolean result = false;

		if (floorPlanDesign != null) {

			if (insertEntry) {

				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement("INSERT INTO "
							+ Constants.Table.FloorPlanDesign.TABLE_NAME + " ( "
							+ Constants.Table.Society.FieldName.SOCIETY_ID + " , "
							+ Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " , "
							+ Constants.Table.FloorPlanDesign.FieldName.PROPERTY_NUMBER + " , "
							+ Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP + " , "
							+ Constants.Table.PropertyType.FieldName.PROPERTY_TYPE + " ) " + " VALUES (?, ?, ?, ?, ?)");
				}

				if (insertStatement != null) {
					try {
						insertStatement.clearParameters();
						insertStatement.setInt(1, floorPlanDesign.societyId);
						insertStatement.setInt(2, floorPlanDesign.floorPlanId);
						insertStatement.setInt(3, floorPlanDesign.propertyNumber);
						insertStatement.setString(4, floorPlanDesign.propertyGroup);
						insertStatement.setString(5, floorPlanDesign.propertyType);
						result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());;
					}
				}

			} else {

				if (updateStatement == null) {
					updateStatement = SQLiteManager
							.getPreparedStatement("UPDATE " + Constants.Table.FloorPlanDesign.TABLE_NAME + " SET "
									+ Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP + " = ? "
									+ Constants.Table.PropertyType.FieldName.PROPERTY_TYPE + " = ? " + " WHERE "
									+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
									+ Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " = ?" + " AND "
									+ Constants.Table.FloorPlanDesign.FieldName.PROPERTY_NUMBER + " = ? ");
				}

				if (updateStatement != null) {
					try {
						updateStatement.clearParameters();
						updateStatement.setString(1, floorPlanDesign.propertyGroup);
						updateStatement.setString(2, floorPlanDesign.propertyType);
						updateStatement.setInt(3, floorPlanDesign.societyId);
						updateStatement.setInt(4, floorPlanDesign.floorPlanId);
						updateStatement.setInt(5, floorPlanDesign.propertyNumber);
						result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());;
					}
				}

			}
		}

		if (result) {
			
			if (floorPlanDesignMap == null) {
				floorPlanDesignMap = new HashMap<Integer, HashMap<Integer, HashMap<Integer, FloorPlanDesign>>>();
			}

			HashMap<Integer, HashMap<Integer, FloorPlanDesign>> societyFloorPlanDesign = floorPlanDesignMap.get(floorPlanDesign.societyId);
			if (societyFloorPlanDesign == null) {
				societyFloorPlanDesign = new HashMap<Integer, HashMap<Integer, FloorPlanDesign>>();
				floorPlanDesignMap.put(floorPlanDesign.societyId, societyFloorPlanDesign);
			}

			HashMap<Integer, FloorPlanDesign> floorsPlanDesign = societyFloorPlanDesign.get(floorPlanDesign.floorPlanId);
			if (floorsPlanDesign == null) {
				floorsPlanDesign = new HashMap<Integer, FloorPlanDesign>();
				societyFloorPlanDesign.put(floorPlanDesign.floorPlanId, floorsPlanDesign);
			}
			
			floorsPlanDesign.put(floorPlanDesign.propertyNumber, floorPlanDesign);
			
		}

		return result;
	}

	public static boolean delete(FloorPlanDesign floorPlanDesign) {
		boolean result = false;

		if (floorPlanDesign != null) {

			if (deleteStatement == null) {
				deleteStatement = SQLiteManager
						.getPreparedStatement("DELETE FROM " + Constants.Table.FloorPlanDesign.TABLE_NAME + " WHERE "
								+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ? " + " AND "
								+ Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " = ?" + " AND "
								+ Constants.Table.FloorPlanDesign.FieldName.PROPERTY_NUMBER + " = ?");
			}

			if (deleteStatement != null) {
				try {
					deleteStatement.clearParameters();
					deleteStatement.setInt(1, floorPlanDesign.societyId);
					deleteStatement.setInt(2, floorPlanDesign.floorPlanId);
					deleteStatement.setInt(3, floorPlanDesign.propertyNumber);
					result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
				} catch (SQLException e) {
					LOG.error(e.getMessage());;
				}
			}

		}

		if (result && floorPlanDesignMap != null) {
			HashMap<Integer, HashMap<Integer, FloorPlanDesign>> societyFloorPlanDesign = floorPlanDesignMap
					.get(floorPlanDesign.societyId);
			if (societyFloorPlanDesign != null) {
				HashMap<Integer, FloorPlanDesign> floorsPlanDesign = societyFloorPlanDesign
						.get(floorPlanDesign.floorPlanId);
				if (floorsPlanDesign != null) {
					floorsPlanDesign.remove(floorPlanDesign.propertyNumber);
				}
			}
		}

		return result;
	}

	public static FloorPlanDesign create(int societyId, int floorPlanId) {
		FloorPlanDesign floorPlanDesign = new FloorPlanDesign();
		floorPlanDesign.societyId = societyId;
		floorPlanDesign.floorPlanId = floorPlanId;
		return floorPlanDesign;
	}
	
	public static ArrayList<FloorPlanDesign> getAllFloorPlanDesign(int societyId, int floorPlanId) {
		ArrayList<FloorPlanDesign> allDesign = new ArrayList<FloorPlanDesign>();
		
		String query = "SELECT " + Constants.Table.FloorPlanDesign.FieldName.PROPERTY_NUMBER
				+ " FROM " + Constants.Table.FloorPlanDesign.TABLE_NAME
				+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = " + societyId
				+ " AND " + Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " = " + floorPlanId ;
		
		try {
			ResultSet resultSet = SQLiteManager.executeQuery(query);
			if (resultSet != null) {
				while (resultSet.next()) {
					allDesign.add(FloorPlanDesign.read(societyId, floorPlanId, resultSet.getInt(Constants.Table.FloorPlanDesign.FieldName.PROPERTY_NUMBER)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return allDesign;
	}

}

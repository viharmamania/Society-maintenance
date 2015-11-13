package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class FloorPlan {

	private int societyId;

	private int floorPlanId;

	private String desription;

	private int noOfProperty;

	private static HashMap<Integer, HashMap<Integer, FloorPlan>> floorPlanMap;
	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;

	private FloorPlan() {
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

	public String getDesription() {
		return desription;
	}

	public void setDesription(String desription) {
		this.desription = desription;
	}

	public int getNoOfProperty() {
		return noOfProperty;
	}

	public void setNoOfProperty(int noOfProperty) {
		this.noOfProperty = noOfProperty;
	}
	
	public static FloorPlan read(int societyId, int floorPlanId) {
		FloorPlan floorPlan = null;
		
		if (floorPlanMap == null) {
			floorPlanMap = new HashMap<Integer, HashMap<Integer, FloorPlan>>();
		}
		
		HashMap<Integer, FloorPlan> societyFloorPlan = floorPlanMap.get(societyId);
		if (societyFloorPlan == null) {
			societyFloorPlan = new HashMap<Integer, FloorPlan>();
			floorPlanMap.put(societyId, societyFloorPlan);
		}
		
		floorPlan = societyFloorPlan.get(floorPlanId);
		if (floorPlan == null) {
			
			if (readStatement == null) {
				readStatement = SQLiteManager.getPreparedStatement("SELECT * FROM " + Constants.Table.FloorPlan.TABLE_NAME
						+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ? "
						+ " AND " + Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " = ?");
			}
			
			if (readStatement != null) {
				try {
					readStatement.clearParameters();
					ResultSet resultSet = readStatement.executeQuery();
					if (resultSet != null && resultSet.first()) {
						floorPlan = new FloorPlan();
						floorPlan.societyId = resultSet.getInt(Constants.Table.Society.FieldName.SOCIETY_ID);
						floorPlan.floorPlanId = resultSet.getInt(Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID);
						floorPlan.desription = resultSet.getString(Constants.Table.FloorPlan.FieldName.DESCRIPTION);
						floorPlan.noOfProperty = resultSet.getInt(Constants.Table.FloorPlan.FieldName.NO_OF_PROPERTY);
						societyFloorPlan.put(floorPlanId, floorPlan);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		return floorPlan;
	}
	
	public static boolean save(FloorPlan floorPlan, boolean insertEntry) {
		boolean result = false;
		
		if (floorPlan != null) {
			
			if (insertEntry) {
				
				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement("INSERT INTO " + Constants.Table.FloorPlan.TABLE_NAME
							+ " ("
							+ Constants.Table.Society.FieldName.SOCIETY_ID + ", "
							+ Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + ", "
							+ Constants.Table.FloorPlan.FieldName.DESCRIPTION + ", "
							+ Constants.Table.FloorPlan.FieldName.NO_OF_PROPERTY
							+ " )"
							+ " VALUES (?, ?, ?, ?)");
				}
				
				if (insertStatement != null) {
					try {
						insertStatement.clearParameters();
						insertStatement.setInt(1, floorPlan.societyId);
						insertStatement.setInt(2, floorPlan.floorPlanId);
						insertStatement.setString(3, floorPlan.desription);
						insertStatement.setInt(4, floorPlan.noOfProperty);
						result = insertStatement.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
				}
				
			} else {
				
				if (updateStatement == null) {
					updateStatement = SQLiteManager.getPreparedStatement("UPDATE " + Constants.Table.FloorPlan.TABLE_NAME
							+ " SET "
							+ Constants.Table.FloorPlan.FieldName.DESCRIPTION + " = ?, "
							+ Constants.Table.FloorPlan.FieldName.NO_OF_PROPERTY + " = ? "
							+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ? "
							+ " AND " + Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " = ?");
				}
				
				if (updateStatement != null) {
					try {
						updateStatement.clearParameters();
						updateStatement.setString(1, floorPlan.desription);
						updateStatement.setInt(2, floorPlan.noOfProperty);
						updateStatement.setInt(3, floorPlan.societyId);
						updateStatement.setInt(4, floorPlan.floorPlanId);
						result = updateStatement.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		}
		
		if (result) {
			
			if (floorPlanMap == null) {
				floorPlanMap = new HashMap<Integer, HashMap<Integer, FloorPlan>>();
			}
			
			HashMap<Integer, FloorPlan> societyFloorPlan = floorPlanMap.get(floorPlan.societyId);
			if (societyFloorPlan == null) {
				societyFloorPlan = new HashMap<Integer, FloorPlan>();
				floorPlanMap.put(floorPlan.societyId, societyFloorPlan);
			}
			
			societyFloorPlan.put(floorPlan.floorPlanId, floorPlan);
			
		}
		
		return result;
	}
	
	public static boolean delete(FloorPlan floorPlan) {
		boolean result = false;
		
		if (floorPlan != null) {
			
			if (deleteStatement == null) {
				deleteStatement = SQLiteManager.getPreparedStatement("DELETE FROM " + Constants.Table.FloorPlan.TABLE_NAME
						+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?"
						+ " AND " + Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " = ?");
			}
			
			if (deleteStatement != null) {
				try {
					deleteStatement.clearParameters();
					deleteStatement.setInt(1, floorPlan.societyId);
					deleteStatement.setInt(2, floorPlan.floorPlanId);
					result = deleteStatement.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		if (result && floorPlanMap != null) {
			HashMap<Integer, FloorPlan> societyFloorPlan = floorPlanMap.get(floorPlan.societyId);
			if (societyFloorPlan != null) {
				societyFloorPlan.remove(floorPlan.floorPlanId);
			}
		}
		
		return result;
	}
	
	public static FloorPlan create(int societyId) {
		FloorPlan floorPlan = null;
		floorPlan = new FloorPlan();
		floorPlan.societyId = societyId;
		return floorPlan;
	}

}

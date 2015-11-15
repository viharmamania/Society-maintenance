package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class Property {

	private int propertyId;

	private int societyId;

	private int wingId;

	private int floorNumber;

	private int floorPlanId;

	private int propertyNumber;

	private String ownerName;

	private String ownerNumber;

	private String ownerEmail;

	private double netPayable;

	private boolean notUsed;
	
	private ArrayList<Integer> assets;
	
	private static HashMap<Integer, Property> propertyMap;
	
	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;
	
	private Property() {
		
	}

	public ArrayList<Integer> getAssets() {
		return assets;
	}

	public void setAssets(ArrayList<Integer> assets) {
		this.assets = assets;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int id) {
		this.propertyId = id;
	}

	public int getSocietyId() {
		return societyId;
	}

	public void setSocietyId(int societyId) {
		this.societyId = societyId;
	}

	public int getWingId() {
		return wingId;
	}

	public void setWingId(int wingId) {
		this.wingId = wingId;
	}

	public int getFloorNumber() {
		return floorNumber;
	}

	public void setFloorNumber(int floorNumber) {
		this.floorNumber = floorNumber;
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

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerNumber() {
		return ownerNumber;
	}

	public void setOwnerNumber(String ownerNumber) {
		this.ownerNumber = ownerNumber;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public double getNetPayable() {
		return netPayable;
	}

	public void setNetPayable(double netPayable) {
		this.netPayable = netPayable;
	}

	public boolean isNotUsed() {
		return notUsed;
	}

	public void setNotUsed(boolean notUsed) {
		this.notUsed = notUsed;
	}
	
	public static Property get(int propertyId) {
		Property property = null;
		return property;
	}
	
	public static Property create(int societyId, int wingId, int floorNumber, int floorPlanId, int propertyNumber) {
		Property property = new Property();
		property.societyId = societyId;
		property.wingId = wingId;
		property.floorNumber = floorNumber;
		property.floorPlanId = floorPlanId;
		property.propertyNumber = propertyNumber;
		return property;
	}
	
	public static boolean save(Property property, boolean insertEntry) {
		boolean result = false;
		
		if (property != null) {
			
			if (insertEntry) {
				
				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement("INSERT INTO " + Constants.Table.Property.TABLE_NAME
							+ " ( "
							+ Constants.Table.Society.FieldName.SOCIETY_ID + " , "
							+ Constants.Table.Wing.FieldName.WING_ID + " , "
							+ Constants.Table.Floor.FieldName.FLOOR_NUMBER + " , "
							+ Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " , "
							+ Constants.Table.FloorPlanDesing.FieldName.PROPERTY_NUMBER + " , "
							+ Constants.Table.Property.FieldName.OWNER_NAME + " , "
							+ Constants.Table.Property.FieldName.OWNER_NUMBER + " , "
							+ Constants.Table.Property.FieldName.OWNER_EMAIL + " , "
							+ Constants.Table.Property.FieldName.NET_PAYBALE + " , "
							+ Constants.Table.Property.FieldName.NOT_USED
							+ " ) "
							+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				}
				
				if (insertStatement != null) {
					try {
						insertStatement.clearParameters();
						insertStatement.setInt(1, property.societyId);
						insertStatement.setInt(2, property.wingId);
						insertStatement.setInt(3, property.floorNumber);
						insertStatement.setInt(4, property.floorPlanId);
						insertStatement.setInt(5, property.propertyNumber);
						insertStatement.setString(6, property.ownerName);
						insertStatement.setString(7, property.ownerNumber);
						insertStatement.setString(8, property.ownerEmail);
						insertStatement.setDouble(9, property.netPayable);
						insertStatement.setBoolean(10, property.notUsed);
						result = insertStatement.execute();
						if (result) {
							ResultSet generatedKeys = insertStatement.getGeneratedKeys();
							if (generatedKeys != null && generatedKeys.first()) {
								property.propertyId = generatedKeys.getInt(Constants.Table.Property.FieldName.PROPERTY_ID);
							}
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
			} else {
				
				if (updateStatement == null) {
					updateStatement = SQLiteManager.getPreparedStatement("UPDATE " + Constants.Table.Property.TABLE_NAME
							+ " SET "
							+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ? , "
							+ Constants.Table.Wing.FieldName.WING_ID + " = ? , "
							+ Constants.Table.Floor.FieldName.FLOOR_NUMBER + " = ? , "
							+ Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " = ? , "
							+ Constants.Table.FloorPlanDesing.FieldName.PROPERTY_NUMBER + " = ? , "
							+ Constants.Table.Property.FieldName.OWNER_NAME + " = ? , "
							+ Constants.Table.Property.FieldName.OWNER_NUMBER + " = ? , "
							+ Constants.Table.Property.FieldName.OWNER_EMAIL + " = ? , "
							+ Constants.Table.Property.FieldName.NET_PAYBALE + " = ? , "
							+ Constants.Table.Property.FieldName.NOT_USED + " = ?"
							+ " WHERE "
							+ Constants.Table.Property.FieldName.PROPERTY_ID + " = ? ");
				}
				
				if (updateStatement != null) {
					try {
						updateStatement.clearParameters();
						updateStatement.setInt(1, property.societyId);
						updateStatement.setInt(2, property.wingId);
						updateStatement.setInt(3, property.floorNumber);
						updateStatement.setInt(4, property.floorPlanId);
						updateStatement.setInt(5, property.propertyNumber);
						updateStatement.setString(6, property.ownerName);
						updateStatement.setString(7, property.ownerNumber);
						updateStatement.setString(8, property.ownerEmail);
						updateStatement.setDouble(9, property.netPayable);
						updateStatement.setBoolean(10, property.notUsed);
						updateStatement.setInt(11, property.propertyId);
						result = updateStatement.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		}
		
		if (result) {
			if (propertyMap == null) {
				propertyMap = new HashMap<Integer, Property>();
			}
			propertyMap.put(property.propertyId, property);
		}
		
		return result;
	}
	
	public static boolean delete(Property property) {
		boolean result = false;
		
		if (property != null) {
			
			if (deleteStatement == null) {
				deleteStatement = SQLiteManager.getPreparedStatement("DELETE FROM " + Constants.Table.Property.TABLE_NAME
						+ " WHERE " + Constants.Table.Property.FieldName.PROPERTY_ID + " = ? ");
			}
			
			if (deleteStatement != null) {
				try {
					deleteStatement.clearParameters();
					deleteStatement.setInt(1, property.propertyId);
					result = deleteStatement.execute();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		if (result && propertyMap != null) {
			propertyMap.remove(property.propertyId);
		}
		
		return result;
	}
	
	public static Property read(int propertyId) {
		Property property = null;
		
		if (propertyMap == null) {
			propertyMap = new HashMap<Integer, Property>();
		}
		
		property = propertyMap.get(propertyId);
		if (property == null) {
			
			if (readStatement == null) {
				readStatement = SQLiteManager.getPreparedStatement("SELECT * FROM " + Constants.Table.Property.TABLE_NAME
						+ " WHERE " + Constants.Table.Property.FieldName.PROPERTY_ID + " = ?");
			}
			
			if (readStatement != null) {
				try {
					readStatement.clearParameters();
					readStatement.setInt(1, propertyId);
					ResultSet resultSet = readStatement.executeQuery();
					if (resultSet != null && resultSet.next()) {
						property = new Property();
						property.propertyId = propertyId;
						property.societyId = resultSet.getInt(Constants.Table.Society.FieldName.SOCIETY_ID);
						property.wingId = resultSet.getInt(Constants.Table.Wing.FieldName.WING_ID);
						property.floorNumber = resultSet.getInt(Constants.Table.Floor.FieldName.FLOOR_NUMBER);
						property.floorPlanId = resultSet.getInt(Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID);
						property.propertyNumber = resultSet.getInt(Constants.Table.FloorPlanDesing.FieldName.PROPERTY_NUMBER);
						property.ownerName = resultSet.getString(Constants.Table.Property.FieldName.OWNER_NAME);
						property.ownerNumber = resultSet.getString(Constants.Table.Property.FieldName.OWNER_NUMBER);
						property.ownerEmail = resultSet.getString(Constants.Table.Property.FieldName.OWNER_EMAIL);
						property.netPayable = resultSet.getDouble(Constants.Table.Property.FieldName.NET_PAYBALE);
						property.notUsed = resultSet.getBoolean(Constants.Table.Property.FieldName.NOT_USED);
						propertyMap.put(propertyId, property);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		return property;
	}
	
}

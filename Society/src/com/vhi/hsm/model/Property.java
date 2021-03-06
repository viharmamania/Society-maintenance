package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class Property {

	private final static Logger LOG = Logger.getLogger(SQLiteManager.class);
	private int propertyId;

	private int societyId;

	private int wingId;

	private int floorNumber;

	private int floorPlanId;

	private String propertyName;

	private String ownerName;

	private String ownerNumber;

	private String ownerEmail;

	private double netPayable;

	private boolean notUsed;

	private int propertyNumber;

	private int latestPaymentId;

	private ArrayList<Integer> assets;

	private static HashMap<Integer, Property> propertyMap = new HashMap<>();

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

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyNumber) {
		this.propertyName = propertyNumber;
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

	public int getLatestPaymentId() {
		return latestPaymentId;
	}

	public void setLatestPaymentId(int lastestPaymentId) {
		this.latestPaymentId = lastestPaymentId;
	}

	public static Property create(int societyId, int wingId, int floorNumber, int floorPlanId, String propertyNumber) {
		Property property = new Property();
		property.societyId = societyId;
		property.wingId = wingId;
		property.floorNumber = floorNumber;
		property.floorPlanId = floorPlanId;
		property.propertyName = propertyNumber;
		return property;
	}

	public static Property create() {
		Property property = new Property();
		property.societyId = SystemManager.society.getSocietyId();
		return property;
	}

	public static boolean save(Property property, boolean insertEntry) {
		boolean result = false;

		if (property != null) {

			if (insertEntry) {

				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement("INSERT INTO "
							+ Constants.Table.Property.TABLE_NAME + " ( " + Constants.Table.Society.FieldName.SOCIETY_ID
							+ " , " + Constants.Table.Wing.FieldName.WING_ID + " , "
							+ Constants.Table.Floor.FieldName.FLOOR_NUMBER + " , "
							+ Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " , "
							+ Constants.Table.Property.FieldName.PROPERTY_NAME + " , "
							+ Constants.Table.Property.FieldName.PROPERTY_NUMBER + " , "
							+ Constants.Table.Property.FieldName.OWNER_NAME + " , "
							+ Constants.Table.Property.FieldName.OWNER_NUMBER + " , "
							+ Constants.Table.Property.FieldName.OWNER_EMAIL + " , "
							+ Constants.Table.Property.FieldName.NET_PAYABLE + " , "
							+ Constants.Table.Property.FieldName.NOT_USED + " , "
							+ Constants.Table.Property.FieldName.LATEST_PAYMENT_ID + " ) "
							+ " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
				}

				if (insertStatement != null) {
					try {
						insertStatement.clearParameters();
						insertStatement.setInt(1, property.societyId);
						insertStatement.setInt(2, property.wingId);
						insertStatement.setInt(3, property.floorNumber);
						insertStatement.setInt(4, property.floorPlanId);
						insertStatement.setString(5, property.propertyName);
						insertStatement.setInt(6, property.propertyNumber);
						insertStatement.setString(7, property.ownerName);
						insertStatement.setString(8, property.ownerNumber);
						insertStatement.setString(9, property.ownerEmail);
						insertStatement.setDouble(10, property.netPayable);
						insertStatement.setBoolean(11, property.notUsed);
						insertStatement.setInt(11, property.latestPaymentId);
						result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);
						if (result) {
							ResultSet generatedKeys = insertStatement.getGeneratedKeys();
							if (generatedKeys != null) {
								property.propertyId = generatedKeys.getInt(1);
							}
						}
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}

			} else {

				if (updateStatement == null) {
					updateStatement = SQLiteManager.getPreparedStatement("UPDATE " + Constants.Table.Property.TABLE_NAME
							+ " SET " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ? , "
							+ Constants.Table.Wing.FieldName.WING_ID + " = ? , "
							+ Constants.Table.Floor.FieldName.FLOOR_NUMBER + " = ? , "
							+ Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " = ? , "
							+ Constants.Table.Property.FieldName.PROPERTY_NAME + " = ? , "
							+ Constants.Table.Property.FieldName.PROPERTY_NUMBER + " = ? , "
							+ Constants.Table.Property.FieldName.OWNER_NAME + " = ? , "
							+ Constants.Table.Property.FieldName.OWNER_NUMBER + " = ? , "
							+ Constants.Table.Property.FieldName.OWNER_EMAIL + " = ? , "
							+ Constants.Table.Property.FieldName.NET_PAYABLE + " = ? , "
							+ Constants.Table.Property.FieldName.NOT_USED + " = ?,"
							+ Constants.Table.Property.FieldName.LATEST_PAYMENT_ID + " = ?" + " WHERE "
							+ Constants.Table.Property.FieldName.PROPERTY_ID + " = ? ");
				}

				if (updateStatement != null) {
					try {
						updateStatement.clearParameters();
						updateStatement.setInt(1, property.societyId);
						updateStatement.setInt(2, property.wingId);
						updateStatement.setInt(3, property.floorNumber);
						updateStatement.setInt(4, property.floorPlanId);
						updateStatement.setString(5, property.propertyName);
						updateStatement.setInt(6, property.propertyNumber);
						updateStatement.setString(7, property.ownerName);
						updateStatement.setString(8, property.ownerNumber);
						updateStatement.setString(9, property.ownerEmail);
						updateStatement.setDouble(10, property.netPayable);
						updateStatement.setBoolean(11, property.notUsed);
						updateStatement.setInt(12, property.latestPaymentId);
						updateStatement.setInt(13, property.propertyId);
						result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}

			}

		}

		if (result) {
			if (insertEntry)
				LOG.info("Property Saved :" + property.getPropertyName());
			else
				LOG.info("Property updated :" + property.getPropertyName());

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
				deleteStatement = SQLiteManager
						.getPreparedStatement("DELETE FROM " + Constants.Table.Property.TABLE_NAME + " WHERE "
								+ Constants.Table.Property.FieldName.PROPERTY_ID + " = ? ");
			}

			if (deleteStatement != null) {
				try {
					deleteStatement.clearParameters();
					deleteStatement.setInt(1, property.propertyId);
					result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}
			}

		}

		if (result && propertyMap != null) {
			LOG.info("Property deleted :" + property.getPropertyName() );
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
				readStatement = SQLiteManager
						.getPreparedStatement("SELECT * FROM " + Constants.Table.Property.TABLE_NAME + " WHERE "
								+ Constants.Table.Property.FieldName.PROPERTY_ID + " = ?");
			}

			if (readStatement != null) {
				try {
					readStatement.clearParameters();
					readStatement.setInt(1, propertyId);
					ResultSet resultSet = readStatement.executeQuery();
					addProperties(resultSet);
					property = propertyMap.get(propertyId);
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}
			}

		}

		return property;
	}

	public int getPropertyNumber() {
		return propertyNumber;
	}

	public void setPropertyNumber(int propertyNumber) {
		this.propertyNumber = propertyNumber;
	}

	public static void addProperties(ResultSet resultSet) throws SQLException {

		while (resultSet != null && resultSet.next()) {
			Property property = new Property();
			property.propertyId = resultSet.getInt(Constants.Table.Property.FieldName.PROPERTY_ID);
			property.societyId = resultSet.getInt(Constants.Table.Society.FieldName.SOCIETY_ID);
			property.wingId = resultSet.getInt(Constants.Table.Wing.FieldName.WING_ID);
			property.floorNumber = resultSet.getInt(Constants.Table.Floor.FieldName.FLOOR_NUMBER);
			property.floorPlanId = resultSet.getInt(Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID);
			property.propertyName = resultSet.getString(Constants.Table.Property.FieldName.PROPERTY_NAME);
			property.propertyNumber = resultSet.getInt(Constants.Table.Property.FieldName.PROPERTY_NUMBER);
			property.ownerName = resultSet.getString(Constants.Table.Property.FieldName.OWNER_NAME);
			property.ownerNumber = resultSet.getString(Constants.Table.Property.FieldName.OWNER_NUMBER);
			property.ownerEmail = resultSet.getString(Constants.Table.Property.FieldName.OWNER_EMAIL);
			property.netPayable = resultSet.getDouble(Constants.Table.Property.FieldName.NET_PAYABLE);
			property.notUsed = resultSet.getBoolean(Constants.Table.Property.FieldName.NOT_USED);
			property.latestPaymentId = resultSet.getInt(Constants.Table.Property.FieldName.LATEST_PAYMENT_ID);
			propertyMap.put(property.propertyId, property);
		}

	}

	public static Map<String, Integer> getAllPropertyNames(int societyId2) {
		Map<String, Integer> propertyNameToIdMap = new HashMap<>();
		String searchQuery = "select " + Constants.Table.Property.FieldName.PROPERTY_ID + ","
				+ Constants.Table.Property.FieldName.PROPERTY_NAME + " " + "from " + Constants.Table.Property.TABLE_NAME
				+ " where " + Constants.Table.Society.FieldName.SOCIETY_ID + " = " + societyId2;

		try {
			ResultSet result = SQLiteManager.executeQuery(searchQuery);
			if (result != null && result.next()) {
				do {
					String propertyName = result.getString(Constants.Table.Property.FieldName.PROPERTY_NAME);
					int property_id = result.getInt(Constants.Table.Property.FieldName.PROPERTY_ID);
					propertyNameToIdMap.put(propertyName, property_id);
					result.next();
				} while (!result.isAfterLast());
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		return propertyNameToIdMap;
	}

	public static int getPropertyCount(int societyId2) {
		String countQuery = "select count(*)" + "from " + Constants.Table.Property.TABLE_NAME + " where "
				+ Constants.Table.Society.FieldName.SOCIETY_ID + " = " + societyId2;

		try {
			ResultSet result = SQLiteManager.executeQuery(countQuery);
			if (result != null && result.next()) {
				return result.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			LOG.error(e.getMessage());
		}
		return 0;
	}

	public static List<Property> getAllProperties(int societyId2) {

		List<Property> properties = new ArrayList<>();

		String searchQuery = "select * from " + Constants.Table.Property.TABLE_NAME + " where "
				+ Constants.Table.Society.FieldName.SOCIETY_ID + " = " + societyId2;

		try {
			ResultSet result = SQLiteManager.executeQuery(searchQuery);
			if (result != null && result.next()) {
				do {
					int property_id = result.getInt(Constants.Table.Property.FieldName.PROPERTY_ID);
					properties.add(read(property_id));
					result.next();
				} while (!result.isAfterLast());
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		return properties;
	}

}

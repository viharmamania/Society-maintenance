package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class PropertyType {

	private final static Logger LOG = Logger.getLogger(PropertyType.class);
	private String propertyType;

	private String description;

	private int societyId;

	private static HashMap<Integer, HashMap<String, PropertyType>> propertyTypeMap = new HashMap<>();

	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;

	private static String readString = "SELECT * FROM " + Constants.Table.PropertyType.TABLE_NAME + " WHERE "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
			+ Constants.Table.PropertyType.FieldName.PROPERTY_TYPE + " =? ";

	private static String insertString = "INSERT INTO " + Constants.Table.PropertyType.TABLE_NAME + " , "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " , "
			+ Constants.Table.PropertyType.FieldName.PROPERTY_TYPE + " , "
			+ Constants.Table.PropertyType.FieldName.DESCRIPTION + " , )" + " VALUES (?, ?, ?)";

	private static String updateString = "UPDATE " + Constants.Table.PropertyType.TABLE_NAME + " SET "
			+ Constants.Table.PropertyType.FieldName.DESCRIPTION + " =? " + " WHERE "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
			+ Constants.Table.PropertyType.FieldName.PROPERTY_TYPE + " =? ";

	private static String deleteString = "DELETE " + Constants.Table.PropertyType.TABLE_NAME + " WHERE "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
			+ Constants.Table.PropertyType.FieldName.PROPERTY_TYPE + " = ?";

	private PropertyType() {

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public int getSocietyId() {
		return societyId;
	}

	public void setSocietyId(int societyId) {
		this.societyId = societyId;
	}

	public static PropertyType create(int societyId) {
		PropertyType propertyType = new PropertyType();
		propertyType.setSocietyId(societyId);
		return propertyType;
	}

	public static boolean delete(PropertyType propertyType) {

		if (deleteStatement == null) {
			deleteStatement = SQLiteManager.getPreparedStatement(deleteString);
		}
		boolean result = false;
		try {
			deleteStatement.setInt(1, propertyType.getSocietyId());
			deleteStatement.setString(2, propertyType.getPropertyType());
			result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		
		if(result && propertyTypeMap!=null){
			LOG.info("Property Type deleted :" + propertyType.getDescription());
			/*HashMap<String, PropertyType> hashMap = propertyTypeMap.get(propertyType.getSocietyId());
			hashMap.remove(propertyType.getDescription());
			propertyTypeMap.put(propertyType.getSocietyId(), hashMap);*/
		}
		return result;
	}

	public static boolean save(PropertyType propertyType, boolean insertEntry) {

		boolean result = false;
		try {
			if (insertEntry) {
				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement(insertString);
				}
				insertStatement.setInt(1, propertyType.getSocietyId());
				insertStatement.setString(2, propertyType.getPropertyType());
				insertStatement.setString(3, propertyType.getDescription());
				result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);
			} else {

				if (updateStatement == null) {
					updateStatement = SQLiteManager.getPreparedStatement(updateString);
				}
				updateStatement.setString(1, propertyType.getDescription());
				updateStatement.setInt(2, propertyType.getSocietyId());
				updateStatement.setString(3, propertyType.getPropertyType());
				result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
			}

			// updating hashmap
			if (result) {

				if (propertyTypeMap == null) {
					propertyTypeMap = new HashMap<Integer, HashMap<String, PropertyType>>();
				}

				HashMap<String, PropertyType> propertyGroupType = propertyTypeMap.get(propertyType.getSocietyId());
				if (propertyGroupType == null) {
					propertyGroupType = new HashMap<>();
					propertyTypeMap.put(propertyType.getSocietyId(), propertyGroupType);
				}
				propertyGroupType.put(propertyType.getDescription(), propertyType);

			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		return result;
	}

	public static PropertyType read(int societyId, String propType) {
		PropertyType propertyTypeObject = null;

		HashMap<String, PropertyType> societyPropertyTypes = propertyTypeMap.get(societyId);

		if (societyPropertyTypes == null) {
			societyPropertyTypes = new HashMap<>();
			propertyTypeMap.put(societyId, societyPropertyTypes);
		}

		if (societyPropertyTypes != null) {
			propertyTypeObject = societyPropertyTypes.get(propType);
			if (propertyTypeObject == null) {
				propertyTypeObject = new PropertyType();
				try {
					if (readStatement == null) {
						readStatement = SQLiteManager.getPreparedStatement(readString);
					}
					readStatement.setInt(1, societyId);
					readStatement.setString(2, propType);
					ResultSet resultSet = readStatement.executeQuery();
					if (resultSet != null && resultSet.next()) {
						propertyTypeObject.setDescription(
								resultSet.getString(Constants.Table.PropertyGroup.FieldName.DESCRIPTION));
						propertyTypeObject.setSocietyId(societyId);
						propertyTypeObject.setPropertyType(propType);
						societyPropertyTypes.put(propType, propertyTypeObject);
					}
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}

			}
		}
		return propertyTypeObject;
	}
	
	public static List<PropertyType> getAllPropertyType(int societyId) {
		List<PropertyType> list = new ArrayList<>();
		
		String searchQuery = "select * from " + Constants.Table.PropertyType.TABLE_NAME + " where "
				+ Constants.Table.Society.FieldName.SOCIETY_ID + " = " + societyId;

		try {
			ResultSet result = SQLiteManager.executeQuery(searchQuery);
			if (result != null && result.next()) {
				do {
					String propertyType = result.getString(Constants.Table.PropertyType.FieldName.PROPERTY_TYPE);
					list.add(read(societyId, propertyType));
					result.next();
				} while (!result.isAfterLast());
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		
		return list;
	}

}

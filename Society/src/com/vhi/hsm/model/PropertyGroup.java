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

public class PropertyGroup {

	private final static Logger LOG = Logger.getLogger(PropertyGroup.class);
	private String propertyGroup;

	private String description;

	private int societyId;

	private static HashMap<Integer, HashMap<String, PropertyGroup>> propertyGroupMap = new HashMap<>();

	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;
	/*
	 * private static PreparedStatement readStatement = SQLiteManager
	 * .getPreparedStatement("SELECT * FROM " +
	 * Constants.Table.PropertyGroup.TABLE_NAME + " WHERE " +
	 * Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND " +
	 * Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP +" =? ");;
	 * 
	 * private static PreparedStatement insertStatement =
	 * SQLiteManager.getPreparedStatement( "INSERT INTO " +
	 * Constants.Table.PropertyGroup.TABLE_NAME + " VALUES (?, ?, ?)");
	 * 
	 * 
	 * private static PreparedStatement updateStatement =
	 * SQLiteManager.getPreparedStatement( "UPDATE " +
	 * Constants.Table.PropertyGroup.TABLE_NAME + " SET "
	 * +Constants.Table.PropertyGroup.FieldName.DESCRIPTION + " =? " + " WHERE "
	 * + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND " +
	 * Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP +" =? ");
	 * 
	 * private static PreparedStatement deleteStatement =
	 * SQLiteManager.getPreparedStatement("DELETE " +
	 * Constants.Table.PropertyGroup.TABLE_NAME + " WHERE " +
	 * Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND " +
	 * Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP + " = ?");
	 */

	private static String readString = "SELECT * FROM " + Constants.Table.PropertyGroup.TABLE_NAME + " WHERE "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
			+ Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP + " =? ";

	private static String insertString = "INSERT INTO " + Constants.Table.PropertyGroup.TABLE_NAME + " ( "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " , "
			+ Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP + " , "
			+ Constants.Table.PropertyGroup.FieldName.DESCRIPTION + " ,) " + " VALUES (?, ?, ?)";

	private static String updateString = "UPDATE " + Constants.Table.PropertyGroup.TABLE_NAME + " SET "
			+ Constants.Table.PropertyGroup.FieldName.DESCRIPTION + " =? " + " WHERE "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
			+ Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP + " =? ";

	private static String deleteString = "DELETE " + Constants.Table.PropertyGroup.TABLE_NAME + " WHERE "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
			+ Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP + " = ?";

	public String getPropertygroup() {
		return propertyGroup;
	}

	private void setPropertygroup(String propertyGroup) {
		this.propertyGroup = propertyGroup;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getSocietyId() {
		return societyId;
	}

	public void setSocietyId(int societyId) {
		this.societyId = societyId;
	}

	public static PropertyGroup create(int societyId) {
		PropertyGroup propertyGroup = new PropertyGroup();
		propertyGroup.setSocietyId(societyId);
		return propertyGroup;
	}

	public static boolean delete(PropertyGroup propertyGroup) {

		boolean result = false;
		try {
			if (deleteStatement == null) {
				deleteStatement = SQLiteManager.getPreparedStatement(deleteString);
			}
			deleteStatement.setInt(1, propertyGroup.getSocietyId());
			deleteStatement.setString(2, propertyGroup.getPropertygroup());
			result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		return result;
	}

	public static boolean save(PropertyGroup propertyGroup, boolean insertEntry) {
		boolean result = false;

		try {
			if (insertEntry) {
				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement(insertString);
				}
				insertStatement.setInt(0, propertyGroup.getSocietyId());
				insertStatement.setString(1, propertyGroup.getPropertygroup());
				insertStatement.setString(2, propertyGroup.getDescription());
				result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);
			} else {
				if (updateStatement == null) {
					updateStatement = SQLiteManager.getPreparedStatement(updateString);
				}
				updateStatement.setString(0, propertyGroup.getDescription());
				updateStatement.setInt(1, propertyGroup.getSocietyId());
				updateStatement.setString(2, propertyGroup.getPropertygroup());
				result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
			}

			// updating hashmap
			if (result) {

				if (propertyGroupMap == null) {
					propertyGroupMap = new HashMap<Integer, HashMap<String, PropertyGroup>>();
				}

				HashMap<String, PropertyGroup> propertyGroupType = propertyGroupMap.get(propertyGroup.getSocietyId());
				if (propertyGroupType == null) {
					propertyGroupType = new HashMap<>();
					propertyGroupMap.put(propertyGroup.getSocietyId(), propertyGroupType);
				}
				propertyGroupType.put(propertyGroup.getDescription(), propertyGroup);

			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		return result;
	}

	public static PropertyGroup read(int societyId, String propertyGroup) {
		PropertyGroup group = null;

		HashMap<String, PropertyGroup> societygroupTypes = propertyGroupMap.get(societyId);

		if (societygroupTypes == null) {
			societygroupTypes = new HashMap<>();
			propertyGroupMap.put(societyId, societygroupTypes);
		}

		if (societygroupTypes != null) {
			group = societygroupTypes.get(propertyGroup);
			if (group == null) {
				group = new PropertyGroup();
				try {
					if (readStatement == null) {
						readStatement = SQLiteManager.getPreparedStatement(readString);
					}
					readStatement.setInt(1, societyId);
					readStatement.setString(2, propertyGroup);
					ResultSet resultSet = readStatement.executeQuery();
					if (resultSet != null && resultSet.next()) {
						group.setDescription(resultSet.getString(Constants.Table.PropertyGroup.FieldName.DESCRIPTION));
						group.setSocietyId(societyId);
						group.setPropertygroup(propertyGroup);
					}
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}

			}
		}
		return group;
	}
	
	public static List<PropertyGroup> getAllPropertyGroup(int societyId) {
		List<PropertyGroup> list = new ArrayList<>();
		
		String searchQuery = "select * from " + Constants.Table.PropertyGroup.TABLE_NAME + " where "
				+ Constants.Table.Society.FieldName.SOCIETY_ID + " = " + societyId;

		ResultSet result = SQLiteManager.executeQuery(searchQuery);
		try {
			if (result != null && result.next()) {
				do {
					String propertyGroup = result.getString(Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP);
					list.add(read(societyId, propertyGroup));
					result.next();
				} while (!result.isAfterLast());
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		
		return list;
	}

}

package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class Wing {

	private final static Logger LOG = Logger.getLogger(Wing.class);
	private int societyId;

	private int wingId;

	private String name;

	private int noOfFloors;

	private HashMap<Integer, Floor> floors;

	private static HashMap<Integer, HashMap<Integer, Wing>> wingMap = new HashMap<>();

	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;


	private static String readString = "SELECT * FROM " + Constants.Table.Wing.TABLE_NAME + " WHERE "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND " + Constants.Table.Wing.FieldName.WING_ID
			+ " =? ";

	private static String insertString = "INSERT INTO " + Constants.Table.User.TABLE_NAME + "("
			+ Constants.Table.Wing.FieldName.WING_NAME + " , " + Constants.Table.Wing.FieldName.NUMBER_OF_FLOORS + " , "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + ") VALUES (?, ?, ?)";

	private static String updateString = "UPDATE " + Constants.Table.User.TABLE_NAME + " SET "
			+ Constants.Table.Wing.FieldName.WING_NAME + " =? " + Constants.Table.Wing.FieldName.NUMBER_OF_FLOORS
			+ " =? " + " WHERE " + Constants.Table.Wing.FieldName.WING_ID + " =? " + " AND "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?";

	private static String deleteString = "DELETE " + Constants.Table.Wing.TABLE_NAME + " WHERE "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND " + Constants.Table.Wing.FieldName.WING_ID
			+ " = ?";

	private Wing() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNoOfFloors() {
		return noOfFloors;
	}

	public void setNoOfFloors(int noOfFloors) {
		this.noOfFloors = noOfFloors;
	}

	public HashMap<Integer, Floor> getFloors() {
		return floors;
	}

	public void setFloors(HashMap<Integer, Floor> floors) {
		this.floors = floors;
	}
	
	@Override
	public String toString() {
		return name;
	}

	public static Wing create(int societyId) {
		Wing wing = new Wing();
		wing.setSocietyId(societyId);
		return wing;
	}

	public static boolean delete(Wing wing) {
		boolean result = false;

		try {
			if (deleteStatement == null) {
				deleteStatement = SQLiteManager.getPreparedStatement(deleteString);
			}
			deleteStatement.setInt(1, wing.getSocietyId());
			deleteStatement.setInt(2, wing.getWingId());
			result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return result;
	}

	public static boolean save(Wing wing, boolean insert) {

		boolean result = false;
		try {
			if (insert) {
				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement(insertString);
				}
				insertStatement.setInt(1, wing.getSocietyId());
				insertStatement.setString(2, wing.getName());
				insertStatement.setInt(3, wing.getNoOfFloors());
				result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);

			} else {

				if (updateStatement == null) {
					updateStatement = SQLiteManager.getPreparedStatement(updateString);
				}
				updateStatement.setString(1, wing.getName());
				updateStatement.setInt(2, wing.getNoOfFloors());
				updateStatement.setInt(3, wing.getWingId());
				updateStatement.setInt(4, wing.getSocietyId());
				result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
			}

			// updating hashmap
			if (result) {

				if (wingMap == null) {
					wingMap = new HashMap<Integer, HashMap<Integer, Wing>>();
				}

				HashMap<Integer, Wing> propertyGroupType = wingMap.get(wing.getSocietyId());
				if (propertyGroupType == null) {
					propertyGroupType = new HashMap<>();
					wingMap.put(wing.getSocietyId(), propertyGroupType);
				}
				propertyGroupType.put(wing.getWingId(), wing);

			}
		} catch (Exception e) {
			LOG.error(e.getMessage());
		}
		return result;
	}

	public static Wing read(int societyId, int wingId) {
		Wing wing = null;

		HashMap<Integer, Wing> societyWings = wingMap.get(societyId);

		if (societyWings == null) {
			societyWings = new HashMap<>();
			wingMap.put(societyId, societyWings);
		}

		if (societyWings != null) {
			wing = societyWings.get(wingId);
			if (wing == null) {
				wing = new Wing();
				try {
					if (readStatement == null) {
						readStatement = SQLiteManager.getPreparedStatement(readString);
					}
					readStatement.setInt(1, societyId);
					readStatement.setInt(2, wingId);
					ResultSet resultSet = readStatement.executeQuery();
					if (resultSet != null && resultSet.next()) {

						wing.setNoOfFloors(resultSet.getInt(Constants.Table.Wing.FieldName.NUMBER_OF_FLOORS));
						wing.setName(resultSet.getString(Constants.Table.Wing.FieldName.WING_NAME));
						wing.setWingId(resultSet.getInt(Constants.Table.Wing.FieldName.WING_ID));

						wing.setSocietyId(societyId);

					}
					societyWings.put(wingId, wing);
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}

			}
		}
		return wing;
	}
	
	public static ArrayList<Wing> getAllWings(int societyId) {
		ArrayList<Wing> allWings = new ArrayList<Wing>();
		
		String query = "SELECT " + Constants.Table.Wing.FieldName.WING_ID 
				+ " FROM " + Constants.Table.Wing.TABLE_NAME
				+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = " + societyId;
		
		ResultSet resultSet;
		try {
			resultSet = SQLiteManager.executeQuery(query);
			if (resultSet != null) {
				while(resultSet.next()) {
					allWings.add(Wing.read(societyId, resultSet.getInt(Constants.Table.Wing.FieldName.WING_ID)));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return allWings;
	}
	
}

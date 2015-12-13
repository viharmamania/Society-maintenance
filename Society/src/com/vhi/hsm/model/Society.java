package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class Society {

	private int societyId;

	private String name;

	private String address;

	private String registrationNumber;

	private String registrationDate;

	private String societyCode;

	public String getSocietyCode() {
		return societyCode;
	}

	public void setSocietyCode(String societyCode) {
		this.societyCode = societyCode;
	}

	private HashMap<Integer, PropertyGroup> propertyGroups;

	private HashMap<Integer, PropertyType> propertyTypes;

	private HashMap<Integer, Wing> wings;

	private HashMap<Integer, FloorPlan> floorPlans;

	private HashMap<Integer, AssetType> assetTypes;

	private static HashMap<Integer, Society> societyMap = new HashMap<>();

	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;

	private static String readString = "SELECT * FROM " + Constants.Table.Society.TABLE_NAME + " WHERE "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?";

	private static String insertString = "INSERT INTO " + Constants.Table.Society.TABLE_NAME + "("
			+ Constants.Table.Society.FieldName.SOCIETY_NAME + " , " + Constants.Table.Society.FieldName.ADDRESS + " , "
			+ Constants.Table.Society.FieldName.REG_NUMBER + " , " + Constants.Table.Society.FieldName.REG_DATE + " , "
			+ Constants.Table.Society.FieldName.SOCIETY_CODE + " )" + " VALUES (?, ?, ?, ?, ?)";

	private static String updateString = "UPDATE " + Constants.Table.Society.TABLE_NAME + " SET "
			+ Constants.Table.Society.FieldName.SOCIETY_NAME + " =? " + Constants.Table.Society.FieldName.ADDRESS
			+ " =? " + Constants.Table.Society.FieldName.REG_NUMBER + " =? "
			+ Constants.Table.Society.FieldName.REG_DATE + " =? " + Constants.Table.Society.FieldName.SOCIETY_CODE
			+ " ?" + " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?";

	private static String deleteString = "DELETE " + Constants.Table.Society.TABLE_NAME + " WHERE "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?";

	private Society() {
		super();
	}

	public Society(int society_id, String name, String address, String registrationNumber, String registrationDate) {
		super();
		this.societyId = society_id;
		this.name = name;
		this.address = address;
		this.registrationNumber = registrationNumber;
		this.registrationDate = registrationDate;
	}

	public Society(String name, String address, String registrationNumber, String registrationDate) {
		super();
		this.name = name;
		this.address = address;
		this.registrationNumber = registrationNumber;
		this.registrationDate = registrationDate;
	}

	public int getSocietyId() {
		return societyId;
	}

	public void setSocietyId(int society_id) {
		this.societyId = society_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(String registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(String registrationDate) {
		this.registrationDate = registrationDate;
	}

	public HashMap<Integer, PropertyGroup> getPropertyGroups() {
		return propertyGroups;
	}

	public void setPropertyGroups(HashMap<Integer, PropertyGroup> propertyGroups) {
		this.propertyGroups = propertyGroups;
	}

	public HashMap<Integer, PropertyType> getPropertyTypes() {
		return propertyTypes;
	}

	public void setPropertyTypes(HashMap<Integer, PropertyType> propertyTypes) {
		this.propertyTypes = propertyTypes;
	}

	public HashMap<Integer, Wing> getWings() {
		return wings;
	}

	public void setWings(HashMap<Integer, Wing> wings) {
		this.wings = wings;
	}

	public HashMap<Integer, FloorPlan> getFloorPlans() {
		return floorPlans;
	}

	public void setFloorPlans(HashMap<Integer, FloorPlan> floorPlans) {
		this.floorPlans = floorPlans;
	}

	public HashMap<Integer, AssetType> getAssetTypes() {
		return assetTypes;
	}

	public void setAssetTypes(HashMap<Integer, AssetType> assetTypes) {
		this.assetTypes = assetTypes;
	}

	public static Society create() {
		return new Society();
	}

	public static boolean delete(Society society) {

		boolean result = false;
		try {
			if (deleteStatement == null) {
				deleteStatement = SQLiteManager.getPreparedStatement(deleteString);
			}
			deleteStatement.setInt(1, society.getSocietyId());
			result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static boolean save(Society society, boolean insertEntry) {
		boolean result = false;

		try {
			if (insertEntry) {
				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement(insertString);
				}
				insertStatement.setString(1, society.getName());
				insertStatement.setString(2, society.getAddress());
				insertStatement.setString(3, society.getRegistrationNumber());
				insertStatement.setString(4, society.getRegistrationDate());
				insertStatement.setString(5, society.getSocietyCode());
				result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);
			} else {
				if (updateStatement == null) {
					updateStatement = SQLiteManager.getPreparedStatement(updateString);
				}
				updateStatement.setString(1, society.getName());
				updateStatement.setString(2, society.getAddress());
				updateStatement.setString(3, society.getRegistrationNumber());
				updateStatement.setString(4, society.getRegistrationDate());
				updateStatement.setString(5, society.getSocietyCode());
				updateStatement.setInt(5, society.getSocietyId());
				result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
			}

			if (result) {

				if (societyMap == null) {
					societyMap = new HashMap<>();
				}

				if (societyMap.get(society.getSocietyId()) == null) {
					societyMap.put(society.getSocietyId(), society);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static Society read(int societyId) {

		Society society = null;
		society = societyMap.get(societyId);

		if (society == null) {
			try {
				society = new Society();
				if (readStatement == null) {
					readStatement = SQLiteManager.getPreparedStatement(readString);
				}
				readStatement.setInt(1, societyId);
				ResultSet resultset = readStatement.executeQuery();
				if (resultset != null && resultset.next()) {
					society.setName(resultset.getString(Constants.Table.Society.FieldName.SOCIETY_NAME));
					society.setRegistrationNumber(resultset.getString(Constants.Table.Society.FieldName.REG_NUMBER));
					society.setAddress(resultset.getString(Constants.Table.Society.FieldName.ADDRESS));
					society.setSocietyId(resultset.getInt(Constants.Table.Society.FieldName.SOCIETY_ID));
					// society.setRegistrationDate(resultset.getString(Constants.Table.Society.FieldName.REG_DATE));
					society.setSocietyCode(resultset.getString(Constants.Table.Society.FieldName.SOCIETY_CODE));
				}
				societyMap.put(societyId, society);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return society;
	}

}

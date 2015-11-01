package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class FloorPlanDesign {

	private int societyId;

	private int floorPlanId;

	private int propertyNumber;

	private String propertyGroup;

	private String propertyType;

	public FloorPlanDesign() {

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
	
	public static FloorPlanDesign get(int societyId, int floorPlanId, int propertyNumber) {
		FloorPlanDesign floorPlanDesign = null;
		ResultSet resultSet = null;
		String query = "SELECT * FROM " + Constants.Table.FloorPlanDesing.TABLE_NAME
				+ "WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?"
				+ " AND " + Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " = ?"
				+ " AND " + Constants.Table.FloorPlanDesing.FieldName.PROPERTY_NUMBER + " = ?";
		PreparedStatement preparedStatement = SQLiteManager.getPreparedStatement(query);
		if (preparedStatement != null) {
			try {
				preparedStatement.setInt(1, societyId);
				preparedStatement.setInt(2, floorPlanId);
				preparedStatement.setInt(3, propertyNumber);
				resultSet = preparedStatement.executeQuery();
				if (resultSet != null && resultSet.next()) {
					floorPlanDesign = new FloorPlanDesign();
					floorPlanDesign.setSocietyId(societyId);
					floorPlanDesign.setFloorPlanId(floorPlanId);
					floorPlanDesign.setPropertyNumber(propertyNumber);
					floorPlanDesign.setPropertyGroup(resultSet.getString(Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP));
					floorPlanDesign.setPropertyType(resultSet.getString(Constants.Table.PropertyType.FieldName.PROPERTY_TYPE));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return floorPlanDesign;
	}

}

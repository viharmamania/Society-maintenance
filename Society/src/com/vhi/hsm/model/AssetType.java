package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class AssetType {

	private int societyId;

	private String assetType;

	private String description;

	private double charges;

	private static HashMap<Integer, HashMap<String, AssetType>> assetTypeMap;
	private static PreparedStatement readStatement, insertStatement, updateStatement;

	private AssetType() {

	}

	public int getSocietyId() {
		return societyId;
	}

	public void setSocietyId(int societyId) {
		this.societyId = societyId;
	}

	public String getAssetType() {
		return assetType;
	}

	public void setAssetType(String assetType) {
		this.assetType = assetType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getCharges() {
		return charges;
	}

	public void setCharges(double charges) {
		this.charges = charges;
	}

	public static AssetType read(int societyId, String assetType) {

		AssetType assetTypeObj = null;

		if (assetTypeMap == null) {
			assetTypeMap = new HashMap<Integer, HashMap<String, AssetType>>();
		}

		Integer societyIdInteger = new Integer(societyId);

		HashMap<String, AssetType> societyAssetTypes = assetTypeMap.get(societyIdInteger);
		if (societyAssetTypes == null) {
			societyAssetTypes = new HashMap<String, AssetType>();
			assetTypeMap.put(societyIdInteger, societyAssetTypes);
		}

		if (societyAssetTypes != null) {
			assetTypeObj = societyAssetTypes.get(assetType);
			if (assetTypeObj == null) {
				assetTypeObj = new AssetType();
				if (readStatement == null) {
					readStatement = SQLiteManager
							.getPreparedStatement("SELECT * FROM " + Constants.Table.AssetType.TABLE_NAME + " WHERE "
									+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
									+ Constants.Table.AssetType.FieldName.ASSET_TYPE + " = ?");
				}
				try {
					if (readStatement != null) {
						readStatement.setInt(1, societyId);
						readStatement.setString(2, assetType);
						ResultSet resultSet = readStatement.executeQuery();
						if (resultSet != null && resultSet.first()) {
							assetTypeObj = new AssetType();
							assetTypeObj.societyId = resultSet.getInt(Constants.Table.Society.FieldName.SOCIETY_ID);
							assetTypeObj.assetType = resultSet.getString(Constants.Table.AssetType.FieldName.ASSET_TYPE);
							assetTypeObj.description = resultSet.getString(Constants.Table.AssetType.FieldName.DESCRIPTION);
							assetTypeObj.charges = resultSet.getDouble(Constants.Table.AssetType.FieldName.CHARGE);
							societyAssetTypes.put(assetType, assetTypeObj);
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return assetTypeObj;
	}

	public static boolean save(AssetType assetType) {
		boolean result = false;
		if (assetType != null && assetType.societyId != -1 && assetType.getAssetType().trim().length() != 0) {
			
		}
		return result;
	}

	public static AssetType create(int societyId) {
		AssetType assetType = new AssetType();
		assetType.societyId = societyId;
		return assetType;
	}

}

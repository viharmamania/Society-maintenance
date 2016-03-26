package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class AssetType {
	
	private final static Logger LOG = Logger.getLogger(AssetType.class);

	private int societyId;

	private String assetType;

	private String description;

	private int chargeId;

	private static HashMap<Integer, HashMap<String, AssetType>> assetTypeMap;
	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;

	private AssetType() {

	}
	

	public int getChargeId() {
		return chargeId;
	}


	public void setChargeId(int chargeId) {
		this.chargeId = chargeId;
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

	public static ArrayList<AssetType> getAllAssetType(int societyId) {
		ArrayList<AssetType> list = new ArrayList<AssetType>();
		try {
			ResultSet resultSet = SQLiteManager.executeQuery("SELECT * FROM " + Constants.Table.AssetType.TABLE_NAME
					+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = " + societyId);
			if (resultSet != null) {
				AssetType assetType;
				while (resultSet.next()) {
					assetType = null;
					assetType = read(societyId, resultSet.getString(Constants.Table.AssetType.FieldName.ASSET_TYPE));
					if (assetType != null) {
						list.add(assetType);
					}
				}

			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		return list;
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
				if (readStatement == null) {
					readStatement = SQLiteManager
							.getPreparedStatement("SELECT * FROM " + Constants.Table.AssetType.TABLE_NAME + " WHERE "
									+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
									+ Constants.Table.AssetType.FieldName.ASSET_TYPE + " = ?");
				}
				try {
					if (readStatement != null) {
						readStatement.clearParameters();
						readStatement.setInt(1, societyId);
						readStatement.setString(2, assetType);
						ResultSet resultSet = readStatement.executeQuery();
						if (resultSet != null && resultSet.next()) {
							assetTypeObj = new AssetType();
							assetTypeObj.societyId = resultSet.getInt(Constants.Table.Society.FieldName.SOCIETY_ID);
							assetTypeObj.assetType = resultSet.getString(Constants.Table.AssetType.FieldName.ASSET_TYPE);
							assetTypeObj.description = resultSet.getString(Constants.Table.AssetType.FieldName.DESCRIPTION);
							assetTypeObj.chargeId = resultSet.getInt(Constants.Table.Charge.FieldName.CHARGE_ID);
							societyAssetTypes.put(assetType, assetTypeObj);
						}
					}
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}
			}
		}

		return assetTypeObj;
	}

	public static boolean save(AssetType assetType, boolean insertEntry) {
		boolean result = false;
		if (assetType != null && assetType.societyId != -1 && assetType.getAssetType().trim().length() != 0) {
			if (insertEntry) {
				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement(
							"INSERT INTO " + Constants.Table.AssetType.TABLE_NAME + " VALUES (?, ?, ?, ?)");
				}
				if (insertStatement != null) {
					try {
						insertStatement.clearParameters();
						insertStatement.setInt(1, assetType.getSocietyId());
						insertStatement.setString(2, assetType.getAssetType());
						insertStatement.setString(3, assetType.getDescription());
						insertStatement.setDouble(4, assetType.getChargeId());
						result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}
			} else {
				if (updateStatement == null) {
					updateStatement = SQLiteManager
							.getPreparedStatement("UPDATE " + Constants.Table.AssetType.TABLE_NAME + " SET "
									+ Constants.Table.AssetType.FieldName.DESCRIPTION + " = ?, "
									+ Constants.Table.AssetType.FieldName.CHARGE_ID + " = ? " + " WHERE "
									+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
									+ Constants.Table.AssetType.FieldName.ASSET_TYPE + " = ?");
				}

				if (updateStatement != null) {
					try {
						updateStatement.clearParameters();
						updateStatement.setString(1, assetType.getDescription());
						updateStatement.setDouble(2, assetType.getChargeId());
						updateStatement.setInt(3, assetType.getSocietyId());
						updateStatement.setString(4, assetType.getAssetType());
						result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}
			}
		}

		// Update the HashMap if entry is created or updated
		if (result) {

			if (insertEntry)
				LOG.info("Asset Type Created:" + assetType.getAssetType());
			else
				LOG.info("Asset Type updated:" + assetType.getAssetType());

			if (assetTypeMap == null) {
				assetTypeMap = new HashMap<Integer, HashMap<String, AssetType>>();
			}

			HashMap<String, AssetType> societyAssetType = assetTypeMap.get(assetType.getSocietyId());
			if (societyAssetType == null) {
				societyAssetType = new HashMap<String, AssetType>();
				assetTypeMap.put(assetType.getSocietyId(), societyAssetType);
			}

			societyAssetType.put(assetType.getAssetType(), assetType);
		}

		return result;
	}

	public static boolean delete(AssetType assetType) {
		boolean result = false;

		if (deleteStatement == null) {
			deleteStatement = SQLiteManager.getPreparedStatement("DELETE FROM " + Constants.Table.AssetType.TABLE_NAME
					+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
					+ Constants.Table.AssetType.FieldName.ASSET_TYPE + " = ?");
		}

		if (deleteStatement != null) {
			try {
				deleteStatement.clearParameters();
				deleteStatement.setInt(1, assetType.getSocietyId());
				deleteStatement.setString(2, assetType.getAssetType());
				result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
		}

		if (result) {

			LOG.info("Asset Type Deleted: " + assetType.getAssetType());

			if (assetTypeMap != null) {
				HashMap<String, AssetType> societyAssetType = assetTypeMap.get(assetType.societyId);
				if (societyAssetType != null) {
					societyAssetType.remove(assetType.assetType);
					
				}
				assetTypeMap.put(assetType.societyId, societyAssetType);
			}
		}

		return result;
	}

	public static AssetType create(int societyId) {
		AssetType assetType = new AssetType();
		assetType.societyId = societyId;
		return assetType;
	}

}

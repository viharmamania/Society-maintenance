package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class PropertyAsset {
	
	private final static Logger LOG = Logger.getLogger(PropertyAsset.class);

	private int societyId;

	private String assetType;

	private int assetNumber;

	private String assetDetails;

	private boolean isCancelled;

	private static HashMap<Integer, HashMap<String, PropertyAsset>> propertyAssetMap;

	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;

	private PropertyAsset() {

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

	public int getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(int assetNumber) {
		this.assetNumber = assetNumber;
	}

	public String getAssetDetails() {
		return assetDetails;
	}

	public void setAssetDetails(String assetDetails) {
		this.assetDetails = assetDetails;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

	public static PropertyAsset read(int societyId, String propertyAsset) {

		PropertyAsset asset = null;

		if (propertyAssetMap == null) {
			propertyAssetMap = new HashMap<Integer, HashMap<String, PropertyAsset>>();
		}

		HashMap<String, PropertyAsset> societyPropertyTypes = propertyAssetMap.get(societyId);

		if (societyPropertyTypes == null) {
			societyPropertyTypes = new HashMap<>();
			propertyAssetMap.put(societyId, societyPropertyTypes);
		}

		if (societyPropertyTypes != null) {
			asset = societyPropertyTypes.get(propertyAsset);
			if (asset == null) {
				asset = new PropertyAsset();
				if (readStatement == null) {
					readStatement = SQLiteManager
							.getPreparedStatement("SELECT * FROM " + Constants.Table.PropertyAsset.TABLE_NAME
									+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
									+ Constants.Table.PropertyAsset.FieldName.ASSET_DETAILS + " = ?"
									+ Constants.Table.PropertyAsset.FieldName.IS_CANCELLED + "= ?");
				}
				try {
					if (readStatement != null) {
						readStatement.setInt(1, societyId);
						readStatement.setString(2, propertyAsset);
						readStatement.setInt(3, 0);
						ResultSet resultSet = readStatement.executeQuery();
						if (resultSet != null && resultSet.next()) {
							asset = new PropertyAsset();
							asset.societyId = resultSet.getInt(Constants.Table.Society.FieldName.SOCIETY_ID);
							asset.assetNumber = resultSet.getInt(Constants.Table.PropertyAsset.FieldName.ASSET_NUMBER);
							asset.assetDetails = resultSet
									.getString(Constants.Table.PropertyAsset.FieldName.ASSET_DETAILS);
							int a = resultSet.getInt(Constants.Table.PropertyAsset.FieldName.IS_CANCELLED);
							if (a == 0)
								asset.isCancelled = false;
							else
								asset.isCancelled = true;
							societyPropertyTypes.put(propertyAsset, asset);
						}
					}
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}

			}
		}

		return asset;
	}

	public static boolean save(PropertyAsset propertyAsset, boolean insertEntry) {
		boolean result = false;
		if (propertyAsset != null && propertyAsset.getSocietyId() != -1
				&& propertyAsset.getAssetType().trim().length() != 0) {
			if (insertEntry) {
				if (insertStatement != null) {
					insertStatement = SQLiteManager.getPreparedStatement(
							"INSERT INTO " + Constants.Table.PropertyAsset.TABLE_NAME + " ( "
									+ Constants.Table.Society.FieldName.SOCIETY_ID + " , "
									+ Constants.Table.PropertyAsset.FieldName.ASSET_NUMBER + " , "
									+ Constants.Table.PropertyAsset.FieldName.ASSET_DETAILS + " , "
									+ Constants.Table.PropertyAsset.FieldName.ASSET_TYPE + " , "
									+" VALUES (?, ?, ?, ?)");
				}
				try {
					if (insertStatement != null) {
						insertStatement.setInt(1, propertyAsset.getSocietyId());
						insertStatement.setInt(2, propertyAsset.getAssetNumber());
						insertStatement.setString(3, propertyAsset.getAssetDetails());
						insertStatement.setString(4, propertyAsset.getAssetType());
						result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);
					}
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}
			} else {
				if (updateStatement != null) {
					updateStatement = SQLiteManager
							.getPreparedStatement("UPDATE " + Constants.Table.PropertyAsset.TABLE_NAME + "SET "
									+ Constants.Table.PropertyAsset.FieldName.ASSET_DETAILS + " =? "
									+ Constants.Table.PropertyAsset.FieldName.ASSET_NUMBER + " =? "
									+ Constants.Table.PropertyAsset.FieldName.ASSET_TYPE + " =? " + " WHERE "
									+ Constants.Table.Society.FieldName.SOCIETY_ID + " =? ");

				}
				if (updateStatement != null) {
					try {
						updateStatement.setString(1, propertyAsset.getAssetDetails());
						updateStatement.setInt(2, propertyAsset.getAssetNumber());
						updateStatement.setString(3, propertyAsset.getAssetType());
						updateStatement.setInt(4, propertyAsset.getSocietyId());
						result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}
			}
			
			//updating hashmap
			if(result){
				
				if(propertyAssetMap == null){
					propertyAssetMap = new HashMap<Integer, HashMap<String, PropertyAsset>>();
				}
				
				HashMap<String, PropertyAsset> assetMap = propertyAssetMap.get(propertyAsset.getSocietyId());
				if(assetMap == null){
					assetMap = new HashMap<>();
					propertyAssetMap.put(propertyAsset.getSocietyId(), assetMap);
				}
				assetMap.put(propertyAsset.getAssetDetails(), propertyAsset);
				
			}
		}
		return result;
	}
	
	public static boolean delete(PropertyAsset propertyAsset) {
		boolean result = false;
		
		if (deleteStatement == null) {
			deleteStatement = SQLiteManager.getPreparedStatement("DELETE " + Constants.Table.PropertyAsset.TABLE_NAME
					+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?"
					+ " AND " + Constants.Table.AssetType.FieldName.ASSET_TYPE + " = ?");
		}
		
		if (deleteStatement != null) {
			try {
				deleteStatement.setInt(1, propertyAsset.getSocietyId());
				deleteStatement.setString(2, propertyAsset.getAssetType());
				result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
		}
		
		return result;
	}

	public static PropertyAsset create(int societyId) {
		PropertyAsset propertyAsset = new PropertyAsset();
		propertyAsset.societyId = societyId;
		return propertyAsset;
	}
}

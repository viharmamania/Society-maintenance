package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class PropertyAsset {

	private final static Logger LOG = Logger.getLogger(PropertyAsset.class);

	private int societyId;

	private int propertyId;

	private String assetType;

	private int assetNumber;

	private String assetDetails;

	private boolean isCancelled;

	private static HashMap<Integer, HashMap<Integer, PropertyAsset>> propertyAssetMap;

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

	public static PropertyAsset read(int propertyId, int propertyAssetNumber) {

		PropertyAsset asset = null;

		if (propertyAssetMap == null) {
			propertyAssetMap = new HashMap<Integer, HashMap<Integer, PropertyAsset>>();
		}

		HashMap<Integer, PropertyAsset> societyPropertyTypes = propertyAssetMap.get(propertyId);

		if (societyPropertyTypes == null) {
			societyPropertyTypes = new HashMap<>();
			propertyAssetMap.put(propertyId, societyPropertyTypes);
		}

		if (societyPropertyTypes != null) {
			asset = societyPropertyTypes.get(propertyAssetNumber);
			if (asset == null) {
				asset = new PropertyAsset();
				if (readStatement == null) {
					readStatement = SQLiteManager
							.getPreparedStatement("SELECT * FROM " + Constants.Table.PropertyAsset.TABLE_NAME
									+ " WHERE " + Constants.Table.Property.FieldName.PROPERTY_ID + " = ?" + " AND "
									+ Constants.Table.PropertyAsset.FieldName.ASSET_NUMBER + " = ?");
				}
				try {
					if (readStatement != null) {
						readStatement.setInt(1, propertyId);
						readStatement.setInt(2, propertyAssetNumber);
						ResultSet resultSet = readStatement.executeQuery();
						if (resultSet != null && resultSet.next()) {
							asset = new PropertyAsset();
							asset.societyId = resultSet.getInt(Constants.Table.Society.FieldName.SOCIETY_ID);
							asset.propertyId = resultSet.getInt(Constants.Table.Property.FieldName.PROPERTY_ID);
							asset.assetNumber = resultSet.getInt(Constants.Table.PropertyAsset.FieldName.ASSET_NUMBER);
							asset.assetDetails = resultSet
									.getString(Constants.Table.PropertyAsset.FieldName.ASSET_DETAILS);
							asset.assetType = resultSet.getString(Constants.Table.AssetType.FieldName.ASSET_TYPE);
							int a = resultSet.getInt(Constants.Table.PropertyAsset.FieldName.IS_CANCELLED);
							if (a == 0)
								asset.isCancelled = false;
							else
								asset.isCancelled = true;
							societyPropertyTypes.put(propertyAssetNumber, asset);
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
		if (propertyAsset != null && propertyAsset.getSocietyId() != -1) {
			if (insertEntry) {
				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement("INSERT INTO "
							+ Constants.Table.PropertyAsset.TABLE_NAME + " ( "
							+ Constants.Table.Property.FieldName.PROPERTY_ID + " , "
							+ Constants.Table.Society.FieldName.SOCIETY_ID + " , "
							+ Constants.Table.PropertyAsset.FieldName.ASSET_NUMBER + " , "
							+ Constants.Table.PropertyAsset.FieldName.ASSET_DETAILS + " , "
							+ Constants.Table.PropertyAsset.FieldName.IS_CANCELLED + " , "
							+ Constants.Table.PropertyAsset.FieldName.ASSET_TYPE + " )" + " VALUES (?, ?, ?, ?, ?,?)");
				}
				try {
					if (insertStatement != null) {
						insertStatement.setInt(1, propertyAsset.propertyId);
						insertStatement.setInt(2, propertyAsset.getSocietyId());
						insertStatement.setInt(3, propertyAsset.getAssetNumber());
						insertStatement.setString(4, propertyAsset.getAssetDetails());
						insertStatement.setBoolean(5, propertyAsset.isCancelled());
						insertStatement.setString(6, propertyAsset.getAssetType());
						result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);
					}
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}
			} else {
				if (updateStatement == null) {
					updateStatement = SQLiteManager
							.getPreparedStatement("UPDATE " + Constants.Table.PropertyAsset.TABLE_NAME + " SET "
									+ Constants.Table.PropertyAsset.FieldName.ASSET_DETAILS + " =?, "
									+ Constants.Table.PropertyAsset.FieldName.ASSET_TYPE + " =?, "
									+ Constants.Table.PropertyAsset.FieldName.IS_CANCELLED + " =? " + " WHERE "
									+ Constants.Table.Property.FieldName.PROPERTY_ID + " =? " + " AND "
									+ Constants.Table.PropertyAsset.FieldName.ASSET_NUMBER + " =?");

				}
				if (updateStatement != null) {
					try {
						updateStatement.setString(1, propertyAsset.getAssetDetails());
						updateStatement.setString(2, propertyAsset.getAssetType());
						updateStatement.setBoolean(3, propertyAsset.isCancelled());
						updateStatement.setInt(4, propertyAsset.propertyId);
						updateStatement.setInt(5, propertyAsset.getAssetNumber());
						result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}
			}

			// updating hashmap
			if (result) {
				if (insertEntry)
					LOG.info("Property Asset Saved :" + propertyAsset.getAssetDetails());
				else
					LOG.info("Property Asset updated :" + propertyAsset.getAssetDetails());


				if (propertyAssetMap == null) {
					propertyAssetMap = new HashMap<Integer, HashMap<Integer, PropertyAsset>>();
				}

				HashMap<Integer, PropertyAsset> assetMap = propertyAssetMap.get(propertyAsset.getPropertyId());
				if (assetMap == null) {
					assetMap = new HashMap<>();
					propertyAssetMap.put(propertyAsset.getPropertyId(), assetMap);
				}
				assetMap.put(propertyAsset.getAssetNumber(), propertyAsset);

			}
		}
		return result;
	}

	public static boolean delete(PropertyAsset propertyAsset) {
		boolean result = false;

		if (deleteStatement == null) {
			deleteStatement = SQLiteManager
					.getPreparedStatement("DELETE FROM " + Constants.Table.PropertyAsset.TABLE_NAME + " WHERE "
							+ Constants.Table.Property.FieldName.PROPERTY_ID + " = ?" + " AND "
							+ Constants.Table.PropertyAsset.FieldName.ASSET_NUMBER + " = ?");
		}

		if (deleteStatement != null) {
			try {
				deleteStatement.setInt(1, propertyAsset.propertyId);
				deleteStatement.setInt(2, propertyAsset.getAssetNumber());
				result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
			} catch (SQLException e) {
				LOG.error(e.getMessage());
			}
		}
		
		if (result && propertyAssetMap != null) {
			LOG.info("Property Asset Deleted :" + propertyAsset.getAssetDetails());
			/*HashMap<Integer, PropertyAsset> hashMap = propertyAssetMap.get(propertyAsset.propertyId);
			Set<Entry<Integer, PropertyAsset>> entrySet = hashMap.entrySet();
			Integer removeEntry = null;
			for (Entry<Integer, PropertyAsset> entry : entrySet) {
				if (entry.getValue().getAssetDetails().equals(propertyAsset.getAssetDetails())) {
					removeEntry = entry.getKey();
					break;
				}
			}
			hashMap.remove(removeEntry);
			propertyAssetMap.put(propertyAsset.propertyId, hashMap);*/
		}

		if (result) {
			if (propertyAssetMap != null) {
				HashMap<Integer, PropertyAsset> assetMap = propertyAssetMap.get(propertyAsset.propertyId);
				if (assetMap != null) {
					assetMap.remove(propertyAsset.getAssetNumber());
				}
			}
		}

		return result;
	}

	public static PropertyAsset create(int societyId) {
		PropertyAsset propertyAsset = new PropertyAsset();
		propertyAsset.societyId = societyId;
		return propertyAsset;
	}

	public int getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(int propertyId) {
		this.propertyId = propertyId;
	}

	public static ArrayList<PropertyAsset> getAllPropertyAssets(int propertyId) {
		ArrayList<PropertyAsset> allPropertyAssets = new ArrayList<>();
		String query = "SELECT * FROM " + Constants.Table.PropertyAsset.TABLE_NAME + " WHERE "
				+ Constants.Table.Property.FieldName.PROPERTY_ID + " = " + propertyId;
		try {
			ResultSet resultSet = SQLiteManager.executeQuery(query);
			if (resultSet != null) {
				while (resultSet.next()) {
					PropertyAsset asset = PropertyAsset.read(propertyId,
							resultSet.getInt(Constants.Table.PropertyAsset.FieldName.ASSET_NUMBER));
					allPropertyAssets.add(asset);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			LOG.error(e.getMessage());
		}
		return allPropertyAssets;
	}

}

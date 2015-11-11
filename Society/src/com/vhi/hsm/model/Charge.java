package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class Charge {
	
	private int societyId;
	
	private int chargeId;
	
	private String description;

	private double amount;

	private boolean tempCharges;

	private boolean isCancelled;
	
	private ArrayList<Integer> assignedPropertyGroup;
	private ArrayList<Integer> assignedPropertyType;
	private ArrayList<Integer> assignedProperty;
	
	private static HashMap<Integer, HashMap<Integer, Charge>> chargeMap;
	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;
	
	private Charge() {
		assignedProperty = new ArrayList<Integer>();
		assignedPropertyGroup = new ArrayList<Integer>();
		assignedPropertyType = new ArrayList<Integer>();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public boolean isTempCharges() {
		return tempCharges;
	}

	public void setTempCharges(boolean tempCharges) {
		this.tempCharges = tempCharges;
	}

	public boolean isCancelled() {
		return isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	
	public ArrayList<Integer> getAssignedPropertyGroup() {
		return assignedPropertyGroup;
	}

	public void setAssignedPropertyGroup(ArrayList<Integer> assignedPropertyGroup) {
		this.assignedPropertyGroup = assignedPropertyGroup;
	}

	public ArrayList<Integer> getAssignedPropertyType() {
		return assignedPropertyType;
	}

	public void setAssignedPropertyType(ArrayList<Integer> assignedPropertyType) {
		this.assignedPropertyType = assignedPropertyType;
	}

	public ArrayList<Integer> getAssignedProperty() {
		return assignedProperty;
	}

	public void setAssignedProperty(ArrayList<Integer> assignedProperty) {
		this.assignedProperty = assignedProperty;
	}
	
	public static Charge read(int societyId, int chargeId) {
		
		Charge charge = null;
		HashMap<Integer, Charge> societyCharges;
		Integer socIdInteger, chargeIdInteger;
		
		if (chargeMap == null) {
			chargeMap = new HashMap<Integer, HashMap<Integer, Charge>>();
		}
		
		socIdInteger = new Integer(societyId);
		societyCharges = chargeMap.get(socIdInteger);
		if (societyCharges == null) {
			societyCharges = new HashMap<Integer, Charge>();
		}
		
		chargeIdInteger = new Integer(chargeId);
		charge = societyCharges.get(chargeIdInteger);
		if (charge == null) {
			
			if (readStatement == null) {
				readStatement = SQLiteManager.getPreparedStatement("SELECT * FORM " + Constants.Table.Charge.TABLE_NAME
						+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?"
						+ " AND " + Constants.Table.Charge.FieldName.CHARGE_ID + " = ?");
			}
			
			if (readStatement != null) {
				try {
					readStatement.setInt(1, societyId);
					readStatement.setInt(2, chargeId);
					ResultSet resultSet = readStatement.executeQuery();
					if (resultSet != null && resultSet.first()) {
						charge = new Charge();
						charge.societyId = resultSet.getInt(Constants.Table.Society.FieldName.SOCIETY_ID);
						charge.chargeId = resultSet.getInt(Constants.Table.Charge.FieldName.CHARGE_ID);
						charge.amount = resultSet.getDouble(Constants.Table.Charge.FieldName.AMOUNT);
						charge.description = resultSet.getString(Constants.Table.Charge.FieldName.DESCRIPTION);
						charge.isCancelled = resultSet.getBoolean(Constants.Table.Charge.FieldName.IS_CANCELLED);
						charge.tempCharges = resultSet.getBoolean(Constants.Table.Charge.FieldName.TEMP_CHARGE); 
						societyCharges.put(chargeIdInteger, charge);
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}
		
		return charge;
	}
	
	public static boolean save(Charge charge, boolean insertEntry) {
		boolean result = false;
		
		if (charge != null) {
			
			if (insertEntry) {
				
				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement("INSERT INTO " + Constants.Table.Charge.TABLE_NAME
							+ "("
							+ Constants.Table.Society.FieldName.SOCIETY_ID + ", "
							+ Constants.Table.Charge.FieldName.CHARGE_ID + ", "
							+ Constants.Table.Charge.FieldName.DESCRIPTION + ", "
							+ Constants.Table.Charge.FieldName.AMOUNT + ", "
							+ Constants.Table.Charge.FieldName.TEMP_CHARGE + ", "
							+ Constants.Table.Charge.FieldName.IS_CANCELLED
							+ ")"
							+ " VALUES (?, ?, ?, ?, ?, ?) ");
				}
				
				if (insertStatement != null) {
					try {
						insertStatement.setInt(1, charge.societyId);
						insertStatement.setInt(2, charge.chargeId);
						insertStatement.setString(3, charge.description);
						insertStatement.setDouble(4, charge.amount);
						insertStatement.setBoolean(5, charge.tempCharges);
						insertStatement.setBoolean(6, charge.isCancelled);
						result = insertStatement.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
			} else {
				
				if (updateStatement == null) {
					updateStatement = SQLiteManager.getPreparedStatement("UPDATE " + Constants.Table.Charge.TABLE_NAME
							+ " SET "
							+ Constants.Table.Charge.FieldName.DESCRIPTION + " = ?, "
							+ Constants.Table.Charge.FieldName.AMOUNT + " = ?, "
							+ Constants.Table.Charge.FieldName.TEMP_CHARGE + " = ?, "
							+ Constants.Table.Charge.FieldName.IS_CANCELLED + " = ?"
							+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?"
							+ " AND " + Constants.Table.Charge.FieldName.CHARGE_ID + " = ?");
				}
				
				if (updateStatement != null) {
					try {
						updateStatement.setString(1, charge.description);
						updateStatement.setDouble(2,  charge.amount);
						updateStatement.setBoolean(3, charge.tempCharges);
						updateStatement.setBoolean(4, charge.isCancelled);
						updateStatement.setInt(5, charge.societyId);
						updateStatement.setInt(6, charge.chargeId);
						result = updateStatement.execute();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
			}
			
		}
		
		if (result) {
			
			if (chargeMap == null) {
				chargeMap = new HashMap<Integer, HashMap<Integer, Charge>>();
			}
			
			HashMap<Integer, Charge> societyCharges = chargeMap.get(charge.societyId);
			if (societyCharges == null) {
				societyCharges = new HashMap<Integer, Charge>();
			}
			
			societyCharges.put(charge.chargeId, charge);
			
		}
		
		return result;
	}
	
	public static boolean delete(Charge charge) {
		
		boolean result = false;
		
		if (deleteStatement == null) {
			deleteStatement = SQLiteManager.getPreparedStatement("DELETE FROM " + Constants.Table.Charge.TABLE_NAME
					+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = ?"
					+ " AND " + Constants.Table.Charge.FieldName.CHARGE_ID + " = ?");
		}
		
		if (deleteStatement != null) {
			try {
				deleteStatement.setInt(1, charge.societyId);
				deleteStatement.setInt(2, charge.chargeId);
				result = deleteStatement.execute();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		if (result) {
			if (chargeMap != null) {
				HashMap<Integer, Charge> societyCharges = chargeMap.get(charge.societyId);
				if (societyCharges != null) {
					societyCharges.remove(charge.chargeId);
				}
			}
		}
		
		return result;
	}
	
	public static Charge create(int societyId) {
		Charge charge = new Charge();
		charge.societyId = societyId;
		return charge;
	}

}

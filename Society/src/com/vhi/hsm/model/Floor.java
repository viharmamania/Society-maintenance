package com.vhi.hsm.model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class Floor {

	private final static Logger LOG = Logger.getLogger(Floor.class);
	private int societyId;

	private int wingId;

	private int floorNumber;

	private int floorPlanId;
	
	private static HashMap<Integer, HashMap<Integer, HashMap<Integer, Floor>>> floorMap;

	private static PreparedStatement readStatement, insertStatement, updateStatement, deleteStatement;

	private Floor() {

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

	public int getFloor_number() {
		return floorNumber;
	}

	public void setFloor_number(int floor_number) {
		this.floorNumber = floor_number;
	}

	public int getFloorPlanId() {
		return floorPlanId;
	}

	public void setFloorPlanId(int floorPlanId) {
		this.floorPlanId = floorPlanId;
	}
	
	public static Floor read(int societyId, int wingId, int floorNumber) {
		Floor floor = null;

		if (floorMap == null) {
			floorMap = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Floor>>>();
		}

		HashMap<Integer, HashMap<Integer, Floor>> societyWingFloor = floorMap.get(societyId);
		if (societyWingFloor == null) {
			societyWingFloor = new HashMap<Integer, HashMap<Integer, Floor>>();
			floorMap.put(societyId, societyWingFloor);
		}

		HashMap<Integer, Floor> wingFloor = societyWingFloor.get(wingId);
		if (wingFloor == null) {
			wingFloor = new HashMap<Integer, Floor>();
			societyWingFloor.put(wingId, wingFloor);
		}

		floor = wingFloor.get(floorNumber);
		if (floor == null) {

			if (readStatement == null) {
				readStatement = SQLiteManager
						.getPreparedStatement("SELECT * FROM " + Constants.Table.Floor.TABLE_NAME + " WHERE "
								+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
								+ Constants.Table.Wing.FieldName.WING_ID + " = ?" + " AND "
								+ Constants.Table.Floor.FieldName.FLOOR_NUMBER + " = ?");
			}

			if (readStatement != null) {
				try {
					readStatement.clearParameters();
					readStatement.setInt(1, societyId);
					readStatement.setInt(2, wingId);
					readStatement.setInt(3, floorNumber);
					ResultSet resultSet = readStatement.executeQuery();
					if (resultSet != null && resultSet.next()) {
						floor = new Floor();
						floor.societyId = societyId;
						floor.wingId = wingId;
						floor.floorNumber = floorNumber;
						floor.floorPlanId = resultSet.getInt(Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID);
						wingFloor.put(floorNumber, floor);
					}
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}
			}

		}

		return floor;
	}

	public static boolean save(Floor floor, boolean insertEntry) {
		
		boolean result = false;

		if (floor != null) {

			if (insertEntry) {

				if (insertStatement == null) {
					insertStatement = SQLiteManager.getPreparedStatement("INSERT INTO "
							+ Constants.Table.Floor.TABLE_NAME + " ( "
							+ Constants.Table.Society.FieldName.SOCIETY_ID + " , "
							+ Constants.Table.Wing.FieldName.WING_ID + " , "
							+ Constants.Table.Floor.FieldName.FLOOR_NUMBER + " , "
							+ Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " ) " + " VALUES (?, ?, ?, ?)");
				}

				if (insertStatement != null) {
					try {
						insertStatement.clearParameters();
						insertStatement.setInt(1, floor.societyId);
						insertStatement.setInt(2, floor.wingId);
						insertStatement.setInt(3, floor.floorNumber);
						insertStatement.setInt(4, floor.floorPlanId);
						result = SQLiteManager.executePrepStatementAndGetResult(insertStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}

			} else {

				if (updateStatement == null) {
					updateStatement = SQLiteManager
							.getPreparedStatement("UPDATE " + Constants.Table.Floor.TABLE_NAME + " SET "
									+ Constants.Table.FloorPlan.FieldName.FLOOR_PLAN_ID + " = ? " + " WHERE "
									+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ?" + " AND "
									+ Constants.Table.Wing.FieldName.WING_ID + " = ?" + " AND "
									+ Constants.Table.Floor.FieldName.FLOOR_NUMBER + " = ? ");
				}

				if (updateStatement != null) {
					try {
						updateStatement.clearParameters();
						updateStatement.setInt(1, floor.floorPlanId);
						updateStatement.setInt(3, floor.societyId);
						updateStatement.setInt(4, floor.wingId);
						updateStatement.setInt(5, floor.floorNumber);
						result = SQLiteManager.executePrepStatementAndGetResult(updateStatement);
					} catch (SQLException e) {
						LOG.error(e.getMessage());
					}
				}

			}
		}

		if (result) {
			
			if (floorMap == null) {
				floorMap = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Floor>>>();
			}

			HashMap<Integer, HashMap<Integer, Floor>> societyWing = floorMap.get(floor.societyId);
			if (societyWing == null) {
				societyWing = new HashMap<Integer, HashMap<Integer, Floor>>();
				floorMap.put(floor.societyId, societyWing);
			}

			HashMap<Integer, Floor> wingFloor = societyWing.get(floor.wingId);
			if (wingFloor == null) {
				wingFloor = new HashMap<Integer, Floor>();
				societyWing.put(floor.wingId, wingFloor);
			}
			
			wingFloor.put(floor.floorNumber, floor);
			
		}

		return result;
	}

	public static boolean delete(Floor floor) {
		boolean result = false;

		if (floor != null) {

			if (deleteStatement == null) {
				deleteStatement = SQLiteManager
						.getPreparedStatement("DELETE FROM " + Constants.Table.Floor.TABLE_NAME + " WHERE "
								+ Constants.Table.Society.FieldName.SOCIETY_ID + " = ? " + " AND "
								+ Constants.Table.Wing.FieldName.WING_ID + " = ?" + " AND "
								+ Constants.Table.Floor.FieldName.FLOOR_NUMBER + " = ?");
			}

			if (deleteStatement != null) {
				try {
					deleteStatement.clearParameters();
					deleteStatement.setInt(1, floor.societyId);
					deleteStatement.setInt(2, floor.wingId);
					deleteStatement.setInt(3, floor.floorNumber);
					result = SQLiteManager.executePrepStatementAndGetResult(deleteStatement);
				} catch (SQLException e) {
					LOG.error(e.getMessage());
				}
			}

		}

		if (result && floorMap != null) {
			HashMap<Integer, HashMap<Integer, Floor>> societyFloor = floorMap
					.get(floor.societyId);
			if (societyFloor != null) {
				HashMap<Integer, Floor> wingFloor = societyFloor
						.get(floor.wingId);
				if (wingFloor != null) {
					wingFloor.remove(floor.floorNumber);
				}
			}
		}

		return result;
	}

	public static Floor create(int societyId, int wingId) {
		Floor floor = new Floor();
		floor.societyId = societyId;
		floor.wingId = wingId;
		return floor;
	}

}

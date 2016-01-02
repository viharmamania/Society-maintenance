package com.vhi.hsm.controller.manager;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.Bill;
import com.vhi.hsm.model.BillCharge;
import com.vhi.hsm.model.Charge;
import com.vhi.hsm.model.Fine;
import com.vhi.hsm.model.Property;
import com.vhi.hsm.utils.Constants;

/**
 * 
 * @author Vihar Mamania
 *
 */
public class BillManager {

	private static String readProperties = "select * from " + Constants.Table.Property.TABLE_NAME + " where "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + "=?";

	/**
	 * Generates all the bills for society
	 * 
	 * @param societyId
	 *            the id of society for which bills need to be generated
	 * @return
	 */
	public static synchronized List<Bill> generateBill(int societyId) {
		List<Bill> societyBills = new ArrayList<>();
		Set<Property> properties = new HashSet<>();
		boolean commit = true;

		if (SQLiteManager.startTransaction()) {
			try {

				PreparedStatement readStatement = SQLiteManager.getPreparedStatement(readProperties);
				readStatement.clearParameters();
				readStatement.setInt(1, societyId);

				ResultSet res = readStatement.executeQuery();

				// adding the properties result in Property Hash-map
				Property.addProperties(res);
				
				readStatement.clearParameters();
				readStatement.setInt(1, societyId);
				ResultSet result = readStatement.executeQuery();

				// fetching the List of properties for this societyId
				if (result != null && result.next()) {
					do {
						properties.add(Property.read(result.getInt(Constants.Table.Property.FieldName.PROPERTY_ID)));
						result.next();
					} while (!result.isAfterLast());
				}
				// generating individual bills and adding to list of bills
				for (Property property : properties) {
					societyBills.add(generatePropertySpecificBill(property));
				}

			} catch (SQLException e) {
				e.printStackTrace();
				commit = false;
			}
			
			if (!commit) {
				societyBills.clear();
			}
			
			SQLiteManager.endTransaction(commit, !commit);
			
		}

		return societyBills;

	}

	/**
	 * Generates bill for individual property
	 * 
	 * @param property
	 * @return
	 */
	private static Bill generatePropertySpecificBill(Property property) {

		double billAmount = 0.0;
		Bill bill = Bill.create();
		ArrayList<Integer> chargeIds = new ArrayList<>();

		// get All charges for this particular property
		chargeIds.addAll(getChargeIds(property));

		// adding fine charge (if any)
		if (property.getNetPayable() > 0) {

			double fineAmount = Fine.getFineAmount(property.getSocietyId(), property.getNetPayable());
			if (fineAmount > 0.0) {

				/*
				 * Charge fineCharge = Charge.create(property.getSocietyId());
				 * fineCharge.setAmount(fineAmount);
				 * fineCharge.setTempCharges(true);
				 * fineCharge.setDescription("Fine"); Charge.save(fineCharge,
				 * true);
				 */
				Charge fineCharge = Charge.getFineCharge();
				fineCharge.setAmount(fineAmount);
				// fineCharge

				chargeIds.add(fineCharge.getChargeId());
			}
		}

		// calculate actual amount by adding charges for all chargeIds
		for (Integer chargeId : chargeIds) {
			Charge read = Charge.read(property.getSocietyId(), chargeId);
			billAmount += read.getAmount();
		}

		long currentTime = System.currentTimeMillis();
		bill.setPropertyId(property.getPropertyId());
		bill.setSocietyId(property.getSocietyId());
		bill.setLastModified(new Timestamp(currentTime));
		bill.setBillDate(new Date(currentTime));
		bill.setCancelled(false);
		bill.setAssignedCharges(chargeIds);
		bill.setAmount(billAmount);

		//if property has available balance to settle this bill then mark it appropriately
		if (property.getNetPayable() < 0 && Math.abs(property.getNetPayable()) >= billAmount) {
			bill.setPaymentId(property.getLatestPaymentId());
		}

		// saving bill in DB
		Bill.save(bill, false);

		// saving individual bill charges in DB
		for (int i = 0; i < chargeIds.size(); i++) {
			BillCharge billCharge = BillCharge.create(bill.getBillId(), chargeIds.get(i));
			Charge charge = Charge.read(property.getSocietyId(), chargeIds.get(i));
			billCharge.setAmount(charge.getAmount());
			BillCharge.save(billCharge, true);
		}

		// updating this properties net payable
		property.setNetPayable(property.getNetPayable() + billAmount);
		Property.save(property, false);

		return bill;
	}

	/**
	 * returns Set of All Applicable charge Id's for the said property
	 * 
	 * @param property
	 * @return
	 */
	private static Collection<? extends Integer> getChargeIds(Property property) {

		Set<Integer> chargeIds = new HashSet<>();
		StringBuilder query = new StringBuilder();

		// fetching charges for this properties group type
		query.append("select " + Constants.Table.Charge.FieldName.CHARGE_ID + " from "
				+ Constants.Table.ChargeToPropertyGroup.TABLE_NAME + " where "
				+ Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP + " = ( select "
				+ Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP + " from "
				+ Constants.Table.FloorPlanDesign.TABLE_NAME + " where " + Constants.Table.Society.FieldName.SOCIETY_ID
				+ " =? " + " and " + Constants.Table.FloorPlanDesign.FieldName.PROPERTY_NUMBER + " =? )");

		query.append(" union ");

		// fetching charges for this property type
		query.append("select " + Constants.Table.Charge.FieldName.CHARGE_ID + " from "
				+ Constants.Table.ChargeToPropertyType.TABLE_NAME + " where "
				+ Constants.Table.PropertyType.FieldName.PROPERTY_TYPE + " = ( select "
				+ Constants.Table.PropertyType.FieldName.PROPERTY_TYPE + " from "
				+ Constants.Table.FloorPlanDesign.TABLE_NAME + " where " + Constants.Table.Society.FieldName.SOCIETY_ID
				+ " =? " + " and " + Constants.Table.FloorPlanDesign.FieldName.PROPERTY_NUMBER + " =? )");

		query.append(" union ");

		// fetching charges for this specific property
		query.append("select " + Constants.Table.Charge.FieldName.CHARGE_ID + " from "
				+ Constants.Table.ChargeToProperty.TABLE_NAME + " where "
				+ Constants.Table.Property.FieldName.PROPERTY_ID + " =? ");

		query.append(" union ");

		// fetching charges which are applicable by-default to every property
		query.append("select " + Constants.Table.Charge.FieldName.CHARGE_ID + " from "
				+ Constants.Table.Charge.TABLE_NAME + " where " + Constants.Table.Charge.FieldName.IS_DEFAULT + "=1");

		query.append(" union ");

		// fetching property asset charges
		query.append("select " + Constants.Table.Charge.FieldName.CHARGE_ID + " from "
				+ Constants.Table.AssetType.TABLE_NAME + " where " + Constants.Table.AssetType.FieldName.ASSET_TYPE
				+ " = ( select " + Constants.Table.PropertyAsset.FieldName.ASSET_TYPE + " from "
				+ Constants.Table.PropertyAsset.TABLE_NAME + " where " + Constants.Table.Property.FieldName.PROPERTY_ID
				+ " =?)");

		PreparedStatement fetchChargesStmt = SQLiteManager.getPreparedStatement(query.toString());

		try {
			fetchChargesStmt.setInt(1, SystemManager.society.getSocietyId());
			fetchChargesStmt.setString(2, property.getPropertyName());
			fetchChargesStmt.setInt(3, SystemManager.society.getSocietyId());
			fetchChargesStmt.setString(4, property.getPropertyName());
			fetchChargesStmt.setInt(5, property.getPropertyId());
			ResultSet result = fetchChargesStmt.executeQuery();
			if (result != null) {
				do {
					result.next();
					chargeIds.add(result.getInt(Constants.Table.Charge.FieldName.CHARGE_ID));
				} while (!result.isAfterLast());
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return chargeIds;
	}
}

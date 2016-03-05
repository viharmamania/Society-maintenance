package com.vhi.hsm.controller.manager;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.sql.Savepoint;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.AssetType;
import com.vhi.hsm.model.Bill;
import com.vhi.hsm.model.BillCharge;
import com.vhi.hsm.model.Charge;
import com.vhi.hsm.model.ChargeToProperty;
import com.vhi.hsm.model.ChargeToPropertyGroup;
import com.vhi.hsm.model.ChargeToPropertyType;
import com.vhi.hsm.model.Fine;
import com.vhi.hsm.model.Floor;
import com.vhi.hsm.model.FloorPlanDesign;
import com.vhi.hsm.model.Property;
import com.vhi.hsm.utils.Constants;

/**
 * 
 * @author Vihar Mamania
 *
 */
public class BillManager {

	private final static Logger LOG = Logger.getLogger(BillManager.class);

	private static String readProperties = "select * from " + Constants.Table.Property.TABLE_NAME + " where "
			+ Constants.Table.Society.FieldName.SOCIETY_ID + "=?";

	private static ArrayList<ChargeToProperty> chargesToProperty;
	private static ArrayList<ChargeToPropertyGroup> chargesToPropertyGroup;
	private static ArrayList<ChargeToPropertyType> chargesToPropertyType;
	private static ArrayList<Charge> defaultCharges;

	static {
		chargesToProperty = new ArrayList<>();
		chargesToPropertyGroup = new ArrayList<>();
		chargesToPropertyType = new ArrayList<>();
		defaultCharges = new ArrayList<>();

		try {
			ResultSet resultSet = SQLiteManager.executeQuery("SELECT * FROM "
					+ Constants.Table.ChargeToProperty.TABLE_NAME + " WHERE "
					+ Constants.Table.Society.FieldName.SOCIETY_ID + " = " + SystemManager.society.getSocietyId());

			while (resultSet.next()) {
				ChargeToProperty property = new ChargeToProperty();
				property.setSocietyId(SystemManager.society.getSocietyId());
				property.setChargeId(resultSet.getInt(Constants.Table.Charge.FieldName.CHARGE_ID));
				property.setPropertyId(resultSet.getInt(Constants.Table.Property.FieldName.PROPERTY_ID));
				chargesToProperty.add(property);
			}

			resultSet = SQLiteManager.executeQuery("SELECT * FROM " + Constants.Table.ChargeToPropertyGroup.TABLE_NAME
					+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = "
					+ SystemManager.society.getSocietyId());
			while (resultSet.next()) {
				ChargeToPropertyGroup propGroup = new ChargeToPropertyGroup();
				propGroup.setSocietyId(SystemManager.society.getSocietyId());
				propGroup.setChargeId(resultSet.getInt(Constants.Table.Charge.FieldName.CHARGE_ID));
				propGroup.setPropertyGroup(resultSet.getString(Constants.Table.PropertyGroup.FieldName.PROPERTY_GROUP));
				chargesToPropertyGroup.add(propGroup);
			}

			resultSet = SQLiteManager.executeQuery("SELECT * FROM " + Constants.Table.ChargeToPropertyType.TABLE_NAME
					+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = "
					+ SystemManager.society.getSocietyId());
			while (resultSet.next()) {
				ChargeToPropertyType propType = new ChargeToPropertyType();
				propType.setSocietyId(SystemManager.society.getSocietyId());
				propType.setChargeId(resultSet.getInt(Constants.Table.Charge.FieldName.CHARGE_ID));
				propType.setPropertyType(resultSet.getString(Constants.Table.PropertyType.FieldName.PROPERTY_TYPE));
				chargesToPropertyType.add(propType);
			}

			resultSet = SQLiteManager.executeQuery("SELECT " + Constants.Table.Charge.FieldName.CHARGE_ID + " FROM "
					+ Constants.Table.Charge.TABLE_NAME + " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID
					+ " = " + SystemManager.society.getSocietyId() + " AND "
					+ Constants.Table.Charge.FieldName.IS_DEFAULT + " = 1 " + " AND "
					+ Constants.Table.Charge.FieldName.IS_CANCELLED + " = 0 ");
			while (resultSet.next()) {
				Charge charge = Charge.read(SystemManager.society.getSocietyId(),
						resultSet.getInt(Constants.Table.Charge.FieldName.CHARGE_ID));
				defaultCharges.add(charge);
			}

		} catch (SQLException e) {
			LOG.error(e.toString());
		}

	}

	/**
	 * Generates all the bills for society
	 * 
	 * @param societyId
	 *            the id of society for which bills need to be generated
	 * @param isPreview
	 * @return
	 */
	public static synchronized List<Bill> generateBill(int societyId, boolean isPreview, List<Integer> tempChargeIds) {
		List<Bill> societyBills = new ArrayList<>();
		Set<Property> properties = new LinkedHashSet<>();
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
			while (result.next()) {
				properties.add(Property.read(result.getInt(Constants.Table.Property.FieldName.PROPERTY_ID)));
			}

			// generating individual bills and adding to list of bills
			for (Property property : properties) {
				societyBills.add(generatePropertySpecificBill(property, isPreview, tempChargeIds));
			}

			BillCharge.saveAll();

		} catch (SQLException e) {
			societyBills.clear();
			LOG.error(e.toString());
		}
		return societyBills;

	}

	/**
	 * Generates bill for individual property
	 * 
	 * @param property
	 * @param isPreview
	 * @param tempChargeIds
	 * @return
	 */
	private static Bill generatePropertySpecificBill(Property property, boolean isPreview, List<Integer> tempChargeIds)
			throws SQLException {

		double billAmount = 0.0;
		Bill bill = Bill.create();
		ArrayList<Integer> chargeIds = new ArrayList<>();

		// get All charges for this particular property
		chargeIds.addAll(getChargeIds(property));

		// add all temporary charges for the month
		for (Integer tempchargeId : tempChargeIds) {
			if (!chargeIds.contains(tempchargeId)) {
				chargeIds.add(tempchargeId);
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

		if (!isPreview) {

			// saving bill and property in DB
			Bill.save(bill, true);
			Property.save(property, false);

			Integer chargeIdArray[] = new Integer[chargeIds.size()];
			chargeIds.toArray(chargeIdArray);
			Arrays.sort(chargeIdArray, new Comparator<Integer>() {
				public int compare(Integer o1, Integer o2) {
					return o1.compareTo(o2);
				}
			});

			for (int i = 0; i < chargeIdArray.length; i++) {
				BillCharge billCharge = BillCharge.create(bill.getBillId(), chargeIdArray[i]);
				Charge charge = Charge.read(property.getSocietyId(), chargeIdArray[i]);
				while (true) {
					billCharge.setAmount(billCharge.getAmount() + charge.getAmount());
					if ((i + 1) < chargeIdArray.length && chargeIdArray[i + 1].equals(chargeIdArray[i])) {
						i++;
					} else {
						break;
					}
				}
				BillCharge.save(billCharge, true);
			}

		}

		// Calculate Fine
		Charge finecharge = Charge.read(SystemManager.society.getSocietyId(), Constants.Charge.FINE_CHARGE_ID);
		double fineAmount = Fine.getFineAmount(SystemManager.society.getSocietyId(), property.getNetPayable());
		BillCharge billCharge = BillCharge.create(bill.getBillId(), finecharge.getChargeId());
		billCharge.setAmount(fineAmount);

		// manipulate previous balances
		Charge previousCharge = Charge.read(SystemManager.society.getSocietyId(), Constants.Charge.PREVIOUS_CHARGE_ID);
		BillCharge billCharge2 = BillCharge.create(bill.getBillId(), previousCharge.getChargeId());
		billCharge2.setAmount(property.getNetPayable());

		if (!isPreview) {

			BillCharge.save(billCharge, true);
			BillCharge.save(billCharge2, true);
		}
		ArrayList<Integer> assignedCharges = bill.getAssignedCharges();
		assignedCharges.add(finecharge.getChargeId());
		assignedCharges.add(previousCharge.getChargeId());
		bill.setAssignedCharges(assignedCharges);
		bill.setAmount(Math.round(bill.getAmount() + fineAmount + property.getNetPayable()));

		if (!isPreview) {
			// updating this properties net payable
			Bill.save(bill, false);
			property.setNetPayable(Math.round(bill.getAmount()));
			Property.save(property, false);
		}
		LOG.debug(bill.getAssignedCharges());
		LOG.debug(bill.getAmount());

		return bill;
	}

	/**
	 * returns Set of All Applicable charge Id's for the said property
	 * 
	 * @param property
	 * @return
	 */
	private static Collection<? extends Integer> getChargeIds(Property property) {

		Collection<Integer> chargeIds = new ArrayList<>();
		// StringBuilder query = new StringBuilder();
		String query;

		Floor floor = Floor.read(property.getSocietyId(), property.getWingId(), property.getFloorNumber());
		FloorPlanDesign floorPlanDesign = FloorPlanDesign.read(floor.getSocietyId(), floor.getFloorPlanId(),
				property.getPropertyNumber());

		try {

			// Get Default Charges
			for (Charge charge : defaultCharges) {
				chargeIds.add(charge.getChargeId());
			}

			// Get Property Specific Charges
			for (ChargeToProperty charge : chargesToProperty) {
				if (charge.getPropertyId() == property.getPropertyId() && !chargeIds.contains(charge.getChargeId())) {
					chargeIds.add(charge.getChargeId());
				}
			}

			// Get Property Group Specific Charges
			for (ChargeToPropertyGroup charge : chargesToPropertyGroup) {
				if (charge.getPropertyGroup().equals(floorPlanDesign.getPropertyGroup())
						&& !chargeIds.contains(charge.getChargeId())) {
					chargeIds.add(charge.getChargeId());
				}
			}

			// Get Property Type Specific Charges
			for (ChargeToPropertyType charge : chargesToPropertyType) {
				if (charge.getPropertyType().equals(floorPlanDesign.getPropertyType())
						&& !chargeIds.contains(charge.getChargeId())) {
					chargeIds.add(charge.getChargeId());
				}
			}

			// Get Property Asset Charges
			query = "SELECT * FROM " + Constants.Table.PropertyAsset.TABLE_NAME + " WHERE "
					+ Constants.Table.Property.FieldName.PROPERTY_ID + " = " + property.getPropertyId();
			ResultSet resultSet = SQLiteManager.executeQuery(query);
			while (resultSet.next()) {
				AssetType assetType = AssetType.read(property.getSocietyId(),
						resultSet.getString(Constants.Table.AssetType.FieldName.ASSET_TYPE));
				if (assetType != null) {
					chargeIds.add(assetType.getChargeId());
				}
			}

		} catch (SQLException e) {
			LOG.error(e.toString());
		}

		return chargeIds;
	}

	public static ArrayList<Bill> getUnpaidBills(Property property) {
		ArrayList<Bill> unpaidBills = new ArrayList<Bill>();

		String query = "SELECT " + Constants.Table.Bill.FieldName.BILL_ID + " FROM " + Constants.Table.Bill.TABLE_NAME
				+ " WHERE " + Constants.Table.Society.FieldName.SOCIETY_ID + " = " + property.getSocietyId() + " AND "
				+ Constants.Table.Property.FieldName.PROPERTY_ID + " = " + property.getPropertyId() + " AND "
				+ Constants.Table.Payment.FieldName.PAYMENT_ID + " = 0";

		try {
			ResultSet resultSet = SQLiteManager.executeQuery(query);
			while (resultSet.next()) {
				Bill bill = Bill.read(resultSet.getInt(Constants.Table.Bill.FieldName.BILL_ID));
				unpaidBills.add(bill);
			}
		} catch (SQLException e) {
			LOG.error(e.toString());
		}

		return unpaidBills;
	}

	public static double getLateFineAmount(Bill bill, java.util.Date paymentDate) {

		double fine = 0;
		int diffInDays = ((int) ((long) (paymentDate.getTime() - bill.getBillDate().getTime())
				/ (1000 * 60 * 60 * 24)));

		if (diffInDays <= SystemManager.society.getPaymentDueDate()) {
			return 0;
		}

		diffInDays -= SystemManager.society.getPaymentDueDate();

		int diffInMonths = (int) Math.ceil(1.0 * diffInDays / 30);

		fine = bill.getAmount();
		for (int i = 1; i <= diffInMonths; i++) {
			fine += fine * SystemManager.society.getLateFineInterest() / 100;
		}

		fine -= bill.getAmount();

		return fine;
	}
}

package com.vhi.hsm.controller.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.Bill;
import com.vhi.hsm.model.Charge;
import com.vhi.hsm.model.Payment;
import com.vhi.hsm.model.Property;
import com.vhi.hsm.utils.Constants;

public class PaymentManager {

	public static synchronized ArrayList<Bill> makePayment(Payment payment) {

		ArrayList<Bill> paidBills = new ArrayList<Bill>();

		boolean commit = true;

		if (SQLiteManager.startTransaction()) {

			if (Payment.save(payment, true)) { // Save payment in DB

				Property property = Property.read(payment.getPropertyId());
				
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(payment.getPaymentDate());
				int i = calendar.get(calendar.DAY_OF_MONTH);
				if(i> SystemManager.society.getPaymentDueDate()){
					Charge createdCharge = Charge.create(SystemManager.society.getSocietyId());
					ArrayList<Integer> propertyList = new ArrayList<>();
					propertyList.add(property.getPropertyId());
					createdCharge.setAssignedProperty(propertyList);
					createdCharge.setTempCharges(true);
					createdCharge.setDescription("Late Payment Fine Charge");
					Charge.save(createdCharge, true);
					
				}

				if (property != null) {

					double remainingAmount = payment.getAmount();
					double billAmount;

					// settle the previous bills
					ResultSet resultSet = null;
					try {
						resultSet = SQLiteManager.executeQuery("SELECT * FROM " + Constants.Table.Bill.TABLE_NAME
								+ " WHERE " + Constants.Table.Payment.FieldName.PAYMENT_ID + " = 0 ORDER BY "
								+ Constants.Table.Bill.FieldName.BILL_TIMESTAMP + " DESC");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (resultSet != null) {
						ArrayList<Bill> bills = Bill.getBillsFromResultSet(resultSet);
						for (Bill bill : bills) {
							billAmount = 0;
							billAmount = bill.getAmount();
							if (billAmount <= remainingAmount) {
								bill.setPaymentId(payment.getPaymentId());
								if (Bill.save(bill, false)) {
									remainingAmount -= billAmount;
									paidBills.add(bill);
								} else {
									commit = false;
									break;
								}
							} else {
								break;
							}
						}
					}

					// save the remaining balance to property
					property.setNetPayable(property.getNetPayable() - remainingAmount);
					if (!Property.save(property, false)) {
						commit = false;
					}
					
					property.setLatestPaymentId(payment.getPaymentId());
					Property.save(property, false);

				}

			} else {
				commit = false;
			}

			if (!commit) {
				paidBills.clear();
			}

			SQLiteManager.endTransaction(commit, !commit);

		}

		return paidBills;
	}

}

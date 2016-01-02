package com.vhi.hsm.controller.manager;

import java.sql.ResultSet;
import java.util.ArrayList;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.Bill;
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

				if (property != null) {

					double remainingAmount = payment.getAmount();
					double billAmount;

					// settle the previous bills
					ResultSet resultSet = SQLiteManager.executeQuery("SELECT * FROM " + Constants.Table.Bill.TABLE_NAME
							+ " WHERE " + Constants.Table.Payment.FieldName.PAYMENT_ID + " = 0 ORDER BY "
							+ Constants.Table.Bill.FieldName.BILL_TIMESTAMP +" DESC");
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

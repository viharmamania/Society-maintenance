package com.vhi.hsm.controller.manager;

import java.util.ArrayList;

import com.vhi.hsm.model.Bill;
import com.vhi.hsm.model.BillCharge;
import com.vhi.hsm.model.Payment;
import com.vhi.hsm.model.Property;

public class PaymentManager {

	public static synchronized ArrayList<Bill> makePayment(Payment payment) {
		
		double remainingAmount;
		ArrayList<Bill> paidBills = new ArrayList<Bill>();

//		boolean commit = true;
//
//		if (SQLiteManager.startTransaction()) {

			if (Payment.save(payment, true)) { // Save payment in DB

				Property property = Property.read(payment.getPropertyId());

				if (property != null) {
					
					ArrayList<Bill> unpaidBills = BillManager.getUnpaidBills(property);
					
					double fineAmount;
					BillCharge billCharge;
					
					remainingAmount = property.getNetPayable() + payment.getAmount();
					
					for (Bill bill : unpaidBills) {
						fineAmount = BillManager.getLateFineAmount(bill, payment.getPaymentDate());
						bill.setAmount(bill.getAmount() + fineAmount);
						if ( remainingAmount >= bill.getAmount() ) {
							remainingAmount -= bill.getAmount();
							billCharge = BillCharge.create(bill.getBillId(), -1); //-1 is for late fine charge id
							billCharge.setAmount(fineAmount);
							BillCharge.save(billCharge, true);
							bill.setPaymentId(payment.getPaymentId());
							Bill.save(bill, false);
							paidBills.add(bill);
						} else {
							bill.setAmount(bill.getAmount() - fineAmount);
							break;
						}
					}

					// save the remaining balance to property
					property.setNetPayable(remainingAmount);
					if (!Property.save(property, false)) {
//						commit = false;
					}
					
					property.setLatestPaymentId(payment.getPaymentId());
					Property.save(property, false);

				}

//			} else {
//				commit = false;
//			}
//
//			if (!commit) {
//				paidBills.clear();
//			}

//			SQLiteManager.endTransaction(commit, !commit);
//
		}

		return paidBills;
	}

}

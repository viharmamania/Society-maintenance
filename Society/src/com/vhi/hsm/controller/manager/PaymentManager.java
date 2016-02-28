package com.vhi.hsm.controller.manager;

import java.util.ArrayList;

import com.vhi.hsm.model.Bill;
import com.vhi.hsm.model.Payment;
import com.vhi.hsm.model.Property;

public class PaymentManager {

	@SuppressWarnings("deprecation")
	public static synchronized ArrayList<Bill> makePayment(Payment payment) {

		ArrayList<Bill> paidBills = new ArrayList<Bill>();
		double newNetPaybale;

		if (Payment.save(payment, true)) { // Save payment in DB

			Property property = Property.read(payment.getPropertyId());

			if (property != null) {

				newNetPaybale = property.getNetPayable();
				newNetPaybale = newNetPaybale - payment.getAmount();

				/***
				 * Add fine if Last paymentis done before due date and current
				 * payment date is after the due date
				 */
				if (payment.getPaymentDate().getDay() > SystemManager.society.getPaymentDueDate()
						&& newNetPaybale > 0) {
					Payment lastPayment = Payment.read(property.getLatestPaymentId());
					if (lastPayment.getPaymentDate().getDay() >= SystemManager.society.getPaymentDueDate()) {
						newNetPaybale = newNetPaybale * (1.0 + SystemManager.society.getLateFineInterest());
					}
				}

				// save the remaining balance to property
				property.setNetPayable(newNetPaybale);

				property.setLatestPaymentId(payment.getPaymentId());
				Property.save(property, false);

			}

		}

		return paidBills;
	}

}

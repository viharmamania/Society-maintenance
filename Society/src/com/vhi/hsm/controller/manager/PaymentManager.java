package com.vhi.hsm.controller.manager;

import java.util.ArrayList;
import java.util.Calendar;

import com.vhi.hsm.model.Bill;
import com.vhi.hsm.model.Payment;
import com.vhi.hsm.model.Property;

public class PaymentManager {

	public static synchronized ArrayList<Bill> makePayment(Payment payment) {

		ArrayList<Bill> paidBills = new ArrayList<Bill>();
		double newNetPaybale;
		
		int paymentDay, paymentMonth, paymentYear, lastPaymentDay, lastPaymentMonth, lastPaymentYear;

		if (Payment.save(payment, true)) { // Save payment in DB

			Property property = Property.read(payment.getPropertyId());

			if (property != null) {

				newNetPaybale = property.getNetPayable();
				newNetPaybale = newNetPaybale - payment.getAmount();
				
				Calendar cal = Calendar.getInstance();
				cal.setTime(payment.getPaymentDate());
				
				paymentDay = cal.get(Calendar.DATE);
				paymentMonth = cal.get(Calendar.MONTH);
				paymentYear = cal.get(Calendar.YEAR);

				/***
				 * Add fine if Last paymentis done before due date and current
				 * payment date is after the due date
				 * Or, Last payment is in prevous months and current payment is after the due date
				 * 
				 * And, do not add fine, if Last payment is in same month and after the due date
				 */
				if (paymentDay > SystemManager.society.getPaymentDueDate()
						&& newNetPaybale > 0) {
					
					Payment lastPayment = Payment.read(property.getLatestPaymentId());
					
					if (lastPayment != null) {
						
						Calendar lastPaymentCal = Calendar.getInstance();
						lastPaymentCal.setTime(lastPayment.getPaymentDate());
						
						lastPaymentDay = lastPaymentCal.get(Calendar.DATE);
						lastPaymentMonth = lastPaymentCal.get(Calendar.MONTH);
						lastPaymentYear = lastPaymentCal.get(Calendar.YEAR);
						
						if (lastPaymentMonth == paymentMonth
								&& lastPaymentYear == paymentYear
								&& lastPaymentDay <= SystemManager.society.getPaymentDueDate()
								&& paymentDay > SystemManager.society.getPaymentDueDate()) {
							newNetPaybale = newNetPaybale * (1.0 + SystemManager.society.getLateFineInterest());
						}
					} else {
						newNetPaybale = newNetPaybale * (1.0 + (SystemManager.society.getLateFineInterest() / 100));
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

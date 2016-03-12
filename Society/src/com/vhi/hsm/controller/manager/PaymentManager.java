package com.vhi.hsm.controller.manager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

import org.apache.log4j.Logger;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.Bill;
import com.vhi.hsm.model.Payment;
import com.vhi.hsm.model.Property;
import com.vhi.hsm.utils.Constants;

public class PaymentManager {
	
	private final static Logger LOG = Logger.getLogger(BillManager.class);

	public static synchronized ArrayList<Bill> makePayment(Payment payment) {

		ArrayList<Bill> paidBills = new ArrayList<Bill>();
		double newNetPaybale;
		boolean addInterest = false;
		
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
							addInterest = true;
						}
					} else {
						addInterest = true;
					}
					
				}
				
				if (addInterest) {
					newNetPaybale = newNetPaybale * (1.0 + (SystemManager.society.getLateFineInterest() / 100));
				}

				// save the remaining balance to property
				property.setNetPayable(newNetPaybale);

				property.setLatestPaymentId(payment.getPaymentId());
				Property.save(property, false);

			}

		}

		return paidBills;
	}
	
	public static ArrayList<Payment> getPropertyPayments(int propertyId) {
		ArrayList<Payment> payments = new ArrayList<Payment>();
		try {
			ResultSet resultSet = SQLiteManager.executeQuery("SELECT * FROM " + Constants.Table.Payment.TABLE_NAME
					+ " WHERE " + Constants.Table.Property.FieldName.PROPERTY_ID + " = " + propertyId);
			payments = Payment.getPaymentsFromResultSet(resultSet);
		} catch (SQLException e) {
			LOG.error(e.getStackTrace());
		}
		return payments;
	}

}

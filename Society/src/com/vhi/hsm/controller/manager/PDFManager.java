package com.vhi.hsm.controller.manager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.Bill;
import com.vhi.hsm.model.Charge;
import com.vhi.hsm.model.ModeOfPayment;
import com.vhi.hsm.model.Payment;
import com.vhi.hsm.model.Property;
import com.vhi.hsm.utils.Constants;

public class PDFManager {

	static String paymentBillContent = "Received with thanks from Shri/Smt./M /s. [1] for Flat/Shop/Garage No. [2] , \nthe sum of Rupees [3] by [4] [5], in part/full payment.";

	public static void generateBillPDF(List<Bill> bills) throws DocumentException, FileNotFoundException {
		Document document = new Document();
		PdfWriter pdfWriter = PdfWriter.getInstance(document,
				new FileOutputStream("Maintenance Bills " + Calendar.getInstance().get(Calendar.YEAR) + " "
						+ Calendar.getInstance().get(Calendar.MONTH) + ".pdf"));
		document.open();

//		Image instance;
//		try {
//			instance = Image.getInstance("C:/Users/Vihar/Desktop/logo.png");
//			document.add(instance);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		PdfPTable headerTable = new PdfPTable(1);
		headerTable.addCell(SystemManager.society.getName());
		headerTable.addCell(SystemManager.society.getAddress());
		headerTable.addCell(
				SystemManager.society.getRegistrationNumber() + " " + SystemManager.society.getRegistrationDate());

		Bill bill;
		ArrayList<Integer> billAssignedCharges;
		for (int i = 0; i < bills.size(); i++) {
			bill = null;
			bill = bills.get(i);
			if (bill != null) {
				// Society Information
				document.add(headerTable);

				// Owner Information
				PdfPTable flatTable = new PdfPTable(2);
				Property property = Property.read(bill.getPropertyId());
				if (property != null) {
					flatTable.addCell(property.getOwnerName());
					flatTable.addCell("");
					flatTable.addCell("Bill Amount");
					flatTable.addCell(Double.toString(bill.getAmount()));
					document.add(flatTable);
				}

				// Bill charges
				PdfPTable billTable = new PdfPTable(2);
				billTable.addCell("Charge");
				billTable.addCell("Amount");
				billAssignedCharges = null;
				billAssignedCharges = bill.getAssignedCharges();
				for (Integer chargeId : billAssignedCharges) {
					Charge charge = Charge.read(SystemManager.society.getSocietyId(), chargeId.intValue());
					if (charge != null) {
						billTable.addCell(charge.getDescription());
						billTable.addCell(Double.toString(charge.getAmount()));
					}
				}
				document.add(billTable);

				// add new bill in new page
				if (i != (bills.size() - 1)) {
					document.newPage();
				}
			}
		}

		document.close();
		pdfWriter.close();
	}

	public static void generateReceiptsPDF(List<Payment> payments)
			throws DocumentException, MalformedURLException, IOException {

		Document document = new Document();
		PdfWriter pdfWriter = PdfWriter.getInstance(document,
				new FileOutputStream("Maintenance Bills Receipt" + Calendar.getInstance().get(Calendar.YEAR) + " "
						+ Calendar.getInstance().get(Calendar.MONTH) + ".pdf"));
		document.open();

		PdfPTable headerTable = new PdfPTable(1);
		headerTable.addCell(SystemManager.society.getName());
		headerTable.addCell(SystemManager.society.getAddress());
		headerTable.addCell(
				SystemManager.society.getRegistrationNumber() + " " + SystemManager.society.getRegistrationDate());

		Payment payment;
		for (int i = 0; i < payments.size(); i++) {
			payment = null;
			payment = payments.get(i);
			if (payment != null) {
				PdfPTable flatTable = new PdfPTable(2);

				PdfPCell receiptNoCell = new PdfPCell(new Phrase("Receipt No:" + payment.getPaymentId()));
				receiptNoCell.setBorder(Rectangle.NO_BORDER);
				// receiptNoCell.setBorderColor(new Color(255, 255, 45));
				PdfPCell dateCell = new PdfPCell(new Phrase("Date:" + new Date()));
				dateCell.setBorder(Rectangle.NO_BORDER);
				// dateCell.setBorderColor(new Color(255, 255, 45));
				flatTable.addCell(dateCell);

				// flatTable.addCell("Receipt No:" + payment.getPaymentId());
				// flatTable.addCell("Date:" + new Date());
				document.add(flatTable);
			}
			Property property = Property.read(payment.getPaymentId());
			paymentBillContent = paymentBillContent.replace("[1]", property.getOwnerName());
			paymentBillContent = paymentBillContent.replace("[2]", property.getPropertyName());
			paymentBillContent = paymentBillContent.replace("[3]", String.valueOf(payment.getAmount()));
			paymentBillContent = paymentBillContent.replace("[4]", payment.getModeOfPayment());
			if (payment.getModeOfPayment().equalsIgnoreCase(ModeOfPayment.CHEQUE.name())) {
				paymentBillContent = paymentBillContent.replace("[5]", " by Cheque No: " + payment.getChequeNumber());
			} else if (payment.getModeOfPayment().equalsIgnoreCase(ModeOfPayment.CASH.name()))
				paymentBillContent = paymentBillContent.replace("[5]", "");

			Integer billId = null;
			String query = "select bill_id from bill where payment_id =" + payment.getPaymentId()
					+ " order by bill_id  ASC";
			try {
				ResultSet executeQuery = SQLiteManager.executeQuery(query);
				if (executeQuery != null & executeQuery.next()) {
					do {
						billId = executeQuery.getInt(Constants.Table.Payment.FieldName.PAYMENT_ID);
						executeQuery.next();
					} while (!executeQuery.isAfterLast());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (billId != null)
				paymentBillContent = paymentBillContent.replace("[6]", " on account of Bill No. " + billId);

			document.add(new Paragraph(paymentBillContent));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));
			Image instance = Image.getInstance("C:/Users/Vihar/Desktop/Y.jpg");
			document.add(instance);

			document.add(new Paragraph("Rs: " + payment.getAmount()));

			if (i != (payments.size() - 1)) {
				document.newPage();
			}
		}
		try {
			document.close();
			pdfWriter.close();
		} catch (Exception exception) {
//			document.add(new Paragraph("Done Adding"));
//			document.close();
//			pdfWriter.close();
		}

	}
}

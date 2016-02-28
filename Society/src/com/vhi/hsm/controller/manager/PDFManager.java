package com.vhi.hsm.controller.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.Bill;
import com.vhi.hsm.model.BillCharge;
import com.vhi.hsm.model.Charge;
import com.vhi.hsm.model.ModeOfPayment;
import com.vhi.hsm.model.Payment;
import com.vhi.hsm.model.Property;
import com.vhi.hsm.utils.Constants;
import com.vhi.hsm.utils.Utility;

public class PDFManager {

	static String paymentBillContent = "Received with thanks from Shri/Smt./M /s. [1] for Flat/Shop/Garage No. [2] , \nthe sum of Rupees [3] by [4] [5], in part/full payment.";

	public static void generateBillPDF(List<Bill> bills, boolean isPreview) throws DocumentException, IOException {
		Document document = new Document();
		File file;

		StringBuilder pdfLocation = new StringBuilder();
		if (isPreview) {
			pdfLocation.append(Constants.Path.PREVIEW_PDF_LOCATION + "Preview Bill ");
			file = new File(Constants.Path.PREVIEW_PDF_LOCATION);
		} else {
			pdfLocation.append(Constants.Path.BILL_PDF_LOCATION + "Maintenance Bill ");
			file = new File(Constants.Path.BILL_PDF_LOCATION);
		}

		if (!file.exists()) {
			file.mkdirs();
		}

		Calendar calendar = Calendar.getInstance();
		pdfLocation.append(Utility.getMonthNameFromNumber((calendar.get(Calendar.MONTH))) + " "
				+ calendar.get(Calendar.YEAR) + ".pdf");
		PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfLocation.toString()));
		pdfWriter.setPageEvent(new EventHelper());
		document.open();

		document.add(new Paragraph("\n\n\n"));

		// Printing Society Information
		PdfPTable headerTable = new PdfPTable(1);
		headerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		headerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		headerTable.addCell(SystemManager.society.getName().toUpperCase());
		headerTable.addCell("Regn. No" + SystemManager.society.getRegistrationNumber() + " Dated "
				+ SystemManager.society.getRegistrationDate());
		headerTable.addCell(SystemManager.society.getAddress());

		Bill bill;
		ArrayList<Integer> billAssignedCharges;
		for (int i = 0; i < bills.size(); i++) {
			bill = null;
			bill = bills.get(i);
			if (bill != null) {
				// Society Information
				document.add(headerTable);

				document.add(new Paragraph("\n\n\n"));
				// Owner Information and general details
				PdfPTable flatTable = new PdfPTable(3);
				flatTable.setWidthPercentage(100);
				flatTable.setSpacingAfter(10);
				flatTable.setSpacingAfter(10);
				flatTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				flatTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
				Property property = Property.read(bill.getPropertyId());
				if (property != null) {

					Paragraph billNoParagraph = new Paragraph("Bill No: " + bill.getBillId());
					billNoParagraph.setAlignment(Element.ALIGN_LEFT);
					flatTable.addCell(billNoParagraph);

					Calendar cal = Calendar.getInstance();
					cal.setTime(bill.getBillDate());
					cal.set(Calendar.DAY_OF_MONTH, 1);
					Paragraph billDateParagraph = new Paragraph("Bill Date : " + getDate(cal));
					billDateParagraph.setAlignment(Element.ALIGN_CENTER);
					flatTable.addCell(billDateParagraph);

					cal.add(Calendar.DAY_OF_MONTH, 14);
					Paragraph dueDateParagraph = new Paragraph("Due Date : " + getDate(cal));
					dueDateParagraph.setAlignment(Element.ALIGN_RIGHT);
					flatTable.addCell(dueDateParagraph);

					document.add(flatTable);
				}

				String string = " Shri./smt. " + property.getOwnerName()
						+ " find below Detailed bill for Flat/Shop/Garage No. " + property.getPropertyName()
						+ " for the period of " + Utility.getMonthNameFromNumber((calendar.get(Calendar.MONTH))) + " "
						+ calendar.get(Calendar.YEAR);
				Paragraph infoParagraph = new Paragraph(string);
				infoParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
				document.add(infoParagraph);

				document.add(new Paragraph("\n\n\n"));

				// printing charge Details
				// Bill charges
				PdfPTable billTable = new PdfPTable(3);
				billTable.setWidthPercentage(100);
				billTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
				billTable.addCell(getCell("Sr No.", PdfPCell.ALIGN_LEFT));
				billTable.addCell(getCell("Particulars", PdfPCell.ALIGN_CENTER));
				billTable.addCell(getCell("Amount", PdfPCell.ALIGN_CENTER));
				billAssignedCharges = null;
				billAssignedCharges = bill.getAssignedCharges();
				Collections.sort(billAssignedCharges);
				int count = 0;
				for (int k = 0; k < billAssignedCharges.size(); k++) {
					Charge charge = Charge.read(SystemManager.society.getSocietyId(),
							billAssignedCharges.get(k).intValue());
					double amount = 0.0;
					if (!isPreview) {
						BillCharge billCharge = BillCharge.read(bill.getBillId(), charge.getChargeId());
						amount = billCharge.getAmount();
					} else {
						amount = charge.getAmount();
					}
					while (true) {
						if ((k + 1) < billAssignedCharges.size()
								&& billAssignedCharges.get(k + 1).equals(billAssignedCharges.get(k))) {
							amount += amount;
							k++;
						} else {
							break;
						}
					}
					if (charge != null) {
						billTable.addCell(getCell(String.valueOf(++count), PdfPCell.ALIGN_LEFT));
						billTable.addCell(getCell(charge.getDescription(), PdfPCell.ALIGN_LEFT));
						billTable.addCell(getCell(String.valueOf(amount), PdfPCell.ALIGN_CENTER));

					}
				}
				document.add(billTable);

				Paragraph par = new Paragraph("Payable Amount  Rs: " + bill.getAmount());
				par.setAlignment(Element.ALIGN_RIGHT);
				par.setAlignment(Element.ALIGN_BOTTOM);
				document.add(par);

				// add new bill in new page
				if (i != (bills.size() - 1)) {
					document.newPage();
				}
			}
		}

		document.close();
		pdfWriter.close();
	}

	private static String getDate(Calendar cal) {

		StringBuilder date = new StringBuilder();
		date.append(cal.get(Calendar.DAY_OF_MONTH) + "/");
		date.append(cal.get(Calendar.MONTH) + "/");
		date.append(cal.get(Calendar.YEAR));
		return date.toString();
	}

	public static PdfPCell getCell(String text, int alignment) {
		PdfPCell cell = new PdfPCell(new Phrase(text));
		cell.setPadding(10);
		cell.setHorizontalAlignment(alignment);
		cell.setBorder(PdfPCell.NO_BORDER);
		return cell;

	}

	public static void generateReceiptsPDF(List<Payment> payments)
			throws DocumentException, MalformedURLException, IOException {

		StringBuilder pdfLocation = new StringBuilder();

		File file = new File(Constants.Path.PAYMENT_PDF_LOCATION);
		if (!file.exists()) {
			file.mkdirs();
		}

		pdfLocation.append(Constants.Path.PAYMENT_PDF_LOCATION + "Payment Receipts ");
		pdfLocation.append(Utility.getMonthNameFromNumber((Calendar.getInstance().get(Calendar.MONTH))) + " "
				+ Calendar.getInstance().get(Calendar.YEAR) + ".pdf");

		// new File(pdfLocation.toString()).createNewFile();
		Document document = new Document();
		PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(pdfLocation.toString(), true));
		pdfWriter.setPageEvent(new EventHelper());
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

			ArrayList<Integer> billId = new ArrayList<>();
			String query = "SELECT " + Constants.Table.Bill.FieldName.BILL_ID + " FROM "
					+ Constants.Table.Bill.TABLE_NAME + " WHERE " + Constants.Table.Payment.FieldName.PAYMENT_ID + " = "
					+ payment.getPaymentId() + " ORDER BY " + Constants.Table.Bill.FieldName.BILL_ID + " ASC";
			try {
				ResultSet executeQuery = SQLiteManager.executeQuery(query);
				while (executeQuery.next()) {
					billId.add(executeQuery.getInt(Constants.Table.Bill.FieldName.BILL_ID));
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			if (billId != null)
				paymentBillContent = paymentBillContent.replace("[6]", " on account of Bill No. " + billId.toString());

			document.add(new Paragraph(paymentBillContent));
			document.add(new Paragraph("\n"));
			document.add(new Paragraph("\n"));

			document.add(new Paragraph("Rs: " + payment.getAmount()));

			if (i != (payments.size() - 1)) {
				document.newPage();
			}
		}
		try {
			document.close();
			pdfWriter.close();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}

class EventHelper extends PdfPageEventHelper {

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		PdfContentByte canvas = writer.getDirectContent();
		canvas.rectangle(20, 20, document.getPageSize().getWidth() - 50, document.getPageSize().getHeight() - 50);
		canvas.stroke();
	}

	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		PdfContentByte canvas = writer.getDirectContent();
		canvas.rectangle(20, 20, document.getPageSize().getWidth() - 50, document.getPageSize().getHeight() - 50);
		canvas.stroke();

	}
}

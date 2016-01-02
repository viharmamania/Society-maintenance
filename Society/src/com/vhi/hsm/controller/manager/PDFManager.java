package com.vhi.hsm.controller.manager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vhi.hsm.model.Bill;
import com.vhi.hsm.model.Charge;
import com.vhi.hsm.model.Property;

public class PDFManager {

	public static void generateBillPDF(List<Bill> bills) throws DocumentException, FileNotFoundException {
		Document document = new Document();
		PdfWriter pdfWriter = PdfWriter.getInstance(document,
				new FileOutputStream("Maintenance Bills " + Calendar.getInstance().get(Calendar.YEAR) + " "
						+ Calendar.getInstance().get(Calendar.MONTH) + ".pdf"));
		document.open();
		
		PdfPTable headerTable = new PdfPTable(1);
		headerTable.addCell(SystemManager.society.getName());
		headerTable.addCell(SystemManager.society.getAddress());
		headerTable.addCell(SystemManager.society.getRegistrationNumber() + " " + SystemManager.society.getRegistrationDate());
		
		Bill bill;
		ArrayList<Integer> billAssignedCharges;
		for (int i = 0; i < bills.size(); i++) {
			bill = null;
			bill = bills.get(i);
			if (bill != null) {
				//Society Information		
				document.add(headerTable);
				
				//Owner Information
				PdfPTable flatTable = new PdfPTable(2);
				Property property = Property.read(bill.getPropertyId());
				if (property != null) {
					flatTable.addCell(property.getOwnerName());
					flatTable.addCell("");
					flatTable.addCell("Bill Amount");
					flatTable.addCell(Double.toString(bill.getAmount()));
					document.add(flatTable);
				}
				
				//Bill charges
				PdfPTable billTable = new PdfPTable(2);
				billTable.addCell("Charge");
				billTable.addCell("Amount");
				billAssignedCharges = null;
				billAssignedCharges = bill.getAssignedCharges();
				for(Integer chargeId : billAssignedCharges) {
					Charge charge = Charge.read(SystemManager.society.getSocietyId(), chargeId.intValue());
					if (charge != null) {
						billTable.addCell(charge.getDescription());
						billTable.addCell(Double.toString(charge.getAmount()));
					}
				}
				
				//add new bill in new page
				if (i != (bills.size() - 1)) {
					document.newPage();
				}
			}
		}

		document.close();
		pdfWriter.close();
	}
}

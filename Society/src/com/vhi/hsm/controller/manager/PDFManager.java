package com.vhi.hsm.controller.manager;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.List;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.vhi.hsm.model.Bill;

public class PDFManager {

	public static void main(String[] args) {
		try {
			PDFManager.generateBillPDF(null);
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		}
	}

	public static void generateBillPDF(List<Bill> bills) throws DocumentException, FileNotFoundException {
		Document document = new Document();
		PdfWriter pdfWriter = PdfWriter.getInstance(document,
				new FileOutputStream("Maintenance Bills " + Calendar.getInstance().get(Calendar.MONTH) + " "
						+ Calendar.getInstance().get(Calendar.YEAR) + ".pdf"));

		document.open();
		PdfPTable pdfTable = new PdfPTable(2);

		for (Bill bill : bills) {
			PdfPCell pCell = new PdfPCell(new Paragraph("Particulars"));
			PdfPCell pCell2 = new PdfPCell(new Paragraph("charges"));
			PdfPCell pCell3 = new PdfPCell(new Paragraph("Water tax"));
			PdfPCell pcell4 = new PdfPCell(new Paragraph("123"));
			pdfTable.addCell(pCell);
			pdfTable.addCell(pCell2);
			pdfTable.addCell(pCell3);
			pdfTable.addCell(pcell4);

			document.add(pdfTable);
		}

		document.close();
		pdfWriter.close();
	}
}

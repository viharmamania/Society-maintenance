package com.vhi.hsm.view;

import java.awt.Dialog;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.vhi.hsm.controller.manager.PaymentManager;
import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.ModeOfPayment;
import com.vhi.hsm.model.Property;

/**
 * Payment View for making bill payment
 * 
 * @author Vihar
 *
 */
public class Payment extends JDialog implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8038931709318079644L;
	private final static Logger LOG = Logger.getLogger(Payment.class);
	private JTextField chequeNoTextField, remarksTextField, amountTextField = null;
	private JLabel chequeNoLabel, propertyNameLabel, modeOfPaymentLabel, amountLabel, remarksLabel = null;
	private JComboBox<String> propertyNamesComboBox, modeOfPaymentComboBox = null;
	private JButton confirmButton, backButton, uploadButton;
	private int societyId;
	private Set<String> propertyNames = new HashSet<>();
	private static Map<String, Integer> propertyNameToIdmap = null;

	static {
		propertyNameToIdmap = Property.getAllPropertyNames(SystemManager.society.getSocietyId());
	}

	public Payment(Dialog parentDialog) {

		super(parentDialog);
		this.setTitle("Payments");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setLocationRelativeTo(parentDialog);
		setVisible(true);
		societyId = SystemManager.society.getSocietyId();
		fetchPropertyNames();
		intializeLayout();
	}

	public Payment(JFrame parentFrame) {

		super(parentFrame);
		this.setTitle("Payments");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setLocationRelativeTo(parentFrame);
		setVisible(true);
		societyId = SystemManager.society.getSocietyId();
		fetchPropertyNames();
		intializeLayout();
	}

	public Payment() {

		this.setTitle("Payments");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setVisible(true);
		societyId = SystemManager.society.getSocietyId();
		fetchPropertyNames();
		intializeLayout();
	}

	private void intializeLayout() {
		propertyNameLabel = new JLabel("Property :");
		propertyNamesComboBox = new JComboBox<>(propertyNames.toArray(new String[propertyNames.size()]));

		modeOfPaymentLabel = new JLabel("Mode of payment");
		modeOfPaymentComboBox = new JComboBox<>(ModeOfPayment.getNames());
		modeOfPaymentComboBox.addActionListener(e -> {
			selectionListener();
		});

		chequeNoLabel = new JLabel("Cheque No");
		chequeNoTextField = new JTextField();
		chequeNoTextField.setEditable(false);

		amountLabel = new JLabel("Amount");
		amountTextField = new JTextField();

		remarksLabel = new JLabel("Remarks");
		remarksTextField = new JTextField();

		confirmButton = new JButton("Confirm Payment");
		confirmButton.addActionListener(e -> {
			makePayment();
		});

		backButton = new JButton("Cancel Payment");
		backButton.addActionListener(e -> {
			cancelPayment();
		});

		uploadButton = new JButton("Upload Excel");
		uploadButton.addActionListener(e -> {
			uploadExcel();
		});

		GroupLayout groupLayout = new GroupLayout(getContentPane());

		getContentPane().setLayout(groupLayout);

		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(propertyNameLabel)
						.addComponent(modeOfPaymentLabel).addComponent(chequeNoLabel).addComponent(amountLabel)
						.addComponent(remarksLabel).addComponent(confirmButton))
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(propertyNamesComboBox)
						.addComponent(modeOfPaymentComboBox).addComponent(chequeNoTextField)
						.addComponent(amountTextField).addComponent(remarksTextField).addGroup(groupLayout
								.createSequentialGroup().addComponent(backButton).addComponent(uploadButton))));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(propertyNameLabel)
						.addComponent(propertyNamesComboBox))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(modeOfPaymentLabel)
						.addComponent(modeOfPaymentComboBox))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(chequeNoLabel)
						.addComponent(chequeNoTextField))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(amountLabel)
						.addComponent(amountTextField))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(remarksLabel)
						.addComponent(remarksTextField))
				.addGroup(groupLayout.createParallelGroup().addComponent(confirmButton).addComponent(backButton)
						.addComponent(uploadButton)));
		pack();

		// selectionListener();
	}

	private void selectionListener() {

		String selection = (String) modeOfPaymentComboBox.getSelectedItem();
		if (selection.equalsIgnoreCase(ModeOfPayment.CHEQUE.name())) {
			chequeNoTextField.setEditable(true);
		} else {
			chequeNoTextField.setEditable(false);
		}

	}

	private void fetchPropertyNames() {

		if (propertyNames != null)
			if (propertyNames.size() == Property.getPropertyCount(societyId))
				return;

		propertyNames = propertyNameToIdmap.keySet();

	}

	@Override
	public void windowActivated(WindowEvent e) {

	}

	@Override
	public void windowClosed(WindowEvent e) {
		this.dispose();
	}

	@Override
	public void windowClosing(WindowEvent e) {

	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {

	}

	@Override
	public void windowIconified(WindowEvent e) {

	}

	@Override
	public void windowOpened(WindowEvent e) {

	}

	public static void main(String[] args) {

		// Sets the System theme
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {

		}

		// set up database connection
		if (SQLiteManager.setUpDB()) {

			// runs UI on other than Main thread
			SwingUtilities.invokeLater(() -> {
				new Payment();
			});

		} else {
			// Show error message
			JOptionPane.showMessageDialog(null, "Can not connect to database", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void uploadExcel() {

		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showDialog(this, "save");
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		if (result == JFileChooser.APPROVE_OPTION) {
			File selectedFile = fileChooser.getSelectedFile();
			FileInputStream fileInputStream = null;
			try {
				fileInputStream = new FileInputStream(selectedFile);

				Workbook workbook = new XSSFWorkbook(fileInputStream);

				int numberOfSheets = workbook.getNumberOfSheets();

				for (int i = 0; i < numberOfSheets; i++) {
					Sheet sheet = workbook.getSheetAt(i);
					Iterator<Row> rowIterator = sheet.iterator();

					while (rowIterator.hasNext()) {

						Row row = (Row) rowIterator.next();
						if (row.getRowNum() == 0 || row.getRowNum() == 1)
							continue;

						int cellCount = row.getLastCellNum();
						String propertyName = null, chequeNo = null, modeOfPayment = null, remarks = null;
						Double amount = null;
						for (int j = 0; j < cellCount; j++) {
							Cell cell = row.getCell(j);
							if (j == 0 && cell.getCellType() == Cell.CELL_TYPE_STRING) {
								propertyName = cell.getStringCellValue();
							}
							if (j == 1 && cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
								amount = cell.getNumericCellValue();
							}
							if (j == 2 && cell.getCellType() == Cell.CELL_TYPE_STRING) {
								modeOfPayment = cell.getStringCellValue();
							}
							if (j == 3 && cell.getCellType() == Cell.CELL_TYPE_STRING) {
								chequeNo = cell.getStringCellValue();
							}
							if (j == 4 && cell.getCellType() == Cell.CELL_TYPE_STRING) {
								remarks = cell.getStringCellValue();
							}
						}

						com.vhi.hsm.model.Payment payment = com.vhi.hsm.model.Payment
								.create(propertyNameToIdmap.get(propertyName));
						payment.setAmount(amount);
						payment.setChequeNumber(chequeNo);
						payment.setModeOfPayment(modeOfPayment);
						payment.setRemarks(remarks);

						PaymentManager.makePayment(payment);

					}
				}

				fileInputStream.close();
				JOptionPane.showMessageDialog(this, "Payment Saved successfully ", "Success",
						JOptionPane.INFORMATION_MESSAGE);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				LOG.error(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				LOG.error(e.getMessage());
			}
		}

	}

	private void cancelPayment() {
		dispose();
	}

	private void makePayment() {
		if (validateInput(true)) {
			// TODO call actual payment save service
			com.vhi.hsm.model.Payment payment = com.vhi.hsm.model.Payment
					.create(propertyNameToIdmap.get(propertyNamesComboBox.getSelectedItem()));

			payment.setRemarks(remarksTextField.getText());
			payment.setAmount(Double.valueOf(amountTextField.getText()));
			payment.setModeOfPayment((String) modeOfPaymentComboBox.getSelectedItem());
			payment.setChequeNumber(chequeNoTextField.getText().trim());

			// boolean paymentSaved = com.vhi.hsm.model.Payment.save(payment,
			// true);
			PaymentManager.makePayment(payment);
			JOptionPane.showMessageDialog(this, "Payment Saved successfully ", "Success",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
		}
	}

	private boolean validateInput(boolean showErrorMessage) {

		if (chequeNoTextField.isEditable()) {
			String chString = chequeNoTextField.getText().trim();
			String replacedChString = chString.replaceAll("[A-za-z]", "");
			if (chString.isEmpty()) {
				JOptionPane.showMessageDialog(this, "please Enter cheque number", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			if (chString.length() != replacedChString.length()) {
				JOptionPane.showMessageDialog(this, "Please Enter correct cheque Number", "Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
			}
		}

		try {
			Double.parseDouble(amountTextField.getText());
		} catch (NumberFormatException exception) {
			JOptionPane.showMessageDialog(this, "Please Enter Payment amount in valid format", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}
}

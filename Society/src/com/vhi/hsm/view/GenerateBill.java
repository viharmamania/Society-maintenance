/**
 * 
 */
package com.vhi.hsm.view;

import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.itextpdf.text.DocumentException;
import com.vhi.hsm.controller.manager.BillManager;
import com.vhi.hsm.controller.manager.PDFManager;
import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.Charge;
import com.vhi.hsm.utils.Constants;

/**
 * @author Hardik Senghani
 *
 */
public class GenerateBill extends JDialog implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JButton generateBills, previewBills;
	JPanel tempChargePanel;
	HashMap<Integer, JCheckBox> tempChargeCheckBox;
	ArrayList<Charge> tempCharges;

	public GenerateBill(JFrame parent) {
		super(parent);
		this.setTitle("Create New Bills");
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setLocationRelativeTo(parent);
		setVisible(true);
		checkIfBillAlreadyCreated();
	}

	private void checkIfBillAlreadyCreated() {
		boolean created = false;
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DAY_OF_MONTH, 1);
		instance.set(Calendar.MILLISECOND, 0);
		instance.set(Calendar.HOUR, 0);
		instance.set(Calendar.HOUR_OF_DAY, 0);
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);
		
		long time = instance.getTime().getTime();
		
		String billGeneratedCheck = "select bill_id from "+Constants.Table.Bill.TABLE_NAME+" where " + Constants.Table.Bill.FieldName.BILL_TIMESTAMP +">=" + time; 
		try {
			ResultSet executeQuery = SQLiteManager.executeQuery(billGeneratedCheck);
			if(executeQuery != null && executeQuery.next()){
				if(!executeQuery.isAfterLast()){
					created = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (!created) {
			prepareTempChargeList();
			initializeLayout();
		}else{
			JOptionPane.showMessageDialog(this, "Bills have already been created for this month, you can find them at this location: " + Constants.BILL_PDF_LOCATION, "Error", JOptionPane.INFORMATION_MESSAGE);
		}

	}

	private void prepareTempChargeList() {

		tempCharges = new ArrayList<Charge>();
		tempChargeCheckBox = new HashMap<Integer, JCheckBox>();

		ArrayList<Charge> allCharges = Charge.getAllCharge(SystemManager.society.getSocietyId());
		for (Charge c : allCharges) {
			if (c.isTempCharges() && !c.isCancelled()) {
				tempCharges.add(c);
				tempChargeCheckBox.put(c.getChargeId(), new JCheckBox(c.getDescription()));
			}
		}
	}

	private void initializeLayout() {

		tempChargePanel = new JPanel();
		tempChargePanel.setBorder(BorderFactory.createTitledBorder("Tempoprary Charges Assignment"));
		tempChargePanel.setLayout(new GridLayout(0, 3));

		for (Charge c : tempCharges) {
			tempChargePanel.add(tempChargeCheckBox.get(c.getChargeId()));
		}

		generateBills = new JButton("Generate Bills");
		generateBills.addActionListener(e -> {
			createPDF(false);
			dispose();
		});

		previewBills = new JButton("Preveiw Bills");
		previewBills.addActionListener(e -> {
			createPDF(true);
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup().addComponent(tempChargePanel)
				.addGroup(layout.createSequentialGroup().addComponent(previewBills).addComponent(generateBills)));

		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(tempChargePanel)
				.addGroup(layout.createParallelGroup().addComponent(previewBills).addComponent(generateBills)));

		pack();
	}

	private void createPDF(boolean isPreview) {
		ArrayList<Integer> tempChargeIds = new ArrayList<Integer>();

		JCheckBox box;
		for (Charge c : tempCharges) {
			box = null;
			box = tempChargeCheckBox.get(c.getChargeId());
			if (box != null && box.isSelected()) {
				tempChargeIds.add(c.getChargeId());
			}
		}

		try {
			PDFManager.generateBillPDF(
					BillManager.generateBill(SystemManager.society.getSocietyId(), isPreview, tempChargeIds) , isPreview);
		} catch (FileNotFoundException | DocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void windowActivated(WindowEvent arg0) {

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		dispose();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {

	}

	@Override
	public void windowIconified(WindowEvent arg0) {

	}

	@Override
	public void windowOpened(WindowEvent arg0) {

	}

}

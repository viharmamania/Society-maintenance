package com.vhi.hsm.view.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

//import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;
import com.vhi.hsm.controller.manager.PDFManager;
//import com.vhi.hsm.controller.manager.PreviewManager;
import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.Bill;
import com.vhi.hsm.utils.Constants;
import com.vhi.hsm.view.AssetTypeScreen;
import com.vhi.hsm.view.ChargeScreen;
import com.vhi.hsm.view.GenerateBill;
import com.vhi.hsm.view.Payment;
import com.vhi.hsm.view.PropertyView;
//import com.vhi.hsm.view.TempChargesConfirmationView;

public class DashBoard extends JFrame implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8224505516276632916L;
//	private final static Logger LOG = Logger.getLogger(DashBoard.class);
	private JPanel societyInfoPanel, infoPanel, treePanel, paymentPanel, billPanel, propertyPanel, chargePanel;

	private JTree billTree;
	private DefaultMutableTreeNode rootNode;

	private JButton //propertyPayButton, 
					propertyAssetButton, propertyViewButton, billGenerateButton, billViewButton,
			chargeViewButton, paymentViewButton, makePaymentButton,paymentReceiptsButton;

	public DashBoard() {

		
		societyInfoPanel = new JPanel();
		infoPanel = new JPanel();
		treePanel = new JPanel();

		paymentPanel = new JPanel();
		paymentPanel.setBorder(BorderFactory.createTitledBorder("Payment"));
		billPanel = new JPanel();
		billPanel.setBorder(BorderFactory.createTitledBorder("Bill"));
		propertyPanel = new JPanel();
		propertyPanel.setBorder(BorderFactory.createTitledBorder("Property"));
		chargePanel = new JPanel();
		chargePanel.setBorder(BorderFactory.createTitledBorder("Charge"));

		//propertyPayButton = new JButton("Pay Bills");
		propertyAssetButton = new JButton("View & Edit Assets");
		propertyAssetButton.addActionListener(e -> {
			new AssetTypeScreen(this);
		});

		paymentReceiptsButton = new JButton("Generate Payment Receipts");
		paymentReceiptsButton.addActionListener(e -> {
			try {
				generateMonthlyPaymentReceipts();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		});
		
		propertyViewButton = new JButton("View & Edit Properties");
		propertyViewButton.addActionListener(e -> {
			new PropertyView(this);
		});

		billGenerateButton = new JButton("Generate Monthly Bill");
		billGenerateButton.addActionListener(e ->{
			//new TempChargesConfirmationView(this);
//			generateBillPreview();
			new GenerateBill(this);
		});
		
		billViewButton = new JButton("View Bills");
		chargeViewButton = new JButton("View & Edit Charges");
		chargeViewButton.addActionListener(e -> {
			new ChargeScreen(this);
		});

		paymentViewButton = new JButton("View & Edit Payments");

		makePaymentButton = new JButton("Make Payment");
		makePaymentButton.addActionListener(e -> {
			new Payment(this);
		});

		rootNode = new DefaultMutableTreeNode("Previous Months Bill");
		billTree = new JTree(rootNode);
		billTree.setEditable(false);
		billTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		billTree.setShowsRootHandles(true);

		addWindowListener(this);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		prepareTreeData();

		setTitle("HSM");
		setResizable(false);
		initLayout();
		setVisible(true);
		setLocationRelativeTo(null);
	}

//	private List<Bill> generateBillPreview() {
//		List<Bill> bills = null;
//		try {
//			bills = PreviewManager.generateBill(SystemManager.society.getSocietyId());
//			for(Bill bill : bills){
//				System.out.println(bill);
//			}
//			JOptionPane.showMessageDialog(this, "The Bills have been generated successfully ", "Success", JOptionPane.INFORMATION_MESSAGE);
//		} catch (Exception e) {
//			LOG.error(e);
//		}
//		return bills;
//	}
//	
//	private void generateBill() {
////		try {
////			List<Bill> bills = BillManager.generateBill(SystemManager.society.getSocietyId(), true);
////			PDFManager.generateBillPDF(bills);
////			JOptionPane.showMessageDialog(this, "The Bills have been generated successfully ", "Success", JOptionPane.INFORMATION_MESSAGE);
////			dispose();
////		} catch (FileNotFoundException | DocumentException e) {
////			LOG.error(e.getMessage());
////		}
//	}

	private void initLayout() {

		JScrollPane treeScrollPane = new JScrollPane(billTree);
		treePanel.add(treeScrollPane);

		getContentPane().setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;

		c.gridx = 1;
		c.gridy = 1;
		c.gridwidth = 2;
		getContentPane().add(societyInfoPanel, c);

		c.gridx = 1;
		c.gridy = 2;
		c.gridwidth = 1;
		getContentPane().add(treePanel, c);

		c.gridx = 2;
		c.gridy = 2;
		c.gridwidth = 1;
		getContentPane().add(infoPanel, c);

		paymentPanel.setLayout(new GridLayout(0, 2));
		paymentPanel.add(paymentViewButton);
		paymentPanel.add(makePaymentButton);
		paymentPanel.add(paymentReceiptsButton);

		billPanel.setLayout(new GridLayout(0, 1));
		billPanel.add(billViewButton);
		billPanel.add(billGenerateButton);

		propertyPanel.setLayout(new GridLayout(0, 1));
		propertyPanel.add(propertyViewButton);
		propertyPanel.add(propertyAssetButton);
//		propertyPanel.add(propertyPayButton);

		chargePanel.setLayout(new GridLayout(0, 1));
		chargePanel.add(chargeViewButton);

		infoPanel.setLayout(new GridLayout(0, 2));
		infoPanel.add(billPanel);
		infoPanel.add(paymentPanel);
		infoPanel.add(propertyPanel);
		infoPanel.add(chargePanel);

		societyInfoPanel.setLayout(new GridBagLayout());
		JLabel label;
		label = new JLabel(SystemManager.society.getName());
		c = new GridBagConstraints();
		c.gridx = 1;
		c.gridy = 1;
		societyInfoPanel.add(label, c);

		label = new JLabel(SystemManager.society.getAddress());
		c.gridx = 1;
		c.gridy = 2;
		societyInfoPanel.add(label, c);

		label = new JLabel(
				SystemManager.society.getRegistrationNumber() + " " + SystemManager.society.getRegistrationDate());
		c.gridx = 1;
		c.gridy = 3;
		societyInfoPanel.add(label, c);

		pack();

	}

	private void prepareTreeData() {
		// fetching past 6 months generated bills
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -6);
		cal.set(Calendar.HOUR, 0);
		long time = cal.getTime().getTime();
		try {
			String query = "select " + Constants.Table.Bill.FieldName.BILL_ID + " , "
					+ Constants.Table.Bill.FieldName.BILL_TIMESTAMP + " from " + Constants.Table.Bill.TABLE_NAME
					+ " where " + Constants.Table.Bill.FieldName.BILL_TIMESTAMP + " >= " + time;
			ResultSet resultSet = SQLiteManager.executeQuery(query);
			if (resultSet != null) {
				while (resultSet.next()) {
					Calendar instance = Calendar.getInstance();
					instance.setTime(new Date(resultSet.getLong(Constants.Table.Bill.FieldName.BILL_TIMESTAMP)));
					DefaultMutableTreeNode node = new DefaultMutableTreeNode(instance.get(Calendar.MONTH));
					node.setUserObject(Bill.read(resultSet.getInt(Constants.Table.Bill.FieldName.BILL_ID)));
					rootNode.add(node);
				}
			}
			rootNode.add(new DefaultMutableTreeNode("Test"));

		} catch (SQLException e) {
			e.printStackTrace();
		}}

	@Override
	public void windowActivated(WindowEvent arg0) {

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
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
	
	private void generateMonthlyPaymentReceipts() throws SQLException, DocumentException, MalformedURLException, IOException {
		Calendar instance = Calendar.getInstance();
//		instance.add(Calendar.MONTH, -1);
		instance.set(Calendar.DAY_OF_MONTH, 1);
		instance.set(Calendar.HOUR_OF_DAY, 0);
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);
		instance.set(Calendar.MILLISECOND, 0);

		long startTime = instance.getTime().getTime();

		instance.set(Calendar.DAY_OF_MONTH, instance.getActualMaximum(Calendar.DAY_OF_MONTH));
		instance.set(Calendar.HOUR_OF_DAY, instance.getActualMaximum(Calendar.HOUR_OF_DAY));
		instance.set(Calendar.MINUTE, instance.getActualMaximum(Calendar.MINUTE));
		instance.set(Calendar.SECOND, instance.getActualMaximum(Calendar.SECOND));
		instance.set(Calendar.MILLISECOND, instance.getActualMaximum(Calendar.MILLISECOND));
		long endTime = instance.getTime().getTime();

		String query = "select payment_id from payment where payment_date >= '" + startTime + "' and payment_date <='"
				+ endTime + "' and is_cancelled = 0";
		ResultSet executeQuery = SQLiteManager.executeQuery(query);
		List<Integer> paymentIds = new ArrayList<>();
		if (executeQuery != null & executeQuery.next()) {
			do {
				paymentIds.add(executeQuery.getInt(Constants.Table.Payment.FieldName.PAYMENT_ID));
				executeQuery.next();
			} while (!executeQuery.isAfterLast());
		}

		List<com.vhi.hsm.model.Payment> payments = new ArrayList<>();
		for (Integer i : paymentIds) {
			payments.add(com.vhi.hsm.model.Payment.read(i));
		}

		PDFManager.generateReceiptsPDF(payments);
	}

}
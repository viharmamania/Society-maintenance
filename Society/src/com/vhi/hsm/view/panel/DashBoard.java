package com.vhi.hsm.view.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;
import com.vhi.hsm.controller.manager.BillManager;
import com.vhi.hsm.controller.manager.PDFManager;
import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.model.Bill;
import com.vhi.hsm.view.AssetTypeScreen;
import com.vhi.hsm.view.ChargeScreen;
import com.vhi.hsm.view.Payment;
import com.vhi.hsm.view.PropertyView;

public class DashBoard extends JFrame implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8224505516276632916L;
	private final static Logger LOG = Logger.getLogger(DashBoard.class);
	private JPanel societyInfoPanel, infoPanel, treePanel, paymentPanel, billPanel, propertyPanel, chargePanel;

	private JTree billTree;
	private DefaultMutableTreeNode rootNode;

	private JButton //propertyPayButton, 
					propertyAssetButton, propertyViewButton, billGenerateButton, billViewButton,
			chargeViewButton, paymentViewButton, makePaymentButton;

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

		propertyViewButton = new JButton("View & Edit Properties");
		propertyViewButton.addActionListener(e -> {
			new PropertyView(this);
		});

		billGenerateButton = new JButton("Generate Monthly Bill");
		billGenerateButton.addActionListener(e ->{
			generateBill();
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
		setLocationRelativeTo(null);
		initLayout();
		setVisible(true);
	}

	private void generateBill() {
		try {
			List<Bill> bills = BillManager.generateBill(SystemManager.society.getSocietyId());
			PDFManager.generateBillPDF(bills);
			JOptionPane.showMessageDialog(this, "The Bills have been generated successfully ", "Success", JOptionPane.INFORMATION_MESSAGE);
		} catch (FileNotFoundException | DocumentException e) {
			LOG.error(e.getMessage());
		}
	}

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

		paymentPanel.setLayout(new GridLayout(0, 1));
		paymentPanel.add(paymentViewButton);
		paymentPanel.add(makePaymentButton);

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
		rootNode.add(new DefaultMutableTreeNode("Node 1"));
		for (int i = 0; i < 100; i++) {
			DefaultMutableTreeNode node = new DefaultMutableTreeNode(Integer.toString(i));
			rootNode.add(node);
			node.add(new DefaultMutableTreeNode("Test"));
		}
	}

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

}

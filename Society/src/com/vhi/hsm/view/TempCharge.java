package com.vhi.hsm.view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.utils.Constants;

public class TempCharge extends JDialog implements WindowListener {

	private Map<String, Integer> chargeToIdMap = new HashMap<>();
	private final static Logger LOG = Logger.getLogger(TempCharge.class);
	private JLabel tempChargeLabel, newTempChargeLabel, newTempChargeValueLabel;
	private JComboBox<String> tempChargeComboBox;
	private JTextField newTempChargeTextField, newTempChargeValueTextField;
	private JButton confirmButton, backButton;

	/**
	 * 
	 */
	private static final long serialVersionUID = -2015821985866412659L;

	public TempCharge(JDialog parentDialog) {

		super(parentDialog);
		this.setTitle("Payments");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setLocationRelativeTo(parentDialog);
		setVisible(true);
		fetchTempCharges();
		intializeLayout();
	}

	public TempCharge() {

		this.setTitle("Temp Charges");
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setVisible(true);
		fetchTempCharges();
		intializeLayout();
	}

	private void intializeLayout() {

		tempChargeLabel = new JLabel("Available Temp charges");
		tempChargeComboBox = new JComboBox<>(fetchTempCharges());
		tempChargeComboBox.addActionListener(e -> {
			comboBoxActionListener();
		});

		newTempChargeLabel = new JLabel("New Charge Name");
		newTempChargeTextField = new JTextField();
		newTempChargeLabel.setVisible(false);
		newTempChargeTextField.setVisible(false);

		newTempChargeValueLabel = new JLabel("New Charge Amount");
		newTempChargeValueTextField = new JTextField();
		newTempChargeValueLabel.setVisible(false);
		newTempChargeValueTextField.setVisible(false);

		confirmButton = new JButton("Confirm Charge");
		confirmButton.addActionListener(e -> {
			makePayment();
		});

		backButton = new JButton("Cancel Charge");
		backButton.addActionListener(e -> {
			cancelPayment();
		});

		GroupLayout groupLayout = new GroupLayout(getContentPane());

		getContentPane().setLayout(groupLayout);

		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(tempChargeLabel)
						.addComponent(newTempChargeLabel).addComponent(newTempChargeValueLabel)
						.addComponent(confirmButton))
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(tempChargeComboBox)
						.addComponent(newTempChargeTextField).addComponent(newTempChargeValueTextField)
						.addGroup(groupLayout.createSequentialGroup().addComponent(backButton))));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(tempChargeLabel)
						.addComponent(tempChargeComboBox))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(newTempChargeLabel)
						.addComponent(newTempChargeTextField))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(newTempChargeValueLabel)
						.addComponent(newTempChargeValueTextField))
				.addGroup(groupLayout.createParallelGroup().addComponent(confirmButton).addComponent(backButton)));
		pack();

	}

	private void comboBoxActionListener() {

		if (tempChargeComboBox.getSelectedItem().equals("Other")) {
			newTempChargeLabel.setVisible(true);
			newTempChargeTextField.setVisible(true);

			newTempChargeValueLabel.setVisible(true);
			newTempChargeValueTextField.setVisible(true);

			// repaint();
		}

	}

	private void cancelPayment() {
		// TODO Auto-generated method stub

	}

	private void makePayment() {
		// TODO Auto-generated method stub

	}

	private String[] fetchTempCharges() {
		String FetchChargeQuery = "select " + Constants.Table.Charge.FieldName.CHARGE_ID + ","
				+ Constants.Table.Charge.FieldName.DESCRIPTION + " from " + Constants.Table.Charge.TABLE_NAME
				+ " where  " + Constants.Table.Charge.FieldName.TEMP_CHARGE + " =1";
		
		Set<String> charges = new HashSet<>();
		charges.add("Select..");
		try {
			ResultSet result = SQLiteManager.executeQuery(FetchChargeQuery);
			if (result != null && result.next()) {
				do {
					charges.add(result.getString(2));
					chargeToIdMap.put(result.getString(2), result.getInt(1));
					result.next();
				} while (!result.isAfterLast());

			}
			charges.add("Other");
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}

		return charges.toArray(new String[charges.size()]);

	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

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
				new TempCharge();
			});

		} else {
			// Show error message
			JOptionPane.showMessageDialog(null, "Can not connect to database", "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

}

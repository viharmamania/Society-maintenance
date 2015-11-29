package com.vhi.hsm.view;

import java.awt.Dialog;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
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
	private JTextField chequeNoTextField, remarksTextField = null;
	private JLabel chequeNoLabel, propertyNameLabel, modeOfPaymentLabel, remarksLabel = null;
	private JComboBox<String> propertyNamesComboBox, modeOfPaymentComboBox = null;
	private JButton confirmButton, backButton, uploadButton;
	private int societyId;
	private Set<Integer> propertyNames = new HashSet<>();

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
		modeOfPaymentComboBox.addActionListener(e ->{
			selectionListener();
		});

		chequeNoLabel = new JLabel("Cheque No");
		chequeNoTextField = new JTextField();

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
						.addComponent(modeOfPaymentLabel).addComponent(chequeNoLabel).addComponent(remarksLabel).addComponent(confirmButton))
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(propertyNamesComboBox)
						.addComponent(modeOfPaymentComboBox).addComponent(chequeNoTextField).addComponent(remarksTextField)
						.addGroup(groupLayout.createSequentialGroup().addComponent(backButton).addComponent(uploadButton))));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(propertyNameLabel)
						.addComponent(propertyNamesComboBox))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(modeOfPaymentLabel)
						.addComponent(modeOfPaymentComboBox))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(chequeNoLabel)
						.addComponent(chequeNoTextField))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(remarksLabel)
						.addComponent(remarksTextField))
				.addGroup(groupLayout.createParallelGroup().addComponent(confirmButton).addComponent(backButton)
						.addComponent(uploadButton)));
		pack();
		
		selectionListener();
	}

	private void selectionListener() {
		
		String selection = (String) modeOfPaymentComboBox.getSelectedItem();
		if(selection.equals(ModeOfPayment.CHEQUE.name())){
			chequeNoTextField.setEditable(true);
		}else{
			chequeNoTextField.setEditable(false);
		}
		
	}

	

	private void fetchPropertyNames() {

		if (propertyNames != null)
			if (propertyNames.size() == Property.getPropertyCount(societyId))
				return;

		Map<Integer, Integer> propertyNameToIdmap = Property.getAllProperties(societyId);
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
		this.dispose();
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
		// TODO Auto-generated method stub

	}

	private void cancelPayment() {
		// TODO Auto-generated method stub

	}

	private void makePayment() {
		if(validateInput(true)){
			
		}
	}

	private boolean validateInput(boolean showErrorMessage) {

		if (propertyNamesComboBox.getSelectedItem().equals("select")) {
			if (showErrorMessage) {
				JOptionPane.showMessageDialog(this, "Select Applicable Property ", "Error", JOptionPane.ERROR_MESSAGE);
			}
			return false;
		}

		if (modeOfPaymentComboBox.getSelectedItem().equals(ModeOfPayment.SELECT.name())) {
			if (showErrorMessage) {
				JOptionPane.showMessageDialog(this, "Select Appropriate Payment Mode", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			return false;
		}

		return true;
	}
}

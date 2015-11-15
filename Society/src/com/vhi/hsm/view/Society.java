package com.vhi.hsm.view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import com.vhi.hsm.controller.manager.SocietyManager;

/**
 * Swing Dialogue to register a new Society in HMS system.
 * 
 * @author Hardik
 *
 */
public class Society extends JDialog implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6025246483865753487L;
	
	private JTextField txtSocietyName, txtRegDate, txtRegNumber;
	private JTextArea txtAreaSocietyAddr;
	private JButton btnRegister, btnCancel;
	
	public Society(JFrame owner) {
		super(owner, "Create Society", true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		initializeLayout();
		setLocationRelativeTo(owner);
		setVisible(true);
	}

	private void initializeLayout() {
		
		txtAreaSocietyAddr = new JTextArea(3, 30);
		
		txtSocietyName = new JTextField(30);
		txtRegDate = new JTextField();
		txtRegNumber = new JTextField();
		
		txtAreaSocietyAddr.setBorder(txtSocietyName.getBorder());
		
		btnRegister = new JButton("Register");
		btnRegister.addActionListener(e -> {
			register();
		});
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> {
			cancel();
		});
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());

		getContentPane().setLayout(groupLayout);

		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);

		JLabel labName = new JLabel("Name");
		JLabel labAddress = new JLabel("Address");
		JLabel labRegNum = new JLabel("Registration Number");
		JLabel labRegDate = new JLabel("Registration Date");
		
		groupLayout.setHorizontalGroup(
				groupLayout.createSequentialGroup()
					.addGroup(
							groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(labName)
								.addComponent(labAddress)
								.addComponent(labRegNum)
								.addComponent(labRegDate)
					)
					.addGroup(
							groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(txtSocietyName)
								.addComponent(txtAreaSocietyAddr)
								.addComponent(txtRegNumber)
								.addComponent(txtRegDate)
								.addGroup(
										groupLayout.createSequentialGroup()
											.addComponent(btnRegister)
											.addComponent(btnCancel)
								)
					)
		);

		groupLayout.setVerticalGroup(
				groupLayout.createSequentialGroup()
					.addGroup(
							groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(labName)
								.addComponent(txtSocietyName)
					)
					.addGroup(
							groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(labAddress)
								.addComponent(txtAreaSocietyAddr)
					)
					.addGroup(
							groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(labRegNum)
								.addComponent(txtRegNumber)
					)
					.addGroup(
							groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(labRegDate)
								.addComponent(txtRegDate)
					)
					.addGroup(
							groupLayout.createParallelGroup()
								.addComponent(btnRegister)
								.addComponent(btnCancel)
					)
		);

		pack();
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		cancel();
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
	
	private void register() {
		if (validatedInput(true)) {
			String societyName = txtSocietyName.getText().trim();
			String societyAddress = txtAreaSocietyAddr.getText().trim();
			String regNumber = txtRegNumber.getText().trim();
			String regDate = txtRegDate.getText().trim();
			
			SocietyManager.registerSociety(societyName, societyAddress, regNumber , regDate);
		}
	}

	private boolean validatedInput(boolean showErrorMessages) {
		String societyName = txtSocietyName.getText().trim();
		String societyAddress = txtAreaSocietyAddr.getText().trim();
		String regNumber = txtRegNumber.getText().trim();
		String regDate = txtRegDate.getText().trim();
		
		
		if (societyName.length() == 0) {
			if (showErrorMessages) {
				JOptionPane.showMessageDialog(this, "Enter Society Name", "Error", JOptionPane.ERROR_MESSAGE);
			}
			return false;
		} else if (societyAddress.length() == 0) {
			if (showErrorMessages) {
				JOptionPane.showMessageDialog(this, "Enter Society Address", "Error", JOptionPane.ERROR_MESSAGE);
			}
			return false;
		} else if (regNumber.length() == 0) {
			if (showErrorMessages) {
				JOptionPane.showMessageDialog(this, "Enter Society Registration Number", "Error", JOptionPane.ERROR_MESSAGE);
			}
			return false;
		} else if (regDate.length() == 0 ) {
			if (showErrorMessages) {
				JOptionPane.showMessageDialog(this, "Enter Society Registration Date in DD-MM-YYYY format", "Error", JOptionPane.ERROR_MESSAGE);
			}
			return false;
		}
			return true;
	}

	private void cancel() {
		dispose();
	}

}


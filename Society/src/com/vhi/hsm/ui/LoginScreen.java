package com.vhi.hsm.ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LoginScreen extends JFrame implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7776929895937333850L;
	private JTextField txtUserName, txtPassword;
	private JButton btnLogin, btnCancel, btnRegister;

	/**
	 * Create the frame.
	 */
	public LoginScreen() {
		setTitle("Login");
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		initializeLayout();
	}

	private void initializeLayout() {

		txtUserName = new JTextField(30);
		txtPassword = new JTextField(30);

		btnLogin = new JButton("Log in");
		btnLogin.addActionListener(e -> {
			login();
		});
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(e -> {
			cancel();
		});
		
		btnRegister = new JButton("Register");
		btnRegister.addActionListener(e -> {
			register();
		});

		GroupLayout groupLayout = new GroupLayout(getContentPane());

		getContentPane().setLayout(groupLayout);

		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);

		JLabel labUsername = new JLabel("Username");
		JLabel labPassword = new JLabel("Password");
		
		groupLayout.setHorizontalGroup(
				groupLayout.createSequentialGroup()
					.addGroup(
							groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(labUsername)
								.addComponent(labPassword)
								.addComponent(btnRegister)
					)
					.addGroup(
							groupLayout.createParallelGroup(Alignment.TRAILING)
								.addComponent(txtUserName)
								.addComponent(txtPassword)
								.addGroup(
										groupLayout.createSequentialGroup()
											.addComponent(btnLogin)
											.addComponent(btnCancel)
								)
					)
		);

		groupLayout.setVerticalGroup(
				groupLayout.createSequentialGroup()
					.addGroup(
							groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(labUsername)
								.addComponent(txtUserName)
					)
					.addGroup(
							groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(labPassword)
								.addComponent(txtPassword)
					)
					.addGroup(
							groupLayout.createParallelGroup()
								.addComponent(btnRegister)
								.addComponent(btnLogin)
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
		
	}
	
	private void cancel() {
		dispose();
	}
	
	private void login() {
		
	}

}

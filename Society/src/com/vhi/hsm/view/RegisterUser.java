package com.vhi.hsm.view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 * Swing Dialogue to Register a new User in HMS system
 * 
 * @author Vihar
 *
 */

public class RegisterUser extends JDialog implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5757176338287483513L;

	private JTextField textUserName, textEmail, textPassword, textFullName;
	private JLabel username, email, password, fullName;
	private JButton register, cancel;

	public RegisterUser(JFrame owner) {
		super(owner, "Register User", true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		intializeLayout();
		setLocationRelativeTo(owner);
		setVisible(true);
	}

	private void intializeLayout() {
		fullName = new JLabel("Full Name");
		textFullName = new JTextField(30);

		email = new JLabel("Email");
		textEmail = new JTextField();

		username = new JLabel("Username");
		textUserName = new JTextField();

		password = new JLabel("Password");
		textPassword = new JPasswordField();

		register = new JButton("Submit");
		register.addActionListener(e -> register());

		cancel = new JButton("Cancel");
		cancel.addActionListener(e -> cancel());

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		getContentPane().setLayout(groupLayout);
		
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);

		groupLayout.setHorizontalGroup(
				groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(fullName)
						.addComponent(email)
						.addComponent(username)
						.addComponent(password)
						)
				
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(textFullName)
						.addComponent(textEmail)
						.addComponent(textUserName)
						.addComponent(textPassword)
						.addGroup(
								groupLayout.createSequentialGroup()
									.addComponent(register)
									.addComponent(cancel)
						)
				)
		);
		
		groupLayout.setVerticalGroup(
				groupLayout.createSequentialGroup()
						.addGroup(
								groupLayout.createParallelGroup(Alignment.BASELINE)
									.addComponent(fullName)
									.addComponent(textFullName))
						.addGroup(
								groupLayout.createParallelGroup(Alignment.BASELINE)
								.addComponent(email)
								.addComponent(textEmail))
						.addGroup(
								groupLayout.createParallelGroup(Alignment.BASELINE)
									.addComponent(username)
									.addComponent(textUserName))
						.addGroup(
								groupLayout.createParallelGroup(Alignment.BASELINE)
									.addComponent(password)
									.addComponent(textPassword))
						.addGroup(
								groupLayout.createParallelGroup()
									.addComponent(register)
									.addComponent(cancel)
						)
		);
		pack();
	}

	private void cancel() {
		dispose();
	}

	private Object register() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		cancel();
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

}

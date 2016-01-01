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
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;

import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.model.User;
import com.vhi.hsm.utils.Constants;

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
	private JLabel societyCodeLabel;
	private JTextField societyCodeTextField;

	public RegisterUser(JDialog owner) {
		super(owner, "Register User", true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		intializeLayout();
		setLocationRelativeTo(owner);
		setVisible(true);
	}

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

		societyCodeLabel = new JLabel("Society Code");
		societyCodeTextField = new JTextField();

		register = new JButton("Submit");
		register.addActionListener(e -> register());

		cancel = new JButton("Cancel");
		cancel.addActionListener(e -> cancel());

		GroupLayout groupLayout = new GroupLayout(getContentPane());
		getContentPane().setLayout(groupLayout);

		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);

		groupLayout
				.setHorizontalGroup(groupLayout.createSequentialGroup()
						.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(fullName)
								.addComponent(email).addComponent(username).addComponent(password)
								.addComponent(societyCodeLabel))

		.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(textFullName).addComponent(textEmail)
				.addComponent(textUserName).addComponent(textPassword).addComponent(societyCodeTextField)
				.addGroup(groupLayout.createSequentialGroup().addComponent(register).addComponent(cancel))));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(fullName)
						.addComponent(textFullName))
				.addGroup(
						groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(email).addComponent(textEmail))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(username)
						.addComponent(textUserName))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(password)
						.addComponent(textPassword))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(societyCodeLabel)
						.addComponent(societyCodeTextField))
				.addGroup(groupLayout.createParallelGroup().addComponent(register).addComponent(cancel)));
		pack();
	}

	private void cancel() {
		dispose();
	}

	private Object register() {
		if (inputValidated(true)) {
			if (SystemManager.society != null) {
				User user = User.create(SystemManager.society.getSocietyId());
				user.setName(textFullName.getText());
				user.setEmail(textEmail.getText());
				user.setUserName(textUserName.getText());

				StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
				encryptor.setPassword(Constants.SALT);
				String encryptedPassword = encryptor.encrypt(textPassword.getText());

				if (!User.save(user, encryptedPassword, true)) {
					JOptionPane.showMessageDialog(this, "Error while creating user", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					dispose();
					new Login();
				}
			}
		}
		return null;
	}

	private boolean inputValidated(boolean showErrorMessages) {

		String uName = textUserName.getText().trim();
		String pass = textPassword.getText().trim();
		String email = textEmail.getText().trim();
		String name = textFullName.getText().trim();
		if (uName.length() == 0) {
			if (showErrorMessages) {
				JOptionPane.showMessageDialog(this, "Enter Username", "Error", JOptionPane.ERROR_MESSAGE);
			}
			return false;
		} else if (uName.length() > 10) {
			if (showErrorMessages) {
				JOptionPane.showMessageDialog(this, "Username should not be more than 10 characters", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			return false;
		} else if (pass.length() == 0) {
			if (showErrorMessages) {
				JOptionPane.showMessageDialog(this, "Enter password", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else if (pass.length() < 6) {
			if (showErrorMessages) {
				JOptionPane.showMessageDialog(this, "password should be bigger than 6 characters", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
			return false;
		} else if (email.indexOf('@') == -1 || email.length() <= 2 || email.indexOf('.') == -1) {
			if (showErrorMessages) {
				JOptionPane.showMessageDialog(this, "please enter valid email Id", "Error", JOptionPane.ERROR_MESSAGE);
			}
			return false;
		} else if (name.length() == 0) {
			if (showErrorMessages) {
				JOptionPane.showMessageDialog(this, "Enter Full Name", "Error", JOptionPane.ERROR_MESSAGE);
			}
			return false;
		}

		return true;
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

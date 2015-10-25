package com.vhi.hsm.view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.vhi.hsm.db.SQLiteManager;

/**
 * First Frame that user will see in HMS system
 * 
 * @author Hardik
 *
 */
public class Login extends JFrame implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7776929895937333850L;
	private JTextField txtUserName, txtPassword;
	private JButton btnLogin, btnCancel, btnRegister;

	/**
	 * Create the frame.
	 */
	public Login() {
		setTitle("Login");
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		initializeLayout();
		setLocationRelativeTo(null);
	}

	private void initializeLayout() {

		txtUserName = new JTextField(30);
		txtPassword = new JPasswordField(30);

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

		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(labUsername)
						.addComponent(labPassword).addComponent(btnRegister))
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(txtUserName)
						.addComponent(txtPassword)
						.addGroup(groupLayout.createSequentialGroup().addComponent(btnLogin).addComponent(btnCancel))));

		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(labUsername)
						.addComponent(txtUserName))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(labPassword)
						.addComponent(txtPassword))
				.addGroup(groupLayout.createParallelGroup().addComponent(btnRegister).addComponent(btnLogin)
						.addComponent(btnCancel)));

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

		JDialog dialog = null;
		try {

			String query = "select count(*) from society";
			ResultSet result = SQLiteManager.executeQuery(query);
			result.next();
			if (result.getInt(1) != 0) {
				// register user page
				dialog = new RegisterUser();
				dialog.setTitle("register user");
				System.out.println("registering user");

			} else {
				// register society page
				dialog = new Society();
				System.out.println("registering society");
			}
			dialog.pack();
			dialog.setLocationRelativeTo(null);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void cancel() {
		dispose();
	}
	
	private void login() {
		
	}

}

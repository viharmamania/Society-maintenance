/**
 * 
 */
package com.vhi.hsm.view;

import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.model.Charge;


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
		prepareTempChargeList();
		initializeLayout();
	}

	private void prepareTempChargeList() {
		
		tempCharges = new ArrayList<Charge>();
		tempChargeCheckBox = new HashMap<Integer, JCheckBox>();
		
		ArrayList<Charge> allCharges = Charge.getAllCharge(SystemManager.society.getSocietyId());
		for (Charge c : allCharges) {
			if (c.isTempCharges() && ! c.isCancelled()) {
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
			
		});
		
		previewBills = new JButton("Preveiw Bills");
		previewBills.addActionListener(e -> {
			
		});
		
		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(
			layout.createParallelGroup()
				.addComponent(tempChargePanel)
				.addGroup(layout.createSequentialGroup()
					.addComponent(previewBills)
					.addComponent(generateBills)
				)
		);
		
		layout.setVerticalGroup(
			layout.createSequentialGroup()
				.addComponent(tempChargePanel)
				.addGroup(layout.createParallelGroup()
					.addComponent(previewBills)
					.addComponent(generateBills)
				)
		);
		
		pack();
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

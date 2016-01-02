package com.vhi.hsm.view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.model.AssetType;

public class AssetTypeEdit extends JDialog implements WindowListener {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2019471594533712359L;
	private int societyId;
	private AssetType assetType;
	private JLabel assetTypeLabel,descriptionLabel,chargeLabel;
	private JTextField assetTypeField,descriptionField,chargeField;
	private JButton confirmButton,cancelButton;
	
	public AssetTypeEdit(JDialog parentDialog, AssetType assetType) {
		super(parentDialog);
		this.setTitle("Property Asset");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setLocationRelativeTo(parentDialog);
		setVisible(true);
		societyId = SystemManager.society.getSocietyId();
		intializeLayout();
		this.assetType = assetType;
	}

	private void intializeLayout() {
		/*private JLabel assetTypeLabel,descriptionLabel,chargeLabel;
	private JTextField assetTypeField,descriptionField,chargeField;
	private JButton confirmButton,cancelButton;*/
		
		assetTypeLabel = new JLabel("Asset Type");
		assetTypeField = new JTextField();
		assetTypeField.setText(assetType.getAssetType());

		descriptionLabel = new JLabel("Description");
		descriptionField = new JTextField();
		descriptionField.setText(assetType.getDescription());
		
		chargeLabel = new JLabel("Charge");
		chargeField = new JTextField();
		chargeField.setText(String.valueOf(assetType.getCharges()));
		
		confirmButton = new JButton("Cancel Payment");
		confirmButton.addActionListener(e -> {
			confirmEdit();
		});

		cancelButton = new JButton("Upload Excel");
		cancelButton.addActionListener(e -> {
			cancelEdit();
		});
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());

		getContentPane().setLayout(groupLayout);

		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(assetTypeLabel)
						.addComponent(descriptionLabel).addComponent(chargeLabel).addComponent(confirmButton))
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(assetTypeField)
						.addComponent(descriptionField).addComponent(chargeField)
						.addGroup(groupLayout.createSequentialGroup().addComponent(cancelButton))));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(assetTypeLabel)
						.addComponent(assetTypeField))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(descriptionLabel)
						.addComponent(descriptionField))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(chargeLabel)
						.addComponent(chargeField))
				.addGroup(groupLayout.createParallelGroup().addComponent(confirmButton).addComponent(cancelButton)
						));
		pack();
		
	}

	private void confirmEdit() {
		// TODO Auto-generated method stub
		
	}

	private void cancelEdit() {
		// TODO Auto-generated method stub
		
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

}

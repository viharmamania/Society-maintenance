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
import javax.swing.JTextField;

import org.apache.log4j.Logger;

import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.AssetType;
import com.vhi.hsm.utils.Constants;

public class AssetTypeView extends JDialog implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9221087433400751884L;
	private final static Logger LOG = Logger.getLogger(AssetTypeView.class);
	private int societyId;
	private JLabel availableAssets;
	private JComboBox<String> availableAssetTypeCombobox;
	private JButton createNewButton, editButton, backButton;
	private JLabel assetTypeLabel,descriptionLabel,chargeLabel;
	private JTextField assetTypeField,descriptionField,chargeField;
	private Map<String, AssetType> descriptionToAssetObjectMap = new HashMap<>(); 

	public AssetTypeView(JDialog parentDialog) {

		super(parentDialog);
		this.setTitle("Property Asset");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setLocationRelativeTo(parentDialog);
		setVisible(true);
		societyId = SystemManager.society.getSocietyId();
		fetchPropertyAssets();
		intializeLayout();
	}
	
	public AssetTypeView() {

		this.setTitle("Property Asset");
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setVisible(true);
		societyId = SystemManager.society.getSocietyId();
		fetchPropertyAssets();
		intializeLayout();
	
	}

	private String[] fetchPropertyAssets() {

		String propertyAssetsQuery = "select * from " + Constants.Table.PropertyAsset.TABLE_NAME + " where "
				+ Constants.Table.Society.FieldName.SOCIETY_ID + "  = " + societyId;
		
		Set<String> assetSet = new HashSet<>();
		ResultSet result = SQLiteManager.executeQuery(propertyAssetsQuery);
		
		try {
			if (result != null && result.next()) {
				do {
					String desc = result.getString(Constants.Table.AssetType.FieldName.DESCRIPTION);
					String assetType = result.getString(Constants.Table.AssetType.FieldName.ASSET_TYPE);
					double charge = result.getDouble(Constants.Table.AssetType.FieldName.CHARGE);
					
					AssetType type = AssetType.create(societyId);
					type.setAssetType(assetType);
					type.setDescription(desc);
					//type.setCharges(charge);
					
					assetSet.add(desc);
					descriptionToAssetObjectMap.put(desc, type);
					
					
				} while (!result.isAfterLast());
			}
		} catch (SQLException e) {
			LOG.error(e.getMessage());
		}
		return assetSet.toArray(new String[assetSet.size()]);
	}

	private void intializeLayout() {
		availableAssets = new JLabel("Available Asset Type");
		availableAssetTypeCombobox = new JComboBox<>(fetchPropertyAssets());
		availableAssetTypeCombobox.addActionListener(e->{ onSelected();});
		
		
		
		assetTypeLabel = new JLabel("Asset Type");
		assetTypeField = new JTextField();
		assetTypeField.setVisible(false);
		assetTypeLabel.setVisible(false);
//		assetTypeField.setText(assetType.getAssetType());

		descriptionLabel = new JLabel("Description");
		descriptionField = new JTextField();
		descriptionLabel.setVisible(false);
		descriptionField.setVisible(false);
//		descriptionField.setText(assetType.getDescription());
		
		chargeLabel = new JLabel("Charge");
		chargeField = new JTextField();
		chargeLabel.setVisible(false);
		chargeField.setVisible(false);
//		chargeField.setText(String.valueOf(assetType.getCharges()));
		
		createNewButton  = new JButton("Create New");
		createNewButton.addActionListener(e -> {
			createNewAssetType();
		});

		backButton = new JButton("Cancel");
		backButton.addActionListener(e -> {
			backPressed();
		});

		editButton = new JButton("Edit");
		editButton.addActionListener(e -> {
			String selectedItem = (String) availableAssetTypeCombobox.getSelectedItem();
			
			editAssetType(descriptionToAssetObjectMap.get(selectedItem));
		});
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());

		getContentPane().setLayout(groupLayout);

		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		
		groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(availableAssets)
						.addComponent(assetTypeLabel).addComponent(descriptionLabel).addComponent(chargeLabel).addComponent(createNewButton))
				.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(availableAssetTypeCombobox)
						.addComponent(assetTypeField).addComponent(descriptionField).addComponent(chargeField)
						.addGroup(groupLayout.createSequentialGroup().addComponent(editButton).addComponent(backButton))));
		
		groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(availableAssets)
						.addComponent(availableAssetTypeCombobox))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(assetTypeLabel)
						.addComponent(assetTypeField))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(descriptionLabel)
						.addComponent(descriptionField))
				.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(chargeLabel)
						.addComponent(chargeField))
				.addGroup(groupLayout.createParallelGroup().addComponent(createNewButton).addComponent(editButton)
						.addComponent(backButton)));
		pack();

	}

	private void onSelected() {

		String selectedItem = (String) availableAssetTypeCombobox.getSelectedItem();
		AssetType asset = descriptionToAssetObjectMap.get(selectedItem);

		assetTypeField.setVisible(true);
		assetTypeLabel.setVisible(true);
		assetTypeField.setText(asset.getAssetType());
		assetTypeField.setEditable(false);

		descriptionLabel.setVisible(true);
		descriptionField.setVisible(true);
		descriptionField.setText(asset.getDescription());
		descriptionField.setEditable(false);

		chargeLabel.setVisible(true);
		chargeField.setVisible(true);
		chargeField.setText(String.valueOf(asset.getCharges()));
		chargeField.setEditable(false);
	}

	private void editAssetType(AssetType assetType) {
		
		new AssetTypeEdit(this,assetType);
		
	}

	private void backPressed() {
		// TODO Auto-generated method stub
		
	}

	private void createNewAssetType() {
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

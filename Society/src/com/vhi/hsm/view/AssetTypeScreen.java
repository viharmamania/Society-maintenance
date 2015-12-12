/**
 * 
 */
package com.vhi.hsm.view;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.AssetType;
import com.vhi.hsm.model.Society;
import com.vhi.hsm.view.masterdetail.MasterDetailPanel;
import com.vhi.hsm.view.masterdetail.MasterDetailPanel.MasterDetailCallback;
import com.vhi.hsm.view.masterdetail.MasterListPanel.MasterListItem;

/**
 * @author Hardik Senghani
 *
 */
public class AssetTypeScreen extends JDialog implements WindowListener {

	private static final long serialVersionUID = -3655110841225401217L;

	private MasterDetailPanel assetTypeMasterDetailPanel;
	private HashMap<Integer, AssetTypeMasterListItem> listItems;
	private AssetTypeDetails detailsPanel;
	private MasterDetailCallback callback = new MasterDetailCallback() {

		@Override
		public boolean showDeleteButton() {
			return true;
		}

		@Override
		public boolean showAddButton() {
			return true;
		}

		@Override
		public void itemSelected(int id) {
			AssetTypeMasterListItem item = listItems.get(id);
			if (item != null) {
				detailsPanel.setAssetType(item.assetType);
			}
		}

		@Override
		public void deleteItem(int id) {
			AssetTypeMasterListItem item = listItems.get(id);
			if (item != null) {
				item.delete();
				prepareList();
			}
		}

		@Override
		public void addNewItem() {
			AssetTypeMasterListItem item = new AssetTypeMasterListItem("", false);
			listItems.put(item.getItemId(), item);
			assetTypeMasterDetailPanel.getMasterListPanel().addListItem(item);
		}

		@Override
		public boolean showSaveButton() {
			return true;
		}

		@Override
		public void saveItem(int id) {
			AssetTypeMasterListItem item = listItems.get(id);
			if (item != null) {
				detailsPanel.getFieldValues(item.assetType);
				item.save();
			}
		}

	};

	public AssetTypeScreen(JFrame parent) {
		super(parent);
		this.setTitle("Asset Types");
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setLocationRelativeTo(parent);
		setVisible(true);
		listItems = new HashMap<Integer, AssetTypeMasterListItem>();
		assetTypeMasterDetailPanel = new MasterDetailPanel(callback);
		detailsPanel = new AssetTypeDetails();
		assetTypeMasterDetailPanel.getMasterDetailPanel().setDetailPanel(detailsPanel);
		prepareList();
		initializeLayout();
	}
	
	private void initializeLayout() {
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(assetTypeMasterDetailPanel));
		layout.setVerticalGroup(layout.createParallelGroup().addComponent(assetTypeMasterDetailPanel));
		getContentPane().setLayout(layout);
		pack();
	}

	private void prepareList() {
		listItems.clear();
		assetTypeMasterDetailPanel.getMasterListPanel().clearAllItems();
		ArrayList<AssetType> list = AssetType.getAllAssetType(SystemManager.society.getSocietyId());
		for (AssetType assetType : list) {
			AssetTypeMasterListItem item = new AssetTypeMasterListItem(assetType.getAssetType(), true);
			listItems.put(item.getItemId(), item);
			assetTypeMasterDetailPanel.getMasterListPanel().addListItem(item);
		}
	}

	public void saveAll() {
		Set<Integer> keys = listItems.keySet();
		for (Integer id : keys) {
			listItems.get(id).save();
		}
	}

	private static class AssetTypeMasterListItem extends MasterListItem {

		private static final long serialVersionUID = -3072620090535928759L;
		AssetType assetType;
		int itemId;
		boolean isNewAssetType;
		private static int itemIdCounter = 0;
		JLabel assetTypeLable, descriptionLable;

		public AssetTypeMasterListItem(String assetType, boolean readExisting) {
			super();
			if (readExisting) {
				this.assetType = AssetType.read(SystemManager.society.getSocietyId(), assetType);
				isNewAssetType = false;
			} else {
				this.assetType = AssetType.create(SystemManager.society.getSocietyId());
				isNewAssetType = true;
			}

			if (!isNewAssetType) {
				itemId = this.assetType.hashCode();
			} else {
				itemId = --itemIdCounter;
			}

			assetTypeLable = new JLabel(this.assetType.getAssetType());
			descriptionLable = new JLabel(this.assetType.getDescription());
			
			GroupLayout layout = new GroupLayout(this);
			setLayout(layout);
			layout.setHorizontalGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(assetTypeLable).addComponent(descriptionLable));
			layout.setVerticalGroup(
					layout.createSequentialGroup().addComponent(assetTypeLable).addComponent(descriptionLable));
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
		}

		public void save() {
			AssetType.save(assetType, isNewAssetType);
			assetTypeLable.setText(this.assetType.getAssetType());
			descriptionLable.setText(this.assetType.getDescription());
		}

		public void delete() {
			AssetType.delete(assetType);
		}

		@Override
		public int getItemId() {
			return itemId;
		}

	}
	
	private static class AssetTypeDetails extends JPanel {

		private static final long serialVersionUID = 6035574266262639889L;
		
		JLabel assetTypeLable, descriptionLabel, chargeLabel;
		JTextField assetTypeField, descriptionField, chargeField;
		
		public AssetTypeDetails() {
			assetTypeLable = new JLabel("Asset Type");
			descriptionLabel = new JLabel("Description");
			chargeLabel = new JLabel("Charge");
			
			assetTypeField = new JTextField(30);
			descriptionField = new JTextField();
			chargeField = new JTextField();
			
			GroupLayout layout = new GroupLayout(this);
			setLayout(layout);
			layout.setAutoCreateContainerGaps(true);
			layout.setAutoCreateGaps(true);
			
			layout.setHorizontalGroup(
					layout.createSequentialGroup()
						.addGroup(
								layout.createParallelGroup(Alignment.LEADING)
									.addComponent(assetTypeLable)
									.addComponent(descriptionLabel)
									.addComponent(chargeLabel)
						)
						.addGroup(
								layout.createParallelGroup(Alignment.LEADING)
								.addComponent(assetTypeField)
								.addComponent(descriptionField)
								.addComponent(chargeField)
						)
			);
			
			layout.setVerticalGroup(
					layout.createSequentialGroup()
						.addGroup(
								layout.createParallelGroup(Alignment.BASELINE)
									.addComponent(assetTypeLable)
									.addComponent(assetTypeField)
						)
						.addGroup(
								layout.createParallelGroup(Alignment.BASELINE)
									.addComponent(descriptionLabel)
									.addComponent(descriptionField)
						)
						.addGroup(
								layout.createParallelGroup(Alignment.BASELINE)
									.addComponent(chargeLabel)
									.addComponent(chargeField)
						)
			);
		}
		
		public void setAssetType(AssetType assetType) {
			assetTypeField.setText(assetType.getAssetType());
			descriptionField.setText(assetType.getDescription());
			chargeField.setText(Double.toString(assetType.getCharges()));
		}
		
		public void getFieldValues(AssetType assetType) {
			assetType.setAssetType(assetTypeField.getText());
			assetType.setDescription(descriptionField.getText());
			assetType.setCharges(Double.parseDouble(chargeField.getText()));
		}
		
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
				SystemManager.society = Society.read(1);
				new AssetTypeScreen(null);
			});

		} else {
			// Show error message
			JOptionPane.showMessageDialog(null, "Can not connect to database", "Error", JOptionPane.ERROR_MESSAGE);
		}
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
	
	public void cancel() {
		dispose();
	}

}

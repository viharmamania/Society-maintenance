package com.vhi.hsm.view;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.model.AssetType;
import com.vhi.hsm.model.Property;
import com.vhi.hsm.view.masterdetail.MasterDetailPanel;
import com.vhi.hsm.view.masterdetail.MasterDetailPanel.MasterDetailCallback;
import com.vhi.hsm.view.masterdetail.MasterListPanel.MasterListItem;

public class PropertyAsset extends JDialog implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MasterDetailPanel propertyAssetsMasterDetailPanel;
	private HashMap<Integer, PropertyAssetMasterListItem> listItems;
	private MasterDetailCallback callback = new MasterDetailCallback() {

		@Override
		public boolean showDeleteButton() {
			return false;
		}

		@Override
		public boolean showAddButton() {
			return false;
		}

		@Override
		public void itemSelected(int id) {
			PropertyAssetMasterListItem item = listItems.get(id);
			if (item != null) {
				propertyAssetsMasterDetailPanel.getMasterDetailPanel().setDetailPanel(item.detailPanel);
				repaint();
//				pack();
			}
		}

		@Override
		public void deleteItem(int id) {
		}

		@Override
		public void addNewItem() {
		}

		@Override
		public boolean showSaveButton() {
			return true;
		}

		@Override
		public void saveItem(int id) {
			PropertyAssetMasterListItem item = listItems.get(id);
			if (item != null) {
				item.detailPanel.save();
			}
		}

	};

	public PropertyAsset(JFrame parent) {
		super(parent);
		this.setTitle("Property Assets");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		listItems = new HashMap<Integer, PropertyAssetMasterListItem>();
		propertyAssetsMasterDetailPanel = new MasterDetailPanel(callback);
		prepareList();
		initializeLayout();
		setPreferredSize(new Dimension(800, 400));
//		setSize(new Dimension(800, 400));
		setVisible(true);
		setResizable(true);
		setLocationRelativeTo(parent);
	}

	private void initializeLayout() {
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(propertyAssetsMasterDetailPanel));
		layout.setVerticalGroup(layout.createParallelGroup().addComponent(propertyAssetsMasterDetailPanel));
		getContentPane().setLayout(layout);
		pack();
	}

	private void prepareList() {
		ArrayList<Property> allSocietyProperties = (ArrayList<Property>) Property
				.getAllProperties(SystemManager.society.getSocietyId());
		listItems.clear();
		for (Property property : allSocietyProperties) {
			PropertyAssetMasterListItem item = new PropertyAssetMasterListItem(property.getPropertyId());
			listItems.put(property.getPropertyId(), item);
			propertyAssetsMasterDetailPanel.getMasterListPanel().addListItem(item);
		}
	}

	private static class PropertyAssetMasterListItem extends MasterListItem {

		private static final long serialVersionUID = -3072620090535928759L;
		Property property;
		int itemId;
		JLabel propertyNameLable, ownerNameLable;
		PropertyAssetDetails detailPanel;

		public PropertyAssetMasterListItem(int propertyId) {
			super();

			property = Property.read(propertyId);
			this.itemId = propertyId;

			propertyNameLable = new JLabel(this.property.getPropertyName());
			ownerNameLable = new JLabel(this.property.getOwnerName());
			
			detailPanel = new PropertyAssetDetails(property);

			GroupLayout layout = new GroupLayout(this);
			setLayout(layout);
			layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(propertyNameLable).addComponent(ownerNameLable));
			layout.setVerticalGroup(
					layout.createSequentialGroup().addComponent(propertyNameLable).addComponent(ownerNameLable));
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
		}

		@Override
		public int getItemId() {
			return itemId;
		}

	}

	public static class PropertyAssetDetails extends JPanel {

		private static final long serialVersionUID = 6035574266262639889L;

		Property property;
		JLabel propertyNameLable;
		ArrayList<PropertyAssetTypeAssetsPanel> assetTypePanels;
		int newAssetNumber;

		public PropertyAssetDetails(Property property) {
			
			this.property = property;
			
			propertyNameLable = new JLabel("Property: " + this.property.getPropertyName());

			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			add(propertyNameLable);

			assetTypePanels = new ArrayList<>();

			ArrayList<AssetType> allAssetTypes = AssetType.getAllAssetType(SystemManager.society.getSocietyId());
			ArrayList<com.vhi.hsm.model.PropertyAsset> allAssets = com.vhi.hsm.model.PropertyAsset
					.getAllPropertyAssets(this.property.getPropertyId());

			newAssetNumber = 0;
			for (com.vhi.hsm.model.PropertyAsset asset : allAssets) {
				if (newAssetNumber < asset.getAssetNumber()) {
					newAssetNumber = asset.getAssetNumber();
				}
			}

			for (AssetType assetType : allAssetTypes) {
				PropertyAssetTypeAssetsPanel panel = new PropertyAssetTypeAssetsPanel(this, assetType, allAssets);
				assetTypePanels.add(panel);
				add(panel);
			}

		}

		public int getNewAssetNumber() {
			return ++newAssetNumber;
		}

		public void save() {
			for (PropertyAssetTypeAssetsPanel panel : assetTypePanels) {
				panel.save();
			}
		}

	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowClosed(WindowEvent e) {
		cancel();
	}

	@Override
	public void windowClosing(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
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

	private void cancel() {
		dispose();
	}

}

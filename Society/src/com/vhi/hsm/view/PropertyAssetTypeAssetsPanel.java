package com.vhi.hsm.view;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.model.AssetType;
import com.vhi.hsm.model.PropertyAsset;
import com.vhi.hsm.view.PropertyAsset.PropertyAssetDetails;

public class PropertyAssetTypeAssetsPanel extends JPanel {

	/**
	 * 	
	 */
	private static final long serialVersionUID = 1L;

	private AssetType assetType;

	JTable assetsList;
	Vector<PropertyAsset> propertyAssetList;
	PropertyAssetTableModel assetsTableModel;
	ArrayList<PropertyAsset> allPropertyAssets;

	ArrayList<PropertyAsset> deletedAssets;
	ArrayList<PropertyAsset> newAssets;

	PropertyAssetDetails parent;

	JButton addButton, removeButton;

	public PropertyAssetTypeAssetsPanel(PropertyAssetDetails parent, AssetType assetType,
			ArrayList<PropertyAsset> allAssets) {

		this.assetType = assetType;

		allPropertyAssets = allAssets;
		this.parent = parent;

		propertyAssetList = new Vector<PropertyAsset>();

		deletedAssets = new ArrayList<>();
		newAssets = new ArrayList<>();

		assetsTableModel = new PropertyAssetTableModel();
		prepareAssets();

		assetsList = new JTable(assetsTableModel);
		assetsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		assetsList.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

		addButton = new JButton("Add");
		addButton.addActionListener(e -> {
			if (assetsTableModel != null && assetsList != null) {
				PropertyAsset asset = PropertyAsset.create(SystemManager.society.getSocietyId());
				asset.setPropertyId(parent.property.getPropertyId());
				asset.setAssetType(this.assetType.getAssetType());
				asset.setAssetNumber(parent.getNewAssetNumber());
				newAssets.add(asset);
				propertyAssetList.add(asset);

				assetsTableModel.fireTableDataChanged();
			}
		});

		removeButton = new JButton("Remove");
		removeButton.addActionListener(e -> {
			if (assetsTableModel != null && assetsList != null) {
				int index = assetsList.getSelectedRow();
				PropertyAsset asset = propertyAssetList.get(index);
				if(newAssets.contains(asset)) {
					newAssets.remove(asset);
				} else {
					deletedAssets.add(asset);
				}
				propertyAssetList.remove(index);
				assetsTableModel.fireTableDataChanged();
			}
		});

		setBorder(BorderFactory.createTitledBorder(assetType.getDescription()));
		
		JScrollPane tableScrollPane = new JScrollPane(assetsList,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		tableScrollPane.setViewportView(assetsList);
//		tableScrollPane.setMinimumSize(new Dimension(500, 100));
		
		GroupLayout layout = new GroupLayout(this);
		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);
		
		layout.setHorizontalGroup(
				layout.createSequentialGroup()
					.addGroup(
							layout.createParallelGroup(Alignment.TRAILING)
								.addComponent(tableScrollPane)
								.addGroup(layout.createSequentialGroup()
										.addComponent(addButton)
										.addComponent(removeButton))
							)
					);
		
		layout.setVerticalGroup(
				layout.createParallelGroup()
					.addGroup(
							layout.createSequentialGroup()
								.addComponent(tableScrollPane)
								.addGroup(layout.createParallelGroup(Alignment.BASELINE)
										.addComponent(addButton)
										.addComponent(removeButton))
							)
					);
		
		setLayout(layout);
	}

	public void save() {

		if (deletedAssets.size() > 0) {
			for (PropertyAsset asset : deletedAssets) {
				PropertyAsset.delete(asset);
			}
			deletedAssets.clear();
		}

		for (PropertyAsset asset : propertyAssetList) {
			if (newAssets.contains(asset)) {
				if (PropertyAsset.save(asset, true)) {
					newAssets.remove(asset);
				}
			} else {
				PropertyAsset.save(asset, false);
			}
		}
		
		assetsTableModel.fireTableDataChanged();

	}

	private void prepareAssets() {

		for (PropertyAsset asset : allPropertyAssets) {
			if (asset.getAssetType().equals(assetType.getAssetType())) {
				propertyAssetList.add(asset);
			}
		}

		if (propertyAssetList.size() > 0) {
			assetsTableModel.fireTableDataChanged();
		}
	}

	class PropertyAssetTableModel extends DefaultTableModel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public int getRowCount() {
			if (propertyAssetList != null) {
				return propertyAssetList.size();
			}
			return 0;
		}

		@Override
		public Class<?> getColumnClass(int index) {
			switch (index) {
			case 0:
				return Integer.class;
			case 1:
				return String.class;
			case 2:
				return Boolean.class;
			default:
				break;
			}
			return super.getColumnClass(index);
		}

		@Override
		public String getColumnName(int index) {
			switch (index) {
			case 0:
				return "Number";
			case 1:
				return "Detail";
			case 2:
				return "Cancelled";
			default:
				break;
			}
			return super.getColumnName(index);
		}

		@Override
		public int getColumnCount() {
			return 3;
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			if (col == 0)
				return false;
			return true;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			if (propertyAssetList != null) {
				PropertyAsset propertyAsset = propertyAssetList.get(rowIndex);
				switch (columnIndex) {
				case 0:
					return propertyAsset.getAssetNumber();

				case 1:
					return propertyAsset.getAssetDetails();

				case 2:
					return propertyAsset.isCancelled();

				default:
					break;
				}
			}
			return null;
		}

		@Override
		public void setValueAt(Object value, int row, int col) {

			if (propertyAssetList != null) {
				PropertyAsset propertyAsset = propertyAssetList.get(row);
				switch (col) {
				case 0:
					break;

				case 1:
					propertyAsset.setAssetDetails(value.toString());

				case 2:
					propertyAsset.setCancelled(Boolean.parseBoolean(value.toString()));

				default:
					break;
				}
			}
			fireTableCellUpdated(row, col);
		}

	}

}

/**
 * 
 */
package com.vhi.hsm.view;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.model.AssetType;
import com.vhi.hsm.model.Charge;
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
				assetTypeMasterDetailPanel.getMasterDetailPanel().setDetailPanel(detailsPanel);
				detailsPanel.setAssetType(item.assetType, !item.isNewAssetType);
				pack();
			}
		}

		@Override
		public void deleteItem(int id) {
			AssetTypeMasterListItem item = listItems.get(id);
			if (item != null) {
				item.delete();
				prepareList();
//				assetTypeMasterDetailPanel.getMasterDetailPanel().removePanel();
			}
		}

		@Override
		public void addNewItem() {
			AssetTypeMasterListItem item = new AssetTypeMasterListItem("", false);
			listItems.put(item.getItemId(), item);
			assetTypeMasterDetailPanel.getMasterListPanel().addListItem(item);
			callback.itemSelected(item.getItemId());
			repaint();
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
				detailsPanel.setAssetType(item.assetType, !item.isNewAssetType);
			}
		}

	};

	public AssetTypeScreen(JFrame parent) {
		super(parent);
		this.setTitle("Asset Types");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		listItems = new HashMap<Integer, AssetTypeMasterListItem>();
		assetTypeMasterDetailPanel = new MasterDetailPanel(callback);
		detailsPanel = new AssetTypeDetails();
		prepareList();
		initializeLayout();
		setPreferredSize(new Dimension(500,300));
		setVisible(true);
		setResizable(false);
		setLocationRelativeTo(parent);
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
			layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(assetTypeLable).addComponent(descriptionLable));
			layout.setVerticalGroup(
					layout.createSequentialGroup().addComponent(assetTypeLable).addComponent(descriptionLable));
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
		}

		public void save() {
			if (validateInput()) {
				AssetType.save(assetType, isNewAssetType);
				isNewAssetType = false;
				assetTypeLable.setText(this.assetType.getAssetType());
				descriptionLable.setText(this.assetType.getDescription());
			}
		}

		public void delete() {
			AssetType.delete(assetType);
		}

		@Override
		public int getItemId() {
			return itemId;
		}

		private boolean validateInput() {

			if (assetType.getAssetType().trim().length() == 0) {
				
				JOptionPane.showMessageDialog(this,
						"Enter asset type", "Error",
						JOptionPane.ERROR_MESSAGE);
				return false;
				
			} else {

				if (isNewAssetType) {
					AssetType type = AssetType.read(assetType.getSocietyId(), assetType.getAssetType());
					if (type != null) {
						// duplicate entry
						JOptionPane.showMessageDialog(this,
								"Asset Type " + assetType.getAssetType() + " is already exist", "Error",
								JOptionPane.ERROR_MESSAGE);
						return false;
					} else {
						if (assetType.getAssetType().contains(" ")) {
							JOptionPane.showMessageDialog(this,
									"Space is not allowed in asset type", "Error",
									JOptionPane.ERROR_MESSAGE);
							return false;
						}
					}
				}

			}
			
			

			return true;
		}

	}

	private static class AssetTypeDetails extends JPanel {

		private static final long serialVersionUID = 6035574266262639889L;

		JLabel assetTypeLable, descriptionLabel, chargeLabel;
		JTextField assetTypeField, descriptionField;// chargeField;
		JComboBox<Charge> chargeIdComboBox;
		DefaultComboBoxModel<Charge> chargeIdComboBoxModel;

		public AssetTypeDetails() {
			assetTypeLable = new JLabel("Asset Type");
			descriptionLabel = new JLabel("Description");
			chargeLabel = new JLabel("Charge");

			assetTypeField = new JTextField(30);
			descriptionField = new JTextField();
			chargeIdComboBoxModel = new DefaultComboBoxModel<>();
			
			chargeIdComboBoxModel.addElement(null);
			ArrayList<Charge> allCharges = Charge.getAllCharge(SystemManager.society.getSocietyId());
			for (Charge charge : allCharges) {
				if (!charge.isdefault() && !charge.isCancelled() && !charge.isTempCharges()) {
					chargeIdComboBoxModel.addElement(charge);
				}
			}
			
			chargeIdComboBox = new JComboBox<>(chargeIdComboBoxModel);

			GroupLayout layout = new GroupLayout(this);
			setLayout(layout);
			layout.setAutoCreateContainerGaps(true);
			layout.setAutoCreateGaps(true);

			layout.setHorizontalGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(assetTypeLable)
							.addComponent(descriptionLabel).addComponent(chargeLabel))
					.addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(assetTypeField)
							.addComponent(descriptionField).addComponent(chargeIdComboBox)));

			layout.setVerticalGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(assetTypeLable)
							.addComponent(assetTypeField))
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(descriptionLabel)
							.addComponent(descriptionField))
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(chargeLabel)
							.addComponent(chargeIdComboBox)));
		}

		public void setAssetType(AssetType assetType, boolean disableAssetType) {
			assetTypeField.setText(assetType.getAssetType());
			assetTypeField.setEditable(!disableAssetType);
			descriptionField.setText(assetType.getDescription());
			Charge read = Charge.read(SystemManager.society.getSocietyId(), assetType.getChargeId());
//			chargeField.setText(Double.toString(read.getAmount()));
			chargeIdComboBox.setSelectedIndex(chargeIdComboBoxModel.getIndexOf(read));
		}

		public void getFieldValues(AssetType assetType) {
			assetType.setAssetType(assetTypeField.getText());
			assetType.setDescription(descriptionField.getText());
			if (chargeIdComboBox.getSelectedIndex() != -1 && chargeIdComboBox.getSelectedIndex() != 0) {
				assetType.setChargeId(chargeIdComboBoxModel.getElementAt(chargeIdComboBox.getSelectedIndex()).getChargeId());
			} else {
				assetType.setChargeId(0);
			}
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

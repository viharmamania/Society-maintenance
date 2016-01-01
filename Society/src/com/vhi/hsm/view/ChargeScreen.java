package com.vhi.hsm.view;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.model.Charge;
import com.vhi.hsm.view.masterdetail.MasterDetailPanel;
import com.vhi.hsm.view.masterdetail.MasterDetailPanel.MasterDetailCallback;
import com.vhi.hsm.view.masterdetail.MasterListPanel.MasterListItem;

/**
 * @author Hardik Senghani
 *
 */
public class ChargeScreen extends JDialog implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MasterDetailPanel chargeMasterDetailPanel;
	private HashMap<Integer, ChargeMasterListItem> listItems;
	private ChargeDetails detailsPanel;
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
			ChargeMasterListItem item = listItems.get(id);
			if (item != null) {
				chargeMasterDetailPanel.getMasterDetailPanel().setDetailPanel(detailsPanel);
				detailsPanel.setCharge(item.charge);
				pack();
			}
		}

		@Override
		public void deleteItem(int id) {
			ChargeMasterListItem item = listItems.get(id);
			if (item != null) {
				item.delete();
				prepareList();
				chargeMasterDetailPanel.getMasterDetailPanel().removePanel();
			}
		}

		@Override
		public void addNewItem() {
			ChargeMasterListItem item = new ChargeMasterListItem(-1, false);
			listItems.put(item.getItemId(), item);
			chargeMasterDetailPanel.getMasterListPanel().addListItem(item);
		}

		@Override
		public boolean showSaveButton() {
			return true;
		}

		@Override
		public void saveItem(int id) {
			ChargeMasterListItem item = listItems.get(id);
			if (item != null) {
				detailsPanel.getFieldValues(item.charge);
				item.save();
				detailsPanel.setCharge(item.charge);
			}
		}

	};

	public ChargeScreen(JFrame parent) {
		super(parent);
		this.setTitle("Charges");
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setLocationRelativeTo(parent);
		setVisible(true);
		listItems = new HashMap<Integer, ChargeMasterListItem>();
		chargeMasterDetailPanel = new MasterDetailPanel(callback);
		detailsPanel = new ChargeDetails();
		prepareList();
		getContentPane().add(chargeMasterDetailPanel);
		pack();
	}

	private void prepareList() {
		listItems.clear();
		chargeMasterDetailPanel.getMasterListPanel().clearAllItems();
		ArrayList<Charge> list = Charge.getAllCharge(SystemManager.society.getSocietyId());
		for (Charge charge : list) {
			ChargeMasterListItem item = new ChargeMasterListItem(charge.getChargeId(), true);
			listItems.put(item.getItemId(), item);
			chargeMasterDetailPanel.getMasterListPanel().addListItem(item);
		}
	}

	public void saveAll() {
		Set<Integer> keys = listItems.keySet();
		for (Integer id : keys) {
			listItems.get(id).save();
		}
	}

	private static class ChargeMasterListItem extends MasterListItem {

		private static final long serialVersionUID = -3072620090535928759L;
		Charge charge;
		int itemId;
		boolean isNewCharge;
		private static int itemIdCounter = 0;
		JLabel descriptionLable;

		public ChargeMasterListItem(int chargeId, boolean readExisting) {
			super();
			if (readExisting) {
				this.charge = Charge.read(SystemManager.society.getSocietyId(), chargeId);
				isNewCharge = false;
			} else {
				this.charge = Charge.create(SystemManager.society.getSocietyId());
				isNewCharge = true;
			}

			if (!isNewCharge) {
				itemId = this.charge.hashCode();
			} else {
				itemId = --itemIdCounter;
			}

			descriptionLable = new JLabel(this.charge.getDescription());

			setLayout(new FlowLayout());
			add(descriptionLable);
		}

		public void save() {
			if (validateInput()) {
				Charge.save(this.charge, this.isNewCharge);
				this.isNewCharge = false;
				descriptionLable.setText(this.charge.getDescription());
			}
		}

		public void delete() {
			Charge.delete(this.charge);
		}

		@Override
		public int getItemId() {
			return itemId;
		}

		private boolean validateInput() {

			if (this.charge.getDescription().trim().length() == 0) {

				JOptionPane.showMessageDialog(this, "Enter description", "Error", JOptionPane.ERROR_MESSAGE);
				return false;

			} else {

			}

			return true;
		}

	}

	private static class ChargeDetails extends JPanel {

		private static final long serialVersionUID = 6035574266262639889L;

		JLabel chargeIdLabel, chargeIdValue, descriptionLabel, amountLabel;
		JTextField descriptionField, amountField;
		JCheckBox defaultChargeChkBox, tempChargeChkBox, cancelledChargeChkBox;

		public ChargeDetails() {
			chargeIdLabel = new JLabel("Charge");
			chargeIdValue = new JLabel();
			descriptionLabel = new JLabel("Description");
			amountLabel = new JLabel("Amount");

			descriptionField = new JTextField(30);
			amountField = new JTextField();

			defaultChargeChkBox = new JCheckBox("Default");
			tempChargeChkBox = new JCheckBox("Temporary");
			cancelledChargeChkBox = new JCheckBox("Cancelled");

			setLayout(new GridBagLayout());
			GridBagConstraints c = new GridBagConstraints();

			c.fill = GridBagConstraints.HORIZONTAL;

			c.gridx = 1;
			c.gridy = 1;
			c.anchor = GridBagConstraints.WEST;
			add(chargeIdLabel, c);

			c.gridy = 2;
			add(descriptionLabel, c);

			c.gridy = 3;
			add(amountLabel, c);

			c.gridx = 2;
			c.gridy = 1;
			c.gridwidth = GridBagConstraints.REMAINDER;
			c.anchor = GridBagConstraints.EAST;
			add(chargeIdValue, c);

			c.gridy = 2;
			add(descriptionField, c);

			c.gridy = 3;
			add(amountField, c);

			c.gridwidth = 1;
			c.gridx = 2;
			c.gridy = 4;
			add(defaultChargeChkBox, c);

			c.gridx = 3;
			add(tempChargeChkBox, c);

			c.gridx = 4;
			add(cancelledChargeChkBox, c);
		}

		public void setCharge(Charge charge) {
			chargeIdValue.setText(Integer.toString(charge.getChargeId()));
			descriptionField.setText(charge.getDescription());
			amountField.setText(Double.toString(charge.getAmount()));
			cancelledChargeChkBox.setSelected(charge.isCancelled());
			tempChargeChkBox.setSelected(charge.isTempCharges());
			defaultChargeChkBox.setSelected(charge.isdefault());
		}

		public void getFieldValues(Charge charge) {
			charge.setDescription(descriptionField.getText());
			charge.setAmount(Double.parseDouble(amountField.getText()));
			charge.setCancelled(cancelledChargeChkBox.isSelected());
			charge.setTempCharges(tempChargeChkBox.isSelected());
			charge.setdefault(defaultChargeChkBox.isSelected());
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

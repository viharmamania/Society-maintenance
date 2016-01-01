package com.vhi.hsm.view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.HashMap;
import java.util.List;

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
import com.vhi.hsm.model.Property;
import com.vhi.hsm.model.Society;
import com.vhi.hsm.view.masterdetail.MasterDetailPanel;
import com.vhi.hsm.view.masterdetail.MasterDetailPanel.MasterDetailCallback;
import com.vhi.hsm.view.masterdetail.MasterListPanel.MasterListItem;

public class PropertyView extends JDialog implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6359125264785300019L;
	private HashMap<Integer, PropertyMasterListItems> propertyListItems;
	private MasterDetailPanel propertyMasterDetailPanel;
	private PropertyDetails detailsPanel;

	private MasterDetailCallback callback = new MasterDetailCallback() {

		@Override
		public boolean showAddButton() {
			return true;
		}

		@Override
		public boolean showDeleteButton() {
			return true;
		}

		@Override
		public boolean showSaveButton() {
			return true;
		}

		@Override
		public void addNewItem() {
			PropertyMasterListItems item = new PropertyMasterListItems(null, false);
			propertyListItems.put(item.getItemId(), item);
			propertyMasterDetailPanel.getMasterListPanel().addListItem(item);
			repaint();
		}

		@Override
		public void deleteItem(int id) {
			PropertyMasterListItems item = propertyListItems.get(id);
			if (item != null) {
				item.delete();
				prepareList();
			}
		}

		@Override
		public void itemSelected(int id) {
			PropertyMasterListItems propertyMasterListItems = propertyListItems.get(id);
			if (propertyMasterListItems != null) {
				detailsPanel.setProperty(propertyMasterListItems.property, !propertyMasterListItems.isNewProperty);
			}

		}

		@Override
		public void saveItem(int id) {

			PropertyMasterListItems item = propertyListItems.get(id);
			if (item != null) {
				detailsPanel.getFieldValues(item.property);
				item.save();
				detailsPanel.setProperty(item.property, !item.isNewProperty);
			}

		}

	};

	public PropertyView(JFrame parent) {

		super(parent);
		this.setTitle("Manage Flats");
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setLocationRelativeTo(parent);
		setVisible(true);
		propertyListItems = new HashMap<Integer, PropertyMasterListItems>();
		propertyMasterDetailPanel = new MasterDetailPanel(callback);
		detailsPanel = new PropertyDetails();
		propertyMasterDetailPanel.getMasterDetailPanel().setDetailPanel(detailsPanel);
		prepareList();
		initializeLayout();

	}

	private void prepareList() {
		propertyListItems.clear();
		propertyMasterDetailPanel.getMasterListPanel().clearAllItems();
		List<Property> list = Property.getAllProperties(SystemManager.society.getSocietyId());
		for (Property property : list) {
			PropertyMasterListItems item = new PropertyMasterListItems(property, true);
			propertyListItems.put(item.getItemId(), item);
			propertyMasterDetailPanel.getMasterListPanel().addListItem(item);
		}
	}

	private void initializeLayout() {
		GroupLayout layout = new GroupLayout(getContentPane());
		layout.setHorizontalGroup(layout.createSequentialGroup().addComponent(propertyMasterDetailPanel));
		layout.setVerticalGroup(layout.createParallelGroup().addComponent(propertyMasterDetailPanel));
		getContentPane().setLayout(layout);
		pack();
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

	private static class PropertyMasterListItems extends MasterListItem {

		/**
		 * 
		 */
		private static final long serialVersionUID = 878805713977689135L;
		private Property property;
		boolean isNewProperty;
		private int itemId;
		private static int itemIdCounter = 0;
		JLabel propertyNameLabel, ownerNameLabel;
		private JLabel ownerPhoneLabel;
		private JLabel ownerEmailLabel;

		public PropertyMasterListItems(Property property, boolean readExisting) {
			super();
			if (readExisting) {
				this.property = Property.read(property.getPropertyId());
				isNewProperty = false;
			} else {
				this.property = Property.create();
				isNewProperty = true;
			}

			if (!isNewProperty) {
				itemId = this.property.hashCode();
			} else {
				itemId = --itemIdCounter;
			}

			propertyNameLabel = new JLabel(this.property.getPropertyName());
			ownerNameLabel = new JLabel(this.property.getOwnerName());
			ownerEmailLabel = new JLabel(this.property.getOwnerEmail());
			ownerPhoneLabel = new JLabel(this.property.getOwnerNumber());

			GroupLayout layout = new GroupLayout(this);
			setLayout(layout);
			layout.setHorizontalGroup(
					layout.createParallelGroup(GroupLayout.Alignment.LEADING).addComponent(propertyNameLabel));
			layout.setVerticalGroup(layout.createSequentialGroup().addComponent(propertyNameLabel));
			layout.setAutoCreateGaps(true);
			layout.setAutoCreateContainerGaps(true);
		}

		@Override
		public int getItemId() {
			return itemId;
		}

		public void save() {
			if (validateInput()) {
				Property.save(property, isNewProperty);
				propertyNameLabel.setText(this.property.getPropertyName());
				ownerNameLabel.setText(this.property.getOwnerName());
				ownerEmailLabel.setText(this.property.getOwnerEmail());
				ownerPhoneLabel.setText(this.property.getOwnerNumber());
				isNewProperty = false;
			}
		}

		private boolean validateInput() {

			if (!(property.getPropertyName().trim().length() > 0)) {
				JOptionPane.showMessageDialog(this, "Enter property Name", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			if (!(property.getOwnerName().trim().length() > 0)) {
				JOptionPane.showMessageDialog(this, "Enter property Name", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			String email = property.getOwnerEmail().trim();
			if (email.indexOf('@') == -1 || email.length() <= 2 || email.indexOf('.') == -1) {
				JOptionPane.showMessageDialog(this, "please enter valid email Id", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			String phone = property.getOwnerNumber().trim();
			phone = phone.replaceAll("[a-zA-z]", "");
			if (phone.length() < 10) {
				JOptionPane.showMessageDialog(this, "Enter property Name", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			return true;
		}

		public void delete() {
			Property.delete(property);
		}

	}

	private static class PropertyDetails extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4143521738795391972L;
		JLabel propertyNameLabel, ownerNameLabel, ownerPhoneLabel, ownerEmailLabel;
		JTextField propertyNameText, ownerNameText, ownerPhoneText, ownerEmailText;

		public PropertyDetails() {
			propertyNameLabel = new JLabel("Property Name");
			ownerNameLabel = new JLabel("Owner Name ");
			ownerEmailLabel = new JLabel("Email");
			ownerPhoneLabel = new JLabel("Mobile Number");

			propertyNameText = new JTextField();
			ownerNameText = new JTextField();
			ownerPhoneText = new JTextField();
			ownerEmailText = new JTextField();

			GroupLayout layout = new GroupLayout(this);
			setLayout(layout);
			layout.setAutoCreateContainerGaps(true);
			layout.setAutoCreateGaps(true);

			layout.setHorizontalGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(propertyNameLabel)
							.addComponent(ownerNameLabel).addComponent(ownerEmailLabel).addComponent(ownerPhoneLabel))
					.addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(propertyNameText))
					.addComponent(ownerNameText).addComponent(ownerEmailText).addComponent(ownerPhoneText));

			layout.setVerticalGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(propertyNameLabel)
							.addComponent(propertyNameText))
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(ownerNameLabel)
							.addComponent(ownerNameText))
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(ownerEmailLabel)
							.addComponent(ownerEmailText))
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(ownerPhoneLabel)
							.addComponent(ownerPhoneText)));
			
		}

		public void getFieldValues(Property property) {
			property.setPropertyName(propertyNameText.getText().trim());
			property.setOwnerEmail(ownerEmailText.getText().trim());
			property.setOwnerName(ownerNameText.getText().trim());
			property.setOwnerNumber(ownerPhoneText.getText().trim());

		}

		public void setProperty(Property property, boolean b) {
			propertyNameText.setText(property.getPropertyName());
			ownerNameText.setText(property.getOwnerName());
			ownerEmailText.setText(property.getOwnerEmail());
			ownerPhoneText.setText(property.getOwnerNumber());
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
				new PropertyView(null);
			});

		} else {
			// Show error message
			JOptionPane.showMessageDialog(null, "Can not connect to database", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}
}

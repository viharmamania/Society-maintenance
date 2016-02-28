package com.vhi.hsm.view;

import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
import com.vhi.hsm.model.Floor;
import com.vhi.hsm.model.FloorPlanDesign;
import com.vhi.hsm.model.Property;
import com.vhi.hsm.model.Wing;
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
				propertyMasterDetailPanel.getMasterDetailPanel().setDetailPanel(detailsPanel);
				detailsPanel.setProperty(propertyMasterListItems.property, !propertyMasterListItems.isNewProperty);
				pack();
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
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		propertyListItems = new HashMap<Integer, PropertyMasterListItems>();
		propertyMasterDetailPanel = new MasterDetailPanel(callback);
		detailsPanel = new PropertyDetails();
		// propertyMasterDetailPanel.getMasterDetailPanel().setDetailPanel(detailsPanel);
		prepareList();
		setPreferredSize(new Dimension(500, 500));
		setVisible(true);
		initializeLayout();
		setResizable(true);
		setLocationRelativeTo(parent);

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

			if (isNewProperty) {
				propertyNameLabel.setText("New Property");
			}

			GroupLayout layout = new GroupLayout(this);
			setLayout(layout);
			layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
					.addComponent(propertyNameLabel).addComponent(ownerNameLabel));
			layout.setVerticalGroup(
					layout.createSequentialGroup().addComponent(propertyNameLabel).addComponent(ownerNameLabel));
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
				JOptionPane.showMessageDialog(this, "Enter owner Name", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			String email = property.getOwnerEmail().trim();
			if (email.indexOf('@') == -1 || email.length() <= 2 || email.indexOf('.') == -1) {
				JOptionPane.showMessageDialog(this, "Enter valid email Id", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}

			String phone = property.getOwnerNumber().trim();
			phone = phone.replaceAll("[a-zA-z]", "");
			if (phone.length() < 10) {
				JOptionPane.showMessageDialog(this, "Enter owner number", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			if (property.getWingId() == 0) {
				JOptionPane.showMessageDialog(this, "Select wing", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			if (property.getFloorPlanId() == 0) {
				JOptionPane.showMessageDialog(this, "Select floor", "Error", JOptionPane.ERROR_MESSAGE);
				return false;
			}
			
			if (property.getPropertyNumber() == 0) {
				JOptionPane.showMessageDialog(this, "Select property number", "Error", JOptionPane.ERROR_MESSAGE);
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
		JLabel propertyNameLabel, ownerNameLabel, ownerPhoneLabel, ownerEmailLabel, netPayableLable, wingIdLabel,
				floorIdLabel, propertyNumberLabel;
		JTextField propertyNameText, ownerNameText, ownerPhoneText, ownerEmailText, netPayableText;
		JComboBox<Wing> wingIdText;
		JComboBox<Floor> floorIdText;
		JComboBox<Integer> propertyNumberText;

		public PropertyDetails() {
			propertyNameLabel = new JLabel("Property Name");
			ownerNameLabel = new JLabel("Owner Name ");
			ownerEmailLabel = new JLabel("Email");
			ownerPhoneLabel = new JLabel("Mobile Number");
			netPayableLable = new JLabel("Net Payable");
			wingIdLabel = new JLabel("Wing");
			floorIdLabel = new JLabel("Floor Number");
			propertyNumberLabel = new JLabel("Property Number from Floor Plan");

			propertyNameText = new JTextField(30);
			ownerNameText = new JTextField();
			ownerPhoneText = new JTextField();
			ownerEmailText = new JTextField();
			netPayableText = new JTextField();
			wingIdText = new JComboBox<Wing>();
			floorIdText = new JComboBox<Floor>();
			propertyNumberText = new JComboBox<Integer>();

			wingIdText.setEditable(false);
			floorIdText.setEditable(false);
			propertyNumberText.setEditable(false);

			intializeWingComboBox();

			netPayableText.setEditable(false);

			GroupLayout layout = new GroupLayout(this);
			setLayout(layout);
			layout.setAutoCreateContainerGaps(true);
			layout.setAutoCreateGaps(true);

			layout.setHorizontalGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.TRAILING).addComponent(propertyNameLabel)
							.addComponent(ownerNameLabel).addComponent(ownerEmailLabel).addComponent(ownerPhoneLabel)
							.addComponent(netPayableLable).addComponent(wingIdLabel).addComponent(floorIdLabel)
							.addComponent(propertyNumberLabel))
					.addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(propertyNameText)
							.addComponent(ownerNameText).addComponent(ownerEmailText).addComponent(ownerPhoneText)
							.addComponent(netPayableText).addComponent(wingIdText).addComponent(floorIdText)
							.addComponent(propertyNumberText)));

			// layout.setVerticalGroup(layout.createParallelGroup()
			// .addGroup(layout.createSequentialGroup()
			// .addComponent(propertyNameLabel)
			// .addComponent(ownerNameLabel)
			// .addComponent(ownerEmailLabel)
			// .addComponent(ownerPhoneLabel))
			// .addGroup(layout.createSequentialGroup()
			// .addComponent(propertyNameText)
			// .addComponent(ownerNameText)
			// .addComponent(ownerEmailText)
			// .addComponent(ownerPhoneText)));

			layout.setVerticalGroup(layout.createSequentialGroup()
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(propertyNameLabel)
							.addComponent(propertyNameText))
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(ownerNameLabel)
							.addComponent(ownerNameText))
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(ownerEmailLabel)
							.addComponent(ownerEmailText))
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(ownerPhoneLabel)
							.addComponent(ownerPhoneText))
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(netPayableLable)
							.addComponent(netPayableText))
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(wingIdLabel)
							.addComponent(wingIdText))
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(floorIdLabel)
							.addComponent(floorIdText))
					.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(propertyNumberLabel)
							.addComponent(propertyNumberText)));

			// layout.setVerticalGroup(layout.createParallelGroup()
			// .addGroup(layout.createSequentialGroup().addComponent(propertyNameLabel)
			// .addComponent(propertyNameText))
			// .addGroup(layout.createSequentialGroup().addComponent(ownerNameLabel)
			// .addComponent(ownerNameText))
			// .addGroup(layout.createSequentialGroup().addComponent(ownerEmailLabel)
			// .addComponent(ownerEmailText))
			// .addGroup(layout.createSequentialGroup().addComponent(ownerPhoneLabel)
			// .addComponent(ownerPhoneText)));
			//
		}

		private void intializeWingComboBox() {

			wingIdText.removeAllItems();
			ArrayList<Wing> allWings = Wing.getAllWings(SystemManager.society.getSocietyId());
			for (Wing wing : allWings) {
				wingIdText.addItem(wing);
			}

			wingIdText.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						intializeFloorComboBox();
					}
				}
			});

		}

		private void intializeFloorComboBox() {
			floorIdText.removeAllItems();

			Wing wing = (Wing) wingIdText.getSelectedItem();

			if (wing != null) {

				ArrayList<Floor> allFloors = Floor.getAllFloors(SystemManager.society.getSocietyId(), wing.getWingId());
				for (Floor floor : allFloors) {
					floorIdText.addItem(floor);
				}

				floorIdText.addItemListener(new ItemListener() {
					@Override
					public void itemStateChanged(ItemEvent e) {
						if (e.getStateChange() == ItemEvent.SELECTED) {
							intializePropertyNumberComboBox();
						}
					}
				});

			}
		}

		private void intializePropertyNumberComboBox() {
			propertyNumberText.removeAllItems();

			Floor floor = (Floor) floorIdText.getSelectedItem();
			if (floor != null) {
				ArrayList<FloorPlanDesign> allDesigns = FloorPlanDesign
						.getAllFloorPlanDesign(SystemManager.society.getSocietyId(), floor.getFloorPlanId());
				for (FloorPlanDesign design : allDesigns) {
					propertyNumberText.addItem(design.getPropertyNumber());
				}
			}
		}

		public void getFieldValues(Property property) {
			property.setPropertyName(propertyNameText.getText().trim());
			property.setOwnerEmail(ownerEmailText.getText().trim());
			property.setOwnerName(ownerNameText.getText().trim());
			property.setOwnerNumber(ownerPhoneText.getText().trim());
			property.setNetPayable(Double.parseDouble(netPayableText.getText()));
			property.setWingId(((Wing) wingIdText.getSelectedItem()).getWingId());
			Floor floor = (Floor) floorIdText.getSelectedItem();
			property.setFloorPlanId(floor.getFloorPlanId());
			property.setFloorNumber(floor.getFloor_number());
			property.setPropertyNumber((Integer) propertyNumberText.getSelectedItem());
		}

		public void setProperty(Property property, boolean b) {
			propertyNameText.setText(property.getPropertyName());
			ownerNameText.setText(property.getOwnerName());
			ownerEmailText.setText(property.getOwnerEmail());
			ownerPhoneText.setText(property.getOwnerNumber());
			netPayableText.setText(Double.toString(property.getNetPayable()));
			intializeWingComboBox();
			wingIdText.setSelectedItem(Wing.read(property.getSocietyId(), property.getWingId()));
			intializeFloorComboBox();
			Floor floor = Floor.read(property.getSocietyId(), property.getWingId(), property.getFloorNumber());
			floorIdText.setSelectedItem(floor);
			intializePropertyNumberComboBox();
			propertyNumberText.setSelectedItem(property.getPropertyNumber());
		}
	}

}

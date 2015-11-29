package com.vhi.hsm.view;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;

public class PropertyType extends JDialog implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7266320703225444811L;

	public PropertyType(JFrame owner) {
		super(owner, "Property Types", true);
		setResizable(false);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		initializeLayout();
		setLocationRelativeTo(owner);
		setVisible(true);
	}

	private void initializeLayout() {

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

	public static void createPropertyType(JFrame owner, int societyId) {
		com.vhi.hsm.model.PropertyType propertyTypeModel = com.vhi.hsm.model.PropertyType.create(societyId);
		new PropertyType.PropertyTypeDetail(propertyTypeModel, true);
	}

	public static void editPropertyType(JFrame owner, int societyId, String propertyType) {
		com.vhi.hsm.model.PropertyType propertyTypeModel = com.vhi.hsm.model.PropertyType.read(societyId, propertyType);
		new PropertyType.PropertyTypeDetail(propertyTypeModel, false);
	}

	private static class PropertyTypeDetail extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = -7553814370742805272L;

		private JLabel propertyTypeLable, descriptionLable;
		private JTextField propertyTypeField, descriptionField;
		private JButton saveButton;
		private com.vhi.hsm.model.PropertyType propertyTypeModel;
		private boolean isCreate;

		public PropertyTypeDetail(com.vhi.hsm.model.PropertyType model, boolean isCreate) {
			propertyTypeModel = model;
			this.isCreate = isCreate;
			initializeLayout();
			fillValues();
			setVisible(true);
		}

		private void initializeLayout() {
			
			propertyTypeField = new JTextField();
			descriptionField = new JTextField();
			
			propertyTypeLable = new JLabel("Property Type");
			descriptionLable = new JLabel("Description");
			
			saveButton = new JButton("Save");
			saveButton.addActionListener(e -> {
				save();
			});
			
			GroupLayout groupLayout = new GroupLayout(getRootPane());

			getRootPane().setLayout(groupLayout);

			groupLayout.setAutoCreateContainerGaps(true);
			groupLayout.setAutoCreateGaps(true);
			
			groupLayout.setHorizontalGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(propertyTypeLable)
							.addComponent(descriptionLable)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING).addComponent(propertyTypeField)
							.addComponent(descriptionField)
							.addGroup(groupLayout.createSequentialGroup().addComponent(saveButton)))));

			groupLayout.setVerticalGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(propertyTypeLable)
							.addComponent(propertyTypeField))
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE).addComponent(descriptionLable)
							.addComponent(descriptionField))
					.addGroup(groupLayout.createParallelGroup().addComponent(saveButton)));

		}

		private void fillValues() {
			propertyTypeField.setText(propertyTypeModel.getPropertyType());
			descriptionField.setText(propertyTypeModel.getDescription());
			propertyTypeField.setEditable(!this.isCreate);
		}

		private boolean validateInput(boolean showErrorMessage) {
			if (!this.isCreate && propertyTypeField.getText().trim().length() == 0) {
				if (showErrorMessage) {
					JOptionPane.showMessageDialog(this, "Enter property type", "Error", JOptionPane.ERROR_MESSAGE);
				}
				return false;
			}
			if (descriptionField.getText().trim().length() == 0) {
				if (showErrorMessage) {
					JOptionPane.showMessageDialog(this, "Enter description", "Error", JOptionPane.ERROR_MESSAGE);
				}
				return true;
			}
			return true;
		}

		private void save() {
			if (validateInput(true)) {
				if (this.isCreate) {
					propertyTypeModel.setPropertyType(propertyTypeField.getText());
					propertyTypeModel.setDescription(descriptionField.getText());
					com.vhi.hsm.model.PropertyType.save(propertyTypeModel, true);
				} else {
					propertyTypeModel.setDescription(descriptionField.getText());
					com.vhi.hsm.model.PropertyType.save(propertyTypeModel, false);
				}
			}
		}

	}

}

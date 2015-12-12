/**
 * 
 */
package com.vhi.hsm.view.masterdetail;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.vhi.hsm.view.masterdetail.MasterDetailPanel.MasterDetailCallback;

/**
 * @author Hardik Senghani
 *
 */
public class MasterListPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int selectedItemIndex;

	private ArrayList<MasterListItem> listItems;

	private MasterDetailPanel parentPanel;
	
	private JButton addButton, deleteButton;
	
	private JPanel buttonPanel, listPanel;

	public MasterListPanel(MasterDetailPanel parentPanel) {
		listItems = new ArrayList<MasterListItem>();
		this.parentPanel = parentPanel;
		setSize(300, 600);
		
		this.buttonPanel = new JPanel();
		this.listPanel = new JPanel();
		
		addButton = new JButton("Add");
		addButton.addActionListener(e -> {
			parentPanel.getCallback().addNewItem();
		});
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(e -> {
			if (selectedItemIndex != -1 && selectedItemIndex < listItems.size()) {
				MasterListItem item = listItems.get(selectedItemIndex);
				if (item != null) {
					parentPanel.getCallback().deleteItem(item.getItemId());
				}
			}
		});
		
		GroupLayout groupLayout = new GroupLayout(buttonPanel);
		buttonPanel.setLayout(groupLayout);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setHorizontalGroup(
				groupLayout.createSequentialGroup()
					.addComponent(addButton)
					.addComponent(deleteButton)
		);
		
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup()
					.addComponent(addButton)
					.addComponent(deleteButton)
		);
		
		listPanel.setLayout(new GridBagLayout());
		
		JScrollPane scrollPane = new JScrollPane(listPanel);
		
		groupLayout = new GroupLayout(this);
		this.setLayout(groupLayout);
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		groupLayout.setVerticalGroup(
				groupLayout.createSequentialGroup()
					.addComponent(scrollPane)
					.addComponent(buttonPanel)
		);
		
		groupLayout.setHorizontalGroup(
				groupLayout.createParallelGroup()
					.addComponent(scrollPane)
					.addComponent(buttonPanel)
		);
		
		setSize(400, 600);
		initializeLayout();
	}

	private void initializeLayout() {
		refreshList(false);
	}

	private void itemSelected(MasterListItem item) {
		MasterDetailCallback callback = this.parentPanel.getCallback();
		if (item != null && callback != null) {
			selectedItemIndex = listItems.indexOf(item);
			callback.itemSelected(item.getItemId());
			refreshList(true);
		}
	}
	
	public void clearAllItems() {
		listItems.clear();
	}

	private void refreshList(boolean preserveSelection) {
//		listItems.clear();
		listPanel.removeAll();
		if (!preserveSelection) {
			selectedItemIndex = -1;
		}
		MasterListItem item;
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1;
        //gbc.weighty = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
		for (int i = 0; i < listItems.size(); i++) {
			item = null;
			item = listItems.get(i);
			if (item != null) {
				if (i == selectedItemIndex) {
					item.setBorder(BorderFactory.createLineBorder(Color.BLACK));
				} else {
					item.setBorder(BorderFactory.createEmptyBorder());
				}
			}
			gbc.gridx = 1;
			gbc.gridy = i;
			listPanel.add(item, gbc);
		}

		addButton.setVisible(parentPanel.getCallback().showAddButton());
		deleteButton.setVisible(parentPanel.getCallback().showDeleteButton());
		revalidate();
	}

	public void addListItem(MasterListItem listItem) {
		listItem.parent = this;
		listItems.add(listItem);
		refreshList(true);
		repaint();
	}

	public int getSelectedItemId() {
		return listItems.get(selectedItemIndex).getItemId();
	}
	
	public void setSelectedItem(int itemId) {
		for (int i = 0; i < listItems.size(); i++) {
			if (listItems.get(i).getItemId() == itemId) {
				selectedItemIndex = i;
			}
		}
		refreshList(false);
	}

	public static abstract class MasterListItem extends JPanel implements MouseListener {
		private static final long serialVersionUID = 4615458731962273022L;
		
		private MasterListPanel parent;
		
		public MasterListItem() {
			//setSize(300, 100);
			
			addMouseListener(this);
			setBackground(Color.WHITE);
		}
		
		public abstract int getItemId();
		
		@Override
		public void mouseReleased(MouseEvent e) {
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			setBackground(Color.WHITE);
		}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			setBackground(Color.GRAY);
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			if (parent != null) {
				parent.itemSelected(this);
			}
		}
		
	}

}

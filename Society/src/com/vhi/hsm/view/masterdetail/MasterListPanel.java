/**
 * 
 */
package com.vhi.hsm.view.masterdetail;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

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
	
	private JPanel buttonPanel;

	public MasterListPanel(MasterDetailPanel parentPanel) {
		listItems = new ArrayList<MasterListItem>();
		this.parentPanel = parentPanel;
		setSize(300, 600);
		
		this.buttonPanel = new JPanel();
		
		addMouseListener( new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseClicked(MouseEvent e) {
				MasterListItem item = (MasterListItem) getComponentAt(e.getPoint());
				if (item != null) {
					itemSelected(item);
				}
			}
			
		});
		
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
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
		removeAll();
		if (!preserveSelection) {
			selectedItemIndex = -1;
		}
		MasterListItem item;
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
			add(item);
		}
		addButton.setVisible(parentPanel.getCallback().showAddButton());
		deleteButton.setVisible(parentPanel.getCallback().showDeleteButton());
		add(buttonPanel);
		revalidate();
	}

	public void addListItem(MasterListItem listItem) {
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

	public static abstract class MasterListItem extends JPanel {
		private static final long serialVersionUID = 4615458731962273022L;
		
		public MasterListItem() {
			setSize(300, 100);
		}
		
		public abstract int getItemId();
	}

}

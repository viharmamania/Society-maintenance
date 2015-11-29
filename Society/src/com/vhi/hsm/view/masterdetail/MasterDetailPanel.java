/**
 * 
 */
package com.vhi.hsm.view.masterdetail;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author Hardik Senghani
 *
 */
public class MasterDetailPanel extends JPanel {
	
	private MasterListPanel masterPanel;
	private DetailPanel detailPanel;
	
	private JButton addButton, deleteButton;
	
	private MasterDetailCallback callback;
	
	public MasterDetailPanel(MasterDetailCallback callback) {
		this.callback = callback;
		intializeLayout();
	}
	
	private void intializeLayout() {

	}

	public MasterDetailCallback getCallback() {
		return this.callback;
	}
	
	public MasterListPanel getMasterListPanel() {
		return masterPanel;
	}
	
	public DetailPanel getMasterDetailPanel() {
		return detailPanel;
	}
	
	public static interface MasterDetailCallback {
		public boolean showAddButton();
		public boolean showDeleteButton();
		public void addNewItem();
		public void deleteItem(int id);
		public void itemSelected(int id);
	}
	
}

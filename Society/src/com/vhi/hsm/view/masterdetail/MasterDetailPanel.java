/**
 * 
 */
package com.vhi.hsm.view.masterdetail;

import javax.swing.GroupLayout;
import javax.swing.JPanel;

/**
 * @author Hardik Senghani
 *
 */
public class MasterDetailPanel extends JPanel {
	
	private static final long serialVersionUID = 768726184816291577L;
	private MasterListPanel masterPanel;
	private DetailPanel detailPanel;
	
	private MasterDetailCallback callback;
	
	public MasterDetailPanel(MasterDetailCallback callback) {
		this.callback = callback;
		this.masterPanel = new MasterListPanel(this);
		this.detailPanel = new DetailPanel(this);
		intializeLayout();
	}
	
	private void intializeLayout() {
		
		GroupLayout groupLayout = new GroupLayout(this);
		this.setLayout(groupLayout);
		
		groupLayout.setHorizontalGroup(
				groupLayout.createSequentialGroup()
					.addComponent(masterPanel)
					.addComponent(detailPanel)
		);
		
		groupLayout.setVerticalGroup(
				groupLayout.createParallelGroup()
					.addComponent(masterPanel)
					.addComponent(detailPanel)
		);
		
		groupLayout.setAutoCreateContainerGaps(true);
		groupLayout.setAutoCreateGaps(true);
		
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
	
	public void setSelectedItem(int itemId) {
		masterPanel.setSelectedItem(itemId);
	}
	
	public static interface MasterDetailCallback {
		public boolean showAddButton();
		public boolean showDeleteButton();
		public boolean showSaveButton();
		public void addNewItem();
		public void deleteItem(int id);
		public void itemSelected(int id);
		public void saveItem(int id);
	}
	
}

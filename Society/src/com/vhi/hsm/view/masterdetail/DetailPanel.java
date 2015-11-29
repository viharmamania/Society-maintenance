package com.vhi.hsm.view.masterdetail;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.vhi.hsm.view.masterdetail.MasterDetailPanel.MasterDetailCallback;

/**
 * @author Hardik Senghani
 *
 */
public class DetailPanel extends JPanel {
	
	private MasterDetailPanel parentPanel;
	
	private JPanel contentPanel;
	private JButton deleteButton;
	
	public DetailPanel(MasterDetailPanel parentPanel) {
		this.parentPanel = parentPanel;
		
		deleteButton = new JButton("Delete");
		deleteButton.addActionListener(e -> {
			MasterDetailCallback callback = this.parentPanel.getCallback();
			if (callback != null) {
				callback.deleteItem(this.parentPanel.getMasterListPanel().getSelectedItemId());
			}
		});
		
		intializeLayout();
	}
	
	private void intializeLayout() {
		
	}

	public void setDetailPanel(JPanel panel) {
		contentPanel.removeAll();
		contentPanel.add(panel);
	}
	
}

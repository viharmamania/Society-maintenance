package com.vhi.hsm.view.masterdetail;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.vhi.hsm.view.masterdetail.MasterDetailPanel.MasterDetailCallback;

/**
 * @author Hardik Senghani
 *
 */
public class DetailPanel extends JPanel {

	private static final long serialVersionUID = 2463570292668528713L;

	private MasterDetailPanel parentPanel;

	private JPanel contentPanel;
	private JButton saveButton;

	public DetailPanel(MasterDetailPanel parentPanel) {
		this.parentPanel = parentPanel;
		setSize(1800, 600);

		saveButton = new JButton("Save");
		saveButton.addActionListener(e -> {
			MasterDetailCallback callback = this.parentPanel.getCallback();
			if (callback != null) {
				callback.saveItem(this.parentPanel.getMasterListPanel().getSelectedItemId());
			}
		});

		contentPanel = new JPanel();
		intializeLayout();
	}

	private void intializeLayout() {
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		add(contentPanel);
		add(saveButton);

		contentPanel.setSize(1800, 600);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
	}

	public void setDetailPanel(JPanel panel) {
		removePanel();
		contentPanel.add(panel);
	}
	
	public void removePanel() {
		contentPanel.removeAll();
	}

}

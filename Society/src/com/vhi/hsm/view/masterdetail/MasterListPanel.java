/**
 * 
 */
package com.vhi.hsm.view.masterdetail;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

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

	public MasterListPanel(MasterDetailPanel parentPanel) {
		listItems = new ArrayList<MasterListItem>();
		this.parentPanel = parentPanel;
		
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
				itemSelected((MasterListItem) getComponentAt(e.getPoint()));
			}
			
		});
		
		initializeLayout();
	}

	private void initializeLayout() {
		
	}

	private void itemSelected(MasterListItem item) {
		MasterDetailCallback callback = this.parentPanel.getCallback();
		if (item != null && callback != null) {
			callback.itemSelected(item.getItemId());
		}
	}

	private void refreshList(boolean preserveSelection) {
		listItems.clear();
		
	}

	public void addListItem(MasterListItem listItem) {
		listItems.add(listItem);
		refreshList(true);
	}

	public int getSelectedItemId() {
		return listItems.get(selectedItemIndex).getItemId();
	}

	public static abstract class MasterListItem extends JPanel {
		public abstract int getItemId();
	}

}

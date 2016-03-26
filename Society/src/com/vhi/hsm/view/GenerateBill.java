/**
 * 
 */
package com.vhi.hsm.view;

import java.awt.GridLayout;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;
import com.vhi.hsm.controller.manager.BillManager;
import com.vhi.hsm.controller.manager.PDFManager;
import com.vhi.hsm.controller.manager.SystemManager;
import com.vhi.hsm.db.SQLiteManager;
import com.vhi.hsm.model.Bill;
import com.vhi.hsm.model.Charge;
import com.vhi.hsm.utils.Constants;
import com.vhi.hsm.view.panel.DashBoard;

/**
 * @author Hardik Senghani
 *
 */
public class GenerateBill extends JDialog implements WindowListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final static Logger LOG = Logger.getLogger(GenerateBill.class);
	private JButton generateBills, previewBills;
	JPanel tempChargePanel;
	JProgressBar progressBar;
	JLabel progressLabel;
	JDialog progressDialog;
	HashMap<Integer, JCheckBox> tempChargeCheckBox;
	ArrayList<Charge> tempCharges;

	public GenerateBill(JFrame parent) {
		super(parent);
		this.setTitle("Create New Bills");
		setResizable(true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		addWindowListener(this);
		setLocationRelativeTo(parent);
		setVisible(true);

		checkIfBillAlreadyCreated();
	}

	private void checkIfBillAlreadyCreated() {
		boolean created = false;
		Calendar instance = Calendar.getInstance();
		instance.set(Calendar.DAY_OF_MONTH, 1);
		instance.set(Calendar.MILLISECOND, 0);
		instance.set(Calendar.HOUR, 0);
		instance.set(Calendar.HOUR_OF_DAY, 0);
		instance.set(Calendar.MINUTE, 0);
		instance.set(Calendar.SECOND, 0);

		long time = instance.getTime().getTime();

		String billGeneratedCheck = "SELECT " + Constants.Table.Bill.FieldName.BILL_ID + " FROM "
				+ Constants.Table.Bill.TABLE_NAME + " WHERE " + Constants.Table.Bill.FieldName.BILL_TIMESTAMP + ">="
				+ time;

		List<Integer> billIds = new ArrayList<>();
		try {
			ResultSet executeQuery = SQLiteManager.executeQuery(billGeneratedCheck);
			if (executeQuery != null) {
				while (executeQuery.next()) {
					billIds.add(executeQuery.getInt(Constants.Table.Bill.FieldName.BILL_ID));
				}
				if (billIds.size() > 0) {
					created = true;
				}
			}
			created = false;

			if (!created) {
				prepareTempChargeList();
				initializeLayout();
			} else {
				List<Bill> allBills = new ArrayList<>();
				for (Integer integer : billIds) {
					allBills.add(Bill.read(integer));
				}
				PDFManager.generateBillPDF(allBills, true);
				JOptionPane.showMessageDialog(this,
						"Bills have already been created for this month, you can find them at this location: "
								+ Constants.Path.BILL_PDF_LOCATION,
						"Error", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		} catch (SQLException | DocumentException | IOException e) {
			LOG.error(e.toString());
		}

	}

	private void prepareTempChargeList() {

		tempCharges = new ArrayList<Charge>();
		tempChargeCheckBox = new HashMap<Integer, JCheckBox>();

		ArrayList<Charge> allCharges = Charge.getAllCharge(SystemManager.society.getSocietyId());
		for (Charge c : allCharges) {
			if (c.isTempCharges() && !c.isCancelled()) {
				tempCharges.add(c);
				tempChargeCheckBox.put(c.getChargeId(), new JCheckBox(c.getDescription()));
			}
		}
	}

	private void initializeLayout() {

		tempChargePanel = new JPanel();
		tempChargePanel.setBorder(BorderFactory.createTitledBorder("Tempoprary Charges Assignment"));
		tempChargePanel.setLayout(new GridLayout(0, 3));

		for (Charge c : tempCharges) {
			tempChargePanel.add(tempChargeCheckBox.get(c.getChargeId()));
		}

		generateBills = new JButton("Generate Bills");
		generateBills.addActionListener(e -> {
			createPDF(false);
		});

		previewBills = new JButton("Preveiw Bills");
		previewBills.addActionListener(e -> {
			createPDF(true);
		});

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		layout.setAutoCreateContainerGaps(true);
		layout.setAutoCreateGaps(true);

		layout.setHorizontalGroup(layout.createParallelGroup().addComponent(tempChargePanel)
				.addGroup(layout.createSequentialGroup().addComponent(previewBills).addComponent(generateBills)));

		layout.setVerticalGroup(layout.createSequentialGroup().addComponent(tempChargePanel)
				.addGroup(layout.createParallelGroup().addComponent(previewBills).addComponent(generateBills)));

		pack();
	}

	private void createPDF(boolean isPreview) {
		
		ArrayList<Integer> tempChargeIds = new ArrayList<Integer>();

		JCheckBox box;
		for (Charge c : tempCharges) {
			box = null;
			box = tempChargeCheckBox.get(c.getChargeId());
			if (box != null && box.isSelected()) {
				tempChargeIds.add(c.getChargeId());
			}
		}

//		ProgressDialog progressDialog = 
		new ProgressDialog(this, tempChargeIds, isPreview);
		
	}

	@Override
	public void windowActivated(WindowEvent arg0) {

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		dispose();
	}

	@Override
	public void windowClosing(WindowEvent arg0) {

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {

	}

	@Override
	public void windowIconified(WindowEvent arg0) {

	}

	@Override
	public void windowOpened(WindowEvent arg0) {

	}
	
	public class ProgressDialog extends JDialog {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ProgressDialog(JDialog parent, ArrayList<Integer> tempChargeIds, boolean isPreview) {
			super(parent, "Progress", true);
			JProgressBar dpb = new JProgressBar(0, 500);
			JLabel progressLabel = new JLabel();
			
			dpb.setIndeterminate(true);
			add(progressLabel);
			add(dpb);
			setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
			setLocationRelativeTo(this);
			setResizable(false);
			pack();
			
			GenerateBill generateBill = (GenerateBill) parent;
			
			Thread thread = new Thread(new Runnable() {
				@Override
				public void run() {
					long startTime, endTime;
					String message;
					try {
						setTitle("Generating bills");
						startTime = System.currentTimeMillis();
						List<Bill> bill = BillManager.generateBill(SystemManager.society.getSocietyId(), isPreview,
								tempChargeIds);
						endTime = System.currentTimeMillis();
						message = "Total time for bill generation: " + (endTime - startTime) + " in milliSeconds";
						LOG.info(message);
						setTitle("Generating PDF files");
						startTime = System.currentTimeMillis();
						PDFManager.generateBillPDF(bill, isPreview);
						endTime = System.currentTimeMillis();
						message = "Total time for bill generation: " + (endTime - startTime) + " in milliSeconds";
						LOG.info(message);
						if (isPreview) {
							JOptionPane.showMessageDialog(null, "The Preview has been generated successfully ", "Success",
									JOptionPane.INFORMATION_MESSAGE);
						} else {
							JOptionPane.showMessageDialog(null, "The Bills have been generated successfully ", "Success",
									JOptionPane.INFORMATION_MESSAGE);
							DashBoard.prepareTreeData();
						}
					} catch (FileNotFoundException | DocumentException e) {
						LOG.error(e.toString());
					} catch (IOException e) {
						LOG.error(e.toString());
					} finally {
						dispose();
						generateBill.dispose();
					}
				}
			});
			
			thread.start();
			setVisible(true);
			
		}
		
	}

}

/*
 * Erick Cabrera
 * Dec. 9, 2018
 * TicketsGUI.java
 * Tickets_FA18
 */

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class TicketsGUI implements ActionListener {
	// class level member objects
	String checkIfAdmin;
	private JFrame mainFrame;
	private JTable ticketView;
    private  boolean logsShowing;
	JScrollPane sp = null;
	// main menu object items
	private JMenu mFile = new JMenu("File");
	private JMenu mAdmin = new JMenu("Admin");
	private JMenu mTickets = new JMenu("Tickets");
	/* add any more Main menu object items below */
	// Sub menu item objects for all Main menu item objects
    JMenuItem mItemExit;
    JMenuItem mLogout;
	JMenuItem mItemUpdate;
	JMenuItem mItemDelete;
	JMenuItem mItemOpenTicket;
    JMenuItem mItemViewTicket;
    JMenuItem mItemViewLogs;
	/* add any more Sub object items below */
	// constructor
    // checks for admin to allow access
	public TicketsGUI(String verifyRole) {
		checkIfAdmin = verifyRole; 
		JOptionPane.showMessageDialog(null, "Welcome " + verifyRole);
		createMenu();
		prepareGUI();
	}
	private void createMenu() {
		/* Initialize sub menu items **************************************/
		// initialize sub menu item for File main menu for users to select options
        mLogout = new JMenuItem("Logout");
        // add to File main menu item
        mFile.add(mLogout);
        mItemExit = new JMenuItem("Exit");
        // add to File main menu item
        mFile.add(mItemExit);
        // initialize first sub menu items for Admin main menu
        mItemViewLogs = new JMenuItem("View Logs");
        // add to Admin main menu item
        mAdmin.add(mItemViewLogs);
        // initialize first sub menu items for Admin main menu
        mItemUpdate = new JMenuItem("Update Ticket");
        // add to Admin main menu item
        mAdmin.add(mItemUpdate);
		// initialize second sub menu items for Admin main menu
		mItemDelete = new JMenuItem("Delete Ticket");
		// add to Admin main menu item
		mAdmin.add(mItemDelete);
		// initialize first sub menu item for Tickets main menu
		mItemOpenTicket = new JMenuItem("Open Ticket");
		// add to Ticket Main menu item
		mTickets.add(mItemOpenTicket);
		// initialize second sub menu item for Tickets main menu
		mItemViewTicket = new JMenuItem("View Ticket");
		// add to Ticket Main menu item
		mTickets.add(mItemViewTicket);
		// initialize any more desired sub menu items below
		/* Add action listeners for each desired menu item *************/
		mItemExit.addActionListener(this);
		mItemUpdate.addActionListener(this);
		mItemDelete.addActionListener(this);
		mItemOpenTicket.addActionListener(this);
        mItemViewTicket.addActionListener(this);
        mLogout.addActionListener(this);
        mItemViewLogs.addActionListener(this);
		// add any more listeners for any additional sub menu items if desired
	}
	private void prepareGUI() {
		// initialize frame object
		mainFrame = new JFrame("Tickets");
		// create jmenu bar
		JMenuBar bar = new JMenuBar();
		bar.add(mFile); // add main menu items in order, to JMenuBar
		//bar for admin
        if(Login.LoggedInUser != null && Login.LoggedInUser.role.equalsIgnoreCase("admin"))
		    bar.add(mAdmin);
		bar.add(mTickets);
		// add menu bar components to frame
		mainFrame.setJMenuBar(bar);
		mainFrame.addWindowListener(new WindowAdapter() {
			// define a window close operation
			public void windowClosing(WindowEvent wE) {
				System.exit(0);
			}
		});
		// set frame options
		mainFrame.setSize(400, 400);
		mainFrame.getContentPane().setBackground(Color.LIGHT_GRAY);
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setVisible(true);
	}
	/*
	 * action listener fires up items clicked on from sub menus with one action
	 * performed event handler!
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// implement actions for sub menu items
        if (e.getSource() == mItemExit) {
            System.exit(0);
        }
        else if (e.getSource() == mLogout) {
            Login.LoggedInUser = null;
            mainFrame.dispose();
            new Login();
        }
		else if (e.getSource() == mItemOpenTicket) {
				// get ticket information
				String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
                if(ticketDesc == null || ticketDesc.isEmpty())
                    return;
                if(ticketDesc.length() >= 201){
                    ticketDesc = ticketDesc.substring(0, 200);
                }
				// insert ticket information to database
				int id = Dao.getInstance().addTicket(ticketDesc);
				if (id != -1) {
					System.out.println("Ticket ID : " + id + " created successfully!!!");
					JOptionPane.showMessageDialog(null, "Ticket ID: " + id + " created");
                    mainFrame.setVisible(false);
                    viewTickets();
                    mainFrame.setVisible(true);
				} else {
					System.out.println("Ticket cannot be created!");
				}
		}
        // option to view tickets
		else if (e.getSource() == mItemViewTicket) {
            mainFrame.setVisible(false);
            viewTickets();
            mainFrame.setVisible(true);
        // option to delete ticket
		}else if (e.getSource() == mItemDelete){
		    if(logsShowing)
		        return;
            int selectedRow = ticketView.getSelectedRow();
            if(selectedRow == -1){
                JOptionPane.showMessageDialog(mainFrame, "Please select a ticket to delete.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            DefaultTableModel model = (DefaultTableModel) ticketView.getModel();
            int ticketId = (int)model.getValueAt(selectedRow, 0);
                boolean deleted = Dao.getInstance().removeTicket(ticketId);
                // catch error for deleted
                if(deleted) {
                    JOptionPane.showMessageDialog(mainFrame, "Successfully deleted ticket.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    mainFrame.setVisible(false);
                    viewTickets();
                    mainFrame.setVisible(true);
                }else{
                    JOptionPane.showMessageDialog(mainFrame, "Failed to delete ticket", "Error", JOptionPane.ERROR_MESSAGE);
                }
        // option to update ticket        
        }else if (e.getSource() == mItemUpdate){
            if(logsShowing)
                return;
            int selectedRow = ticketView.getSelectedRow();
            if(selectedRow == -1){
                JOptionPane.showMessageDialog(mainFrame, "Please select a ticket to update.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            //set default table
            DefaultTableModel model = (DefaultTableModel) ticketView.getModel();
            int ticketId = (int)model.getValueAt(selectedRow, 0);
            // get ticket information
            String ticketDesc = JOptionPane.showInputDialog(null, "Enter a ticket description");
            if(ticketDesc == null || ticketDesc.isEmpty())
                ticketDesc = model.getValueAt(selectedRow, 2).toString();
            if(ticketDesc.length() >= 201){
                ticketDesc = ticketDesc.substring(0, 200);
            }
            // ticket status
            String ticketStatus = JOptionPane.showInputDialog(null, "Enter a ticket status");
            if(ticketStatus == null || ticketStatus.isEmpty()) {
                ticketStatus = model.getValueAt(selectedRow, 3).toString();
                if(ticketStatus == null || ticketStatus.isEmpty()) {
                    return;
                }
            }
            if(ticketStatus.length() >= 30) {
                ticketStatus = ticketStatus.substring(0, 30);
            }
            boolean updated = Dao.getInstance().updateTicket(ticketId, "ticket_descripton", ticketDesc, false) && Dao.getInstance().updateTicket(ticketId, "ticket_status", ticketStatus, true);
            // catch error for updating
            if(updated) {
                JOptionPane.showMessageDialog(mainFrame, "Successfully updated ticket.", "Success", JOptionPane.INFORMATION_MESSAGE);
                mainFrame.setVisible(false);
                viewTickets();
                mainFrame.setVisible(true);
            }else{
                JOptionPane.showMessageDialog(mainFrame, "Failed to update ticket", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        else if (e.getSource() == mItemViewLogs){
            logsShowing = true;
            mainFrame.setVisible(false);
            viewLogs();
            mainFrame.setVisible(true);
        }
	}
    // retrieve ticket information for viewing in JTable
    private void viewLogs(){
        try {
            Statement statement = Dao.getConnection().createStatement();
            ResultSet results = statement.executeQuery("SELECT * FROM erickCab_logTable");
            // Use JTable built in functionality to build a table model and
            // display the table model off your result set!!!
            if(ticketView != null) {
                mainFrame.dispose();
                mainFrame = null;
                prepareGUI();
                ticketView = null;
            }
            ticketView = new JTable(TicketsJTable.buildTableModel(results));
            ticketView.setBounds(30, 40, 200, 300);
            sp = new JScrollPane(ticketView);
            mainFrame.add(sp);
            mainFrame.setVisible(true); // refreshes or repaints frame on
            // screen
            statement.close();  // close connections!!!
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
    // retrieve ticket information for viewing in JTable
    private void viewTickets(){
        logsShowing = false;
        try {
            Statement statement = Dao.getConnection().createStatement();
            ResultSet results;
            if(Login.LoggedInUser.role.equalsIgnoreCase("admin"))
                results= statement.executeQuery("SELECT * FROM erickCab_tickets");
            else
                results= statement.executeQuery("SELECT * FROM erickCab_tickets WHERE ticket_issuer='"+ Login.LoggedInUser.name +"'");

            // Use JTable built in functionality to build a table model and
            // display the table model off your result set!!!
            if(ticketView != null) {
                mainFrame.dispose();
                mainFrame = null;
                prepareGUI();
                ticketView = null;
            }
            ticketView = new JTable(TicketsJTable.buildTableModel(results));
            ticketView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            ticketView.setBounds(30, 40, 200, 300);
            sp = new JScrollPane(ticketView);
            mainFrame.add(sp);
            mainFrame.setVisible(true); // refreshes or repaints frame on
            // screen
            statement.close();  // close connections!!!
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }
    // get methods
    public JFrame getMainFrame() {
        return mainFrame;
    }
    public JTable getmTicketView() {
        return ticketView;
    }
    public boolean ismLogsAreShowing() {
        return logsShowing;
    }
    public JScrollPane getSp() {
        return sp;
    }
    public JMenu getMnuFile() {
        return mFile;
    }
    public JMenu getMnuAdmin() {
        return mAdmin;
    }
    public JMenu getMnuTickets() {
        return mTickets;
    }
    public JMenuItem getMnuItemExit() {
        return mItemExit;
    }
    public JMenuItem getMnuLogout() {
        return mLogout;
    }
    public JMenuItem getMnuItemUpdate() {
        return mItemUpdate;
    }
    public JMenuItem getMnuItemDelete() {
        return mItemDelete;
    }
    public JMenuItem getMnuItemOpenTicket() {
        return mItemOpenTicket;
    }
    public JMenuItem getMnuItemViewTicket() {
        return mItemViewTicket;
    }
    public JMenuItem getMnuItemViewLogs() {
        return mItemViewLogs;
    }
}

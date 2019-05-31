/*
 * Erick Cabrera
 * Dec. 9, 2018
 * Dao.java
 * Tickets_FA18
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dao {
	// instance fields
	private static Connection connect = null;
	private Statement statement = null;
	private static Dao instance;
	public boolean exceptionCaught;
	// constructor
	private Dao() {
	}
	public static  Dao getInstance(){
	    if(instance == null){
	        synchronized (Dao.class){
	            if(instance == null)
	                instance = new Dao();
            }
        }
        return instance;
    }
	// Setup the connection with the DB
	public static Connection getConnection() {
		try {
			connect = DriverManager
					.getConnection("jdbc:mysql://www.papademas.net:3307/tickets?autoReconnect=true&useSSL=false"
							+ "&user=fp411&password=411");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return connect;
	}
	public void createTables(boolean alter) {
	    exceptionCaught = false;
		// variables for SQL Query table creations
        final String createTicketsTable = "CREATE TABLE IF NOT EXISTS erickCab_tickets(ticket_id INT AUTO_INCREMENT PRIMARY KEY, ticket_issuer VARCHAR(30), ticket_description VARCHAR(200))";
        final String createUsersTable = "CREATE TABLE IF NOT EXISTS erickCab_users(uid INT AUTO_INCREMENT PRIMARY KEY, uname VARCHAR(30), upass VARCHAR(256))";
        final String createLogTable = "CREATE TABLE IF NOT EXISTS erickCab_logTable(uid INT AUTO_INCREMENT PRIMARY KEY, operation VARCHAR(30), uname VARCHAR(30), table_name VARCHAR(200), column_name VARCHAR(200), value VARCHAR(200), pk_id VARCHAR(200))";
        final String alterUserTable = "ALTER TABLE erickCab_users MODIFY COLUMN upass VARCHAR(256);";
        final String alterTicketTable = "ALTER TABLE erickCab_tickets ADD COLUMN ticket_status VARCHAR(30) NULL AFTER ticket_description;";
        final String removeEmptyRoles = "DELETE FROM erickCab_users WHERE urole IS NOT NULL";
        final String removeEmptyTickets = "DELETE FROM erickCab_tickets WHERE ticket_issuer IS NULL OR ticket_description IS NULL OR ticket_status IS NULL";
		try {
			// execute queries to create tables
			statement = getConnection().createStatement();
            statement.executeUpdate(createTicketsTable);
            statement.executeUpdate(createLogTable);
            statement.executeUpdate(createUsersTable);
            if(alter) {
                statement.execute(alterUserTable);
                statement.execute(alterTicketTable);
            }
            statement.executeUpdate(removeEmptyRoles);
            statement.executeUpdate(removeEmptyTickets);
			System.out.println("Created tables in given database...");
			// end create table
			// close connection/statement object
			statement.close();
			connect.close();
		} catch (Exception e) {
			exceptionCaught = true;
		}
		// add users to user table
		addUsers();
	}
	// add list of users from userlist.csv file to users table
	public void addUsers() {
		// variables for SQL Query inserts
		String sql;
		Statement statement;
		BufferedReader br;
		List<List<String>> array = new ArrayList<>(); // array list to hold
												  	  // spreadsheet rows &
													  // columns
		// read data from file
		try {
		    exceptionCaught = false;
			br = new BufferedReader(new FileReader(new File("./userlist.csv")));

			String line;
			while ((line = br.readLine()) != null) {
				array.add(Arrays.asList(line.split(",")));
			}
		} catch (Exception e) {
		    exceptionCaught = true;
			System.out.println("There was a problem loading the file");
		}
		try {
			// Setup the connection with the DB
            exceptionCaught = false;
			statement = getConnection().createStatement();
			// create loop to grab each array index containing a list of values
			// and PASS (insert) that data into your User table
			for (List<String> rowData : array) {
				sql = "insert into erickCab_users(uname,upass,urole) " + "values('" + rowData.get(0) + "','" + User.hashPassword(rowData.get(1))+ "','"+rowData.get(2)+"');";
				statement.executeUpdate(sql);
			}
			System.out.println("Inserts completed in the given database...");
			// close statement object
			statement.close();
		} catch (Exception e) {
		    exceptionCaught = true;
			System.out.println( e.getMessage());
		}
	}
	// update ticket in db
    public boolean updateTicket(int ticketId, String column, String value, boolean status){
        try {
            exceptionCaught = false;
            Statement statement = getConnection().createStatement();
            if(!status)
                statement.executeUpdate("UPDATE erickCab_tickets SET ticket_description = '" + value + "' WHERE ticket_id=" + ticketId);
            else
                statement.executeUpdate("UPDATE erickCab_tickets SET ticket_status = '" + value + "' WHERE ticket_id=" + ticketId);
            logTranstaction("Update", "erickCab_tickets", column, value, ""+ticketId);
        }catch (Exception ex){
            exceptionCaught = true;
            return false;
        }
        return true;
    }
    // delete ticket in db
    public boolean removeTicket(int ticketId){
        try {
            exceptionCaught = false;
            Statement statement = getConnection().createStatement();
            statement.executeUpdate("DELETE FROM erickCab_tickets WHERE ticket_id="+ticketId);
            logTranstaction("Delete", "erickCab_tickets", "NA", "NA", ""+ticketId);
        }catch (Exception ex){
            exceptionCaught = true;
            return false;
        }
        return true;
    }
    // add ticket to db
    public int addTicket(String ticketDesc){
        try {
            exceptionCaught = false;
            Statement statement = getConnection().createStatement();

            statement.executeUpdate("Insert into erickCab_tickets(ticket_issuer, ticket_description, ticket_status) values(" + " '"
                            + Login.LoggedInUser.name + "','" + ticketDesc + "','OPEN')", Statement.RETURN_GENERATED_KEYS);
            ResultSet resultSet = null;
            resultSet = statement.getGeneratedKeys();
            int id = 0;
            if (resultSet.next()) {
                id = resultSet.getInt(1); // retrieve first field in table
            }
            logTranstaction("INSERT", "erickCab_tickets", "ticket_description", ticketDesc, ""+id);
            return id;
        }catch (Exception ex){
            exceptionCaught = true;
            return -1;
        }
    }
    // log all ticket transactions
    private void logTranstaction(String operation, String table, String col, String val, String rmv){
        String sql = String.format("Insert into erickCab_logTable(operation, uname, table_name, column_name, value, pk_id) values('%s','%s','%s','%s','%s','%s')",
                operation, Login.LoggedInUser.name, table, col, val, rmv);
        try {
            Statement statement = getConnection().createStatement();
            statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
        }catch (Exception ex){
            exceptionCaught = true;
        }
    }
}

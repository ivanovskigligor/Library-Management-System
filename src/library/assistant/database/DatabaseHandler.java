package library.assistant.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class DatabaseHandler {

	private static DatabaseHandler handler = null;
	private static final String DB_URL = "jdbc:derby:mydb;create=true";

	private static Connection conn = null;
	private static Statement stmt = null;

	private DatabaseHandler() {

		createConnection();
		setupBooksTable();
		setupMemberTable();
		
	}

	public static DatabaseHandler getInstance() {
		if (handler == null) {
			handler = new DatabaseHandler();
		}
		return handler;
	}

	private static void createConnection() {
		try {
			// If using newer Derby versions, Class.forName might not be necessary
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			conn = DriverManager.getConnection(DB_URL);
		} catch (Exception e) {
			e.printStackTrace();  // Print stack trace for more details
	        JOptionPane.showMessageDialog(null, "Can't load database: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
	        System.exit(0);
		}
	}

	private static void setupBooksTable() {

		String TABLE_NAME = "BOOK";
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + " already exists.");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" 
						+ " id varchar(200) PRIMARY KEY, \n"
						+ " title varchar(200), \n" 
						+ " author varchar(200), \n" 
						+ " publisher varchar(200), \n"
						+ " isAvail boolean DEFAULT true" 
						+ " )");
				System.out.println("Table " + TABLE_NAME + " created successfully.");
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage() + " --- setupBooksTable");
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				System.out.println("Failed to close statement: " + e.getMessage());
			}
		}
	}
	
	private void setupMemberTable() {
		// TODO Auto-generated method stub
		String TABLE_NAME = "MEMBER";
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + " already exists.");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" 
						+ " id varchar(200) PRIMARY KEY, \n"
						+ " name varchar(200), \n" 
						+ " mobile varchar(20), \n" 
						+ " email varchar(100) \n"
						+ " )");
				System.out.println("Table " + TABLE_NAME + " created successfully.");
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage() + " --- setupMembersTable");
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				System.out.println("Failed to close statement: " + e.getMessage());
			}
		}
	}
	public ResultSet execQuery(String query) {
		ResultSet result = null;
		try {
			stmt = conn.createStatement();
			result = stmt.executeQuery(query);

		} catch (SQLException e) {
			System.out.println("Exception at execQuery:dataHandler" + e.getLocalizedMessage());
			return null;
		}
		return result;

	}

	public boolean execAction(String query) {

		try {
			stmt = conn.createStatement();
			stmt.execute(query);
			return true;
		} catch (SQLException e) {
			JOptionPane.showMessageDialog(null, "Error:" + e.getMessage(), "Error occured", JOptionPane.ERROR_MESSAGE);
			System.out.println("Exception at execQuery:dataHandler" + e.getLocalizedMessage());
			return false;
		}

	}

}

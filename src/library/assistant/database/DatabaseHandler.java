package library.assistant.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import netscape.javascript.JSException;

public class DatabaseHandler {

	private static DatabaseHandler handler;
	private static final String DB_URL = "jdbc:derby:database;create=true";
	private static Connection conn = null;
	private static Statement stmt = null;
	
	public DatabaseHandler() {
		createConnection();
		setupBooksTable();	
	}
	
	
	void createConnection() {
		try (Connection conn = DriverManager.getConnection(DB_URL)) {
	        if (conn != null) {
	            
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	void setupBooksTable() {
		String TABLE_NAME = "BOOK";
		try{
			stmt = conn.createStatement();
			DatabaseMetaData dbm = conn.getMetaData();
			ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
			if(tables.next() ) {
				System.out.println("Table " + TABLE_NAME + "already exists.");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME +  "("
						+ " id varchar(200) primary key, \n"
						+ " title varchar(200) primary key, \n"
						+ " author varchar(200), \n"
						+ " publisher varchar(200), \n"
						+ " intcode varchar(100), \n"
						+ " isAvail boolean default true"
						+ " )");
			}
		} catch(SQLException e) {
			System.out.println(e.getMessage() + "--- setupDatabase");
		} finally {
			
		}
		
	}
	
}

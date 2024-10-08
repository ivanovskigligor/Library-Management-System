package library.assistant.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import library.assistant.ui.listbooks.ListBooksController.Book;
import library.assistant.ui.listmember.ListMembersController.Member;

public class DatabaseHandler {

	private static DatabaseHandler handler = null;
	private static final String DB_URL = "jdbc:derby:mydb;create=true";

	private static Connection conn = null;
	private static Statement stmt = null;

	private DatabaseHandler() {

		createConnection();
		setupBooksTable();
		setupMemberTable();
		setupIssueTable();
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
			JOptionPane.showMessageDialog(null, "Can't load database: ", "Database Error", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace(); // Print stack trace for more details
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
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + " id varchar(200) PRIMARY KEY, \n"
						+ " title varchar(200), \n" + " author varchar(200), \n" + " publisher varchar(200), \n"
						+ " isAvail boolean DEFAULT true" + " )");
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
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + " id varchar(200) PRIMARY KEY, \n"
						+ " name varchar(200), \n" + " mobile varchar(20), \n" + " email varchar(100) \n" + " )");
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

	private static void setupIssueTable() {

		String TABLE_NAME = "ISSUE";
		try {
			stmt = conn.createStatement();
			DatabaseMetaData dbm = conn.getMetaData();

			ResultSet tables = dbm.getTables(null, null, TABLE_NAME.toUpperCase(), null);
			if (tables.next()) {
				System.out.println("Table " + TABLE_NAME + " already exists.");
			} else {
				stmt.execute("CREATE TABLE " + TABLE_NAME + "(" + "bookID varchar(200) PRIMARY KEY, \n"
						+ "memberID varchar(200), \n" + "issueTime timestamp default CURRENT_TIMESTAMP, \n"
						+ "renewCount integer default 0, \n" + "FOREIGN KEY (bookID) REFERENCES BOOK(id), \n"
						+ "FOREIGN KEY (memberID) REFERENCES MEMBER(id) \n" + ")");

				System.out.println("Table " + TABLE_NAME + " created successfully.");
			}
		} catch (SQLException e) {
			System.out.println("SQL Error: " + e.getMessage() + " --- setupIssueTable");
		} finally {
			try {
				if (stmt != null)
					stmt.close();
			} catch (SQLException e) {
				System.out.println("Failed to close statement: " + e.getMessage());
			}
		}
	}

	public static ResultSet execQuery(String query) {
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

	public boolean deleteBook(Book book) {

		try {
			String deleteStatement = "DELETE FROM BOOK WHERE ID = ?";
			PreparedStatement stmt = conn.prepareStatement(deleteStatement);
			stmt.setString(1, book.getId());
			int res = stmt.executeUpdate();
			if (res == 1) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean deleteMember(Member member) {

		try {
			String deleteStatement = "DELETE FROM MEMBER WHERE ID = ?";
			PreparedStatement stmt = conn.prepareStatement(deleteStatement);
			stmt.setString(1, member.getId());
			int res = stmt.executeUpdate();
			if (res == 1) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public boolean isBookAlreadyIssued(Book book) {
		try {
			String issuedStatement = "SELECT COUNT(*) FROM ISSUE WHERE bookid = ?";
			PreparedStatement stmt = conn.prepareStatement(issuedStatement);
			stmt.setString(1, book.getId());
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				int count = rs.getInt(1);
				System.out.println(count);
				if (count > 0) {
					return true;
				} else {
					return false;
				}
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateBook(Book book) {
		String update = "UPDATE BOOK SET TITLE = ?, AUTHOR = ?, PUBLISHER =? WHERE ID = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(update);
			stmt.setString(1, book.getTitle());
			stmt.setString(2, book.getAuthor());
			stmt.setString(3, book.getPublisher());
			stmt.setString(4, book.getId());
			int res = stmt.executeUpdate();

			return (res == 1);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean updateMember(Member member) {
		String update = "UPDATE MEMBER SET NAME = ?, MOBILE = ?, EMAIL =? WHERE ID = ?";
		try {
			PreparedStatement stmt = conn.prepareStatement(update);
			stmt.setString(1, member.getName());
			stmt.setString(2, member.getMobile());
			stmt.setString(3, member.getEmail());
			stmt.setString(4, member.getId());
			int res = stmt.executeUpdate();

			return (res == 1);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public static ObservableList<PieChart.Data> bookGraphStatistics() {

		ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

		
		try {
			String query1 = "SELECT COUNT(*) FROM BOOK";
			String query2 = "SELECT COUNT(*) FROM ISSUE";
			ResultSet rs = execQuery(query1);
			
			if (rs.next()) {
				int count = rs.getInt(1);
				data.add(new PieChart.Data("Total Books(" + count + ")", count));

			}

			rs = execQuery(query2);
			if (rs.next()) {
				int count = rs.getInt(1);
				data.add(new PieChart.Data("Issued Books(" + count + ")", count));

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;

	}
	
	public static ObservableList<PieChart.Data> memberGraphStatistics() {

		ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

		
		try {
			String query1 = "SELECT COUNT(*) FROM MEMBER";
			String query2 = "SELECT COUNT(DISTINCT memberID) FROM ISSUE";
			ResultSet rs = execQuery(query1);
			
			if (rs.next()) {
				int count = rs.getInt(1);
				data.add(new PieChart.Data("Total Members(" + count + ")", count));

			}

			rs = execQuery(query2);
			if (rs.next()) {
				int count = rs.getInt(1);
				data.add(new PieChart.Data("Issued to members(" + count + ")", count));

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;

	}
	
}

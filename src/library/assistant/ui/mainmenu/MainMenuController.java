
package library.assistant.ui.mainmenu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.effects.JFXDepthManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import library.assistant.database.DatabaseHandler;
import library.assistant.util.LibraryAssistantUtil;

public class MainMenuController implements Initializable {

	@FXML
	private Text bookAuthor;

	@FXML
	private Text memberContact;

	@FXML
	private TextField memberIdInput;

	@FXML
	private Text memberName;

	@FXML
	private ListView<String> issueDataList;

	@FXML
	private TextField bookIdInput;
	@FXML
	private TextField bookID;
	@FXML
	private Text bookName;

	@FXML
	private Text bookStatus;

	@FXML
	private HBox book_info;
	
	@FXML
	private HBox member_info;
	
    @FXML
    private StackPane rootPane;

	DatabaseHandler databaseHandler;

	Boolean readyForSubmission = false;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		JFXDepthManager.setDepth(book_info, 1);
		JFXDepthManager.setDepth(member_info, 1);
		databaseHandler = DatabaseHandler.getInstance();
	}

	@FXML
	void loadAddBook(ActionEvent event) {
		loadWindow("/library/assistant/ui/addbook/addbook.fxml", "Add New Book");
	}

	@FXML
	void loadAddMember(ActionEvent event) {

		loadWindow("/library/assistant/ui/addmember/Addmember.fxml", "Add New Member");
	}

	@FXML
	void loadBookTable(ActionEvent event) {

		loadWindow("/library/assistant/ui/listbooks/listbooks.fxml", "List Books");
	}

	@FXML
	void loadMemberTable(ActionEvent event) {

		loadWindow("/library/assistant/ui/listmember/ListMembers.fxml", "List Members");
	}

	@FXML
	void loadSettings(ActionEvent event) {
		loadWindow("/library/assistant/settings/Settings.fxml", "Settings");

	}

	@FXML
    void handleMenuClose(ActionEvent event) {
		((Stage)rootPane.getScene().getWindow()).close();
    }
	
	@FXML
    void handleMenuFullScreen(ActionEvent event) {
		
		Stage stage = (Stage)rootPane.getScene().getWindow();
		stage.setFullScreen(!stage.isFullScreen());
    }
	
	
	@FXML
	void loadBookInfo(ActionEvent event) {
		clearBookCache();
		String id = bookIdInput.getText();
		String query = "SELECT * FROM BOOK WHERE id = '" + id + "'";
		System.out.println(query);
		ResultSet result = databaseHandler.execQuery(query);
		boolean flag = false;
		try {
			while (result.next()) {
				String bName = result.getString("title");
				String bAuthor = result.getString("author");
				Boolean bStatus = result.getBoolean("isAvail");

				bookName.setText(bName);
				bookAuthor.setText(bAuthor);
				String status = (bStatus) ? "Available" : "Not Available";
				bookStatus.setText(status);
				flag = true;
			}
			if (!flag)
				bookName.setText("No such book available");
		} catch (Exception e) {

		}
	}

	@FXML
	public void loadBookInfo2(ActionEvent event) {
		readyForSubmission = false;
		ObservableList<String> issueData = FXCollections.observableArrayList();
		String id = bookID.getText();
		String query = "SELECT * FROM ISSUE WHERE bookID = '" + id + "'";
		ResultSet result = databaseHandler.execQuery(query);

		try {
			while (result.next()) {
				String mBookID = id;
				String mMemberID = result.getString("memberID");
				Timestamp mIssueTime = result.getTimestamp("issueTime");
				int mRenewCount = result.getInt("renewCount");

				// Format the Timestamp
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
						.withZone(ZoneId.systemDefault());
				String issueTimeFormatted = formatter.format(Instant.ofEpochMilli(mIssueTime.getTime()));

				issueData.add("Issue Date and Time: " + issueTimeFormatted);
				issueData.add("Renew Count: " + mRenewCount);

				issueData.add("Book Information:- ");
				query = "SELECT * FROM BOOK WHERE id = '" + mBookID + "'";
				try (ResultSet rs = databaseHandler.execQuery(query)) {
					while (rs.next()) {
						issueData.add("Book Name: " + rs.getString("title"));
						issueData.add("Book ID: " + rs.getString("id"));
						issueData.add("Book Author: " + rs.getString("author"));
						issueData.add("Book Publisher: " + rs.getString("publisher"));
					}
				}

				query = "SELECT * FROM MEMBER WHERE id = '" + mMemberID + "'";
				try (ResultSet rs = databaseHandler.execQuery(query)) {
					issueData.add("Member Information:- ");
					while (rs.next()) {
						issueData.add("Name: " + rs.getString("name"));
						issueData.add("Contact Information: " + rs.getString("mobile"));
						issueData.add("Email: " + rs.getString("email"));
					}

					readyForSubmission = true;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Log the exception to see what went wrong
		}

		javafx.application.Platform.runLater(() -> issueDataList.getItems().setAll(issueData));
	}

	@FXML
	void loadMemberInformation(ActionEvent event) {
		clearMemberCache();
		String id = memberIdInput.getText();
		String query = "SELECT * FROM MEMBER WHERE id = '" + id + "'";
		System.out.println(query);
		ResultSet result = databaseHandler.execQuery(query);
		boolean flag = false;
		try {
			while (result.next()) {
				String mName = result.getString("name");
				String mContact = result.getString("mobile");

				memberName.setText(mName);
				memberContact.setText(mContact);
				flag = true;
			}
			if (!flag)
				memberName.setText("Member doesn't exist");
		} catch (Exception e) {

		}
	}

	@FXML
	void loadIssue(ActionEvent event) {
		String memberID = memberIdInput.getText();
		String bookID = bookIdInput.getText();

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Confirm Operation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to issue the book " + bookName.getText() + "\n to "
				+ memberName.getText() + " ?");
		Optional<ButtonType> response = alert.showAndWait();

		if (response.get() == ButtonType.OK) {
			// Corrected query
			String query = "INSERT INTO ISSUE(memberID, bookID) VALUES (" + "'" + memberID + "', " + "'" + bookID
					+ "')";

			String query2 = "UPDATE BOOK SET isAvail = false WHERE id = '" + bookID + "'";

			if (databaseHandler.execAction(query) && databaseHandler.execAction(query2)) {
				Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
				alert1.setTitle("Success");
				alert1.setHeaderText(null);
				alert1.setContentText("Book Issue Complete!");
				alert1.showAndWait();
			} else {
				Alert alert1 = new Alert(Alert.AlertType.ERROR);
				alert1.setTitle("Failed");
				alert1.setHeaderText(null);
				alert1.setContentText("Book Issue Failed!");
				alert1.showAndWait();
			}
		} else {
			Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Canceled");
			alert1.setHeaderText(null);
			alert1.setContentText("Canceled!");
			alert1.showAndWait();
		}
	}

	@FXML
	void loadSubmission(ActionEvent event) {

		if (!readyForSubmission) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Failed");
			alert.setHeaderText(null);
			alert.setContentText("Please select book to submit");
			alert.showAndWait();
			return;
		}
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Confirm Operation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to return the book?");

		Optional<ButtonType> response = alert.showAndWait();

		if (response.get() == ButtonType.OK) {
			String id = bookID.getText();
			String acl = "DELETE FROM ISSUE WHERE BOOKID = '" + id + "'";
			String acl1 = "UPDATE BOOK SET isAvail = TRUE WHERE id = '" + id + "'";
			if (databaseHandler.execAction(acl) && databaseHandler.execAction(acl1)) {
				Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
				alert1.setTitle("Success");
				alert1.setHeaderText(null);
				alert1.setContentText("Book Has Been Submitted");
				alert1.showAndWait();
			} else {
				Alert alert1 = new Alert(Alert.AlertType.ERROR);
				alert1.setTitle("Failed");
				alert1.setHeaderText(null);
				alert1.setContentText("Submmition has failed");
				alert1.showAndWait();
			}
		} else if (response.get() == ButtonType.CANCEL) {
			Alert alert1 = new Alert(Alert.AlertType.ERROR);
			alert1.setTitle("Failed");
			alert1.setHeaderText(null);
			alert1.setContentText("Canceled");
			alert1.showAndWait();
		}

	}

	@FXML
	void loadRenew(ActionEvent event) {

		if (!readyForSubmission) {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setTitle("Failed");
			alert.setHeaderText(null);
			alert.setContentText("Please select book to renew");
			alert.showAndWait();
			return;
		}

		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Confirm Operation");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to renew the book?");

		Optional<ButtonType> response = alert.showAndWait();

		if (response.get() == ButtonType.OK) {
			String ac = "UPDATE ISSUE SET issueTime = CURRENT_TIMESTAMP, renewCount = renewCount+1 WHERE bookID = '"
					+ bookID.getText() + "'";
			System.out.println(ac);
			if (databaseHandler.execAction(ac)) {
				Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
				alert1.setTitle("Success");
				alert1.setHeaderText(null);
				alert1.setContentText("Book Has Been Renewed");
				alert1.showAndWait();
//				loadBookInfo2();
			} else {
				Alert alert1 = new Alert(Alert.AlertType.ERROR);
				alert1.setTitle("Failed");
				alert1.setHeaderText(null);
				alert1.setContentText("Renew has failed");
				alert1.showAndWait();
			}

		}
	}

	void clearBookCache() {

		bookName.setText("");
		bookAuthor.setText("");
		bookStatus.setText("");
	}

	void clearMemberCache() {

		memberName.setText("");
		memberContact.setText("");
	}

	void loadWindow(String loc, String title) {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource(loc));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle(title);
			stage.setScene(new Scene(parent));
			stage.show();
			LibraryAssistantUtil.setStageIcon(stage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
}

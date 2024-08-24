
package library.assistant.ui.mainmenu;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;

import javafx.event.Event;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.effects.JFXDepthManager;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import library.assistant.database.DatabaseHandler;
import library.assistant.util.LibraryAssistantUtil;

public class MainMenuController implements Initializable {

	@FXML
	private BorderPane rootBorderPane;
	
	@FXML
	private Text bookAuthor;

	@FXML
	private TextField bookAuthorHolder;

	@FXML
	private TextField bookID;

	@FXML
	private TextField bookIdInput;

	@FXML
	private Text bookName;

	@FXML
	private TextField bookNameHolder;

	@FXML
	private TextField bookPublisherHolder;

	@FXML
	private Text bookStatus;

	@FXML
	private HBox book_info;

	@FXML
	private JFXDrawer drawer;

	@FXML
	private TextField fineInfoHolder;

	@FXML
	private JFXHamburger hamburger;

	@FXML
	private TextField issueDateHolder;

	@FXML
	private Text memberContact;

	@FXML
	private TextField memberContactHolder;

	@FXML
	private TextField memberEmailHolder;

	@FXML
	private TextField memberIdInput;

	@FXML
	private Text memberName;

	@FXML
	private TextField memberNameHolder;

	@FXML
	private HBox member_info;

	@FXML
	private TextField numberDaysHolder;

	@FXML
	private StackPane rootPane;

	@FXML
	private HBox submissionDataContainer;
	@FXML
	private ListView<String> issueDataList;

	DatabaseHandler databaseHandler;

	Boolean readyForSubmission = false;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		JFXDepthManager.setDepth(book_info, 1);
		JFXDepthManager.setDepth(member_info, 1);
		databaseHandler = DatabaseHandler.getInstance();
		initDrawer();
	}

	@FXML
	void handleMenuClose(ActionEvent event) {
		((Stage) rootPane.getScene().getWindow()).close();
	}

	@FXML
	void handleMenuFullScreen(ActionEvent event) {

		Stage stage = (Stage) rootPane.getScene().getWindow();
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
		String id = bookID.getText();
		String query = "SELECT ISSUE.bookID, ISSUE.memberID, ISSUE.issueTime, ISSUE.renewCount, "
				+ "MEMBER.name, MEMBER.mobile, MEMBER.email, "
				+ "BOOK.title, BOOK.author, BOOK.publisher, BOOK.isAvail " + "FROM ISSUE "
				+ "LEFT JOIN MEMBER ON ISSUE.memberID = MEMBER.ID " + "LEFT JOIN BOOK ON ISSUE.bookID = BOOK.ID "
				+ "WHERE ISSUE.bookID = '" + id + "'";

		ResultSet result = databaseHandler.execQuery(query);

		try {
			if (result.next()) {
				memberNameHolder.setText(result.getString("name"));
				memberContactHolder.setText(result.getString("mobile"));
				memberEmailHolder.setText(result.getString("email"));

				bookNameHolder.setText(result.getString("title"));
				bookAuthorHolder.setText(result.getString("author"));
				bookPublisherHolder.setText(result.getString("author"));

				Timestamp mIssueTime = result.getTimestamp("issueTime");
				Date dateOfIssue = new Date(mIssueTime.getTime());
				issueDateHolder.setText(dateOfIssue.toString());

				Long timeElapsed = System.currentTimeMillis() - mIssueTime.getTime();
				Long DaysElapsed = TimeUnit.DAYS.convert(timeElapsed, TimeUnit.MILLISECONDS);
				numberDaysHolder.setText(DaysElapsed.toString());
				readyForSubmission = true;
			} else {

				BoxBlur blur = new BoxBlur(3,3,3);
				JFXDialogLayout dialogLayout = new JFXDialogLayout();
				JFXDialog dialog = new JFXDialog(rootPane, dialogLayout, JFXDialog.DialogTransition.CENTER);
				dialogLayout.setHeading(new Label("Book is not issued"));
				
				Button button = new Button("Okay");
				button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent)-> {

					rootBorderPane.setEffect(null);
					dialog.close();
				});
				dialogLayout.setActions(button);
				dialog.show();
				dialog.setOnDialogClosed((Event event1) ->{
					rootBorderPane.setEffect(null);
				});
				rootBorderPane.setEffect(blur);
				
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Log the exception to see what went wrong
		}

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

	private void initDrawer() {
		try {
			VBox toolbar = FXMLLoader
					.load(getClass().getResource("/library/assistant/ui/mainmenu/toolbar/Toolbar.fxml"));
			drawer.setSidePane(toolbar);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HamburgerSlideCloseTransition task = new HamburgerSlideCloseTransition(hamburger);
		task.setRate(-1);
		hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event e) -> {

			// animation
			task.setRate(task.getRate() * -1);
			task.play();
			System.out.println(drawer.isClosed());
			if (drawer.isClosed()) {
				drawer.open();
				drawer.setMinWidth(100);
			} else {
				drawer.close();
			}

		});
	}

}

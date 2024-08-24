
package library.assistant.ui.mainmenu;

import javafx.event.ActionEvent;

import javafx.event.Event;
import javafx.event.EventHandler;

import java.io.IOException;
import java.net.URL;
import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tab;
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
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.util.LibraryAssistantUtil;

public class MainMenuController implements Initializable {

	@FXML
	private Button renewButton;

	@FXML
	private Button submissionButton;

	@FXML
	private BorderPane rootBorderPane;

	@FXML
	private StackPane bookInfoContainer;

	@FXML
	private StackPane memberInfoContainer;
	
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
	private Tab bookIssueTab;
	
	@FXML
	private Tab renewTab;
	
	@FXML
	private ListView<String> issueDataList;

	DatabaseHandler databaseHandler;

	Boolean readyForSubmission = false;
	
	PieChart bookPieChart;
	PieChart memberPieChart;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		JFXDepthManager.setDepth(book_info, 1);
		JFXDepthManager.setDepth(member_info, 1);
		
		databaseHandler = DatabaseHandler.getInstance();
		
		initDrawer();
		initGraphs();
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
		
		graphContainerHandler(false);
		
		
		boolean flag = false;
		try {
			String id = bookIdInput.getText();
			String query = "SELECT * FROM BOOK WHERE id = '" + id + "'";
			System.out.println(query);
			ResultSet result = DatabaseHandler.execQuery(query);
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

		clearEntries();
		readyForSubmission = false;

		try {

			String id = bookID.getText();
			String query = "SELECT ISSUE.bookID, ISSUE.memberID, ISSUE.issueTime, ISSUE.renewCount, "
					+ "MEMBER.name, MEMBER.mobile, MEMBER.email, "
					+ "BOOK.title, BOOK.author, BOOK.publisher, BOOK.isAvail " + "FROM ISSUE "
					+ "LEFT JOIN MEMBER ON ISSUE.memberID = MEMBER.ID " + "LEFT JOIN BOOK ON ISSUE.bookID = BOOK.ID "
					+ "WHERE ISSUE.bookID = '" + id + "'";

			ResultSet result = DatabaseHandler.execQuery(query);
			
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
				controlsHandler(true);
				submissionDataContainer.setOpacity(1);
			} else {

				Button button = new Button("Okay");

				AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(button),
						"Book has not been issued", null);

			}
		} catch (SQLException e) {
			e.printStackTrace(); // Log the exception to see what went wrong
		}

	}

	private void clearEntries() {

		memberNameHolder.setText("");
		memberContactHolder.setText("");
		memberEmailHolder.setText("");

		bookNameHolder.setText("");
		bookAuthorHolder.setText("");
		bookPublisherHolder.setText("");

		issueDateHolder.setText("");
		numberDaysHolder.setText("");
		fineInfoHolder.setText("");

		controlsHandler(false);
		submissionDataContainer.setOpacity(0);
	}

	private void controlsHandler(Boolean enableFlag) {

		if (enableFlag) {
			renewButton.setDisable(false);
			submissionButton.setDisable(false);

		} else {
			renewButton.setDisable(true);
			submissionButton.setDisable(true);
		}
	}

	@FXML
	void loadMemberInformation(ActionEvent event) {
		
		clearMemberCache();

		graphContainerHandler(false);
		
		
		boolean flag = false;
		try {
			String id = memberIdInput.getText();
			String query = "SELECT * FROM MEMBER WHERE id = '" + id + "'";
			System.out.println(query);
			ResultSet result = DatabaseHandler.execQuery(query);
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
		
		Button confirmButton = new Button("Yes!");
		confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {

			// Corrected query
			String query = "INSERT INTO ISSUE(memberID, bookID) VALUES (" + "'" + memberID + "', " + "'" + bookID
					+ "')";

			String query2 = "UPDATE BOOK SET isAvail = false WHERE id = '" + bookID + "'";

			if (databaseHandler.execAction(query) && databaseHandler.execAction(query2)) {

				Button button = new Button("Done!");

				AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(button),
						"Book Issue Complete!", null);
				refreshGraphs();

			} else {
				Button button = new Button("Yes");

				AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(button),
						"Book Issue Failed!", null);
			}
			clearIssueEntries();
		});
		
		Button declineButton = new Button("No!");
		declineButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
			Button button = new Button("Cancel");

			AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(button),
					"Book Issue Canceled!", null);
			clearIssueEntries();
		});
		
		
		AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(confirmButton, declineButton),"Confirm Issue", "Are you sure you want to issue the book " + bookName.getText() + " to "
				+  memberName.getText() + " ?");

	}

	@FXML
	void loadSubmission(ActionEvent event) {

		
		if (!readyForSubmission) {
			
			Button button = new Button("Okay!");
			AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(button), "Please select a book!", null);	
			return;
		}
		
		Button confirmButton = new Button("Yes!");
		confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {


			String id = bookID.getText();
			String acl = "DELETE FROM ISSUE WHERE BOOKID = '" + id + "'";
			String acl1 = "UPDATE BOOK SET isAvail = TRUE WHERE id = '" + id + "'";
			
			if (databaseHandler.execAction(acl) && databaseHandler.execAction(acl1)) {
				
				Button button = new Button("Done!");
				AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(button), "Book Has Been Submitted", null);

				controlsHandler(false);
				submissionDataContainer.setOpacity(0);
				
			} else {
				
				Button button = new Button("Okay!");
				AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(button), "Submmition has failed", null);


			}	
		});
		
		Button declineButton = new Button("No!");
		declineButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {


			Button button = new Button("Okay!");
			AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(button), "Submmition cancelled", null);
			
		});
		
		AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(confirmButton, declineButton), "Confirm Submission", "Are you sure you want to return this book?");

		
	}

	@FXML
	void loadRenew(ActionEvent event) {

		if (!readyForSubmission) {
			
			Button button = new Button("Okay!");
			AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(button), "Please select a book!", null);	
			
			return;
		}

		

		Button confirmButton = new Button("Yes!");
		confirmButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {
			
			String ac = "UPDATE ISSUE SET issueTime = CURRENT_TIMESTAMP, renewCount = renewCount + 1 WHERE bookID = '"
					+ bookID.getText() + "'";
			
			if (databaseHandler.execAction(ac)) {
				
				Button button = new Button("Done!");
				AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(button), "Book Has Been Renewed", null);
				controlsHandler(false);
				submissionDataContainer.setOpacity(0);
				
			} else {

				Button button = new Button("Okay!");
				AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(button), "Submmition has failed", null);
			}
		});
		
		Button declineButton = new Button("No!");
		declineButton.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent event1) -> {


			Button button = new Button("Okay!");
			AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(button), "Renewal cancelled", null);
			
		});
	
		AlertMaker.showMaterialDialog(rootPane, rootBorderPane, Arrays.asList(confirmButton, declineButton), "Confirm Renewal", "Are you sure you want to renew this book?");

	}

	private void clearBookCache() {

		bookName.setText("");
		bookAuthor.setText("");
		bookStatus.setText("");
	}

	private void clearMemberCache() {

		memberName.setText("");
		memberContact.setText("");
	}

	private void clearIssueEntries() {
		
		bookIdInput.clear();
		memberIdInput.clear();
		
		bookName.setText("");
		bookAuthor.setText("");
		bookStatus.setText("");
		
		memberName.setText("");
		memberContact.setText("");

		graphContainerHandler(true);
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
	
	private void initGraphs() {

		bookPieChart = new PieChart(DatabaseHandler.bookGraphStatistics());
		bookInfoContainer.getChildren().add(bookPieChart);

		memberPieChart = new PieChart(DatabaseHandler.memberGraphStatistics());
		memberInfoContainer.getChildren().add(memberPieChart);
		
		bookIssueTab.setOnSelectionChanged(new EventHandler<Event>(){
			@Override
			public void handle(Event event) {
				
				clearIssueEntries();
				if(bookIssueTab.isSelected()) {
					refreshGraphs();
				}
			}
		});
	}
	
	private void refreshGraphs() {
		
		bookPieChart.setData(DatabaseHandler.bookGraphStatistics());
		memberPieChart.setData(DatabaseHandler.memberGraphStatistics());
		
	}
	private void graphContainerHandler(Boolean status) {
		
		if(status) {
			bookPieChart.setOpacity(1);
			memberPieChart.setOpacity(1);
		} else {
			bookPieChart.setOpacity(0);
			memberPieChart.setOpacity(0);
		}
	}

}

package library.assistant.ui.listbooks;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.addbook.AddBookController;
import library.assistant.util.LibraryAssistantUtil;

public class ListBooksController implements Initializable {

	ObservableList<Book> list = FXCollections.observableArrayList();

	@FXML
	private TableColumn<Book, String> authorColumn;

	@FXML
	private TableColumn<Book, Boolean> availabilityColumn;

	@FXML
	private TableColumn<Book, String> idColumn;

	@FXML
	private TableColumn<Book, String> publisherColumn;

	@FXML
	private AnchorPane rootPanel;

	@FXML
	private TableView<Book> tableView;

	@FXML
	private TableColumn<Book, String> titleColumn;

	DatabaseHandler databaseHandler;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		initCol();
		loadData();
	}

	private void loadData() {
		// TODO Auto-generated method stub

		list.clear();
		databaseHandler = DatabaseHandler.getInstance();
		String query = "SELECT * FROM BOOK";
		ResultSet rs = databaseHandler.execQuery(query);
		if (rs == null) {
			System.out.println("Query returned null ResultSet.");
			return;
		}
		try {
			while (rs.next()) {
				String title = rs.getString("title");
				String author = rs.getString("author");
				String id = rs.getString("id");
				String publisher = rs.getString("publisher");
				Boolean availability = rs.getBoolean("isAvail");
				System.out.println(
						"Fetched: " + title + ", " + author + ", " + id + ", " + publisher + ", " + availability);

				list.add(new Book(title, id, author, publisher, availability));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		tableView.setItems(list);
	}

	private void initCol() {
		// TODO Auto-generated method stub
		titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
		idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
		authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
		publisherColumn.setCellValueFactory(new PropertyValueFactory<>("publisher"));
		availabilityColumn.setCellValueFactory(new PropertyValueFactory<>("availability"));
	}

	@FXML
	void handleBookDelete(ActionEvent event) {
		Book selectedForDeletion = tableView.getSelectionModel().getSelectedItem();
		if(selectedForDeletion == null) {
			AlertMaker.showErrorMessage("No book selected", "Please select book");
			return;
		}
		
		
		if (DatabaseHandler.getInstance().isBookAlreadyIssued(selectedForDeletion)) {
            AlertMaker.showErrorMessage("Cant be deleted", "This book is already issued and cant be deleted.");
            return;
        }
		
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Delete Book");
		alert.setContentText("Are you sure you want to delete the book " + selectedForDeletion.getTitle() + " ? ");
		Optional<ButtonType> answer = alert.showAndWait();
		if(answer.get() == ButtonType.OK) {
			Boolean result = DatabaseHandler.getInstance().deleteBook(selectedForDeletion);
			if(result) {
				AlertMaker.showSimpleAlert("Book deleted", selectedForDeletion.getTitle() + " was deleted successfuly");
				list.remove(selectedForDeletion);
				
			} else {
				AlertMaker.showSimpleAlert("Failed. ", selectedForDeletion.getTitle() + " could not be deleted");
			}
		} else {
			AlertMaker.showSimpleAlert("Deletion cancelled", "Process cancelled");
		}
	}
	@FXML
	void handleBookEdit(ActionEvent event) {
		Book selectedForEdit = tableView.getSelectionModel().getSelectedItem();
		if(selectedForEdit == null) {
			AlertMaker.showErrorMessage("No book selected", "Please select book");
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/library/assistant/ui/addbook/addbook.fxml"));
			Parent parent = loader.load();
			
			AddBookController controller = (AddBookController) loader.getController();
			controller.inflateUI(selectedForEdit);
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Edit book");
			stage.setScene(new Scene(parent));
			stage.show();
			LibraryAssistantUtil.setStageIcon(stage);
			stage.setOnCloseRequest((e) -> {

				handleRefresh(new ActionEvent());
				
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	@FXML
	void handleRefresh(ActionEvent event) {
		loadData();
	}
	
	public static class Book {
		private final SimpleStringProperty title;
		private final SimpleStringProperty id;
		private final SimpleStringProperty author;
		private final SimpleStringProperty publisher;
		private final SimpleBooleanProperty availability;

		public Book(String title, String id, String author, String publisher, Boolean availability) {
			this.title = new SimpleStringProperty(title);
			this.id = new SimpleStringProperty(id);
			this.author = new SimpleStringProperty(author);
			this.publisher = new SimpleStringProperty(publisher);
			this.availability = new SimpleBooleanProperty(availability);
		}

		public String getTitle() {
			return title.get();
		}

		public String getId() {
			return id.get();
		}

		public String getAuthor() {
			return author.get();
		}

		public String getPublisher() {
			return publisher.get();
		}

		public Boolean getAvailability() {
			return availability.get();
		}
	}
}

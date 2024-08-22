package library.assistant.ui.listbooks;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import library.assistant.database.DatabaseHandler;

public class ListBooksController implements Initializable{

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

		databaseHandler = DatabaseHandler.getInstance();
		String query = "SELECT * FROM BOOK";
		ResultSet rs = databaseHandler.execQuery(query);
		if (rs == null) {
		    System.out.println("Query returned null ResultSet.");
		    return;
		}
		try {
			while(rs.next()) {
				String title =rs.getString("title");
				String author =rs.getString("author");
				String id =rs.getString("id");
				String publisher=rs.getString("publisher");
				Boolean availability =rs.getBoolean("isAvail");
				System.out.println("Fetched: " + title + ", " + author + ", " + id + ", " + publisher + ", " + availability);
			    
				
				list.add(new Book(title, author, id, publisher, availability));
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

	public static class Book{
		private final SimpleStringProperty title;
		private final SimpleStringProperty id;
		private final SimpleStringProperty author;
		private final SimpleStringProperty publisher;
		private final SimpleBooleanProperty availability;
		
		Book(String title, String id, String author, String publisher, Boolean availability){
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

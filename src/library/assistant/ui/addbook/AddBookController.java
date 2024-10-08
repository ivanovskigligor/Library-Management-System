package library.assistant.ui.addbook;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listbooks.ListBooksController;

public class AddBookController implements Initializable {
	
    @FXML
    private AnchorPane rootPanel;
    
    @FXML
    private TextField booktitle;
    
    @FXML
    private TextField bookid;
    
	@FXML
    private TextField bookauthor;

    @FXML
    private TextField bookpublisher;

    @FXML
    private Button cancelbutton;

    @FXML
    private Button savebutton;


    private Boolean editMode = Boolean.FALSE;
    
    DatabaseHandler databaseHandler;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = DatabaseHandler.getInstance();
        checkData();
        
   
    }
    
    private void checkData() {
		String query = "SELECT title FROM BOOK";
		ResultSet rs = databaseHandler.execQuery(query);
		try {
			while(rs.next()) {
				String title =rs.getString("title");
				System.out.println(title);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@FXML
    void addBook(ActionEvent event) {
    	String bookID = bookid.getText();
    	String bookAuthor = bookauthor.getText();
    	String bookName = booktitle.getText();
    	String bookPublisher = bookpublisher.getText();
    	
    	if(bookID.isEmpty() || bookAuthor.isEmpty()|| bookName.isEmpty()|| bookPublisher.isEmpty()) {
    		Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setHeaderText(null);
    		alert.setContentText("Enter information in all fields");
    		alert.showAndWait();
    		return;
    		
    	}
    	
    	if(editMode) {
    		handleEdit();
    		return;
    	}
    	String query = "INSERT INTO BOOK VALUES (" +
    			 "'" + bookID + "'," +
    			 "'" + bookName + "'," +
    			 "'" + bookAuthor + "'," +
    			 "'" + bookPublisher + "'," +
    			 "'" + "true" + "'" +
				 " )";
    System.out.println(query);
    
    if(databaseHandler.execAction(query)) {
    	Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setHeaderText(null);
		alert.setContentText("Success");
		alert.showAndWait();
    } else {
    	 Alert alert = new Alert(Alert.AlertType.ERROR);
    		alert.setHeaderText(null);
    		alert.setContentText("Failed");
    		alert.showAndWait();
    }
    
   
    }

    private void handleEdit() {

    	ListBooksController.Book book = new ListBooksController.Book(booktitle.getText(), bookid.getText(), bookauthor.getText(), bookpublisher.getText(), true);
		if(databaseHandler.updateBook(book)) {
			AlertMaker.showSimpleAlert("Success", "Book Updated");
			
		} else {
			AlertMaker.showErrorMessage("Failed", "Book Update Failed");
		}
	}

	@FXML
    void cancel(ActionEvent event) {
    	Stage stage = (Stage) rootPanel.getScene().getWindow();
    	stage.close();
    }
    
    public void inflateUI(ListBooksController.Book book) {
    	
    	editMode = Boolean.TRUE;
    	booktitle.setText(book.getTitle());
    	bookid.setText(book.getId());
    	bookauthor.setText(book.getAuthor());
    	bookpublisher.setText(book.getPublisher());
    	bookid.setEditable(false);
    }

}

package library.assistant.ui.addbook;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import library.assistant.database.DatabaseHandler;

public class AddBookController implements Initializable {
	@FXML
    private TextField bookauthor;

    @FXML
    private TextField bookid;

    @FXML
    private TextField bookpublisher;

    @FXML
    private TextField booktitle;

    @FXML
    private Button cancelbutton;

    @FXML
    private Button savebutton;


    DatabaseHandler databaseHandler;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        databaseHandler = new DatabaseHandler();
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
    	String query = "INSERT INTO BOOK VALUES (" +
    			 "'" + bookID + "'," +
    			 "'" + bookAuthor + "'," +
    			 "'" + bookName + "'," +
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

    @FXML
    void cancel(ActionEvent event) {

    }

}

package library.assistant.ui.addbook;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import library.assistant.database.DatabaseHandler;

public class AddBookController {
    @FXML
    private Button addbook;

    @FXML
    private TextField author;

    @FXML
    private Button cancel;

    @FXML
    private TextField id;

    @FXML
    private TextField publisher;

    @FXML
    private TextField title;

    DatabaseHandler databaseHandler;
    
    public void initialize(URL url, ResourceBundle rb) {
    	databaseHandler = new DatabaseHandler();
    }
    @FXML
    void addBook(ActionEvent event) {

    }

    @FXML
    void cancel(ActionEvent event) {

    }

}

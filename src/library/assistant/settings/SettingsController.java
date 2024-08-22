package library.assistant.settings;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class SettingsController implements Initializable {

    @FXML
    private TextField finePerDay;

    @FXML
    private TextField nDaysWithoutFine;

    @FXML
    private PasswordField password;

    @FXML
    private TextField username;

    @FXML
    void handleCancelButtonAction(ActionEvent event) {

    }

    @FXML
    void handleSaveButtonAction(ActionEvent event) {

    }

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

}

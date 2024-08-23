package library.assistant.settings;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
		
		Stage stage = (Stage) nDaysWithoutFine.getScene().getWindow();
		stage.close();
	}

	@FXML
	void handleSaveButtonAction(ActionEvent event) {
		int nDays = Integer.parseInt(nDaysWithoutFine.getText());
		float fine = Float.parseFloat(finePerDay.getText());
		String uName = username.getText();
		String pass = password.getText();

		Preferences preferences = Preferences.getPreferences();
		preferences.setnDaysWithoutFine(nDays);
		preferences.setFinePerDay(fine);
		preferences.setUsername(uName);
		preferences.setPassword(pass);
		
		Preferences.writePreferencesToFile(preferences);
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		initDefaultValues();
	}

	private void initDefaultValues() {
		// TODO Auto-generated method stub
		Preferences preferences = Preferences.getPreferences();
		nDaysWithoutFine.setText(String.valueOf(preferences.getnDaysWithoutFine()));
		finePerDay.setText(String.valueOf(preferences.getFinePerDay()));
		username.setText(String.valueOf(preferences.getUsername()));
		password.setText(String.valueOf(preferences.getPassword()));

	}

}

package library.assistant.ui.login;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.commons.codec.digest.DigestUtils;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import library.assistant.settings.Preferences;
import library.assistant.util.LibraryAssistantUtil;

public class LoginController implements Initializable {

	
	Preferences preferences;
	
    @FXML
    private PasswordField password;

    @FXML
    private TextField username;


    
    @FXML
    void handleCancel(ActionEvent event) {
    	System.exit(0);
    }

    @FXML
    void handleLogin(ActionEvent event) {
    	

		
    	preferences = Preferences.getPreferences();
    	String uName = username.getText();
		String pass = DigestUtils.shaHex(password.getText());

		if(uName.equals(preferences.getUsername())&&pass.equals(preferences.getPassword())) {
			closeStage();
			loadMain();
		} else {
			username.getStyleClass().add("wrong-credentials");
            password.getStyleClass().add("wrong-credentials");
		}

    }

	private void closeStage() {
		// TODO Auto-generated method stub
		Stage stage = (Stage) username.getScene().getWindow();
		stage.close();
	}
	
	void loadMain() {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource("/library/assistant/ui/mainmenu/Mainmenu.fxml"));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle("Library assistant");
			stage.setScene(new Scene(parent));
			stage.show();
			LibraryAssistantUtil.setStageIcon(stage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		preferences = Preferences.getPreferences();
	}

}

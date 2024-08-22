
package library.assistant.ui.mainmenu;

import javafx.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainMenuController implements Initializable {
	
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}
	@FXML
	void loadAddBook(ActionEvent event) {
		loadWindow("/library/assistant/ui/addbook/addbook.fxml", "Add New Book");
	}

	@FXML
	void loadAddMember(ActionEvent event) {

		loadWindow("/library/assistant/ui/addmember/Addmember.fxml", "Add New Member");
	}

	@FXML
	void loadBookTable(ActionEvent event) {

		loadWindow("/library/assistant/ui/listbooks/listbooks.fxml", "List Books");
	}

	@FXML
	void loadMemberTable(ActionEvent event) {

		loadWindow("/library/assistant/ui/listmember/ListMembers.fxml", "List Members");
	}

	void loadWindow(String loc, String title) {
		try {
			Parent parent = FXMLLoader.load(getClass().getResource(loc));
			Stage stage = new Stage(StageStyle.DECORATED);
			stage.setTitle(title);
			stage.setScene(new Scene(parent));
			stage.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
}

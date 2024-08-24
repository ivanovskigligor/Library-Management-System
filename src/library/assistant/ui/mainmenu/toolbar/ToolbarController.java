package library.assistant.ui.mainmenu.toolbar;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import library.assistant.util.LibraryAssistantUtil;

public class ToolbarController implements Initializable {


	@FXML
	void loadAddBook(ActionEvent event) {
		LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/addbook/addbook.fxml"), "Add New Book", null);
	}

	@FXML
	void loadAddMember(ActionEvent event) {

		LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/addmember/Addmember.fxml"), "Add New Member", null);
	}

	@FXML
	void loadBookTable(ActionEvent event) {

		LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/listbooks/listbooks.fxml"), "List Books", null);
	}

	@FXML
	void loadMemberTable(ActionEvent event) {

		LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/ui/listmember/ListMembers.fxml"), "List Members", null);
	}

	@FXML
	void loadSettings(ActionEvent event) {
		LibraryAssistantUtil.loadWindow(getClass().getResource("/library/assistant/settings/Settings.fxml"), "Settings", null);

	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
}

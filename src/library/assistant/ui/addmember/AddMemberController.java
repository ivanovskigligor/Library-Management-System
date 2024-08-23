package library.assistant.ui.addmember;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import library.assistant.alert.AlertMaker;
import library.assistant.database.DatabaseHandler;
import library.assistant.ui.listbooks.ListBooksController;
import library.assistant.ui.listmember.ListMembersController;
import library.assistant.ui.listmember.ListMembersController.Member;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;

public class AddMemberController implements Initializable {

	DatabaseHandler handler;

	@FXML
	private Button addMember;

	@FXML
	private Button cancelbutton;

	@FXML
	private TextField memberemail;

	@FXML
	private TextField memberid;

	@FXML
	private TextField membername;

	@FXML
	private TextField memberphone;

	@FXML
	private AnchorPane rootPanel;

	private Boolean editMode = false;
	
	@FXML
	void addMember(ActionEvent event) {

		String mname = membername.getText();
		String mid = memberid.getText();
		String mphone = memberphone.getText();
		String memail = memberemail.getText();

		if (mname.isEmpty() || mid.isEmpty() || mphone.isEmpty() || memail.isEmpty()) {
			AlertMaker.showErrorMessage("Can't process member", "Please enter in all fields");
			return;
		}

		if(editMode) {
			handleUpdateMember();
			return;
		}
		String query = "INSERT INTO MEMBER VALUES (" + "'" + mid + "'," + "'" + mname + "'," + "'" + mphone + "'," + "'"
				+ memail + "'" + " )";
		System.out.println(query);
		if (handler.execAction(query)) {
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

	private void handleUpdateMember() {

		Member member = new ListMembersController.Member(membername.getText(), memberid.getText(), memberphone.getText(), memberemail.getText());
		if(DatabaseHandler.getInstance().updateMember(member)) {
			AlertMaker.showSimpleAlert("Success", "Member Updated");
			
		} else {
			AlertMaker.showErrorMessage("Failed", "Member Update Failed");
		}
		
	}

	@FXML
	void cancel(ActionEvent event) {
		Stage stage = (Stage) rootPanel.getScene().getWindow();
		stage.close();
	}

	public void inflateUI(Member member) {
		membername.setText(member.getName());
		memberid.setText(member.getId());
		memberid.setEditable(false);
		memberphone.setText(member.getMobile());
		memberemail.setText(member.getEmail());
		editMode = Boolean.TRUE;
	}
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		// TODO Auto-generated method stub
		handler = DatabaseHandler.getInstance();
	}

}

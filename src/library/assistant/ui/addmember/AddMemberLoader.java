package library.assistant.ui.addmember;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AddMemberLoader extends Application{


	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		Parent root = FXMLLoader.load(getClass().getResource("Addmember.fxml"));
        

        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}


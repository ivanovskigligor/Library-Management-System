package library.assistant.ui.mainmenu;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;
import library.assistant.util.LibraryAssistantUtil;

public class MainMenuLoader extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		Parent root = FXMLLoader.load(getClass().getResource("/library/assistant/ui/login/Login.fxml"));
        

        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Library Assistant Login");
        LibraryAssistantUtil.setStageIcon(stage);
        
        new Thread(() -> {
        	
        	DatabaseHandler.getInstance();
        }).start();
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

}

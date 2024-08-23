package library.assistant.settings;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;

public class SettingsLoader extends Application{

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		Parent root = FXMLLoader.load(getClass().getResource("/library/assistant/settings/Settings.fxml"));
        

        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
        stage.setTitle("Settings");
        
        new Thread(() -> {
        	
        	DatabaseHandler.getInstance();
        }).start();
        
	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

}
package library.assistant.ui.addbook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import library.assistant.database.DatabaseHandler;


public class LibraryAssistant extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
    	
    	Parent root = FXMLLoader.load(getClass().getResource("addbook.fxml"));
         

        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
}
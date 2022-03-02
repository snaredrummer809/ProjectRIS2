package application;
	
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;


public class Main extends Application {


	
	private static Stage stg;
	
	public void start(Stage primaryStage) throws Exception {
			stg = primaryStage;
			primaryStage.setResizable(false);
			Parent root = FXMLLoader.load(getClass().getResource("../Views/Login.fxml"));
			primaryStage.setTitle("RIS");
			primaryStage.setScene(new Scene(root,760,500));
			primaryStage.show();
			
		
	}
	
	
	public void changeScene(String fxml) throws IOException{
		
		Parent pane = FXMLLoader.load(getClass().getResource(fxml));
		stg.getScene().setRoot(pane);
	}
	

	
	
	public static void main(String[] args) {
		
		launch(args);
	}
}

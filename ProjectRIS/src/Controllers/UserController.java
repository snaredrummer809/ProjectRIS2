package Controllers;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;

public class UserController {

	
	public void userLogOut(ActionEvent event) throws IOException {
		
		Main m = new Main();
		
		m.changeScene("../Views/Login.fxml");
	}
	
	public void HomeButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/User.fxml");
	}
	
	public void ResourcesButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/UserResources.fxml");
	}
}

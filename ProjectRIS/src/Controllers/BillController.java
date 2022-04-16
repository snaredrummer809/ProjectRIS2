package Controllers;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;

public class BillController {

	
	public void userLogOut(ActionEvent event) throws IOException {
		
		Main m = new Main();
		
		m.changeScene("../Views/Login.fxml");
	}
}

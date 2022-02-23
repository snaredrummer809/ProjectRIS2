package Controllers;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;

public class TechController {

	
	public void userLogOut(ActionEvent event) throws IOException {
		
		Main m = new Main();
		
		m.changeScene("../Views/Login.fxml");
	}
	
	public void HomeButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/Tech.fxml");
	}
	
	public void AppointmentButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/TechAppointments.fxml");
	}
}

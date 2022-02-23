package Controllers;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;

public class DeskController {

	
	public void userLogOut(ActionEvent event) throws IOException {
		
		Main m = new Main();
		
		m.changeScene("../Views/Login.fxml");
	}
	
	public void AppointmentButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/DeskAppointments.fxml");
	}

	public void OrderButton(ActionEvent event) throws IOException{
	
		Main m = new Main();
		m.changeScene("../Views/DeskOrders.fxml");
	}

	public void HomeButton(ActionEvent event) throws IOException{
	
		Main m = new Main();
		m.changeScene("../Views/Desk.fxml");
	}
}

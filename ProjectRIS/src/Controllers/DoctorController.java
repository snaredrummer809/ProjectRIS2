package Controllers;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;

public class DoctorController {

	
	
	public void userLogOut(ActionEvent event) throws IOException {
		
		Main m = new Main();
		
		m.changeScene("../Views/Login.fxml");
	}
	
	public void HomeButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/Doctor.fxml");
}
	
	public void ReferralsButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/DoctorReferrals.fxml");
}

}

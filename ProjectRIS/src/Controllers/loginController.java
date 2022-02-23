package Controllers;


import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class loginController {

	
	@FXML private Button LoginButton;
	@FXML private Label wrongLogin;
	@FXML private TextField username;
	@FXML private PasswordField password;
	
	
	public void userLogin(ActionEvent event) throws IOException {
		
		checkLogin();
	}


	private void checkLogin() throws IOException {
		// TODO Auto-generated method stub
		
		Main m = new Main();
		if(username.getText().toString().equals("Doc") && password.getText().toString().equals("Doc")) {
		
			wrongLogin.setText("Success");
			m.changeScene("../Views/Doctor.fxml");
		}
		else if(username.getText().toString().equals("Admin") && password.getText().toString().equals("Admin")) {
			
			wrongLogin.setText("Success");
			m.changeScene("../Views/Admin.fxml");
		}
		else if(username.getText().toString().equals("Radio") && password.getText().toString().equals("Radio")) {
			
			wrongLogin.setText("Success");
			m.changeScene("../Views/Radio.fxml");
		}
		else if(username.getText().toString().equals("Desk") && password.getText().toString().equals("Desk")) {
	
			wrongLogin.setText("Success");
			m.changeScene("../Views/Desk.fxml");
		}
		else if(username.getText().toString().equals("Bill") && password.getText().toString().equals("Bill")) {
			
			wrongLogin.setText("Success");
			m.changeScene("../Views/Bill.fxml");
		}
		else if(username.getText().toString().equals("Tech") && password.getText().toString().equals("Tech")) {
			
			wrongLogin.setText("Success");
			m.changeScene("../Views/Tech.fxml");
		}
		else if(username.getText().toString().equals("User") && password.getText().toString().equals("User")) {
		
			wrongLogin.setText("Success");
			m.changeScene("../Views/User.fxml");
		}
		
		else if(username.getText().isEmpty() && password.getText().isEmpty()) {
			
			wrongLogin.setText("Please enter your data.");
		}
		
		else {
			
			wrongLogin.setText("wrong username or password");
		}
	}
	
}

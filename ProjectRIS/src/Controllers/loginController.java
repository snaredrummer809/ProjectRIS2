package Controllers;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class loginController {

	
	@FXML 
	private Button loginButton;
	@FXML
	private Label wrongLogin;
	@FXML 
	private TextField username;
	@FXML 
	private PasswordField password;
	
	
	
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;

	public void userLogin(ActionEvent event) throws IOException, SQLException {
		
		if(username.getText().isEmpty() == false && password.getText().isEmpty() == false) {
		
		validateLogin();
		
		}
		
		else {
			wrongLogin.setText("please enter username and password ");
		}
	}
	
	

	public void validateLogin() throws SQLException {
		
		
		Main m = new Main();
		
		
		try {
			
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ris", "root", "REPLACE WITH YOUR PASSWORD");
			Statement stmt = con.createStatement();
			String verifyLogin = "select * from users where username='" + username.getText() + "' and password='" + password.getText() + "'" ;
			ResultSet rs = stmt.executeQuery(verifyLogin);
			
			
			if(rs.next()) {
				
				
				wrongLogin.setText("success");
				
				
				if(username.getText().toString().equals("admin")) {
					
				
				m.changeScene("../Views/Admin.fxml");
				
				}
				
				else if(username.getText().toString().equals("bill")) {
					
					m.changeScene("../Views/Bill.fxml");
					
				}
				
				else if(username.getText().toString().equals("desk")) {
					
					m.changeScene("../Views/Desk.fxml");
					
				}
				
				else if(username.getText().toString().equals("doc")) {
					
					m.changeScene("../Views/Doctor.fxml");
					
				}
				
				else if(username.getText().toString().equals("radio")) {
					
					m.changeScene("../Views/Radio.fxml");
					
				}
				else if(username.getText().toString().equals("tech")) {
					
					m.changeScene("../Views/Tech.fxml");
					
				}
				else if(username.getText().toString().equals("user")) {
					
					m.changeScene("../Views/User.fxml");
					
				}
			}
			
			
			else {
				
				wrongLogin.setText("wrong username or password");
			
			}
		
			}
		
			catch(Exception e) {
				
				System.out.print(e);
			}
	
	
	
	
	
	}
	
}

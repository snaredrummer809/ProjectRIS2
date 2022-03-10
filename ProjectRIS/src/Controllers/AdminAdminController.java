package Controllers;

import java.io.IOException;
import java.sql.*;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;

public class AdminAdminController {
	
	// newUserPane pane & controls
	@FXML
	Pane newUserPane;
	@FXML
	TextField IDTextField;
	@FXML
	TextField usernameTextField;
	@FXML
	TextField displayNameTextField;
	@FXML
	TextField emailAddressTextField;
	@FXML
	CheckBox enabledCheckBox;
	@FXML
	ChoiceBox roleChoiceBox;
	@FXML
	TextField passwordTextField;
	@FXML
	TextField confirmPasswordTextField;
	Alert errorAlert = new Alert(AlertType.ERROR);
	Alert updateAlert = new Alert(AlertType.CONFIRMATION);
	
	public void userLogOut(ActionEvent event) throws IOException {
		
		Main m = new Main();
		
		m.changeScene("../Views/Login.fxml");
	}
	
	public void HomeButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/Admin.fxml");
	}
	
	public void AppointmentButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/AdminAppointments.fxml");
	}

	public void InvoiceButton(ActionEvent event) throws IOException{
	
		Main m = new Main();
		m.changeScene("../Views/AdminInvoice.fxml");
	}
	
	public void OrderButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/AdminOrders.fxml");
	}

	public void ReferralsButton(ActionEvent event) throws IOException{
	
		Main m = new Main();
		m.changeScene("../Views/AdminReferrals.fxml");
	}
	
	public void NewUserButton(ActionEvent event) throws IOException{
		
		newUserPane.setVisible(true);
	}
	
	public void createUserButton(ActionEvent event) throws IOException{
		int ID;
		String username = null;
		String displayName = null;
		String email = null;
		boolean enabled = enabledCheckBox.isSelected();
		int enableCheck = 0;
		String password = null;
		
		// Set username
		if(usernameTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Username cannot be blank.");
			errorAlert.showAndWait();
		}
		else if(!Character.isLetter(usernameTextField.getText().charAt(0))) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Username must start with a letter.");
			errorAlert.showAndWait();
		}
		else {
			username = usernameTextField.getText();
		}
		// Set display name
		for(int i = 0; i < displayNameTextField.getText().length(); i++) {
			if(!Character.isLetter(displayNameTextField.getText().charAt(i))) {
				errorAlert.setHeaderText("Invalid input");
				errorAlert.setContentText("Display Name must contain only letters.");
				errorAlert.showAndWait();				
				i = displayNameTextField.getText().length();
			}
		}
		if(displayNameTextField.getText().isBlank()){
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Display Name cannot be blank");
			errorAlert.showAndWait();
		}
		else {
			displayName = displayNameTextField.getText();
		}
		// Set email
		if(emailAddressTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Email cannot be blank.");
			errorAlert.showAndWait();
		}
		else {
			email = emailAddressTextField.getText();
		}
		// Set password
		if(passwordTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Password cannot be blank.");
			errorAlert.showAndWait();
		}
		else {
			if(passwordTextField.getText().equals(confirmPasswordTextField.getText())) {
				password = passwordTextField.getText();
			}
			else {
				errorAlert.setHeaderText("Invalid input");
				errorAlert.setContentText("Passwords do not match.");
				errorAlert.showAndWait();
			}
		}
		// Set enabled
		if(enabled == true) {
			enableCheck = 1;
		}
		else {
			enableCheck = 0;
		}
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ris", "root", "Sjs063099!");
			Statement stmt = con.createStatement();
			String IDCheck = "select * from users" ;
			ResultSet rs = stmt.executeQuery(IDCheck);
			
			int num = 0;
			
			while (rs.next()) {
				num = rs.getInt("user_id");
			}
			ID = num + 1;
			
			String newUser = "INSERT INTO users (user_id, email, full_name, username, password, enabled) "
					+ "VALUES (" + ID + ", \'" + email + "\', \'" + displayName + "\', \'" + username + "\', \'" + password
					+ "\', " + enableCheck + ")";
			
			stmt.executeUpdate(newUser);
			
			con.close();
		}
		catch(Exception e) {
			System.out.println("Error.");
		}
		newUserPane.setVisible(false);
		updateAlert.setHeaderText("Success!");
		updateAlert.setContentText("User has been successfully added.");
		updateAlert.showAndWait();
		
	}
	
	public void cancelUserButton(ActionEvent event) throws IOException{
		newUserPane.setVisible(false);
		IDTextField.clear();
		usernameTextField.clear();
		displayNameTextField.clear();
		emailAddressTextField.clear();
		enabledCheckBox.setSelected(false);
		passwordTextField.clear();
		confirmPasswordTextField.clear();
	}
}

package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import application.DatabaseConnection;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class AdminAdminController implements Initializable{
	
	// Nav buttons
	@FXML
	Button HomeButton;
	@FXML
	Button AppointmentButton;
	@FXML
	Button InvoiceButton;
	@FXML
	Button OrdersButton;
	@FXML
	Button ReferralsButton;
	@FXML
	Button LogOut;
	
	// Panes
	@FXML
	Pane systemUsersPane;
	@FXML
	Pane modalitiesPane;
	@FXML
	Pane patientAlertsPane;
	@FXML
	Pane patientsPane;
	@FXML
	Pane appointmentsPane;
	@FXML
	Pane ordersPane;
	@FXML
	Pane fileUploadsPane;
	@FXML
	Pane diagnosticReportsPane;
	
	// System User Pane
	@FXML
	TableView<ModelTable> usersTable;
	@FXML
	TableColumn<ModelTable,Integer> userIDCol;
	@FXML
	TableColumn<ModelTable,String> usernameCol;
	@FXML
	TableColumn<ModelTable,String> displayNameCol;
	@FXML
	TableColumn<ModelTable,String> emailCol;
	@FXML
	TableColumn<ModelTable,String> systemRoleCol;
	@FXML
	TableColumn<ModelTable,String> modifyCol;
	
	// newUserPane pane & controls
	@FXML
	Pane newUserPane;
	@FXML
	Label createUserLabel;
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
	ChoiceBox<String> roleChoiceBox;
	@FXML
	TextField passwordTextField;
	@FXML
	TextField confirmPasswordTextField;
	Alert errorAlert = new Alert(AlertType.ERROR);
	Alert updateAlert = new Alert(AlertType.CONFIRMATION);
	ObservableList<ModelTable> oblist = FXCollections.observableArrayList();
	
	// modUserPane
	@FXML
	Pane modUserPane;
	@FXML
	Label modUserLabel;
	@FXML
	TextField modIDTextField;
	@FXML
	TextField modUsernameTextField;
	@FXML
	TextField modDisplayNameTextField;
	@FXML
	TextField modEmailAddressTextField;
	@FXML
	CheckBox modEnabledCheckBox;
	@FXML
	ChoiceBox<String> modRoleChoiceBox;
	@FXML
	TextField modPasswordTextField;
	@FXML
	TextField modConfirmPasswordTextField;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		try {
			Connection con = DatabaseConnection.getConnection();			
			ResultSet rs = con.createStatement().executeQuery("select * from users");
						
			while (rs.next()) {
				oblist.add(new ModelTable(rs.getInt("user_id"),rs.getString("username"),rs.getString("full_name"),rs.getString("email"), null));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		userIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
		usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
		displayNameCol.setCellValueFactory(new PropertyValueFactory<>("displayName"));
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		systemRoleCol.setCellValueFactory(new PropertyValueFactory<>("role"));
		
		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory=(param) ->{
			
			final TableCell<ModelTable,String> cell = new TableCell<ModelTable,String>(){
				
				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					
					if(empty) {
						setText(null);
					}
					else {
						final Button modButton = new Button("Modify");
						modButton.setOnAction(event ->{
							ModelTable m = getTableView().getItems().get(getIndex());
							
							modUserPane.setVisible(true);
							modalitiesPane.setDisable(true);
							patientAlertsPane.setDisable(true);
							patientsPane.setDisable(true);
							appointmentsPane.setDisable(true);
							ordersPane.setDisable(true);
							fileUploadsPane.setDisable(true);
							diagnosticReportsPane.setDisable(true);
							HomeButton.setDisable(true);
							AppointmentButton.setDisable(true);
							InvoiceButton.setDisable(true);
							OrdersButton.setDisable(true);
							ReferralsButton.setDisable(true);
							LogOut.setDisable(true);
							
							modIDTextField.setText("" + m.getID());
							modUsernameTextField.setText(m.getUsername());
							modDisplayNameTextField.setText(m.getDisplayName());
							modEmailAddressTextField.setText(m.getEmail());
							
						});
						
						setGraphic(modButton);
						setText(null);
					}
				}
			};
			
			return cell;
		};
		modifyCol.setCellFactory(cellFactory);
		
		usersTable.setItems(oblist);
	}

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
		
		createUserLabel.setText("Create New User");
		newUserPane.setVisible(true);
		modalitiesPane.setDisable(true);
		patientAlertsPane.setDisable(true);
		patientsPane.setDisable(true);
		appointmentsPane.setDisable(true);
		ordersPane.setDisable(true);
		fileUploadsPane.setDisable(true);
		diagnosticReportsPane.setDisable(true);
		HomeButton.setDisable(true);
		AppointmentButton.setDisable(true);
		InvoiceButton.setDisable(true);
		OrdersButton.setDisable(true);
		ReferralsButton.setDisable(true);
		LogOut.setDisable(true);
		
	}
	
	@SuppressWarnings("deprecation")
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
			if(!Character.isLetter(displayNameTextField.getText().charAt(i))&& !Character.isSpace(displayNameTextField.getText().charAt(i))) {
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
			Connection con = DatabaseConnection.getConnection();
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
			System.out.println("Error: Failed to add new user.");
		}
		newUserPane.setVisible(false);
		modalitiesPane.setDisable(false);
		patientAlertsPane.setDisable(false);
		patientsPane.setDisable(false);
		appointmentsPane.setDisable(false);
		ordersPane.setDisable(false);
		fileUploadsPane.setDisable(false);
		diagnosticReportsPane.setDisable(false);
		HomeButton.setDisable(false);
		AppointmentButton.setDisable(false);
		InvoiceButton.setDisable(false);
		OrdersButton.setDisable(false);
		ReferralsButton.setDisable(false);
		LogOut.setDisable(false);
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
	
	@SuppressWarnings("deprecation")
	public void modUserButton(ActionEvent event) throws IOException{
		int ID;
		String username = null;
		String displayName = null;
		String email = null;
		boolean enabled = modEnabledCheckBox.isSelected();
		int enableCheck = 0;
		String password = null;
		
		// Set username
		if(modUsernameTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Username cannot be blank.");
			errorAlert.showAndWait();
		}
		else if(!Character.isLetter(modUsernameTextField.getText().charAt(0))) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Username must start with a letter.");
			errorAlert.showAndWait();
		}
		else {
			username = modUsernameTextField.getText();
		}
		
		// Set display name
		for(int i = 0; i < modDisplayNameTextField.getText().length(); i++) {
			if(!Character.isLetter(modDisplayNameTextField.getText().charAt(i))&& !Character.isSpace(modDisplayNameTextField.getText().charAt(i))) {
				errorAlert.setHeaderText("Invalid input");
				errorAlert.setContentText("Display Name must contain only letters.");
				errorAlert.showAndWait();				
				i = modDisplayNameTextField.getText().length();
			}
		}
		if(modDisplayNameTextField.getText().isBlank()){
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Display Name cannot be blank");
			errorAlert.showAndWait();
		}
		else {
			displayName = modDisplayNameTextField.getText();
		}
		// Set email
		if(modEmailAddressTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Email cannot be blank.");
			errorAlert.showAndWait();
		}
		else {
			email = modEmailAddressTextField.getText();
		}
		// Set password
		if(modPasswordTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Password cannot be blank.");
			errorAlert.showAndWait();
		}
		else {
			if(modPasswordTextField.getText().equals(modConfirmPasswordTextField.getText())) {
				password = modPasswordTextField.getText();
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
		
		ID = Integer.parseInt(modIDTextField.getText());
		
		try {
			Connection con = DatabaseConnection.getConnection();
			
			String modUser = "update users "
					+ " set username=\'" + username + "\'"
					+ " where user_id=" + ID + "";
			
			Statement stmt = con.createStatement();
			stmt.executeUpdate(modUser);
			
			con.close();
		}
		catch(Exception e) {
			System.out.println("Error: Failed to add new user.");
		}
		modUserPane.setVisible(false);
		modalitiesPane.setDisable(false);
		patientAlertsPane.setDisable(false);
		patientsPane.setDisable(false);
		appointmentsPane.setDisable(false);
		ordersPane.setDisable(false);
		fileUploadsPane.setDisable(false);
		diagnosticReportsPane.setDisable(false);
		HomeButton.setDisable(false);
		AppointmentButton.setDisable(false);
		InvoiceButton.setDisable(false);
		OrdersButton.setDisable(false);
		ReferralsButton.setDisable(false);
		LogOut.setDisable(false);
		updateAlert.setHeaderText("Success!");
		updateAlert.setContentText("User has been successfully modified.");
		updateAlert.showAndWait();
		
	}

}


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
import javafx.scene.control.DatePicker;

public class AdminAdminController implements Initializable {

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
	TableColumn<ModelTable, Integer> userIDCol;
	@FXML
	TableColumn<ModelTable, String> usernameCol;
	@FXML
	TableColumn<ModelTable, String> displayNameCol;
	@FXML
	TableColumn<ModelTable, String> emailCol;
	@FXML
	TableColumn<ModelTable, String> systemRoleCol;
	@FXML
	TableColumn<ModelTable, String> modifyCol;
	ObservableList<ModelTable> systemUsers = FXCollections.observableArrayList();

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
	ObservableList<String> roles = FXCollections.observableArrayList();

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

	
	//Patients Pane	
	@FXML
	TableView<PatientTableModel> patientsTable;
	@FXML
	TableColumn<PatientTableModel, Integer> patientIDCol; //these are by fx:id found in fxml
	@FXML
	TableColumn<PatientTableModel, String> patientDOBCol;
	@FXML
	TableColumn<PatientTableModel, String> patientLastNameCol;
	@FXML
	TableColumn<PatientTableModel, String> patientFirstNameCol;
	@FXML
	TableColumn<PatientTableModel, String> modPatientCol;
	@FXML
	ObservableList<PatientTableModel> patients = FXCollections.observableArrayList();
	
	//newPatientPane
	@FXML Pane createPatientPane;
	@FXML TextField PatientFirstName;
	@FXML TextField PatientLastName;
	@FXML TextField PatientRace;
	@FXML Button addNewPatientButton;
	@FXML Button cancelNewPatientButton;
	@FXML DatePicker DOB;
	@FXML TextField PatientEthnicity;
	@FXML ChoiceBox sexChoiceBox;
	ObservableList<String> sexChoices = FXCollections.observableArrayList();
	
	//modPatientPane
	@FXML Pane modPatientPane;
	@FXML TextField modPatientFirstName;
	@FXML TextField modPatientLastName;
	@FXML TextField modPatientRace;
	@FXML TextField modPatientEthnicity;
	@FXML Button cancelModPatientButton;
	@FXML Button submitModPatientButton;
	@FXML DatePicker modDOB;
	@FXML ChoiceBox modsexChoiceBox;
	@FXML TextField modPatientIDTextField;
	
	 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		populateSystemUsers();
		populatePatients();
		paneInitialize();

	}

	public void populateSystemUsers() {
		systemUsers.clear();
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from users");

			while (rs.next()) {
				systemUsers.add(new ModelTable(rs.getInt("user_id"), rs.getString("username"),
						rs.getString("full_name"), rs.getString("email"), null));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		userIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
		usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
		displayNameCol.setCellValueFactory(new PropertyValueFactory<>("displayName"));
		emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
		systemRoleCol.setCellValueFactory(new PropertyValueFactory<>("role"));

		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
					} else {
						final Button modButton = new Button("Modify");
						modButton.setOnAction(event -> {
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

		usersTable.setItems(systemUsers);
	}
	
	public void populatePatients() {
		patients.clear();
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from patients");

			while (rs.next()) {
				patients.add(new PatientTableModel(rs.getInt("patient_id"), rs.getString("dob"),
						rs.getString("last_name"), rs.getString("first_name")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		patientIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
		patientDOBCol.setCellValueFactory(new PropertyValueFactory<>("DOB"));
		patientLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		patientFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));	
		modPatientCol.setCellValueFactory(new PropertyValueFactory<>(""));

		Callback<TableColumn<PatientTableModel, String>, TableCell<PatientTableModel, String>> cellFactory = (param) -> {

			final TableCell<PatientTableModel, String> cell = new TableCell<PatientTableModel, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
					} else {
						final Button modPatientButton = new Button("Modify");
						modPatientButton.setOnAction(event -> {
							PatientTableModel m = getTableView().getItems().get(getIndex());
							

							modPatientPane.setVisible(true);
							systemUsersPane.setDisable(true);
							modalitiesPane.setDisable(true);
							patientAlertsPane.setDisable(true);
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

							modPatientFirstName.setText("" + m.getFirstName());
							modPatientLastName.setText("" + m.getLastName());
							modPatientRace.setText("" + m.getRace());
							modPatientEthnicity.setText("" + m.getEthnicity());
							modPatientIDTextField.setText(""+ m.getID());
							modPatientRace.setText("");
							modPatientEthnicity.setText("");
						});

						setGraphic(modPatientButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		modPatientCol.setCellFactory(cellFactory);
		patientsTable.setItems(patients);
	}

	public void paneInitialize() {
		roles.removeAll(roles);
		String a = "Admin";
		String b = "Referral_Doctor";
		String c = "Technician";
		String d = "Radiologist";
		String e = "Receptionist";
		String f = "User";
		String g = "Billing";
		roles.addAll(a, b, c, d, e, f, g);
		roleChoiceBox.setItems(roles);
		modRoleChoiceBox.setItems(roles);
		roleChoiceBox.setValue(a);
		modRoleChoiceBox.setValue(a);
		
		
		//adding newPatientSex choicebox options
		sexChoices.removeAll(sexChoices);
		sexChoices.addAll("Male", "Female", "Other");
		modsexChoiceBox.setItems(sexChoices);
		sexChoiceBox.setItems(sexChoices);
	}

	public void userLogOut(ActionEvent event) throws IOException {

		Main m = new Main();

		m.changeScene("../Views/Login.fxml");
	}

	public void HomeButton(ActionEvent event) throws IOException {

		Main m = new Main();
		m.changeScene("../Views/Admin.fxml");
	}

	public void AppointmentButton(ActionEvent event) throws IOException {

		Main m = new Main();
		m.changeScene("../Views/AdminAppointments.fxml");
	}

	public void InvoiceButton(ActionEvent event) throws IOException {

		Main m = new Main();
		m.changeScene("../Views/AdminInvoice.fxml");
	}

	public void OrderButton(ActionEvent event) throws IOException {

		Main m = new Main();
		m.changeScene("../Views/AdminOrders.fxml");
	}

	public void ReferralsButton(ActionEvent event) throws IOException {

		Main m = new Main();
		m.changeScene("../Views/AdminReferrals.fxml");
	}

	public void NewUserButton(ActionEvent event) throws IOException {

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
	public void createUserButton(ActionEvent event) throws IOException {
		int ID;
		String username = null;
		String displayName = null;
		String email = null;
		String password = null;
		String role = null;
		boolean enabled = enabledCheckBox.isSelected();
		int enableCheck = 0;
		int c = 0;

		// Set username
		if (usernameTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Username cannot be blank.");
			errorAlert.showAndWait();
		} else if (!Character.isLetter(usernameTextField.getText().charAt(0))) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Username must start with a letter.");
			errorAlert.showAndWait();
		} else {
			username = usernameTextField.getText();
		}

		// Set display name
		for (int i = 0; i < displayNameTextField.getText().length(); i++) {
			if (!Character.isLetter(displayNameTextField.getText().charAt(i))
					&& !Character.isSpace(displayNameTextField.getText().charAt(i))) {
				errorAlert.setHeaderText("Invalid input");
				errorAlert.setContentText("Display Name must contain only letters.");
				errorAlert.showAndWait();
				i = displayNameTextField.getText().length();
			}
		}
		if (displayNameTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Display Name cannot be blank");
			errorAlert.showAndWait();
		} else {
			displayName = displayNameTextField.getText();
		}
		// Set email
		if (emailAddressTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Email cannot be blank.");
			errorAlert.showAndWait();
		} else {
			email = emailAddressTextField.getText();
		}
		// Set password
		if (passwordTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Password cannot be blank.");
			errorAlert.showAndWait();
		} else {
			if (passwordTextField.getText().equals(confirmPasswordTextField.getText())) {
				password = passwordTextField.getText();
			} else {
				errorAlert.setHeaderText("Invalid input");
				errorAlert.setContentText("Passwords do not match.");
				errorAlert.showAndWait();
			}
		}
		// Set enabled
		if (enabled == true) {
			enableCheck = 1;
		} else {
			enableCheck = 0;
		}
		role = roleChoiceBox.getValue();
		role = role.toUpperCase();

		try {
			Connection con = DatabaseConnection.getConnection();
			Statement stmt = con.createStatement();
			String IDCheck = "select * from users";
			ResultSet rs = stmt.executeQuery(IDCheck);

			int idNum = 0;
			int idNum2 = 0;

			while (rs.next()) {
				c = rs.getInt("user_id");
			}
			ID = c + 1;

			String getRoleID = "select * from roles where name=\'" + role + "\'";
			rs = stmt.executeQuery(getRoleID);

			while (rs.next()) {
				idNum = rs.getInt("role_id");
			}

			String getUserAndRoleID = "select * from users_roles";
			rs = stmt.executeQuery(getUserAndRoleID);

			while (rs.next()) {
				c = rs.getInt("id");
			}
			idNum2 = c + 1;

			String newUser = "INSERT INTO users (user_id, email, full_name, username, password, enabled) " + "VALUES ("
					+ ID + ", \'" + email + "\', \'" + displayName + "\', \'" + username + "\', \'" + password + "\', "
					+ enableCheck + ")";

			String addUserRole = "INSERT INTO users_roles (user_id, role_id, id) " + "VALUES (" + ID + ", " + idNum
					+ ", " + idNum2 + ")";

			stmt.executeUpdate(newUser);
			stmt.executeUpdate(addUserRole);

			con.close();
			updateAlert.setHeaderText("Success!");
			updateAlert.setContentText("User has been successfully added.");
			updateAlert.showAndWait();
		} catch (Exception e) {
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
		populateSystemUsers();

	}

	public void cancelUserButton(ActionEvent event) throws IOException {
		newUserPane.setVisible(false);
		modUserPane.setVisible(false);
		IDTextField.clear();
		usernameTextField.clear();
		displayNameTextField.clear();
		emailAddressTextField.clear();
		enabledCheckBox.setSelected(false);
		passwordTextField.clear();
		confirmPasswordTextField.clear();
	}

	@SuppressWarnings("deprecation")
	public void modUserButton(ActionEvent event) throws IOException {
		int ID;
		String username = null;
		String displayName = null;
		String email = null;
		String password = null;
		String role = null;
		boolean enabled = modEnabledCheckBox.isSelected();
		int enableCheck = 0;
		int idNum = 0;
		int c = 0;

		// Set username
		if (modUsernameTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Username cannot be blank.");
			errorAlert.showAndWait();
		} else if (!Character.isLetter(modUsernameTextField.getText().charAt(0))) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Username must start with a letter.");
			errorAlert.showAndWait();
		} else {
			username = modUsernameTextField.getText();
		}

		// Set display name
		for (int i = 0; i < modDisplayNameTextField.getText().length(); i++) {
			if (!Character.isLetter(modDisplayNameTextField.getText().charAt(i))
					&& !Character.isSpace(modDisplayNameTextField.getText().charAt(i))) {
				errorAlert.setHeaderText("Invalid input");
				errorAlert.setContentText("Display Name must contain only letters.");
				errorAlert.showAndWait();
				i = modDisplayNameTextField.getText().length();
			}
		}
		if (modDisplayNameTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Display Name cannot be blank");
			errorAlert.showAndWait();
		} else {
			displayName = modDisplayNameTextField.getText();
		}
		// Set email
		if (modEmailAddressTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Email cannot be blank.");
			errorAlert.showAndWait();
		} else {
			email = modEmailAddressTextField.getText();
		}
		// Set password
		if (modPasswordTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Password cannot be blank.");
			errorAlert.showAndWait();
		} else {
			if (modPasswordTextField.getText().equals(modConfirmPasswordTextField.getText())) {
				password = modPasswordTextField.getText();
			} else {
				errorAlert.setHeaderText("Invalid input");
				errorAlert.setContentText("Passwords do not match.");
				errorAlert.showAndWait();
			}
		}
		// Set enabled
		if (enabled == true) {
			enableCheck = 1;
		} else {
			enableCheck = 0;
		}
		
		role = modRoleChoiceBox.getValue();
		role = role.toUpperCase();
		ID = Integer.parseInt(modIDTextField.getText());

		try {
			Connection con = DatabaseConnection.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs;
			
			String getUserAndRoleID = "select * from roles where name=\'" + role + "\'";
			rs = stmt.executeQuery(getUserAndRoleID);

			while (rs.next()) {
				idNum = rs.getInt("role_id");
			}

			String modUser = "update users set username=\'" + username + "\'" + " where user_id=" + ID + "";
			String modUserRole = "update users_roles set role_id=" + idNum + " where user_id=" + ID + "";

			stmt.executeUpdate(modUser);
			stmt.executeUpdate(modUserRole);

			con.close();
			updateAlert.setHeaderText("Success!");
			updateAlert.setContentText("User has been successfully modified.");
			updateAlert.showAndWait();
		} 
		catch (Exception e) {
			System.out.println("Error: Failed to add new user.");
		}
		modUserPane.setVisible(false);
		systemUsersPane.setDisable(false);
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
		populateSystemUsers();

	}
	
	
	
//Patients pane controls
	
	//new Patient controls
	public void NewPatientButton(ActionEvent event) throws IOException {
	
			createPatientPane.setVisible(true);
			systemUsersPane.setDisable(true);
			modalitiesPane.setDisable(true);
			patientAlertsPane.setDisable(true);
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
	
	//cancel create new patient
		//also cancel modify patient
	public void cancelNewPatientButton(ActionEvent event) throws IOException {
		createPatientPane.setVisible(false);
		modPatientPane.setVisible(false);
		PatientFirstName.clear();
		PatientLastName.clear();
		PatientRace.clear();
		PatientEthnicity.clear();
		DOB.setValue(null);
		
		//re-enable all other panes
		systemUsersPane.setDisable(false);
		modalitiesPane.setDisable(false);
		patientAlertsPane.setDisable(false);
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
		
	}
	
	//submit New Patient
	@SuppressWarnings("deprecation")
	public void addNewPatientButton(ActionEvent event) throws IOException {
		int ID;
		String firstName = "";
		String lastName = "";
		String patientDOB = "";
		String race = "";
		String ethnicity = "";
		String sex = "";
		

		// Set firstName
		if(PatientFirstName.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("First name cannot be blank.");
			errorAlert.showAndWait();
		}else if (!Character.isLetter(PatientFirstName.getText().charAt(0))) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText(" must start with a letter.");
			errorAlert.showAndWait();
		}else {
			firstName = PatientFirstName.getText();
		}
		
		//set lastName
		if (PatientLastName.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Last name cannot be blank.");
			errorAlert.showAndWait();
		}else {
			lastName = PatientLastName.getText();
		}
		
		//set DOB
		if (DOB.getValue().toString().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("DOB cannot be blank.");
			errorAlert.showAndWait();
		}else {
			patientDOB = DOB.getValue().toString();
		}
				
		//set race
		if (PatientRace.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Race cannot be blank.");
			errorAlert.showAndWait();
		}else {
			race = PatientRace.getText();
		}
		//set ethnicity
		if (PatientEthnicity.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Ethnicity cannot be blank.");
			errorAlert.showAndWait();
		}else {
			ethnicity = PatientEthnicity.getText();
		}	
		//set sex
		sex = sexChoiceBox.getValue().toString();
		
		try {
			Connection con = DatabaseConnection.getConnection();
			Statement stmt = con.createStatement();
			String IDCheck = "select * from patients";
			ResultSet rs = stmt.executeQuery(IDCheck);

			//assign patient next ID #
			int c = 0;
			while (rs.next()) {
				c = rs.getInt("patient_id");
			}
			ID = c + 1;

			String newPatient = "INSERT INTO patients (patient_id, first_name, last_name, dob, sex, race, ethnicity) " + "VALUES ("
					+ ID + ", \'" + firstName + "\', \'" + lastName + "\', \'" + patientDOB + "\', \'" + sex + "\', \'"
					+ race + "\', \'"+ethnicity+ "\')";
			

			stmt.executeUpdate(newPatient);

			con.close();
			updateAlert.setHeaderText("Success!");
			updateAlert.setContentText("Patient has been successfully added.");
			updateAlert.showAndWait();
		} catch (Exception e) {
			System.out.println("Error: Failed to add new user.");
		}
		
		createPatientPane.setVisible(false);
		systemUsersPane.setDisable(false);
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
		populatePatients();
	}
	
	//modify Patient
	@SuppressWarnings("deprecation")
	public void modPatientButton(ActionEvent event) throws IOException {
		String firstName = null;
		String lastName = null;
		String patientDOB = null;
		String race = null;
		String ethnicity = null;
		String sex = null;
		
		
		int patientID = Integer.parseInt(modPatientIDTextField.getText());
		
		
		// Set firstName
		if (modPatientFirstName.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("First name cannot be blank.");
			errorAlert.showAndWait();
		}else if (!Character.isLetter(PatientFirstName.getText().charAt(0))) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText(" must start with a letter.");
			errorAlert.showAndWait();
		}else {
			firstName = modPatientFirstName.getText();
		}
		
		//set lastName
		if (modPatientLastName.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Last name cannot be blank.");
			errorAlert.showAndWait();
		}else {
			lastName = modPatientLastName.getText();
		}
		
		//set DOB
		if (modDOB.getValue().toString().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("DOB cannot be blank.");
			errorAlert.showAndWait();
		}else {
			patientDOB = modDOB.getValue().toString();
		}
		
				
		//set race
		if (modPatientRace.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Race cannot be blank.");
			errorAlert.showAndWait();
		}else {
			race = modPatientRace.getText();
		}
		//set ethnicity
		if (modPatientEthnicity.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Ethnicity cannot be blank.");
			errorAlert.showAndWait();
		}else {
			ethnicity = modPatientEthnicity.getText();
		}	
		//set sex
		sex = modsexChoiceBox.getValue().toString();
		
		try {
			Connection con = DatabaseConnection.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs;
			 
			String modPatient = "update patients set first_name= \'" + firstName + "\', last_name= \'"+lastName+"\', dob=\'"+patientDOB+"\', sex=\'"+sex+"\', race=\'"+race+"\', ethnicity=\'"+ethnicity+ "' WHERE patient_id= \'" + patientID + "\';";
			stmt.executeUpdate(modPatient);
						
			con.close();
			updateAlert.setHeaderText("Success!");
			updateAlert.setContentText("Patient has been successfully modified.");
			updateAlert.showAndWait();
		} 
		catch (Exception e) {
			System.out.println("Error: Failed to update patient.");
		}
		modPatientPane.setVisible(false);
		systemUsersPane.setDisable(false);
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
		populatePatients();

	}
}

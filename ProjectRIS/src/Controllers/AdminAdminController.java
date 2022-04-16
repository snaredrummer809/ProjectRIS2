package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
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
import javafx.scene.control.TextArea;

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
	TextField searchUsersTextField;
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
	ObservableList<ModelTable> searchUsers = FXCollections.observableArrayList();
	
	// Modality Pane
	@FXML
	TextField searchModalitiesTextField;
	@FXML
	TableView<ModelTable> modalitiesTable;
	@FXML
	TableColumn<ModelTable, Integer> modalitiesTableIDColumn;
	@FXML
	TableColumn<ModelTable, String> modalitiesTableNameColumn;
	@FXML
	TableColumn<ModelTable, String> modalitiesTableCostColumn;
	@FXML
	TableColumn<ModelTable, String> modalitiesTableModifyColumn;
	ObservableList<ModelTable> modalities = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchModalities = FXCollections.observableArrayList();
	
	// Appointment Pane
	@FXML
	TextField searchAppsTextField;
	@FXML
	TableView<ModelTable> appointmentsTable;
	@FXML
	TableColumn<ModelTable, String> appPatientNameColumn;
	@FXML
	TableColumn<ModelTable, String> appModalityColumn;
	@FXML
	TableColumn<ModelTable, String> appDateAndTimeColumn;
	@FXML
	TableColumn<ModelTable, String> appTechNameColumn;
	@FXML
	TableColumn<ModelTable, String> appRadiologistColumn;
	@FXML
	TableColumn<ModelTable, String> appDeleteColumn;
	ObservableList<ModelTable> appointments = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchApps = FXCollections.observableArrayList();

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
	
	// createModalityPane
	@FXML
	Pane createModalityPane;
	@FXML
	Button createModalityButton;
	@FXML
	TextField modalityIDTextField;
	@FXML
	TextField modalityNameTextField;
	@FXML
	TextField modalityCostTextField;
	
	// modModalityPane
	@FXML
	Pane modModalityPane;
	@FXML
	Button modModalityButton;
	@FXML
	TextField modModalityIDTextField;
	@FXML
	TextField modModalityNameTextField;
	@FXML
	TextField modModalityCostTextField;

	//Patients Pane	
	@FXML
	TextField searchPatientsTextField;
	@FXML
	TableView<ModelTable> patientsTable;
	@FXML
	TableColumn<ModelTable, Integer> patientIDCol; //these are by fx:id found in fxml
	@FXML
	TableColumn<ModelTable, String> patientDOBCol;
	@FXML
	TableColumn<ModelTable, String> patientLastNameCol;
	@FXML
	TableColumn<ModelTable, String> patientFirstNameCol;
	@FXML
	TableColumn<ModelTable, String> modPatientCol;
	@FXML
	ObservableList<ModelTable> patients = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchPatients = FXCollections.observableArrayList();
	
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
	@FXML TextField newPatientStreetTextField;
	@FXML TextField newPatientCityTextField;
	@FXML TextField newPatientStateTextField;
	@FXML TextField newPatientZipTextField;
	@FXML TextField newPatientPhoneTextField;
	@FXML TextField newPatientEmailTextField;
	@FXML TextArea newPatientNotesTextArea;	
	
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
	@FXML TextField modPatientStreetTextField;
	@FXML TextField modPatientCityTextField;
	@FXML TextField modPatientStateTextField;
	@FXML TextField modPatientZipTextField;
	@FXML TextField modPatientPhoneTextField;
	@FXML TextField modPatientEmailTextField;
	@FXML TextArea modPatientNotesTextArea;
	// appDeleteConfirmationPane
	@FXML
	Pane appDeleteConfirmationPane;
	@FXML
	TextField appIDTextField;
	@FXML
	Button appConfirmDeleteButton;


	 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		populateSystemUsers();
		populateModalities();
		populateAppointments();
		populatePatients();
		paneInitialize();

	}

	public void populateSystemUsers() {
		systemUsers.clear();
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from users");

			while (rs.next()) {
				systemUsers.add(new ModelTable(rs.getInt("user_id"), 0, 0, rs.getString("username"),
						rs.getString("full_name"), rs.getString("email"), null, null, null));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		userIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
		usernameCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		displayNameCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		emailCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		systemRoleCol.setCellValueFactory(new PropertyValueFactory<>("s4"));

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

							modIDTextField.setText("" + m.getNum1());
							modUsernameTextField.setText(m.getS1());
							modDisplayNameTextField.setText(m.getS2());
							modEmailAddressTextField.setText(m.getS3());

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
	
	public void populateModalities() {
		modalities.clear();
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from modalities");

			while (rs.next()) {
				modalities.add(new ModelTable(rs.getInt("modality_id"), 0, 0, rs.getString("name"),
						rs.getString("cost"), null, null, null, null));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		modalitiesTableIDColumn.setCellValueFactory(new PropertyValueFactory<>("num1"));
		modalitiesTableNameColumn.setCellValueFactory(new PropertyValueFactory<>("s1"));
		modalitiesTableCostColumn.setCellValueFactory(new PropertyValueFactory<>("s2"));
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

							modModalityPane.setVisible(true);
							systemUsersPane.setDisable(true);
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

							modModalityIDTextField.setText("" + m.getNum1());
							modModalityNameTextField.setText(m.getS1());
							modModalityCostTextField.setText(m.getS2());

						});

						setGraphic(modButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		modalitiesTableModifyColumn.setCellFactory(cellFactory);

		modalitiesTable.setItems(modalities);
	}
	
	public void populateAppointments() {
		appointments.clear();
		int patient = 0;
		int modality = 0;
		int tech = 0;
		int radio = 0;
		String patientName = null;
		String modalityName = null;
		String techName = null;
		String radioName = null;
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from appointments");

			while (rs.next()) {
				patient = rs.getInt("patient");
				//System.out.println(patient);
				modality = rs.getInt("modality");
				//System.out.println(modality);
				tech = rs.getInt("technician");
				//System.out.println(tech);
				radio = rs.getInt("radiologist");
				//System.out.println(radio);
				ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
				while(rs2.next()) {
					patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
				}
				rs2 = con.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
				while(rs2.next()) {
					modalityName = rs2.getString("name");
				}
				rs2 = con.createStatement().executeQuery("select * from users where user_id=" + tech);
				while(rs2.next()) {
					techName = rs2.getString("full_name");
				}
				rs2 = con.createStatement().executeQuery("select * from users where user_id=" + radio);
				while(rs2.next()) {
					radioName = rs2.getString("full_name");
				}				
				
				appointments.add(new ModelTable(patient, 0, 0, patientName,
						modalityName, rs.getString("date_time"), techName, 
						radioName, null));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		appPatientNameColumn.setCellValueFactory(new PropertyValueFactory<>("s1"));
		appModalityColumn.setCellValueFactory(new PropertyValueFactory<>("s2"));
		appDateAndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("s3"));
		appTechNameColumn.setCellValueFactory(new PropertyValueFactory<>("s4"));
		appRadiologistColumn.setCellValueFactory(new PropertyValueFactory<>("s5"));
		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
					} else {
						final Button modButton = new Button("Delete");
						modButton.setOnAction(event -> {
							ModelTable m = getTableView().getItems().get(getIndex());

							appDeleteConfirmationPane.setVisible(true);
							systemUsersPane.setDisable(true);
							modalitiesPane.setDisable(true);
							patientAlertsPane.setDisable(true);
							patientsPane.setDisable(true);
							ordersPane.setDisable(true);
							fileUploadsPane.setDisable(true);
							diagnosticReportsPane.setDisable(true);
							HomeButton.setDisable(true);
							AppointmentButton.setDisable(true);
							InvoiceButton.setDisable(true);
							OrdersButton.setDisable(true);
							ReferralsButton.setDisable(true);
							LogOut.setDisable(true);
							
							//System.out.println(m.getNum1());
							appIDTextField.setText(m.getNum1()+"");

						});

						setGraphic(modButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		appDeleteColumn.setCellFactory(cellFactory);

		appointmentsTable.setItems(appointments);
	}
	
	public void populatePatients() {
		patients.clear();
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from patients");
			int patient_id =0;
			String dob =null;
			String lastName=null;
			String firstName=null;
			while (rs.next()) {
				patient_id = rs.getInt("patient_id");
				dob = rs.getString("dob");
				lastName = rs.getString("last_name");
				firstName = rs.getString("first_name");
				patients.add(new ModelTable(patient_id,0,0,firstName, lastName, dob,null,null,null));
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}

		patientIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
		patientDOBCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		patientLastNameCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		patientFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("s1"));	
		modPatientCol.setCellValueFactory(new PropertyValueFactory<>(""));

		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
					} else {
						final Button modPatientButton = new Button("Modify");
						modPatientButton.setOnAction(event -> {
							ModelTable m = getTableView().getItems().get(getIndex());
							

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

							modPatientFirstName.setText("" + m.getS1());
							modPatientLastName.setText("" + m.getS2());
							LocalDate dob = LocalDate.parse(m.getS3());
							modDOB.setValue(dob);
							modPatientIDTextField.setText(""+ m.getNum1());
							
							
							//stuff i need to get from patients table
							
							String race=null;
							String ethnic=null;
							String street =null;
							String city= null;
							String state=null;
							String zip=null;
							String phone=null;
							String email=null;
							String notes = null;
							
							int patient_id = m.getNum1();
							try {
								Connection con = DatabaseConnection.getConnection();
								ResultSet rs = con.createStatement().executeQuery("select * from patients where patient_id="+patient_id+";");

								while (rs.next()) {
									race= rs.getString("race");
									ethnic = rs.getString("ethnicity");
									street = rs.getString("street_address");
									city = rs.getString("city");
									state = rs.getString("state_abbreviation");
									zip = rs.getString("zip");
									phone = rs.getString("phone_number");
									email = rs.getString("email_address");
									notes = rs.getString("patientNotes");
									
								}
							} catch (SQLException e) {
								e.printStackTrace();
							}
							modPatientStreetTextField.setText(street);
							modPatientCityTextField.setText(city);
							modPatientStateTextField.setText(state);
							modPatientZipTextField.setText(zip);
							modPatientPhoneTextField.setText(phone);
							modPatientEmailTextField.setText(email);
							modPatientNotesTextArea.setText(notes);
							modPatientEthnicity.setText(ethnic);
							modPatientRace.setText(race);
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

	public void cancelButton(ActionEvent event) throws IOException {
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
	
	public void newModalityButton (ActionEvent event) throws IOException {
		createModalityPane.setVisible(true);
		systemUsersPane.setDisable(true);
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
	
	public void createModalityButton(ActionEvent event) throws IOException {
		int id = 0;
		int c = 0;
		String name = "";
		String cost = "";
		
		if(modalityNameTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Name cannot be blank.");
			errorAlert.showAndWait();
		}
		else {
			name = modalityNameTextField.getText();
			modalityNameTextField.clear();
		}
		try {
			Double.parseDouble(modalityCostTextField.getText());
			cost = modalityCostTextField.getText();
			modalityCostTextField.clear();
		}
		catch(Exception e) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Input must be in form X.XX");
			errorAlert.showAndWait();
		}
		
		try {
			Connection con = DatabaseConnection.getConnection();
			Statement stmt = con.createStatement();
			ResultSet rs;
			
			String getModalityID = "select * from modalities";
			rs = stmt.executeQuery(getModalityID);

			while (rs.next()) {
				c = rs.getInt("modality_id");
			}
			id = c + 1;
			
			String updateModality = "insert into modalities (modality_id, name, cost) " 
					+ "values (" + id + ", \'" + name + "\', \'" + cost + "\')";
			stmt.executeUpdate(updateModality);
			
			con.close();
			updateAlert.setHeaderText("Success!");
			updateAlert.setContentText("Modality has been successfully created.");
			updateAlert.showAndWait();
		}
		catch(SQLException e) {
			errorAlert.setHeaderText("Error");
			errorAlert.setContentText("Modality could not be created.");
			errorAlert.showAndWait();
		}
		
		createModalityPane.setVisible(false);
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
		populateModalities();
		
	}
	
	public void modModalityButton (ActionEvent event) throws IOException {
		int id = 0;
		String name = null;
		String cost = null;

		// Set name
		if (modModalityNameTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Username cannot be blank.");
			errorAlert.showAndWait();
		} 
		else {
			name = modModalityNameTextField.getText();
		}

		// Set cost
		if (modModalityCostTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Display Name cannot be blank");
			errorAlert.showAndWait();
		} else {
			cost = modModalityCostTextField.getText();
		}
		
		id = Integer.parseInt(modModalityIDTextField.getText());

		try {
			Connection con = DatabaseConnection.getConnection();
			Statement stmt = con.createStatement();

			String modModality = "update modalities set name=\'" + name + "\', cost=\'" + cost + "\' where modality_id=" + id + "";

			stmt.executeUpdate(modModality);

			con.close();
			updateAlert.setHeaderText("Success!");
			updateAlert.setContentText("Modality has been successfully modified.");
			updateAlert.showAndWait();
		} 
		catch (Exception e) {
			errorAlert.setHeaderText("Error");
			errorAlert.setContentText("Failed to modify modality.");
			errorAlert.showAndWait();
		}
		modModalityPane.setVisible(false);
		systemUsersPane.setDisable(false);
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
		populateModalities();
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
		String phone = "";
		String email = "";
		String notes = "";
		String street = "";
		String city = "";
		String state = "";
		String zip = "";
		

		// Set firstName
		if(PatientFirstName.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("First name cannot be blank.");
			errorAlert.showAndWait();
		}else if (!Character.isLetter(PatientFirstName.getText().charAt(0))) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText(" must start with a letter. Please modify patient.");
			errorAlert.showAndWait();
		}else {
			firstName = PatientFirstName.getText();
		}
		
		//set lastName
		if (PatientLastName.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Last name cannot be blank. Please modify patient.");
			errorAlert.showAndWait();
		}else {
			lastName = PatientLastName.getText();
		}
		
		//set DOB
		if (DOB.getValue().toString().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("DOB cannot be blank. Please modify patient.");
			errorAlert.showAndWait();
		}else {
			patientDOB = DOB.getValue().toString();
		}
				
		//set race
		if (PatientRace.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Race cannot be blank. Please modify patient.");
			errorAlert.showAndWait();
		}else {
			race = PatientRace.getText();
		}
		//set ethnicity
		if (PatientEthnicity.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Ethnicity cannot be blank. Please modify patient.");
			errorAlert.showAndWait();
		}else {
			ethnicity = PatientEthnicity.getText();
		}	
		//set sex
		sex = sexChoiceBox.getValue().toString();
		
		//set street
				if (newPatientStreetTextField.getText().isBlank()) {
					errorAlert.setHeaderText("Invalid input");
					errorAlert.setContentText("Street address cannot be blank. Please modify patient.");
					errorAlert.showAndWait();
				}else {
					street = newPatientStreetTextField.getText();
				}
		
		//set city
				if (newPatientCityTextField.getText().isBlank()) {
					errorAlert.setHeaderText("Invalid input");
					errorAlert.setContentText("City cannot be blank. Please modify patient");
					errorAlert.showAndWait();
				}else {
					city = newPatientCityTextField.getText();
				}
				
		//set state
				if (newPatientStateTextField.getText().isBlank()) {
					errorAlert.setHeaderText("Invalid input");
					errorAlert.setContentText("State cannot be blank. Please modify patient.");
					errorAlert.showAndWait();
				}else {
					state = newPatientStateTextField.getText();
				}
				
		//set zip
				if (newPatientZipTextField.getText().isBlank()) {
					errorAlert.setHeaderText("Invalid input");
					errorAlert.setContentText("Zip cannot be blank. Please modify patient.");
					errorAlert.showAndWait();
				}else {
					zip = newPatientZipTextField.getText();
				}
				
		//set phone
				if (newPatientPhoneTextField.getText().isBlank()) {
					errorAlert.setHeaderText("Invalid input");
					errorAlert.setContentText("Phone cannot be blank. Please modify patient.");
					errorAlert.showAndWait();
				}else {
					phone = newPatientPhoneTextField.getText();
				}
				
		//set email
				if (newPatientEmailTextField.getText().isBlank()) {
					errorAlert.setHeaderText("Invalid input");
					errorAlert.setContentText("Email cannot be blank. Please modify patient.");
					errorAlert.showAndWait();
				}else {
					email = newPatientEmailTextField.getText();
				}
		//set notes
				if (newPatientNotesTextArea.getText().isBlank()) {
					errorAlert.setHeaderText("Invalid input");
					errorAlert.setContentText("Notes cannot be blank. Please modify patient.");
					errorAlert.showAndWait();
				}else {
					notes = newPatientNotesTextArea.getText();
				}
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

			String newPatient = "INSERT INTO patients (patient_id, first_name, last_name, dob, sex, race, ethnicity, phone_number, email_address, patientNotes, street_address, city, state_abbreviation, zip) " + "VALUES ("
					+ ID + ", \'" + firstName + "\', \'" + lastName + "\', \'" + patientDOB + "\', \'" + sex + "\', \'"
					+ race + "\', \'"+ethnicity+"\', \'"+phone+"\', \'"+email+"\', \'"+notes+"\', \'"+street+"\', \'"+city+"\', \'"+state+"\', \'"+zip +"\');";
			

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
		String phone = "";
		String email = "";
		String notes = "";
		String street = "";
		String city = "";
		String state = "";
		String zip = "";
		
		int patientID = Integer.parseInt(modPatientIDTextField.getText());
		
		
		// Set firstName
		if (modPatientFirstName.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("First name cannot be blank.");
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
		
		//set phone_number
		if (modPatientPhoneTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Phone cannot be blank.");
			errorAlert.showAndWait();
		}else {
			phone = modPatientPhoneTextField.getText();
		}	
		
		//set email_address
				if (modPatientEmailTextField.getText().isBlank()) {
					errorAlert.setHeaderText("Invalid input");
					errorAlert.setContentText("Email cannot be blank.");
					errorAlert.showAndWait();
				}else {
					email = modPatientEmailTextField.getText();
				}
		//set notes
			notes = modPatientNotesTextArea.getText();
			
		//set street
		if (modPatientStreetTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Street cannot be blank.");
			errorAlert.showAndWait();
		}else {
			street = modPatientStreetTextField.getText();
		}
		//set city
			if (modPatientCityTextField.getText().isBlank()) {
				errorAlert.setHeaderText("Invalid input");
				errorAlert.setContentText("City cannot be blank.");
				errorAlert.showAndWait();
			}else {
				city = modPatientCityTextField.getText();
			}
		//set state
		if (modPatientStateTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("State cannot be blank.");
			errorAlert.showAndWait();
		}else {
			state = modPatientStateTextField.getText();
		}
		
		//set zip
		if (modPatientZipTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Zip cannot be blank.");
			errorAlert.showAndWait();
		}else {
			zip = modPatientZipTextField.getText();
		}
		
		try {
			Connection con = DatabaseConnection.getConnection();
			Statement stmt = con.createStatement();
			 
			String modPatient = "update patients set first_name= \'" + firstName + "\', last_name= \'"+lastName+"\', dob=\'"+patientDOB+"\', sex=\'"+sex+
						"\', race=\'"+race+"\', ethnicity=\'"+ethnicity+"\', phone_number=\'"+phone +"\', email_address=\'"+email+"\', patientNotes=\'"+notes+
						"\', street_address=\'"+street+"\', city=\'"+city+"\', state_abbreviation=\'"+state+"\', zip=\'"+zip+"' WHERE patient_id= \'" + patientID + "\';";
			//System.out.println(modPatient);
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
	
	public void appConfirmDelete(ActionEvent event) throws IOException {
		try {
			Connection con = DatabaseConnection.getConnection();
			Statement stmt = con.createStatement();
			String deleteApp = "delete from appointments where appointment_id=" + appIDTextField.getText();
			
			stmt.executeUpdate(deleteApp);
			con.close();
			appDeleteConfirmationPane.setVisible(false);
			populateAppointments();
		}
		catch(SQLException e) {
			errorAlert.setHeaderText("Error");
			errorAlert.setContentText("Unable to delete appointment.");
			errorAlert.showAndWait();
		}
	}
	
	public void searchUsers() {
		searchUsers.clear();
		String userSearch = searchUsersTextField.getText();
		if(!userSearch.equals("")) {
			for(int i = 0; i < systemUsers.size(); i++) {
				if(systemUsers.get(i).getS2().contains(userSearch)) {
					searchUsers.add(systemUsers.get(i));
				}
			}
			usersTable.setItems(searchUsers);
		}
		else {
			populateSystemUsers();
		}
	}
	
	public void searchModalities() {
		searchModalities.clear();
		String userSearch = searchModalitiesTextField.getText();
		if(!userSearch.equals("")) {
			for(int i = 0; i < modalities.size(); i++) {
				if(modalities.get(i).getS1().contains(userSearch)) {
					searchModalities.add(modalities.get(i));
				}
			}
			modalitiesTable.setItems(searchModalities);
		}
		else {
			populateModalities();
		}
	}
	
	public void searchAlerts() {
		searchAlerts.clear();
		String userSearch = searchAlertsTextField.getText();
		if(!userSearch.equals("")) {
			for(int i = 0; i < alerts.size(); i++) {
				if(alerts.get(i).getS1().contains(userSearch)) {
					searchAlerts.add(alerts.get(i));
				}
			}
			patientAlertsTable.setItems(searchAlerts);
		}
		else {
			populateAlerts();
		}
	}
	
	public void searchPatients() {
		searchPatients.clear();
		String userSearch = searchPatientsTextField.getText();
		if(!userSearch.equals("")) {
			for(int i = 0; i < patients.size(); i++) {
				if(patients.get(i).getS2().contains(userSearch) || patients.get(i).getS3().contains(userSearch)) {
					searchPatients.add(patients.get(i));
				}
			}
			patientsTable.setItems(searchPatients);
		}
		else {
			populatePatients();
		}
	}
	
	public void searchOrders() {
		searchOrders.clear();
		String userSearch = searchOrdersTextField.getText();
		if(!userSearch.equals("")) {
			for(int i = 0; i < orders.size(); i++) {
				if(orders.get(i).getS1().contains(userSearch)) {
					searchOrders.add(orders.get(i));
				}
			}
			orderTable.setItems(searchOrders);
		}
		else {
			populateOrders();
		}
	}
	
	public void searchApps() {
		searchApps.clear();
		String userSearch = searchAppsTextField.getText();
		if(!userSearch.equals("")) {
			for(int i = 0; i < appointments.size(); i++) {
				if(appointments.get(i).getS1().contains(userSearch)) {
					searchApps.add(appointments.get(i));
				}
			}
			appointmentsTable.setItems(searchApps);
		}
		else {
			populateAppointments();
		}
	}
	
	public void searchFiles() {
		searchFiles.clear();
		String userSearch = searchFilesTextField.getText();
		if(!userSearch.equals("")) {
			for(int i = 0; i < files.size(); i++) {
				if(files.get(i).getS1().contains(userSearch)) {
					searchFiles.add(files.get(i));
				}
			}
			filesTable.setItems(searchFiles);
		}
		else {
			populateFiles();
		}
	}
	
	public void searchReports() {
		searchReports.clear();
		String userSearch = searchReportsTextField.getText();
		if(!userSearch.equals("")) {
			for(int i = 0; i < reports.size(); i++) {
				if(reports.get(i).getS1().contains(userSearch)) {
					searchReports.add(reports.get(i));
				}
			}
			reportsTable.setItems(searchReports);
		}
		else {
			populateReports();
		}
	}
}

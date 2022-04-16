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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

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
	
	// Patient Alerts Pane
	@FXML
	TextField searchAlertsTextField;
	@FXML
	TableView<ModelTable> patientAlertsTable;
	@FXML
	TableColumn<ModelTable, String> alertPatientCol;
	@FXML
	TableColumn<ModelTable, String> alertIDCol;
	@FXML
	TableColumn<ModelTable, String> alertNameCol;
	@FXML
	TableColumn<ModelTable, String> alertModifyAlertCol;
	ObservableList<ModelTable> alerts = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchAlerts = FXCollections.observableArrayList();
	
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
	
	// Patient Pane
	@FXML
	TextField searchPatientsTextField;
	@FXML
	TableView<ModelTable> patientsTable;
	@FXML
	TableColumn<ModelTable, Integer> patientIDCol;
	@FXML
	TableColumn<ModelTable, String> patientDOBCol;
	@FXML
	TableColumn<ModelTable, String> patientLastNameCol;
	@FXML
	TableColumn<ModelTable, String> patientFirstNameCol;
	@FXML
	TableColumn<ModelTable, String> modPatientCol;
	ObservableList<ModelTable> patients = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchPatients = FXCollections.observableArrayList();
	
	// Order Pane
	@FXML
	TextField searchOrdersTextField;
	@FXML
	TableView<ModelTable> orderTable;
	@FXML
	TableColumn<ModelTable, Integer> orderIDCol;
	@FXML
	TableColumn<ModelTable, String> orderPatientCol;
	@FXML
	TableColumn<ModelTable, String> orderModalityCol;
	@FXML
	TableColumn<ModelTable, String> orderDocCol;
	@FXML
	TableColumn<ModelTable, String> orderNotesCol;
	@FXML
	TableColumn<ModelTable, String> orderStatusCol;
	@FXML
	TableColumn<ModelTable, String> orderDeleteCol;
	ObservableList<ModelTable> orders = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchOrders = FXCollections.observableArrayList();
	
	// File Uploads Pane
	@FXML
	TextField searchFilesTextField;
	@FXML
	TableView<ModelTable> filesTable;
	@FXML
	TableColumn<ModelTable, String> filesIDCol;
	@FXML
	TableColumn<ModelTable, String> filesNameCol;
	@FXML
	TableColumn<ModelTable, String> filesTypeCol;
	@FXML
	TableColumn<ModelTable, String> filesOrderIDCol;
	@FXML
	TableColumn<ModelTable, String> filesDeleteFileCol;
	ObservableList<ModelTable> files = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchFiles = FXCollections.observableArrayList();
	
	// Diagnostic Reports Pane
	@FXML
	TextField searchReportsTextField;
	@FXML
	TableView<ModelTable> reportsTable;
	@FXML
	TableColumn<ModelTable, String> reportsIDCol;
	@FXML
	TableColumn<ModelTable, String> reportsRadioCol;
	@FXML
	TableColumn<ModelTable, String> reportsOrderIDCol;
	@FXML
	TableColumn<ModelTable, String> reportsReportCol;
	@FXML
	TableColumn<ModelTable, String> reportsModifyReportCol;
	ObservableList<ModelTable> reports = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchReports = FXCollections.observableArrayList();

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
	
	// createAppointmentPane
	@FXML
	Pane createAppointmentPane;
	@FXML
	TextField appPatientIDTextField;
	@FXML
	TextField appDateTextField;
	@FXML
	ChoiceBox<String> appTimeChoiceBox;
	@FXML
	ChoiceBox<String> appRadiologistChoiceBox;
	@FXML
	ChoiceBox<String> appTechnicianChoiceBox;
	@FXML
	ChoiceBox<String> appModalityChoiceBox;
	
	// appDeleteConfirmationPane
	@FXML
	Pane appDeleteConfirmationPane;
	@FXML
	TextField appIDTextField;
	@FXML
	Button appConfirmDeleteButton;
	
	// Create Patient Alert Pane
	@FXML
	Pane createPatientAlertPane;
	@FXML
	TextField PAIDTextField;
	@FXML
	CheckBox PAEnabledCheckBox;
	
	// New Patient Pane
	@FXML
	Pane newPatientPane;
	@FXML
	TextField PatientFirstName;
	@FXML
	TextField PatientLastName;
	@FXML
	TextField PatientRace;
	@FXML
	DatePicker DOB;
	@FXML
	TextField PatientEthnicity;
	@FXML
	ChoiceBox<String> sexChoiceBox;
	@FXML
	Button cancelNewPatientButton;
	@FXML
	Button addNewPatientButton;
	
	// Mod Patient Pane
	@FXML
	Pane modPatientPane;
	@FXML
	TextField modPatientFirstName;
	@FXML
	TextField modPatientLastName;
	@FXML
	TextField modPatientRace;
	@FXML
	DatePicker modDOB;
	@FXML
	TextField modPatientEthnicity;
	@FXML
	ChoiceBox<String> modsexChoiceBox;
	@FXML
	TextField modPatientIDTextField;
	@FXML
	Button cancelModPatientButton;
	@FXML
	Button submitModPatientButton;
	ObservableList<String> sexes = FXCollections.observableArrayList();

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		populateSystemUsers();
		populateModalities();
		populateAppointments();
		populatePatients();
		populateOrders();
		paneInitialize();

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
			System.out.println("Error: Unable to get system user data.");
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
			System.out.println("Error: Unable to get modality data.");
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
	
	public void populateAlerts() {
		alerts.clear();
		
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from patient_alerts");
			
			while(rs.next()) {
				
				ResultSet rs2 = con.createStatement().executeQuery("select * from alerts where alert_id=" 
				+ rs.getInt("alert_id"));
				while(rs2.next()) {
					alerts.add(new ModelTable(rs.getInt("alert_id"), rs.getInt("patient_id"), 0, 
							rs2.getString("alert_name"), null, null, null, null, null));
				}
			}
			
			con.close();
			}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		alertIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
		alertPatientCol.setCellValueFactory(new PropertyValueFactory<>("num2"));
		alertNameCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
					} else {
						final Button modOrderButton = new Button("Modify");
						modOrderButton.setOnAction(event -> {
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

						setGraphic(modOrderButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		alertModifyAlertCol.setCellFactory(cellFactory);

		patientAlertsTable.setItems(alerts);
		
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
				modality = rs.getInt("modality");
				tech = rs.getInt("technician");
				radio = rs.getInt("radiologist");
				
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
		} 
		catch (SQLException e) {
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
						setGraphic(null);
					} 
					else {
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

			while (rs.next()) {
				patients.add(new ModelTable(rs.getInt("patient_id"), 0, 0, rs.getString("dob"),
						rs.getString("last_name"), rs.getString("first_name"), rs.getString("race"), 
						rs.getString("ethnicity"), null));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}

		patientIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
		patientDOBCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		patientLastNameCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		patientFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("s3"));	
		modPatientCol.setCellValueFactory(new PropertyValueFactory<>(""));

		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
						setGraphic(null);
					} 
					else {
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

							modPatientFirstName.setText("" + m.getS3());
							modPatientLastName.setText("" + m.getS2());
							modPatientRace.setText("" + m.getS4());
							modPatientEthnicity.setText("" + m.getS5());
							modPatientIDTextField.setText("" + m.getNum1());
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

	public void populateOrders() {
		orders.clear();
		int orderID = 0;
		int patient = 0;
		int doc = 0;
		int modality = 0;
		int status = 0;
		String patientName = null;
		String docName = null;
		String modalityName = null;
		String notes = null;
		String statusName = null;
		
		
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from orders");

			while (rs.next()) {
				orderID = rs.getInt("order_id");
				System.out.println(orderID);
				patient = rs.getInt("patient");
				System.out.println(patient);
				doc = rs.getInt("referral_md");
				System.out.println(doc);
				notes = rs.getString("notes");
				System.out.println(notes);
				modality = rs.getInt("modality");
				System.out.println(modality);
				status = rs.getInt("status");
				System.out.println(status);
				ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
				while(rs2.next()) {
					patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
				}
				System.out.println(patientName);
				rs2 = con.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
				while(rs2.next()) {
					modalityName = rs2.getString("name");
				}
				System.out.println(modalityName);
				rs2 = con.createStatement().executeQuery("select * from users where user_id=" + doc);
				while(rs2.next()) {
					docName = rs2.getString("full_name");
				}
				System.out.println(docName);
				rs2 = con.createStatement().executeQuery("select * from order_status where order_status_id=" + status);
				while(rs2.next()) {
					statusName = rs2.getString("name");
				}
				orders.add(new ModelTable(orderID, 0, 0, patientName, docName
						, modalityName, notes, 
						statusName, null));
			}
		} 
		catch (SQLException e) {
			System.out.println("Error: Could not get order data.");
		}
		
		orderIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
		orderPatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		orderDocCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		orderModalityCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		orderNotesCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
		orderStatusCol.setCellValueFactory(new PropertyValueFactory<>("s5"));
		/* Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

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

							orderDeleteConfirmationPane.setVisible(true);
							HomeButton.setDisable(true);
							AppointmentButton.setDisable(true);
							LogOut.setDisable(true);
							
							System.out.println(m.getNum1());
							orderIDTextField.setText(m.getNum1()+"");

						});

						setGraphic(modButton);
						setText(null);
					}
				}
			};

			return cell;
		}; */
		// allOrdersDeleteCol.setCellFactory(cellFactory); 

		orderTable.setItems(orders);
	}
	
	public void populateFiles() {
		files.clear();
		int fileID;
		int orderID = 0;
		String fileName = null;
		String fileType = null;
		
		
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from file_uploads");

			while (rs.next()) {
				fileID = rs.getInt("file_upload_id");
				orderID = rs.getInt("order_id");
				fileName = rs.getString("file_name");
				fileType = rs.getString("file_type");
				
				files.add(new ModelTable(fileID, orderID, 0, fileName, fileType
						, null, null, null, null));
			}
		} 
		catch (SQLException e) {
			System.out.println("Error: Could not get file upload data.");
		}
		
		filesIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
		filesNameCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		filesTypeCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		filesOrderIDCol.setCellValueFactory(new PropertyValueFactory<>("num2"));
		/* Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
					} else {
						final Button deleteFileButton = new Button("Delete");
						deleteFileButton.setOnAction(event -> {
							ModelTable m = getTableView().getItems().get(getIndex());

							HomeButton.setDisable(true);
							AppointmentButton.setDisable(true);
							LogOut.setDisable(true);
							
							fileIDTextField.setText(m.getNum1()+"");

						});

						setGraphic(deleteFileButton);
						setText(null);
					}
				}
			};

			return cell;
		}; */
		// filesDeleteFileCol.setCellFactory(cellFactory); 

		filesTable.setItems(files);	
	}
	
	public void populateReports() {
		reports.clear();
		int reportID = 0;
		int patient = 0;
		int radio = 0;
		int orderID = 0;	
		String radioName = null;
		String patientName = null;
		
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from diagnostic_reports");

			while (rs.next()) {
				reportID = rs.getInt("diagnostic_report_id");
				patient = rs.getInt("patient");
				radio = rs.getInt("radiologist");
				orderID = rs.getInt("order_id");
				
				ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
				while(rs2.next()) {
					patientName = rs2.getString("first_name") + " " + rs.getString("last_name");
				}
				
				rs2 = con.createStatement().executeQuery("select * from users where user_id=" + radio);
				while(rs2.next()) {
					radioName = rs2.getString("full_name");
				}
				
				reports.add(new ModelTable(reportID, radio, orderID, patientName, radioName
						, null, null, null, null));
			}
		} 
		catch (SQLException e) {
			System.out.println("Error: Could not get diagnostic report data.");
		}
		
		filesIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
		filesNameCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		filesTypeCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		filesOrderIDCol.setCellValueFactory(new PropertyValueFactory<>("num2"));
		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
						setGraphic(null);
					} 
					else {
						final Button viewReportButton = new Button("View Report");
						viewReportButton.setOnAction(event -> {
							ModelTable m = getTableView().getItems().get(getIndex());

							HomeButton.setDisable(true);
							AppointmentButton.setDisable(true);
							LogOut.setDisable(true);

						});

						setGraphic(viewReportButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		reportsModifyReportCol.setCellFactory(cellFactory); 

		reportsTable.setItems(reports);		
	}
	
	public void paneInitialize() {
		roles.removeAll(roles);
		sexes.removeAll(sexes);
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
		roleChoiceBox.getSelectionModel().selectFirst();
		modRoleChoiceBox.getSelectionModel().selectFirst();
		
		a = "M";
		b ="F";
		c = "Other";
		sexes.addAll(a, b, c);
		sexChoiceBox.setItems(sexes);
		modsexChoiceBox.setItems(sexes);
		sexChoiceBox.getSelectionModel().selectFirst();
		modsexChoiceBox.getSelectionModel().selectFirst();
	}
	
	public void clearPanes() {
		IDTextField.setText("");
		usernameTextField.setText("");
		displayNameTextField.setText("");
		emailAddressTextField.setText("");
		enabledCheckBox.setSelected(false);
		roleChoiceBox.getSelectionModel().selectFirst();
		passwordTextField.setText("");
		confirmPasswordTextField.setText("");
		modIDTextField.setText("");
		modUsernameTextField.setText("");
		modDisplayNameTextField.setText("");
		modEmailAddressTextField.setText("");
		modEnabledCheckBox.setSelected(false);
		modRoleChoiceBox.getSelectionModel().selectFirst();
		modPasswordTextField.setText("");
		modConfirmPasswordTextField.setText("");
		modalityIDTextField.setText("");
		modalityNameTextField.setText("");
		modalityCostTextField.setText("");
		modModalityIDTextField.setText("");
		modModalityNameTextField.setText("");
		modModalityCostTextField.setText("");
		PAIDTextField.setText("");
		PAEnabledCheckBox.setSelected(false);
		PatientFirstName.setText("");
		PatientLastName.setText("");
		PatientRace.setText("");
		PatientEthnicity.setText("");
		DOB.setValue(null);
		sexChoiceBox.getSelectionModel().selectFirst();
		modPatientFirstName.setText("");
		modPatientLastName.setText("");
		modPatientRace.setText("");
		modPatientEthnicity.setText("");
		modDOB.setValue(null);
		modsexChoiceBox.getSelectionModel().selectFirst();
		modPatientIDTextField.setText("");
		appIDTextField.setText("");
		
	}

	public void NewUserButton(ActionEvent event) throws IOException {

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
		} 
		else if (!Character.isLetter(usernameTextField.getText().charAt(0))) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Username must start with a letter.");
			errorAlert.showAndWait();
		} 
		else {
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
		} 
		else {
			displayName = displayNameTextField.getText();
		}
		// Set email
		if (emailAddressTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Email cannot be blank.");
			errorAlert.showAndWait();
		} 
		else {
			email = emailAddressTextField.getText();
		}
		// Set password
		if (passwordTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Password cannot be blank.");
			errorAlert.showAndWait();
		} 
		else {
			if (passwordTextField.getText().equals(confirmPasswordTextField.getText())) {
				password = passwordTextField.getText();
			} 
			else {
				errorAlert.setHeaderText("Invalid input");
				errorAlert.setContentText("Passwords do not match.");
				errorAlert.showAndWait();
			}
		}
		
		// Set enabled
		if (enabled == true) {
			enableCheck = 1;
		} 
		else {
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

			String newUser = "insert into users (user_id, email, full_name, username, password, enabled) " + "values ("
					+ ID + ", \'" + email + "\', \'" + displayName + "\', \'" + username + "\', \'" + password + "\', "
					+ enableCheck + ")";

			String addUserRole = "insert into users_roles (user_id, role_id, id) " + "values (" + ID + ", " + idNum
					+ ", " + idNum2 + ")";

			stmt.executeUpdate(newUser);
			stmt.executeUpdate(addUserRole);

			con.close();
			
			updateAlert.setHeaderText("Success");
			updateAlert.setContentText("User has been successfully added.");
			updateAlert.showAndWait();
		} 
		catch (Exception e) {
			errorAlert.setHeaderText("Error");
			errorAlert.setContentText("Unable to add user.");
			errorAlert.showAndWait();
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
		} 
		else if (!Character.isLetter(modUsernameTextField.getText().charAt(0))) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Username must start with a letter.");
			errorAlert.showAndWait();
		} 
		else {
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
		} 
		else {
			displayName = modDisplayNameTextField.getText();
		}
		// Set email
		if (modEmailAddressTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Email cannot be blank.");
			errorAlert.showAndWait();
		} 
		else {
			email = modEmailAddressTextField.getText();
		}
		// Set password
		if (modPasswordTextField.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Password cannot be blank.");
			errorAlert.showAndWait();
		} 
		else {
			if (modPasswordTextField.getText().equals(modConfirmPasswordTextField.getText())) {
				password = modPasswordTextField.getText();
			} 
			else {
				errorAlert.setHeaderText("Invalid input");
				errorAlert.setContentText("Passwords do not match.");
				errorAlert.showAndWait();
			}
		}
		// Set enabled
		if (enabled == true) {
			enableCheck = 1;
		} 
		else {
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
			errorAlert.setHeaderText("Error");
			errorAlert.setContentText("Unable to modify user.");
			errorAlert.showAndWait();
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
		populateSystemUsers();
		clearPanes();

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
			updateAlert.setHeaderText("Success");
			updateAlert.setContentText("Modality has been successfully created.");
			updateAlert.showAndWait();
		}
		catch(SQLException e) {
			errorAlert.setHeaderText("Error");
			errorAlert.setContentText("Unable to create modality.");
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
		clearPanes();
		
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
		} 
		else {
			cost = modModalityCostTextField.getText();
		}
		
		id = Integer.parseInt(modModalityIDTextField.getText());

		try {
			Connection con = DatabaseConnection.getConnection();
			Statement stmt = con.createStatement();

			String modModality = "update modalities set name=\'" + name + "\', cost=\'" + cost + "\' where modality_id=" + id + "";

			stmt.executeUpdate(modModality);

			con.close();
			
			updateAlert.setHeaderText("Success");
			updateAlert.setContentText("Modality has been successfully modified.");
			updateAlert.showAndWait();
		} 
		catch (Exception e) {
			errorAlert.setHeaderText("Error");
			errorAlert.setContentText("Unable to modify modality.");
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
		clearPanes();
	}
	
	public void createPatientAlertButton(ActionEvent event) throws IOException {
		
	}
	
	//Patients pane controls
	
	//new Patient controls
	public void NewPatientButton(ActionEvent event) throws IOException {
		
			newPatientPane.setVisible(true);
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
		}
		else if (!Character.isLetter(PatientFirstName.getText().charAt(0))) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText(" must start with a letter.");
			errorAlert.showAndWait();
		}
		else {
			firstName = PatientFirstName.getText();
		}
		
		//set lastName
		if (PatientLastName.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Last name cannot be blank.");
			errorAlert.showAndWait();
		}
		else {
			lastName = PatientLastName.getText();
		}
		
		//set DOB
		if (DOB.getValue().toString().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("DOB cannot be blank.");
			errorAlert.showAndWait();
		}
		else {
			patientDOB = DOB.getValue().toString();
		}
				
		//set race
		if (PatientRace.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Race cannot be blank.");
			errorAlert.showAndWait();
		}
		else {
			race = PatientRace.getText();
		}
		//set ethnicity
		if (PatientEthnicity.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Ethnicity cannot be blank.");
			errorAlert.showAndWait();
		}
		else {
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

			String newPatient = "insert into patients (patient_id, first_name, last_name, dob, sex, race, ethnicity) " + "values ("
					+ ID + ", \'" + firstName + "\', \'" + lastName + "\', \'" + patientDOB + "\', \'" + sex + "\', \'"
					+ race + "\', \'"+ethnicity+ "\')";
			

			stmt.executeUpdate(newPatient);

			con.close();
			
			updateAlert.setContentText("Patient has been successfully added.");
			updateAlert.showAndWait();
		} 
		catch (Exception e) {
			System.out.println("Error: Unable to add new patient.");
		}
		
		newPatientPane.setVisible(false);
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
		populateAppointments();
		populatePatients();
		populateOrders();
		clearPanes();
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
		}
		else if (!Character.isLetter(modPatientFirstName.getText().charAt(0))) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Names must start with a letter.");
			errorAlert.showAndWait();
		}
		else {
			firstName = modPatientFirstName.getText();
		}
		
		//set lastName
		if (modPatientLastName.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Last name cannot be blank.");
			errorAlert.showAndWait();
		}
		else {
			lastName = modPatientLastName.getText();
		}
		
		//set DOB
		if (modDOB.getValue().toString().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("DOB cannot be blank.");
			errorAlert.showAndWait();
		}
		else {
			patientDOB = modDOB.getValue().toString();
		}
		
				
		//set race
		if (modPatientRace.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Race cannot be blank.");
			errorAlert.showAndWait();
		}
		else {
			race = modPatientRace.getText();
		}
		//set ethnicity
		if (modPatientEthnicity.getText().isBlank()) {
			errorAlert.setHeaderText("Invalid input");
			errorAlert.setContentText("Ethnicity cannot be blank.");
			errorAlert.showAndWait();
		}
		else {
			ethnicity = modPatientEthnicity.getText();
		}	
		//set sex
		sex = modsexChoiceBox.getValue().toString();
		
		try {
			Connection con = DatabaseConnection.getConnection();
			Statement stmt = con.createStatement();
			 
			String modPatient = "update patients set first_name= \'" + firstName + "\', last_name= \'"+lastName+"\', dob=\'"+patientDOB+"\', sex=\'"+sex+"\', race=\'"+race+"\', ethnicity=\'"+ethnicity+ "' WHERE patient_id= \'" + patientID + "\';";
			stmt.executeUpdate(modPatient);
						
			con.close();
			
			updateAlert.setHeaderText("Success");
			updateAlert.setContentText("Patient has been successfully modified.");
			updateAlert.showAndWait();
		} 
		catch (Exception e) {
			System.out.println("Error: Unable to modify patient.");
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
		populateAppointments();
		populateOrders();
		clearPanes();

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
	
	/* public void allOrdersConfirmDelete(ActionEvent event) throws IOException {
		
		try {
			Connection con = DatabaseConnection.getConnection();
			Statement stmt = con.createStatement();
			
			String deleteApp = "delete from orders where appointment_id=" + allOrdersIDTextField.getText();
			stmt.executeUpdate(deleteApp);
			
			con.close();
			
			allOrdersDeleteConfirmationPane.setVisible(false);
			HomeButton.setDisable(false);
			AppointmentButton.setDisable(false);
			LogOut.setDisable(false);
			updateAlert.setHeaderText("Success");
			updateAlert.setContentText("Order has been successfully deleted.");
			updateAlert.showAndWait();
			populateOrders();
		}
		catch(SQLException e) {
			System.out.println("Error: Could not delete order.");
		}
	} */
	
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
	
	public void cancelButton(ActionEvent event) throws IOException {
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
		newUserPane.setVisible(false);
		modUserPane.setVisible(false);
		createModalityPane.setVisible(false);
		modModalityPane.setVisible(false);
		newPatientPane.setVisible(false);
		modPatientPane.setVisible(false);
		appDeleteConfirmationPane.setVisible(false);
		clearPanes();
		
	}
}

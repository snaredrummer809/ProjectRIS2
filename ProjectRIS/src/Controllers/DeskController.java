package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import application.DatabaseConnection;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class DeskController implements Initializable{
	
	// Nav Buttons
	@FXML
	Button deskAppButton;
	@FXML
	Button deskOrdersButton;
	@FXML
	Button LogOut;
	
	// checkedInAppPane
	@FXML
	Pane deskCheckedInAppPane;
	@FXML
	TextField searchCheckedInAppsTextField;
	@FXML
	TableView<ModelTable> checkedInAppTable;
	@FXML
	TableColumn<ModelTable, String> patientCol;
	@FXML
	TableColumn<ModelTable, String> modalityCol;
	@FXML
	TableColumn<ModelTable, String> dateAndTimeCol;
	@FXML
	TableColumn<ModelTable, String> radiologistCol;
	@FXML
	TableColumn<ModelTable, String> techCol;
	@FXML
	TableColumn<ModelTable, String> statusCol;
	ObservableList<ModelTable> checkedInApps = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchCheckedIn = FXCollections.observableArrayList();
	
	// todaysAppPane
	@FXML
	Pane deskTodaysAppPane;
	@FXML
	TextField searchTodaysAppsTextField;
	@FXML
	TableView<ModelTable> todaysAppTable;
	@FXML
	TableColumn<ModelTable, String> todaysPatientCol;
	@FXML
	TableColumn<ModelTable, String> todaysModalityCol;
	@FXML
	TableColumn<ModelTable, String> todaysDateAndTimeCol;
	@FXML
	TableColumn<ModelTable, String> todaysRadiologistCol;
	@FXML
	TableColumn<ModelTable, String> todaysTechCol;
	@FXML
	TableColumn<ModelTable, String> checkInCol;
	ObservableList<ModelTable> todaysApps = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchTodays = FXCollections.observableArrayList();
	
	// unscheduledOrdersPane
	@FXML
	TextField searchUnscheduledOrdersTextField;
	@FXML
	TableView<ModelTable> unscheduledOrdersTable;
	@FXML
	TableColumn<ModelTable, String> UOPatientCol;
	@FXML
	TableColumn<ModelTable, String> UODocCol;
	@FXML
	TableColumn<ModelTable, String> UOModalityCol;
	@FXML
	TableColumn<ModelTable, String> UONotesCol;
	@FXML
	TableColumn<ModelTable, String> scheduleCol;
	ObservableList<ModelTable> unscheduledOrders = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchUnscheduled = FXCollections.observableArrayList();
	
	// scheduleAppPane
	@FXML
	Pane scheduleAppPane;
	@FXML
	TextField appFirstNameTextField;
	@FXML
	TextField appLastNameTextField;
	@FXML
	TextField appDOBTextField;
	@FXML
	TextField appPhoneTextField;
	@FXML
	TextField appEmailTextField;
	@FXML
	TextField appPatientIDTextField;
	@FXML
	DatePicker appDatePicker;
	@FXML
	ChoiceBox<String> appTimeChoiceBox;
	@FXML
	ChoiceBox<String> appRadioChoiceBox;
	@FXML
	ChoiceBox<String> appTechChoiceBox;
	@FXML
	ChoiceBox<String> appModalityChoiceBox;
	@FXML
	Button scheduleAppButton;
	@FXML
	Button cancelButton;
	ObservableList<String> times = FXCollections.observableArrayList();
	ObservableList<String> radios = FXCollections.observableArrayList();
	ObservableList<String> techs = FXCollections.observableArrayList();
	ObservableList<String> modalities = FXCollections.observableArrayList();
	
	// Alerts
	Alert errorAlert = new Alert(AlertType.ERROR);
	Alert updateAlert = new Alert(AlertType.CONFIRMATION);	
	
	public void userLogOut(ActionEvent event) throws IOException {
		
		Main m = new Main();		
		m.changeScene("../Views/Login.fxml");
	}
	
	public void deskAppButton(ActionEvent event) throws IOException {
		
		Main m = new Main();
		m.changeScene("../Views/DeskAppointments.fxml");
	}
	
	public void deskOrdersButton(ActionEvent event) throws IOException {
		
		Main m = new Main();
		m.changeScene("../Views/DeskOrders.fxml");
	}
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		populateCheckedInApps();
		populateTodaysApps();
		populateUnscheduledOrders();
		scheduleAppPaneInit();
		
		errorAlert.setHeaderText("Error");
		updateAlert.setHeaderText("Success");

	}
	
	// Populate checkedInAppsTable
	public void populateCheckedInApps() {
		checkedInApps.clear();
		int patient = 0;
		int modality = 0;
		int tech = 0;
		int radio = 0;
		int order = 0;
		int status = 0;
		int checkedIn = 0;
		String patientName = null;
		String modalityName = null;
		String techName = null;
		String radioName = null;
		String statusName = null;
		
		// Get all appointment data
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from appointments");

			while (rs.next()) {
				patient = rs.getInt("patient");
				modality = rs.getInt("modality");
				tech = rs.getInt("technician");
				radio = rs.getInt("radiologist");
				order = rs.getInt("order_id");
				checkedIn = rs.getInt("checked_in");
				
				// Check to make sure appointment has been checked in
				if(checkedIn == 1) {
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
					rs2 = con.createStatement().executeQuery("select * from orders where order_id=" + order);
					while(rs2.next()) {
						status = rs2.getInt("status");
					}
					rs2 = con.createStatement().executeQuery("select * from order_status where order_status_id=" + status);
					while(rs2.next()) {
						statusName = rs2.getString("name");
					}
					
					checkedInApps.add(new ModelTable(0, 0, 0, patientName,
							modalityName, rs.getString("date_time"), radioName, 
							techName, statusName));
				}
			}
			
			con.close();
		} 
		catch (SQLException e) {
			System.out.println("Error: Could not get checked in appointment data.");
		}
		
		patientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		modalityCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		dateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		radiologistCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
		techCol.setCellValueFactory(new PropertyValueFactory<>("s5"));
		statusCol.setCellValueFactory(new PropertyValueFactory<>("s6"));

		// Set observable list data in the table
		checkedInAppTable.setItems(checkedInApps);
	}
	
	// Populates the table in the todaysAppsPane
	public void populateTodaysApps() {
		todaysApps.clear();
		int patient = 0;
		int modality = 0;
		int tech = 0;
		int radio = 0;
		int checkedIn = 0;
		int appID = 0;
		LocalDate todaysDate = null;
		String dateCheck = null;
		String date = null;
		String patientName = null;
		String modalityName = null;
		String techName = null;
		String radioName = null;
		
		// Get today's date in the format which dates are stored in the database
		dateCheck = todaysDate.now().getYear() + "-";
		
		if(todaysDate.now().getMonthValue()<10) {
			dateCheck = dateCheck + "0" + todaysDate.now().getMonthValue() + "-";
		}
		else {
			dateCheck = dateCheck + todaysDate.now().getMonthValue() + "-";
		}
		if(todaysDate.now().getDayOfMonth()<10) {
			dateCheck = dateCheck + "0" + todaysDate.now().getDayOfMonth();
		}
		else {
			dateCheck = dateCheck + todaysDate.now().getDayOfMonth();
		}

		
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from appointments");

			// Get all appointment info
			while (rs.next()) {
				patient = rs.getInt("patient");
				modality = rs.getInt("modality");
				tech = rs.getInt("technician");
				radio = rs.getInt("radiologist");
				checkedIn = rs.getInt("checked_in");
				appID = rs.getInt("appointment_id");
				date = rs.getString("date_time");
				
				// Make sure patient isn't already checked in
				if(checkedIn == 0) {
					
					// Only display appointment if it is scheduled for today's date
					if(date.contains(dateCheck)) {
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
						
						todaysApps.add(new ModelTable(appID, 0, 0, patientName,
										modalityName, rs.getString("date_time"), radioName, 
										techName, null));
					}
				}
			}
			
			con.close();
		} 
		catch (SQLException e) {
			System.out.println("Error: Could not get todays appointment data.");
		}
		
		todaysPatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		todaysModalityCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		todaysDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		todaysRadiologistCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
		todaysTechCol.setCellValueFactory(new PropertyValueFactory<>("s5"));
		
		// Create check in button to fill the checkInCol
		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
					} 
					else {
						final Button checkInButton = new Button("Check In");
						checkInButton.setOnAction(event -> {
							ModelTable m = getTableView().getItems().get(getIndex());
							
							int appNum = m.getNum1();
							
							try {
								Connection con = DatabaseConnection.getConnection();
								Statement stmt = con.createStatement();
								String checkInPatient = "update appointments set checked_in=1 where appointment_id=" + appNum;
								stmt.executeUpdate(checkInPatient);
								
								int order_id = 0;
								String getOrderId = "select * from appointments where appointment_id=" + appNum;
								ResultSet rs = stmt.executeQuery(getOrderId);
								while(rs.next())
								{
									order_id = rs.getInt("order_id");
								}
								String changeStatus = "update orders SET status=1 where order_id="+order_id+";";
								stmt.executeUpdate(changeStatus);
								populateCheckedInApps();
								populateTodaysApps();
								
								con.close();
							}
							catch(SQLException e) {
								
							}

						});

						setGraphic(checkInButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		checkInCol.setCellFactory(cellFactory);
		
		// Set observable list data in table
		todaysAppTable.setItems(todaysApps);
	}
	
	// Populate the table in the unscheduledOrdersPane
	public void populateUnscheduledOrders() {
		unscheduledOrders.clear();
		int appNum = 0;
		int patient = 0;
		int doc = 0;
		int modality = 0;
		String patientName = null;
		String docName = null;
		String modalityName = null;
		String notes = null;
		
		// Get all order data
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from orders");
			
			while (rs.next()) {
				appNum = rs.getInt("appointment");
				patient = rs.getInt("patient");
				doc = rs.getInt("referral_md");
				modality = rs.getInt("modality");
				notes = rs.getString("notes");
				
				// Check to make sure the order hasn't already been scheduled
				if(appNum == 0) {
					ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
					while(rs2.next()) {
						patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
					}
					rs2 = con.createStatement().executeQuery("select * from users where user_id=" + doc);
					while(rs2.next()) {
						docName = rs2.getString("full_name");
					}
					rs2 = con.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
					while(rs2.next()) {
						modalityName = rs2.getString("name");
					}
					unscheduledOrders.add(new ModelTable(appNum, patient, 0, patientName, docName,
							modalityName, notes, null, null));
				}
			}
			
			con.close();
		} 
		catch (SQLException e) {
			System.out.println("Error: Could not get unscheduled order data.");
		}
		
		UOPatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		UODocCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		UOModalityCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		UONotesCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
		
		// Create scheduleButton to populate the scheduleCol
		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
					} 
					else {
						final Button scheduleButton = new Button("Schedule");
						scheduleButton.setOnAction(event -> {
							ModelTable m = getTableView().getItems().get(getIndex());
							int patientID = 0;
							patientID = m.getNum2();
							scheduleAppPane.setVisible(true);
							deskAppButton.setDisable(true);
							deskOrdersButton.setDisable(true);
							LogOut.setDisable(true);
							deskCheckedInAppPane.setDisable(true);
							deskTodaysAppPane.setDisable(true);
							
							try {
								Connection con = DatabaseConnection.getConnection();
								ResultSet rs = con.createStatement().executeQuery("select * from patients where patient_id=" + patientID);
								while(rs.next()) {
									appFirstNameTextField.setText(rs.getString("first_name"));
									appLastNameTextField.setText(rs.getString("last_name"));
									appDOBTextField.setText(rs.getString("dob"));
									appPatientIDTextField.setText(patientID+"");
									appPhoneTextField.setText(rs.getString("phone_number"));
									appEmailTextField.setText(rs.getString("email_address"));
								}
								
								con.close();
							}
							catch(SQLException e) {
								
							}						
							
						});

						setGraphic(scheduleButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		scheduleCol.setCellFactory(cellFactory);
		
		// Set the observable list data in the table
		unscheduledOrdersTable.setItems(unscheduledOrders);
	}
	
	// Initializes the choiceboxes in the scheduleAppPane;
	public void scheduleAppPaneInit() {
		radios.clear();
		techs.clear();
		modalities.clear();
		int userID = 0;
		
		// Get a radiologist, technician, and modality data
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from users_roles where role_id=10");
			while(rs.next()) {
				userID = rs.getInt("user_id");
			}
			ResultSet rs2 = con.createStatement().executeQuery("select * from users where user_id=" + userID);
			while(rs2.next()) {
				radios.addAll(rs2.getString("full_name"));
			}
			appRadioChoiceBox.setItems(radios);
			appRadioChoiceBox.getSelectionModel().selectFirst();
			rs = con.createStatement().executeQuery("select * from users_roles where role_id=11");
			while(rs.next()) {
				userID = rs.getInt("user_id");
			}
			rs2 = con.createStatement().executeQuery("select * from users where user_id=" + userID);
			while(rs2.next()) {
				techs.addAll(rs2.getString("full_name"));
			}
			appTechChoiceBox.setItems(techs);
			appTechChoiceBox.getSelectionModel().selectFirst();
			rs = con.createStatement().executeQuery("select * from modalities");
			while(rs.next()) {
				modalities.addAll(rs.getString("name"));
			}
			appModalityChoiceBox.setItems(modalities);
			appModalityChoiceBox.getSelectionModel().selectFirst();
			
			con.close();
		}
		catch(SQLException e1) {
			System.out.println("Error: Could not initialize scheduleAppPane.");
		}
	}
	
	// Gets available times for a selected date and displays them in the appTimeChoiceBox
	public void getTimes(ActionEvent event) throws IOException{
		LocalDate ld = appDatePicker.getValue();
		String date = null;
		if(ld != null) {
			date = ld.getYear() + "-";
			
			if(ld.getMonthValue()<10) {
				date = date + "0" + ld.getMonthValue() + "-";
			}
			else {
				date = date + ld.getMonthValue() + "-";
			}
			if(ld.getDayOfMonth()<10) {
				date = date + "0" + ld.getDayOfMonth();
			}
			else {
				date = date + ld.getDayOfMonth();
			}
		}
		String a = "9:00";
		String b = "9:30";
		String c = "10:00";
		String d = "10:30";
		String e = "11:00";
		String f = "11:30";
		String g = "12:00";
		String h = "12:30";
		String i = "1:00";
		String j = "1:30";
		String k = "2:00";
		String l = "2:30";
		String m = "3:00";
		String n = "3:30";
		String o = "4:00";
		String p = "4:30";
		times.clear();
		String dateTime = null;
		String[] usedTimes = new String[16];
		int counter = 0;
		
		// Get all appointments with the selected date
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from appointments where " +
					"date_time like '%" + date + "%'");
			
			// If there are no appointments then set all times in the choice box
			if(!rs.next()) {
				usedTimes[0] = a;
				usedTimes[1] = b;
				usedTimes[2] = c;
				usedTimes[3] = d;
				usedTimes[4] = e;
				usedTimes[5] = f;
				usedTimes[6] = g;
				usedTimes[7] = h;
				usedTimes[8] = i;
				usedTimes[9] = j;
				usedTimes[10] = k;
				usedTimes[11] = l;
				usedTimes[12] = m;
				usedTimes[13] = n;
				usedTimes[14] = o;
				usedTimes[15] = p;
			}
			
			// If there are appointments, get the time of the appointments and remove them
			rs = con.createStatement().executeQuery("select * from appointments where " +
					"date_time like '%" + date + "%'");
			while(rs.next()) {
				dateTime = rs.getString("date_time");
				if(!dateTime.contains(a)) {
					usedTimes[counter] = a;
					counter++;
				}
				if(!dateTime.contains(b)) {
					usedTimes[counter] = b;
					counter++;
				}
				if(!dateTime.contains(c)) {
					usedTimes[counter] = c;
					counter++;
				}
				if(!dateTime.contains(d)) {
					usedTimes[counter] = d;
					counter++;
				}
				if(!dateTime.contains(e)) {
					usedTimes[counter] = e;
					counter++;
				}
				if(!dateTime.contains(f)) {
					usedTimes[counter] = f;
					counter++;
				}
				if(!dateTime.contains(g)) {
					usedTimes[counter] = g;
					counter++;
				}
				if(!dateTime.contains(h)) {
					usedTimes[counter] = h;
					counter++;
				}
				if(!dateTime.contains(i)) {
					usedTimes[counter] = i;
					counter++;
				}
				if(!dateTime.contains(j)) {
					usedTimes[counter] = j;
					counter++;
				}
				if(!dateTime.contains(k)) {
					usedTimes[counter] = k;
					counter++;
				}
				if(!dateTime.contains(l)) {
					usedTimes[counter] = l;
					counter++;
				}
				if(!dateTime.contains(m)) {
					usedTimes[counter] = m;
					counter++;
				}
				if(!dateTime.contains(n)) {
					usedTimes[counter] = n;
					counter++;
				}
				if(!dateTime.contains(o)) {
					usedTimes[counter] = o;
					counter++;
				}
				if(!dateTime.contains(p)) {
					usedTimes[counter] = p;
					counter++;
				}
				counter = 0;
			}
			
			// Add all available appointments to the observable list
			for(int index=0;index<usedTimes.length;index++) {
				if(usedTimes[index] != null) {
					times.addAll(usedTimes[index]);
				}
			}
			
			appTimeChoiceBox.setItems(times);
			
		}
		catch(SQLException e1) {
			System.out.println("Error: Could not get available appointment times.");
		}
	}
	
	// Button click event for scheduling appointments
	public void scheduleAppButton(ActionEvent event) throws IOException{
		int appNum = 0;
		int patientID = Integer.parseInt(appPatientIDTextField.getText());
		int orderID = 0;
		int modalityID = 0;
		int radioID = 0;
		int techID = 0;
		String phone = null;
		String tempPhone = null;
		boolean check1 = false;
		boolean check2 = false;
		String email = null;
		String date = null;
		String time = null;
		String radio = null;
		String tech = null;
		String modality = null;
		
		tempPhone = "";
		
		// If the phone number contains hyphens remove them
		for(int i=0; i<appPhoneTextField.getText().length();i++) {
			if(appPhoneTextField.getText().charAt(i) != '-') {
				tempPhone = tempPhone + appPhoneTextField.getText().charAt(i);
			}
		}
		
		// After removing hyphens if there are any, check that the number is the correct length
		if(tempPhone.length() != 10) {
			errorAlert.setContentText("Invalid phone number. Format XXX-XXX-XXXX");
			errorAlert.showAndWait();
			check1 = false;			
		}
		else {
			check1 = true;
		}
		
		// Check that all characters are digits
		for(int i=0;i<tempPhone.length();i++) {
			if(!Character.isDigit(tempPhone.charAt(i))) {
				errorAlert.setContentText("Invalid phone number. Format XXX-XXX-XXXX");
				errorAlert.showAndWait();
				check2 = false;
			}
			else {
				check2 = true;
			}
		}
		
		// If the phone number is the correct length, and it only contains digits it passes and can be used
		if(check1 == true && check2 == true) {
			phone = "" + tempPhone.charAt(0) + tempPhone.charAt(1) + tempPhone.charAt(2) + "-" + tempPhone.charAt(3)
			+ tempPhone.charAt(4) + tempPhone.charAt(5) + "-" + tempPhone.charAt(6) + tempPhone.charAt(7)
			+ tempPhone.charAt(8) + tempPhone.charAt(9);
		}

		// Get patient email
		email = appEmailTextField.getText();
		
		// Error message for a blank date field
		if(appDatePicker.getValue() == null) {
			errorAlert.setContentText("Please select a date and time.");
			errorAlert.showAndWait();
		}
		else {
			date = appDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
		}
		
		// Error message for a blank time choice box
		if(appTimeChoiceBox.getValue() == null) {
			errorAlert.setContentText("Please select a time.");
			errorAlert.showAndWait();
		}
		else {
			time = appTimeChoiceBox.getValue() + ":00";
		}
		
		// Get radiologist, technician, and modality selection
		radio = appRadioChoiceBox.getValue();		
		tech = appTechChoiceBox.getValue();
		modality = appModalityChoiceBox.getValue();
		
		try {
			Connection con = DatabaseConnection.getConnection();
			
			// Get next available appointment ID
			try {
				ResultSet rs = con.createStatement().executeQuery("select * from appointments");
				while(rs.next()) {
					appNum = rs.getInt("appointment_id");
				}
				appNum = appNum + 1;
			}
			catch(SQLException e) {
				appNum = 1;
			}
			
			// Get order ID
			try {
				ResultSet rs = con.createStatement().executeQuery("select * from orders where patient=" + patientID);
				while(rs.next()) {
					orderID = rs.getInt("order_id");
				}
			}
			catch(SQLException e) {
				System.out.println("Error: Orders empty.");
			}
			
			// Get modality ID
			try {
				ResultSet rs = con.createStatement().executeQuery("select * from modalities where name=\'" + modality + "\'");
				while(rs.next()) {
					modalityID = rs.getInt("modality_id");
				}
			}
			catch(SQLException e) {
				System.out.println("Error: Modalities empty.");
			}
			
			// Get radiologist ID
			try {
				ResultSet rs = con.createStatement().executeQuery("select * from users where full_name=\'" + radio + "\'");
				while(rs.next()) {
					radioID = rs.getInt("user_id");
				}
			}
			catch(SQLException e) {
					System.out.println("Error: Radiologist not found.");
				}
			
			// Get technician ID
			try {
				ResultSet rs = con.createStatement().executeQuery("select * from users where full_name=\'" + tech + "\'");
				while(rs.next()) {
					techID = rs.getInt("user_id");
				}
			}
			catch(SQLException e) {
				System.out.println("Error: Technician not found.");
			}
			
			Statement stmt = con.createStatement();
			
			// Update the appointments table
			String updateApps = "insert into appointments(appointment_id, patient, order_id, modality,"
					+ "date_time, radiologist, technician, phone_number, email_address) "
					+ " values(" + appNum + ", " + patientID + ", " + orderID + ", " + modalityID + ", \'"
					+ date + " " + time + "\', " + radioID + ", " + techID + ", '" + phone + "', '" + email + "')";
			stmt.executeUpdate(updateApps);
			
			// Update the orders table
			String updateOrders = "update orders set appointment=" + appNum + " where order_id=" + orderID;
			stmt.executeUpdate(updateOrders);
			
			// Update the patients table
			String updatePatients = "update patients set phone_number=\'" + phone + "\', email_address=\'" + email
					+ "\' where patient_id=" + patientID;
			stmt.executeUpdate(updatePatients);
			
			populateUnscheduledOrders();
			populateTodaysApps();
			
			scheduleAppPane.setVisible(false);
			appPatientIDTextField.setText("");
			appPhoneTextField.setText("");
			appEmailTextField.setText("");
			appDatePicker.setValue(null);
			appTimeChoiceBox.setValue(null);
			appRadioChoiceBox.getSelectionModel().selectFirst();
			appTechChoiceBox.getSelectionModel().selectFirst();
			appModalityChoiceBox.getSelectionModel().selectFirst();
			deskAppButton.setDisable(false);
			deskOrdersButton.setDisable(false);
			LogOut.setDisable(false);
			deskCheckedInAppPane.setDisable(false);
			deskTodaysAppPane.setDisable(false);
		}
		catch(SQLException e) {
			System.out.println("Error: Could not schedule appointment.");
		}
	}
	
	public void searchCheckedIn() {
		searchCheckedIn.clear();
		String userSearch = searchCheckedInAppsTextField.getText();
		if(!userSearch.equals("")) {
			for(int i = 0; i < checkedInApps.size(); i++) {
				if(checkedInApps.get(i).getS1().contains(userSearch)) {
					searchCheckedIn.add(checkedInApps.get(i));
				}
			}
			checkedInAppTable.setItems(searchCheckedIn);
		}
		else {
			populateCheckedInApps();
		}
	}
	
	public void searchTodays() {
		searchTodays.clear();
		String userSearch = searchTodaysAppsTextField.getText();
		if(!userSearch.equals("")) {
			for(int i = 0; i < todaysApps.size(); i++) {
				if(todaysApps.get(i).getS1().contains(userSearch)) {
					searchTodays.add(todaysApps.get(i));
				}
			}
			todaysAppTable.setItems(searchTodays);
		}
		else {
			populateTodaysApps();
		}
	}
	
	public void searchUnscheduled() {
		searchUnscheduled.clear();
		String userSearch = searchUnscheduledOrdersTextField.getText();
		if(!userSearch.equals("")) {
			for(int i = 0; i < unscheduledOrders.size(); i++) {
				if(unscheduledOrders.get(i).getS1().contains(userSearch)) {
					searchUnscheduled.add(unscheduledOrders.get(i));
				}
			}
			unscheduledOrdersTable.setItems(searchUnscheduled);
		}
		else {
			populateUnscheduledOrders();
		}
	}
	
	// Cancel and user action and display main page
	public void cancelButton(ActionEvent event) throws IOException{
		scheduleAppPane.setVisible(false);
		appPatientIDTextField.setText("");
		appPhoneTextField.setText("");
		appEmailTextField.setText("");
		appDatePicker.setValue(null);
		appTimeChoiceBox.setValue(null);
		appRadioChoiceBox.getSelectionModel().selectFirst();
		appTechChoiceBox.getSelectionModel().selectFirst();
		appModalityChoiceBox.getSelectionModel().selectFirst();
		deskAppButton.setDisable(false);
		deskOrdersButton.setDisable(false);
		LogOut.setDisable(false);
		deskCheckedInAppPane.setDisable(false);
		deskTodaysAppPane.setDisable(false);
	}
}

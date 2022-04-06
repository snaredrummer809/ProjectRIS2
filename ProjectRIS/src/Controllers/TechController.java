package Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import application.DatabaseConnection;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class TechController implements Initializable{
	
	// Nav buttons
	@FXML
	Button LogOut;
	
	// Checked In Apps Pane
	@FXML
	TableView<ModelTable> checkedInAppsTable;
	@FXML
	TableColumn<ModelTable, String> checkedInPatientCol;
	@FXML
	TableColumn<ModelTable, String> checkedInModalityCol;
	@FXML 
	TableColumn<ModelTable, String> checkedInDateAndTimeCol;
	@FXML 
	TableColumn<ModelTable, String> checkedInRadioCol;
	@FXML 
	TableColumn<ModelTable, String> checkedInTechCol;
	@FXML 
	TableColumn<ModelTable, String> checkedInCompleteOrderCol;
	ObservableList<ModelTable> checkedInApps = FXCollections.observableArrayList();


	// Imaging Order Pane
	@FXML
	Pane ImagingOrderPane;
	@FXML 
	TextField imagingFirstNameTextField;
	@FXML
	TextField imagingLastNameTextField;
	@FXML 
	TextField imagingModalityTextField;
	@FXML 
	TextArea imagingMDNotesTextArea;


	// Patient Overview pane
	@FXML Pane patientOverviewPane;
	@FXML TextField overviewFirstNameTextField;
	@FXML TextField overviewLastNameTextField;
	@FXML TextField overviewDOBTextField;
	@FXML TextField overviewSexTextField;
	@FXML TextField overviewRaceTextField;
	@FXML TextField overviewEthnicityTextField;
	@FXML TextField overviewPhoneTextField;
	@FXML TextField overviewEmailTextField;
	@FXML TextArea overviewAllergiesTextArea;
	@FXML Button closeButton;

	public void initialize(URL arg0, ResourceBundle arg1) {
		populateCheckedInApps();
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
					
					checkedInApps.add(new ModelTable(patient, modality, order, patientName,
							modalityName, rs.getString("date_time"), radioName, 
							techName, null));
				}
			}
			
			con.close();
		} 
		catch (SQLException e) {
			System.out.println("Error: Could not get checked in appointment data.");
		}
		
		checkedInPatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		checkedInModalityCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		checkedInDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		checkedInRadioCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
		checkedInTechCol.setCellValueFactory(new PropertyValueFactory<>("s5"));
		
		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
					} else {
						final Button compOrderButton = new Button("Complete Order");
						compOrderButton.setOnAction(event -> {
							ModelTable m = getTableView().getItems().get(getIndex());
							String firstName = null;
							String lastName = null;
							String DOB = null;
							String sex = null;
							String race = null;
							String ethnicity = null;
							String phone = null;
							String email = null;
							String allergies = null;
							String modalityName = null;
							
							try {
								Connection con = DatabaseConnection.getConnection();
								ResultSet rs = con.createStatement().executeQuery("select * from patients where patient_id=" + m.getNum1());
								
								while(rs.next()) {
									firstName = rs.getString("first_name");
									lastName = rs.getString("last_name");
									DOB = rs.getString("DOB");
									sex = rs.getString("sex");
									race = rs.getString("race");
									ethnicity = rs.getString("ethnicity");
									phone = rs.getString("phone_number");
									email = rs.getString("email_address");
									allergies = rs.getString("allergies");
									
								}
								
							}
							catch(SQLException e) {
								e.printStackTrace();
							}
							
							try {
								Connection con = DatabaseConnection.getConnection();
								ResultSet rs = con.createStatement().executeQuery("select * from modalities where modality_id=" + m.getNum2());
								
								while(rs.next()) {
									modalityName = rs.getString("name");
								}
							}
							catch(SQLException e) {
								e.printStackTrace();
							}

							overviewFirstNameTextField.setText(firstName);
							overviewLastNameTextField.setText(lastName);
							overviewDOBTextField.setText(DOB);
							overviewSexTextField.setText(sex);
							overviewRaceTextField.setText(race);
							overviewEthnicityTextField.setText(ethnicity);
							overviewPhoneTextField.setText(phone);
							overviewEmailTextField.setText(email);
							overviewAllergiesTextArea.setText(allergies);
							
							imagingFirstNameTextField.setText(firstName);
							imagingLastNameTextField.setText(lastName);
							imagingModalityTextField.setText(modalityName);
							
							LogOut.setDisable(true);
							patientOverviewPane.setVisible(true);

						});

						setGraphic(compOrderButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		checkedInCompleteOrderCol.setCellFactory(cellFactory);

		// Set observable list data in the table
		checkedInAppsTable.setItems(checkedInApps);
	}

	//complete order method
	public void completeOrderButton(ActionEvent event) throws IOException {
		ImagingOrderPane.setVisible(false);
		patientOverviewPane.setVisible(false);
		//add method to upload image!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!	
	}
	
	//cancel order method
	public void backButton(ActionEvent event) throws IOException {
		ImagingOrderPane.setVisible(false);
	}


	//patient overview method
	public void patientOverview() {
		ImagingOrderPane.setVisible(false);
		patientOverviewPane.setVisible(true);
		//fill out method to fill in fields
		//querying patients table for patient info
	}

	//patient overview close button
	public void closeOverview() {
		LogOut.setDisable(false);
		patientOverviewPane.setVisible(false);
		ImagingOrderPane.setVisible(true);
		overviewFirstNameTextField.setText("");
		overviewLastNameTextField.setText("");
		overviewDOBTextField.setText("");
		overviewSexTextField.setText("");
		overviewRaceTextField.setText("");
		overviewEthnicityTextField.setText("");
		overviewPhoneTextField.setText("");
		overviewEmailTextField.setText("");
		overviewAllergiesTextArea.setText("");

	}

	public void userLogOut(ActionEvent event) throws IOException {
		
		Main m = new Main();
		
		m.changeScene("../Views/Login.fxml");
	}
	
	public void HomeButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/Tech.fxml");
	}
	
	public void AppointmentButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/TechAppointments.fxml");
	}
}

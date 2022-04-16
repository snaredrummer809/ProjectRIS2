package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class DeskOrdersController implements Initializable{
		
	// Nav buttons
	@FXML
	Button HomeButton;
	@FXML
	Button AppointmentButton;
	@FXML
	Button LogOut;
	
	// Orders Pane
	@FXML
	TextField searchOrdersTextField;
	@FXML
	TableView<ModelTable> deskAllOrdersTable;
	@FXML
	TableColumn<ModelTable, Integer> deskAllOrdersOrderIDCol;
	@FXML
	TableColumn<ModelTable, String> deskAllOrdersPatientCol;
	@FXML
	TableColumn<ModelTable, String> deskAllOrdersModalityCol;
	@FXML
	TableColumn<ModelTable, String> deskAllOrdersReferralDocCol;
	@FXML
	TableColumn<ModelTable, String> deskAllOrdersNotesCol;
	@FXML
	TableColumn<ModelTable, String> deskAllOrdersStatusCol;
	@FXML 
	TableColumn<ModelTable, String> deskAllOrdersContactCol;
	@FXML
	TableColumn<ModelTable, String> deskAllOrdersDeleteCol;
	ObservableList<ModelTable> orders = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchOrders = FXCollections.observableArrayList();
	
	//appDeleteConfirmationPane
	@FXML
	Pane allOrdersDeleteConfirmationPane;
	@FXML
	TextField allOrdersIDTextField;
	@FXML
	Button allOrdersConfirmDeleteButton;
	
	//Alerts
	Alert errorAlert = new Alert(AlertType.ERROR);
	Alert updateAlert = new Alert(AlertType.CONFIRMATION);
	
	//modify patient pane
	@FXML Pane modPatientPane;
	@FXML TextField modPatientFirstName;
	@FXML TextField modPatientLastName;
	@FXML Button cancelModPatientButton;
	@FXML Button submitModPatientButton;
	@FXML TextField modPatientRace;
	@FXML DatePicker modDOB;
	@FXML TextField modPatientEthnicity;
	@FXML ChoiceBox modsexChoiceBox;
	@FXML TextField modPatientIDTextField;
	@FXML TextField modPatientStreetTextField;
	@FXML TextField modPatientCityTextField;
	@FXML TextField modPatientStateTextField;
	@FXML TextField modPatientZipTextField;
	@FXML TextField modPatientPhoneTextField;
	@FXML TextField modPatientEmailTextField;
	@FXML TextArea modPatientNotesTextArea;
	ObservableList<String> sexChoices = FXCollections.observableArrayList();
	ModelTable t = new ModelTable();
	
	
	
	
	
	public void userLogOut(ActionEvent event) throws IOException {

		Main m = new Main();

		m.changeScene("../Views/Login.fxml");
	}

	public void HomeButton(ActionEvent event) throws IOException {

		Main m = new Main();
		m.changeScene("../Views/Desk.fxml");
	}

	public void AppointmentButton(ActionEvent event) throws IOException {
		Main m = new Main();
		m.changeScene("../Views/DeskAppointments.fxml");
	}
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		populateOrders();
		
		//adding newPatientSex choicebox options
				sexChoices.removeAll(sexChoices);
				sexChoices.addAll("Male", "Female", "Other");
				modsexChoiceBox.setItems(sexChoices);

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
				//System.out.println(orderID);
				patient = rs.getInt("patient");
				//System.out.println(patient);
				doc = rs.getInt("referral_md");
				//System.out.println(doc);
				notes = rs.getString("notes");
				//System.out.println(notes);
				modality = rs.getInt("modality");
				//System.out.println(modality);
				status = rs.getInt("status");
				//System.out.println(status);
				ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
				while(rs2.next()) {
					patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
				}
				//System.out.println(patientName);
				rs2 = con.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
				while(rs2.next()) {
					modalityName = rs2.getString("name");
				}
				//System.out.println(modalityName);
				rs2 = con.createStatement().executeQuery("select * from users where user_id=" + doc);
				while(rs2.next()) {
					docName = rs2.getString("full_name");
				}
				//System.out.println(docName);
				rs2 = con.createStatement().executeQuery("select * from order_status where order_status_id=" + status);
				while(rs2.next()) {
					statusName = rs2.getString("name");
				}
				orders.add(new ModelTable(orderID, patient, 0, patientName, docName
						, modalityName, notes, 
						statusName, null));
			}
		} 
		catch (SQLException e) {
			System.out.println("Error: Could not get order data.");
		}
		
		deskAllOrdersOrderIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
		deskAllOrdersPatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		deskAllOrdersReferralDocCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		deskAllOrdersModalityCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		deskAllOrdersNotesCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
		deskAllOrdersStatusCol.setCellValueFactory(new PropertyValueFactory<>("s5"));
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

							allOrdersDeleteConfirmationPane.setVisible(true);
							HomeButton.setDisable(true);
							AppointmentButton.setDisable(true);
							LogOut.setDisable(true);
							
							//System.out.println(m.getNum1());
							allOrdersIDTextField.setText(m.getNum1()+"");

						});

						setGraphic(modButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		//contact info button
		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory2 = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
					} else {
						final Button contactButton = new Button("Contact Info");
						contactButton.setOnAction(event -> {
							ModelTable m = getTableView().getItems().get(getIndex());
							
							int patient = m.getNum2();
							String firstName = null;
							String lastName = null;
							String patientDOB = null;
							String race = null;
							String ethnicity = null;
							//String sex = null;
							String phone = "";
							String email = "";
							String notes = "";
							String street = "";
							String city = "";
							String state = "";
							String zip = "";
							try {
								Connection con = DatabaseConnection.getConnection();
								Statement stmt = con.createStatement();
								 
								String getPatient = "SELECT * FROM patients where patient_id="+patient+";";
								//System.out.println(modPatient);
								ResultSet rs = stmt.executeQuery(getPatient);
											
								while(rs.next())
								{
									firstName = rs.getString("first_name");
									lastName = rs.getString("last_name");
									patientDOB = rs.getString("dob");
									race= rs.getString("race");
									ethnicity = rs.getString("ethnicity");
									street = rs.getString("street_address");
									city = rs.getString("city");
									state = rs.getString("state_abbreviation");
									zip = rs.getString("zip");
									phone = rs.getString("phone_number");
									email = rs.getString("email_address");
									notes = rs.getString("patientNotes");
								}
														
								con.close();
								//updateAlert.setHeaderText("Success!");
								//updateAlert.setContentText("Patient has been successfully modified.");
								//updateAlert.showAndWait();
							} 
							catch (Exception e) {
								System.out.println("Error: Failed to retrieve patient info.");
							}
							
							String patient_id= ""+patient;
							
							modPatientFirstName.setText(firstName);
							modPatientLastName.setText(lastName);
							LocalDate dob = LocalDate.parse(patientDOB);
							modDOB.setValue(dob);
						
							modPatientIDTextField.setText(patient_id);
							modPatientStreetTextField.setText(street);
							modPatientCityTextField.setText(city);
							modPatientStateTextField.setText(state);
							modPatientZipTextField.setText(zip);
							modPatientPhoneTextField.setText(phone);
							modPatientEmailTextField.setText(email);
							modPatientNotesTextArea.setText(notes);
							modPatientEthnicity.setText(ethnicity);
							modPatientRace.setText(race);
							modPatientPane.setVisible(true);

						});

						setGraphic(contactButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		deskAllOrdersDeleteCol.setCellFactory(cellFactory); 
		deskAllOrdersContactCol.setCellFactory(cellFactory2);
		deskAllOrdersTable.setItems(orders);
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
			HomeButton.setDisable(false);
			AppointmentButton.setDisable(false);
			LogOut.setDisable(false);
			
		}

	public void allOrdersConfirmDelete(ActionEvent event) throws IOException {
		
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
			deskAllOrdersTable.setItems(searchOrders);
		}
		else {
			populateOrders();
		}
	}
	
	public void cancelButton(ActionEvent event) throws IOException {
		HomeButton.setDisable(false);
		AppointmentButton.setDisable(false);
		LogOut.setDisable(false);
		allOrdersDeleteConfirmationPane.setVisible(false);
		
	}
	public void cancelNewPatientButton(ActionEvent event) throws IOException {
		modPatientPane.setVisible(false);
		modPatientFirstName.clear();
		modPatientLastName.clear();
		modPatientRace.clear();
		modPatientEthnicity.clear();
		modDOB.setValue(null);
		
		//re-enable all other panes
		HomeButton.setDisable(false);
		AppointmentButton.setDisable(false);
		LogOut.setDisable(false);
		
	}

}

package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import application.DatabaseConnection;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class AdminReferralsController implements Initializable{
	
	// Nav buttons
	@FXML
	Button HomeButton;
	@FXML
	Button AdminButton;
	@FXML
	Button AppointmentButton;
	@FXML
	Button OrdersButton;
	@FXML
	Button InvoiceButton;
	
	//Patients Pane	
		@FXML
		TextField searchReferralPatientTextField;
		@FXML
		TableView<ModelTable> patientsTable;
		@FXML
		TableColumn<ModelTable, Integer> patientIDCol;
		@FXML
		TableColumn<ModelTable, String> patientLastNameCol;
		@FXML
		TableColumn<ModelTable, String> patientFirstNameCol;
		@FXML
		ChoiceBox<String> sexChoiceBox;
		ObservableList<ModelTable> patients = FXCollections.observableArrayList();
		ObservableList<ModelTable> searchPatients = FXCollections.observableArrayList();
		
		
		//overview
		@FXML
		private TextField patientSearch;
		@FXML
		TableView<ModelTable> overviewTable1;
		@FXML
		private TableColumn<ModelTable, String> overviewID;

		public void userLogOut(ActionEvent event) throws IOException {
			
			Main m = new Main();
			
			m.changeScene("../Views/Login.fxml");
		}
		
		public void HomeButton(ActionEvent event) throws IOException{
			
			Main m = new Main();
			m.changeScene("../Views/Admin.fxml");
		}
		
		public void AdminButton(ActionEvent event) throws IOException{
			
			Main m = new Main();
			m.changeScene("../Views/AdminAdmin.fxml");	
		}
		
		public void AppointmentButton(ActionEvent event) throws IOException{
			
			Main m = new Main();
			m.changeScene("../Views/AdminAppointments.fxml");
		}
		
		public void OrderButton(ActionEvent event) throws IOException{
			
			Main m = new Main();
			m.changeScene("../Views/AdminOrders.fxml");
		}
		
		public void InvoiceButton(ActionEvent event) throws IOException{
			
			Main m = new Main();
			m.changeScene("../Views/AdminInvoice.fxml");
		}
		

		public void NewPatientButton(ActionEvent event) throws IOException{
			Main m = new Main();
			m.changeScene("../Views/AdminNewPatient.fxml");
		}
		
		public void initialize(URL arg0, ResourceBundle arg1) {
			populatePatients();
		}
		
		public void populatePatients() {
			patients.clear();
			try {
				Connection con = DatabaseConnection.getConnection();
				ResultSet rs = con.createStatement().executeQuery("select * from patients");

					while (rs.next()) {
						patients.add(new ModelTable(rs.getInt("patient_id"), 0, 0, rs.getString("dob"),
								rs.getString("last_name"), rs.getString("first_name"), null, null, null));
					}
			} catch (SQLException e) {
				e.printStackTrace();
			}

			patientIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
			patientLastNameCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
			patientFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("s3"));	
			patientsTable.setItems(patients);
			}
		
		public void overviewButton() throws IOException {
			Main m = new Main();
			m.changeScene("../Views/AdminPatientOverview.fxml");
		}
		
		public void searchPatients() {
			searchPatients.clear();
			String userSearch = searchReferralPatientTextField.getText();
			if(!userSearch.equals("")) {
				for(int i = 0; i < patients.size(); i++) {
					if(patients.get(i).getS1().contains(userSearch) || patients.get(i).getS2().contains(userSearch)){
						searchPatients.add(patients.get(i));
					}
				}
				patientsTable.setItems(searchPatients);
			}
			else {
				populatePatients();
			}
		}
}

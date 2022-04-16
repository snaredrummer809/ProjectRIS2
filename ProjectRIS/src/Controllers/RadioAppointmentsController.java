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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class RadioAppointmentsController implements Initializable {

	//Nav buttons
	
	@FXML
	Button HomeButton;
	@FXML
	Button AppointmentButton;
	@FXML
	Button OrderButton;
	@FXML
	Button LogOut;
	
	// Appointment Pane
		@FXML
		TextField searchAllAppsTextField;
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
		TableColumn<ModelTable, String> appRadioColumn;
		
		//TableColumn<ModelTable, String> appDeleteColumn;
		ObservableList<ModelTable> appointments = FXCollections.observableArrayList();
		ObservableList<ModelTable> searchApps = FXCollections.observableArrayList();
		
		//appDeleteConfirmationPane
		@FXML
		Pane appDeleteConfirmationPane;
		@FXML
		TextField appIDTextField;
		@FXML
		Button appConfirmDeleteButton;
		
		//Alerts
		Alert errorAlert = new Alert(AlertType.ERROR);
		Alert updateAlert = new Alert(AlertType.CONFIRMATION);
		
		
public void initialize(URL arg0, ResourceBundle arg1) {
			
			populateAppointments();
		}
	
		public void userLogOut(ActionEvent event) throws IOException {
			
			Main m = new Main();
			
			m.changeScene("../Views/Login.fxml");
		}
		
		public void HomeButton(ActionEvent event) throws IOException{
			
			Main m = new Main();
			m.changeScene("../Views/Radio.fxml");
		}

		public void AppointmentButton(ActionEvent event) throws IOException{
		
			Main m = new Main();
			m.changeScene("../Views/RadioAppointments.fxml");
		}


		public void OrderButton(ActionEvent event) throws IOException{
		
			Main m = new Main();
			m.changeScene("../Views/RadioOrders.fxml");
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
					rs2 = con.createStatement().executeQuery("select * from users where user_id= " + radio);
					while(rs2.next()) {
						radioName = rs2.getString("full_name");
					}				
					
					appointments.add(new ModelTable(0, 0, 0, patientName,
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
			appRadioColumn.setCellValueFactory(new PropertyValueFactory<>("s5"));
			
			
			appointmentsTable.setItems(appointments);
		}
	
	public void searchApps() {
			searchApps.clear();
			String userSearch = searchAllAppsTextField.getText();
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
		
		
}

package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

public class DeskAppointmentsController implements Initializable{
		
		// Nav buttons
		@FXML
		Button HomeButton;
		@FXML
		Button ordersButton;
		@FXML
		Button LogOut;
		
		// Appointment Pane
		@FXML
		TextField searchAppsTextField;
		@FXML
		TableView<ModelTable> deskAppsTable;
		@FXML
		TableColumn<ModelTable, String> deskAppsPatientColumn;
		@FXML
		TableColumn<ModelTable, String> deskAppsModalityColumn;
		@FXML
		TableColumn<ModelTable, String> deskAppsDateAndTimeColumn;
		@FXML
		TableColumn<ModelTable, String> deskAppsTechNameColumn;
		@FXML
		TableColumn<ModelTable, String> deskAppsRadiologistColumn;
		@FXML
		TableColumn<ModelTable, String> deskAppsDeleteColumn;
		ObservableList<ModelTable> appointments = FXCollections.observableArrayList();
		ObservableList<ModelTable> searchApps = FXCollections.observableArrayList();
		
		//Alerts
		Alert updateAlert = new Alert(AlertType.CONFIRMATION);
		
		public void userLogOut(ActionEvent event) throws IOException {

			Main m = new Main();

			m.changeScene("../Views/Login.fxml");
		}

		public void HomeButton(ActionEvent event) throws IOException {

			Main m = new Main();
			m.changeScene("../Views/Desk.fxml");
		}

		public void OrderButton(ActionEvent event) throws IOException {

			Main m = new Main();
			m.changeScene("../Views/DeskOrders.fxml");
		}
		
		public void initialize(URL arg0, ResourceBundle arg1) {
			
			populateAppointments();
			updateAlert.setHeaderText("Success");

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
			
			deskAppsPatientColumn.setCellValueFactory(new PropertyValueFactory<>("s1"));
			deskAppsModalityColumn.setCellValueFactory(new PropertyValueFactory<>("s2"));
			deskAppsDateAndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("s3"));
			deskAppsTechNameColumn.setCellValueFactory(new PropertyValueFactory<>("s4"));
			deskAppsRadiologistColumn.setCellValueFactory(new PropertyValueFactory<>("s5"));

			deskAppsTable.setItems(appointments);
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
				deskAppsTable.setItems(searchApps);
			}
			else {
				populateAppointments();
			}
		}
		
		public void cancelButton(ActionEvent event) throws IOException {
			HomeButton.setDisable(false);
			ordersButton.setDisable(false);
			LogOut.setDisable(false);			
		}
}

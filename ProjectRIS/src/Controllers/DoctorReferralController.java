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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;


public class DoctorReferralController implements Initializable{
	//Patients Pane	
	@FXML
	TableView<PatientTableModel> patientsTable;
	@FXML
	TableColumn<PatientTableModel, Integer> patientIDCol; //these are by fx:id found in fxml
	@FXML
	TableColumn<PatientTableModel, String> patientLastNameCol;
	@FXML
	TableColumn<PatientTableModel, String> patientFirstNameCol;
	@FXML
	ChoiceBox<String> sexChoiceBox;
	ObservableList<PatientTableModel> patients = FXCollections.observableArrayList();
	
	
	//overview
	@FXML
	private TextField patientSearch;
	@FXML
	TableView<PatientTableModel> overviewTable1;
	@FXML
	private TableColumn<PatientTableModel, String> overviewID;

	public void userLogOut(ActionEvent event) throws IOException {
		
		Main m = new Main();
		
		m.changeScene("../Views/Login.fxml");
	}
	
	public void HomeButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/Doctor.fxml");
}
	
	public void ReferralsButton(ActionEvent event) throws IOException{
		Main m = new Main();
		m.changeScene("../Views/DoctorReferrals.fxml");	
}
	
	public void OrderButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/Doctor.fxml");
	}
	

	public void NewPatientButton(ActionEvent event) throws IOException{
		Main m = new Main();
		m.changeScene("../Views/DoctorNewPatient.fxml");
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
					patients.add(new PatientTableModel(rs.getInt("patient_id"), rs.getString("dob"),
							rs.getString("last_name"), rs.getString("first_name")));
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		patientIDCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
		patientLastNameCol.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		patientFirstNameCol.setCellValueFactory(new PropertyValueFactory<>("firstName"));	
		patientsTable.setItems(patients);
		}
	
	public void overviewButton() throws IOException {
		Main m = new Main();
		m.changeScene("../Views/DoctorPatientOverview.fxml");
		
	}
}

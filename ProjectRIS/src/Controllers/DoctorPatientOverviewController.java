package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;


public class DoctorPatientOverviewController implements Initializable{
	public int num = 50;
	public int ids[] = new int[num];
	public int numPatients = 0;
	ObservableList<ModelTable> patient = FXCollections.observableArrayList();
	
	@FXML
	TextField enterID;
	@FXML
	TableView<ModelTable> overviewTable1;
	@FXML
	TableView<ModelTable> overviewTable2;
	@FXML
	TableColumn<ModelTable, String> overviewID;
	@FXML
	TableColumn<ModelTable, String> overviewFirst;
	@FXML
	TableColumn<ModelTable, String> overviewLast;
	@FXML
	TableColumn<ModelTable, String> overviewDOB;
	@FXML
	TableColumn<ModelTable, String> overviewSex;
	@FXML
	TableColumn<ModelTable, String> overviewRace;
	@FXML
	TableColumn<ModelTable, String> overviewEthnicity;
	@FXML
	TableColumn<ModelTable, String> overviewEmail;
	@FXML
	TableColumn<ModelTable, String> overviewPhone;
	@FXML
	TableColumn<ModelTable, String> overviewNotes;
	
	
	
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
	
	public void newOrderButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/DoctorNewOrder.fxml");
	}
	
	public void countPatients() {
		
		Connection con;
		try {
			con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from patients");
			
			while(rs.next()) {
				numPatients++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void help() {
		countPatients();
		Connection con;
		try {
			con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from patients");
			for(int i = 0; i < numPatients; i++){
				rs.next();
				ids[i] = rs.getInt("patient_id");
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void ah() throws SQLException {
		patient.clear();
		String firstName = "";
		String lastName = "";
		String dob = "";
		String sex = "";
		String race = "";
		String ethnicity = "";
		String phoneNumber = "";
		String email = "";
		String notes = "";
		
		String field = enterID.getText();
		for(int i = 0; i < ids.length - 1; i++) {
			Integer x = ids[i];
			String xString = x.toString();
			if(field.equals(xString)) {
				Connection con = DatabaseConnection.getConnection();
				ResultSet rs = con.createStatement().executeQuery("select * from db_ris.patients where patient_id = " + field + ";");
				rs.next();
				firstName = rs.getString("first_name");
				lastName = rs.getString("last_name");
				dob = rs.getDate("dob").toString();
				sex = rs.getString("sex");
				race = rs.getString("race");
				ethnicity = rs.getString("ethnicity");
				phoneNumber = rs.getString("phone_number");
				email = rs.getString("email_address");
				notes = rs.getString("patientNotes");
				break;
			}
			else {
				
			}
		}
		//System.out.println(firstName + " " + lastName + " " + dob+ " " +sex+ " " +race+ " " +ethnicity+ " " +phoneNumber+ " " +email+ " " +notes);
		patient.add(new ModelTable(field, firstName, lastName, dob, sex, race, ethnicity, phoneNumber, email, notes));
		overviewID.setCellValueFactory(new PropertyValueFactory<>("s1"));
		overviewFirst.setCellValueFactory(new PropertyValueFactory<>("s2"));
		overviewLast.setCellValueFactory(new PropertyValueFactory<>("s3"));
		overviewDOB.setCellValueFactory(new PropertyValueFactory<>("s4"));
		overviewSex.setCellValueFactory(new PropertyValueFactory<>("s5"));
		overviewRace.setCellValueFactory(new PropertyValueFactory<>("s6"));
		overviewEthnicity.setCellValueFactory(new PropertyValueFactory<>("s7"));
		overviewPhone.setCellValueFactory(new PropertyValueFactory<>("s8"));
		overviewEmail.setCellValueFactory(new PropertyValueFactory<>("s9"));
		overviewNotes.setCellValueFactory(new PropertyValueFactory<>("s10"));
		overviewTable1.setItems(patient);
		overviewTable2.setItems(patient);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		help();
	}
	

}
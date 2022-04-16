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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.control.TextField;

public class TechAppointmentsController implements Initializable{

	


	
	@FXML Button TechAppointments;
	@FXML Pane ApptPane;
	@FXML TableView<ModelTable> ApptTable;
	@FXML TableColumn<ModelTable, String> ApptTablePatientCol;
	@FXML TableColumn<ModelTable, String> ApptTableDateTimeCol;
	@FXML TableColumn<ModelTable, String> ApptTableModalityCol;
	@FXML TableColumn<ModelTable, String> ApptTableRadioCol;
	@FXML TableColumn<ModelTable, String> ApptTableTechCol;
	ObservableList<ModelTable> appointments = FXCollections.observableArrayList();

	public void initialize(URL arg0, ResourceBundle arg1) {
		populateAppointments();
	}
	
	//method to fill checked-in appointments table
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
				ResultSet rs = con.createStatement().executeQuery("select * from appointments where closed=0");

				while (rs.next()) {
					patient = rs.getInt("patient");
					//System.out.println(patient);
					modality = rs.getInt("modality");
					//System.out.println(modality);
					radio = rs.getInt("radiologist");
					//System.out.println(radio);
					tech = rs.getInt("technician");
					//System.out.println(tech);
					ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
					while(rs2.next()) {
						patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
					}
					rs2 = con.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
					while(rs2.next()) {
						modalityName = rs2.getString("name");
					}
					rs2 = con.createStatement().executeQuery("select * from users where user_id=" + radio);
					while(rs2.next()) {
						techName = rs2.getString("full_name");
					}
					rs2 = con.createStatement().executeQuery("select * from users where user_id=" + tech);
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
			
			ApptTablePatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
			ApptTableModalityCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
			ApptTableDateTimeCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
			ApptTableRadioCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
			ApptTableTechCol.setCellValueFactory(new PropertyValueFactory<>("s5"));
			//CompleteOrderColumn.setCellValueFactory(new PropertyValueFactory<>(""));
			
			
			ApptTable.setItems(appointments);
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

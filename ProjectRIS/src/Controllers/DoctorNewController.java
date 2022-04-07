package Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;



public class DoctorNewController {
	
	//orders
	@FXML
	private TextField OrderPatientName;
	@FXML
	private TextField OrderReferralMD;
	@FXML
	private TextField OrderModalityNeeded;
	@FXML
	private TextArea OrderNotes;
	
	@FXML 
	private Button addNewPatientButton;
	@FXML
	private TextField PatientFirstName;
	@FXML 
	private TextField PatientLastName;
	@FXML 
	private TextField PatientRace;
	@FXML
	private TextField PatientEthnicity;
	@FXML
	private DatePicker DOB;
	@FXML
	private TextField sex;
	@FXML
	private TextField phoneNumber;
	@FXML
	private TextField emailAddress;
	@FXML
	private TextArea patientNotes;
	
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
	
	
	public void addNewOrder(ActionEvent event) throws IOException {
		 Connection conn = null;
	      Statement stmt = null;
	      
	      
	      try {
	          try {
	             Class.forName("com.mysql.cj.jdbc.Driver");
	          } catch (Exception e) {
	             System.out.println(e);
	       }
	       conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ris", "root", "Brikev34$");
	       stmt = (Statement) conn.createStatement();
	       
	       
	       String query1 = "INSERT INTO orders(patient, referral_md, modality, notes) VALUES('"+OrderPatientName.getText()+"', '"+OrderReferralMD.getText()+"', '"+OrderModalityNeeded.getText()+"', '"+OrderNotes.getText()+"');";
	       System.out.println(query1);
	       stmt.executeUpdate(query1);
	       } catch (SQLException excep) {
	          excep.printStackTrace();
	       } catch (Exception excep) {
	          excep.printStackTrace();
	       } finally {
	          try {
	             if (stmt != null)
	                conn.close();
	          } catch (SQLException se) {}
	          try {
	             if (conn != null)
	                conn.close();
	          } catch (SQLException se) {
	             se.printStackTrace();
	          }  
	       }
	      HomeButton(event);
	      }
	
	public void addNewPatient(ActionEvent event) throws IOException {
		
		 Connection conn = null;
	      Statement stmt = null;
	      
	      
	      try {
	          try {
	             Class.forName("com.mysql.cj.jdbc.Driver");
	          } catch (Exception e) {
	             System.out.println(e);
	       }
	       conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ris", "root", "Brikev34$");
	       stmt = (Statement) conn.createStatement();
	       
	       
	       String query1 = "INSERT INTO patients(first_name,last_name,dob,sex,race,ethnicity,email_address,phone_number,patientNotes) VALUES  ('"+PatientFirstName.getText()+"','"+PatientLastName.getText()+"', '"+DOB.getValue()+"' ,'"+sex.getText()+"', '"+PatientRace.getText()+"','" +PatientEthnicity.getText()+"','" + emailAddress.getText()+"','" + phoneNumber.getText()+"','" + patientNotes.getText()+"');";
	       stmt.executeUpdate(query1);
	       
	       } catch (SQLException excep) {
	          excep.printStackTrace();
	       } catch (Exception excep) {
	          excep.printStackTrace();
	       } finally {
	          try {
	             if (stmt != null)
	                conn.close();
	          } catch (SQLException se) {}
	          try {
	             if (conn != null)
	                conn.close();
	          } catch (SQLException se) {
	             se.printStackTrace();
	          }  
	       }
	       ReferralsButton(event);
	    }
}



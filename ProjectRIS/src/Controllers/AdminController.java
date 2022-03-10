package Controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import application.Main;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class AdminController {
	
	
	
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
	ChoiceBox<String> sexChoiceBox;

	
	public void userLogOut(ActionEvent event) throws IOException {
		
		Main m = new Main();
		
		m.changeScene("../Views/Login.fxml");
	}
	
	public void AdminButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/AdminAdmin.fxml");
	}
	
	public void AppointmentButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/AdminAppointments.fxml");
	}

	public void InvoiceButton(ActionEvent event) throws IOException{
	
		Main m = new Main();
		m.changeScene("../Views/AdminInvoice.fxml");
	}
	
	public void OrderButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/AdminOrders.fxml");
	}

	public void ReferralsButton(ActionEvent event) throws IOException{
	
		Main m = new Main();
		m.changeScene("../Views/AdminReferrals.fxml");
	}
	public void HomeButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/Admin.fxml");
	}
	public void NewPatientButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/NewPatient.fxml");
	}
	
	
	
	
	public void addNewPatient(ActionEvent event) throws IOException {
		
		
		String sex = "F";
				
		
		 Connection conn = null;
	      Statement stmt = null;
	      
	      
	      try {
	          try {
	             Class.forName("com.mysql.cj.jdbc.Driver");
	          } catch (Exception e) {
	             System.out.println(e);
	       }
	       conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ris", "root", "YOUR PASSWORD HERE");
	       System.out.println("Connection is created successfully:");
	       stmt = (Statement) conn.createStatement();
	       
	       
	       String query1 = "INSERT INTO patients(first_name,last_name,dob,sex,race,ethnicity) VALUES  ('" +PatientFirstName.getText()+"','"+PatientLastName.getText()+"', '"+DOB.getValue()+"' ,'"+sex+"', '"+PatientRace.getText()+"','" +PatientEthnicity.getText()+"');";
	       stmt.executeUpdate(query1);
	       
	       
	       //query1 = "INSERT INTO patients " + "VALUES (2, 'Carol', 42)";
	       //stmt.executeUpdate(query1);
	       
	       
	       System.out.println("Record is inserted in the table successfully..................");
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
	       System.out.println("Please check it in the MySQL Table......... ……..");
	    }
	 
	}

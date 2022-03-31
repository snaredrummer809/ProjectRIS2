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
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;


public class AdminController implements Initializable{
	
	//fxml elements
	
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
	
	//orders
	@FXML
	private TextField OrderPatientName;
	@FXML
	private TextField OrderReferralMD;
	@FXML
	private TextField OrderModalityNeeded;
	@FXML
	private TextArea OrderNotes;
	
	
	//Checked-in appointments pane
		@FXML
		TableView<ModelTable> CIATable;
		@FXML
		TableColumn<ModelTable, String> CIATablePatientColumn;
		@FXML
		TableColumn<ModelTable, String> CIATableModalityColumn;
		@FXML
		TableColumn<ModelTable, String> CIATableDateAndTimeColumn;
		@FXML
		TableColumn<ModelTable, String> CIATableRadiologistColumn;
		@FXML
		TableColumn<ModelTable, String> CIATableTechnicianColumn;
		ObservableList<ModelTable> CIappointments = FXCollections.observableArrayList();
	
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
	
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		populateCheckedInAppointments();
	}

	
	
	//method to fill checked-in appointments table
	public void populateCheckedInAppointments() {
		CIappointments.clear();
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
			ResultSet rs = con.createStatement().executeQuery("select * from appointments where checked_in=1");

			while (rs.next()) {
				patient = rs.getInt("patient");
				System.out.println(patient);
				modality = rs.getInt("modality");
				System.out.println(modality);
				radio = rs.getInt("radiologist");
				System.out.println(radio);
				tech = rs.getInt("technician");
				System.out.println(tech);
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
				
				CIappointments.add(new ModelTable(patient, 0, 0, patientName,
						modalityName, rs.getString("date_time"), techName, 
						radioName, null));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		CIATablePatientColumn.setCellValueFactory(new PropertyValueFactory<>("s1"));
		CIATableModalityColumn.setCellValueFactory(new PropertyValueFactory<>("s2"));
		CIATableDateAndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("s3"));
		CIATableRadiologistColumn.setCellValueFactory(new PropertyValueFactory<>("s4"));
		CIATableTechnicianColumn.setCellValueFactory(new PropertyValueFactory<>("s5"));

		CIATable.setItems(CIappointments);
	}
	
/*	
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
	       System.out.println("Please check it in the MySQL Table......... â€¦â€¦..");
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
	       /*conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ris", "root", "R PASSWORD HERE");
	       System.out.println("Connection is created successfully:");
	       stmt = (Statement) conn.createStatement();
	       * /
	          //my statements
	         conn = DatabaseConnection.getConnection();
			 stmt = conn.createStatement();
				//my end
			 int a = 1;
	      String query1 = "INSERT INTO orders(patient, referral_md, modality, notes) VALUES('"+OrderPatientName.getText()+"', '"+OrderReferralMD.getText()+"', '"+OrderModalityNeeded.getText()+"', '"+OrderNotes.getText()+"');";
	      System.out.println(query1);
	      // String query1 = "INSERT INTO orders(patient, referral_md, modality, notes) VALUES('"+a+"', '"+a+"', '"+a+"', '"+OrderNotes.getText()+"');";
	       //System.out.println(query1);
	       stmt.executeUpdate(query1);
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
	       System.out.println("Please check it in the MySQL Table......... â€¦â€¦..");
	       OrderButton(event);
	      }
*/
	}
	


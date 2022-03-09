package Controllers;

import java.io.IOException;

import application.Main;
import javafx.event.ActionEvent;

public class AdminController {

	
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
	public void addNewPatient(ActionEvent event) throws IOException {
		String firstName = "'Kevin'";
		String lastName = "'Cafferty'";
		String dob = "'2001-05-14'";
		String sex = "'m'";
		String race = "'white'";
		String ethnicity = "'white'";
		
		 Connection conn = null;
	      Statement stmt = null;
	      try {
	          try {
	             Class.forName("com.mysql.jdbc.Driver");
	          } catch (Exception e) {
	             System.out.println(e);
	       }
	       conn = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ris", "root", "ENTER YOUR PASSWORD HERE");
	       System.out.println("Connection is created successfully:");
	       stmt = (Statement) conn.createStatement();
	       String query1 = "INSERT INTO patients " + "VALUES (1, " + firstName + ", " + lastName + ", " + dob + ", " + sex + ", " + race + ", " + ethnicity + ")";;
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
	       System.out.println("Please check it in the MySQL Table......... ……..");
	    }
}

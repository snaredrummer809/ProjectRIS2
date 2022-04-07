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


public class DoctorController implements Initializable{
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
	TableView<ModelTable> allOrdersTable;
	@FXML
	TableColumn<ModelTable, Integer> allOrdersOrderIDCol;
	@FXML
	TableColumn<ModelTable, String> allOrdersPatientCol;
	@FXML
	TableColumn<ModelTable, String> allOrdersModalityCol;
	@FXML
	TableColumn<ModelTable, String> allOrdersReferralDocCol;
	@FXML
	TableColumn<ModelTable, String> allOrdersNotesCol;
	@FXML
	TableColumn<ModelTable, String> allOrdersStatusCol;
	@FXML
	TableColumn<ModelTable, String> allOrdersDeleteCol;
	ObservableList<ModelTable> orders = FXCollections.observableArrayList();
	
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
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		populateOrders();

	}
	
	public void populateOrders() {
		orders.clear();
		int orderID = 0;
		String patientName = null;
		String docName = null;
		String modalityName = null;
		String notes = null;
		String statusName = null;
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from orders");

			while (rs.next()) {
				orderID = rs.getInt("order_id");
				patientName = rs.getString("patient");
				docName = rs.getString("referral_md");
				notes = rs.getString("notes");
				modalityName = rs.getString("modality");
				statusName = rs.getString("status");
				
				orders.add(new ModelTable(orderID, 0, 0, patientName, docName, modalityName, notes, statusName, null));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		allOrdersOrderIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
		allOrdersPatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		allOrdersReferralDocCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		allOrdersModalityCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		allOrdersNotesCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
		allOrdersStatusCol.setCellValueFactory(new PropertyValueFactory<>("s5"));
		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

			};

			return cell;
		};
		allOrdersDeleteCol.setCellFactory(cellFactory);

		allOrdersTable.setItems(orders);
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
	       System.out.println("Connection is created successfully:");
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
	       newOrderButton(event);
	      }

}

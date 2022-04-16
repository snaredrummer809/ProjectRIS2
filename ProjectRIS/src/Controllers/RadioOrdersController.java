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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class RadioOrdersController implements Initializable {

	
	
	//Nav buttons
	
	@FXML
	Button HomeButton;
	@FXML
	Button AppointmentButton;
	@FXML
	Button OrderButton;
	@FXML
	Button LogOut;
	
	
	// Orders Pane
	@FXML
	TableView<ModelTable> allOrdersTable;
	@FXML
	TableColumn<ModelTable, String> allOrdersPatientCol;
	@FXML
	TableColumn<ModelTable, String> allOrdersModalityCol;
	@FXML
	TableColumn<ModelTable, String> allOrdersNotesCol;
	@FXML
	TableColumn<ModelTable, String> allOrdersStatusCol;
	ObservableList<ModelTable> orders = FXCollections.observableArrayList();
	
	
	
	//orders
	@FXML
	private TextField OrderPatientName;
	@FXML
	private TextField OrderReferralMD;
	@FXML
	private TextField OrderModalityNeeded;
	@FXML
	private TextArea OrderNotes;
	
	
	//orderDeleteConfirmationPane
			@FXML
			Pane allOrdersDeleteConfirmationPane;
			@FXML
			TextField allOrdersIDTextField;
			@FXML
			Button allOrdersConfirmDeleteButton;
	
	//Alerts
	Alert errorAlert = new Alert(AlertType.ERROR);
	Alert updateAlert = new Alert(AlertType.CONFIRMATION);
	
public void initialize(URL arg0, ResourceBundle arg1) {
		
		populateOrders();

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
	
	
	
	
	
	
	
	
	
	
	
	@SuppressWarnings("unused")
	public void populateOrders() {
		orders.clear();
		int orderID = 0;
		int patient_id = 0;
		int doc_id=0;
		int status = 0;
		int modality = 0;
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
				patient_id = rs.getInt("patient");
				doc_id = rs.getInt("referral_md");
				notes = rs.getString("notes");
				modality = rs.getInt("modality");
				status = rs.getInt("status");
				//getting values we need to populate table with textual info
				
				
				//get patient name
				try {
					Connection con1 = DatabaseConnection.getConnection();
					ResultSet rs1 = con1.createStatement().executeQuery("select * from patients where patient_id=" + patient_id);
					
					while(rs1.next()) {
						String firstName = rs1.getString("first_name");
						String lastName = rs1.getString("last_name");
						patientName = firstName +" "+ lastName;
						
					}
					con1.close();
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
				
				
				//get referral doc
				try {
					Connection con2 = DatabaseConnection.getConnection();
					ResultSet rs2 = con2.createStatement().executeQuery("select * from users where user_id=" + doc_id);
					
					while(rs2.next()) {
						docName = rs2.getString("full_name");						
					}
					con2.close();
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
				
				//get modality name
				try {
					Connection con3 = DatabaseConnection.getConnection();
					ResultSet rs3 = con3.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
					
					while(rs3.next()) {
						modalityName = rs3.getString("name");						
					}
					con3.close();
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
				
				//get status name
				//get patient name
				try {
					Connection con4 = DatabaseConnection.getConnection();
					ResultSet rs4 = con4.createStatement().executeQuery("select * from order_status where name=" + status);
					
					while(rs4.next()) {
						statusName = rs4.getString("name");
						
						
					}
					con4.close();
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
				
				orders.add(new ModelTable(orderID, 0, 0, patientName, docName, modalityName, notes, statusName, null));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		//allOrdersOrderIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
		allOrdersPatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		//allOrdersReferralDocCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		allOrdersModalityCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		allOrdersNotesCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
		allOrdersStatusCol.setCellValueFactory(new PropertyValueFactory<>("s5"));
		
	

		allOrdersTable.setItems(orders);
	}
	
	}

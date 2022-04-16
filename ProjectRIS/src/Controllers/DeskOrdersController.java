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

public class DeskOrdersController implements Initializable{
		
	// Nav buttons
	@FXML
	Button HomeButton;
	@FXML
	Button AppointmentButton;
	@FXML
	Button LogOut;
	
	// Appointment Pane
	@FXML
	TextField searchOrdersTextField;
	@FXML
	TableView<ModelTable> deskAllOrdersTable;
	@FXML
	TableColumn<ModelTable, Integer> deskAllOrdersOrderIDCol;
	@FXML
	TableColumn<ModelTable, String> deskAllOrdersPatientCol;
	@FXML
	TableColumn<ModelTable, String> deskAllOrdersModalityCol;
	@FXML
	TableColumn<ModelTable, String> deskAllOrdersReferralDocCol;
	@FXML
	TableColumn<ModelTable, String> deskAllOrdersNotesCol;
	@FXML
	TableColumn<ModelTable, String> deskAllOrdersStatusCol;
	@FXML
	TableColumn<ModelTable, String> deskAllOrdersDeleteCol;
	ObservableList<ModelTable> orders = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchOrders = FXCollections.observableArrayList();
	
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

	public void AppointmentButton(ActionEvent event) throws IOException {
		Main m = new Main();
		m.changeScene("../Views/DeskAppointments.fxml");
	}
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		populateOrders();

	}
	
	public void populateOrders() {
		orders.clear();
		int orderID = 0;
		int patient = 0;
		int doc = 0;
		int modality = 0;
		int status = 0;
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
				patient = rs.getInt("patient");
				doc = rs.getInt("referral_md");
				notes = rs.getString("notes");
				modality = rs.getInt("modality");
				status = rs.getInt("status");
				ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
				while(rs2.next()) {
					patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
				}
				rs2 = con.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
				while(rs2.next()) {
					modalityName = rs2.getString("name");
				}
				rs2 = con.createStatement().executeQuery("select * from users where user_id=" + doc);
				while(rs2.next()) {
					docName = rs2.getString("full_name");
				}
				rs2 = con.createStatement().executeQuery("select * from order_status where order_status_id=" + status);
				while(rs2.next()) {
					statusName = rs2.getString("name");
				}
				orders.add(new ModelTable(orderID, 0, 0, patientName, docName
						, modalityName, notes, 
						statusName, null));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		deskAllOrdersOrderIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
		deskAllOrdersPatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		deskAllOrdersReferralDocCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		deskAllOrdersModalityCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		deskAllOrdersNotesCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
		deskAllOrdersStatusCol.setCellValueFactory(new PropertyValueFactory<>("s5"));

		deskAllOrdersTable.setItems(orders);
	}
	
	public void searchOrders() {
		searchOrders.clear();
		String userSearch = searchOrdersTextField.getText();
		if(!userSearch.equals("")) {
			for(int i = 0; i < orders.size(); i++) {
				if(orders.get(i).getS1().contains(userSearch)) {
					searchOrders.add(orders.get(i));
				}
			}
			deskAllOrdersTable.setItems(searchOrders);
		}
		else {
			populateOrders();
		}
	}
	
	public void cancelButton(ActionEvent event) throws IOException {
		HomeButton.setDisable(false);
		AppointmentButton.setDisable(false);
		LogOut.setDisable(false);
		
	}

}

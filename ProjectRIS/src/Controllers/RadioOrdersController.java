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
		int patient = 0;
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
				patientName = rs.getString("patient");
				docName = rs.getString("referral_md");
				notes = rs.getString("notes");
				modalityName = rs.getString("modality");
				statusName = rs.getString("status");
				
				orders.add(new ModelTable(0,0,0, patientName, modalityName, notes, statusName, null, null));
			}
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		//allOrdersOrderIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
		allOrdersPatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		//allOrdersReferralDocCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		allOrdersModalityCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		allOrdersNotesCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		allOrdersStatusCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
		
	

		allOrdersTable.setItems(orders);
	}
	
	}

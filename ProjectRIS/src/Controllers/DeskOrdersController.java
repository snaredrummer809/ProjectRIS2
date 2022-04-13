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
	
	//appDeleteConfirmationPane
	@FXML
	Pane allOrdersDeleteConfirmationPane;
	@FXML
	TextField allOrdersIDTextField;
	@FXML
	Button allOrdersConfirmDeleteButton;
	
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
				//System.out.println(orderID);
				patient = rs.getInt("patient");
				//System.out.println(patient);
				doc = rs.getInt("referral_md");
				//System.out.println(doc);
				notes = rs.getString("notes");
				//System.out.println(notes);
				modality = rs.getInt("modality");
				//System.out.println(modality);
				status = rs.getInt("status");
				//System.out.println(status);
				ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
				while(rs2.next()) {
					patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
				}
				//System.out.println(patientName);
				rs2 = con.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
				while(rs2.next()) {
					modalityName = rs2.getString("name");
				}
				//System.out.println(modalityName);
				rs2 = con.createStatement().executeQuery("select * from users where user_id=" + doc);
				while(rs2.next()) {
					docName = rs2.getString("full_name");
				}
				//System.out.println(docName);
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
			System.out.println("Error: Could not get order data.");
		}
		
		deskAllOrdersOrderIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
		deskAllOrdersPatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		deskAllOrdersReferralDocCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		deskAllOrdersModalityCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		deskAllOrdersNotesCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
		deskAllOrdersStatusCol.setCellValueFactory(new PropertyValueFactory<>("s5"));
		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
					} else {
						final Button modButton = new Button("Delete");
						modButton.setOnAction(event -> {
							ModelTable m = getTableView().getItems().get(getIndex());

							allOrdersDeleteConfirmationPane.setVisible(true);
							HomeButton.setDisable(true);
							AppointmentButton.setDisable(true);
							LogOut.setDisable(true);
							
							//System.out.println(m.getNum1());
							allOrdersIDTextField.setText(m.getNum1()+"");

						});

						setGraphic(modButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		// allOrdersDeleteCol.setCellFactory(cellFactory); 

		deskAllOrdersTable.setItems(orders);
	}
	
	public void allOrdersConfirmDelete(ActionEvent event) throws IOException {
		
		try {
			Connection con = DatabaseConnection.getConnection();
			Statement stmt = con.createStatement();
			
			String deleteApp = "delete from orders where appointment_id=" + allOrdersIDTextField.getText();
			stmt.executeUpdate(deleteApp);
			
			con.close();
			
			allOrdersDeleteConfirmationPane.setVisible(false);
			HomeButton.setDisable(false);
			AppointmentButton.setDisable(false);
			LogOut.setDisable(false);
			updateAlert.setHeaderText("Success");
			updateAlert.setContentText("Order has been successfully deleted.");
			updateAlert.showAndWait();
			populateOrders();
		}
		catch(SQLException e) {
			System.out.println("Error: Could not delete order.");
		}
	}
	
	public void cancelButton(ActionEvent event) throws IOException {
		HomeButton.setDisable(false);
		AppointmentButton.setDisable(false);
		LogOut.setDisable(false);
		allOrdersDeleteConfirmationPane.setVisible(false);
		
	}

}

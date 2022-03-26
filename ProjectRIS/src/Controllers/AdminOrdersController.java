package Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import application.DatabaseConnection;
import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;

public class AdminOrdersController implements Initializable{
	
	// Nav buttons
		@FXML
		Button HomeButton;
		@FXML
		Button AdminButton;
		@FXML
		Button AppointmentButton;
		@FXML
		Button InvoiceButton;
		@FXML
		Button ReferralsButton;
		@FXML
		Button LogOut;
		
		// Appointment Pane
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
		
		//appDeleteConfirmationPane
		@FXML
		Pane allOrdersDeleteConfirmationPane;
		@FXML
		TextField allOrdersIDTextField;
		@FXML
		Button allOrdersConfirmDeleteButton;
		
		//Alerts
		Alert errorAlert = new Alert(AlertType.ERROR);
		Alert updateAlert = new Alert(AlertType.CONFIRMATION);
		
		public void userLogOut(ActionEvent event) throws IOException {

			Main m = new Main();

			m.changeScene("../Views/Login.fxml");
		}

		public void HomeButton(ActionEvent event) throws IOException {

			Main m = new Main();
			m.changeScene("../Views/Admin.fxml");
		}

		public void AdminButton(ActionEvent event) throws IOException {
			Main m = new Main();
			m.changeScene("../Views/AdminAdmin.fxml");
		}
		
		public void AppointmentButton(ActionEvent event) throws IOException {
			Main m = new Main();
			m.changeScene("../Views/AdminAppointments.fxml");
		}

		public void InvoiceButton(ActionEvent event) throws IOException {

			Main m = new Main();
			m.changeScene("../Views/AdminInvoice.fxml");
		}

		public void ReferralsButton(ActionEvent event) throws IOException {

			Main m = new Main();
			m.changeScene("../Views/AdminReferrals.fxml");
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
					System.out.println(orderID);
					patient = rs.getInt("patient");
					System.out.println(patient);
					doc = rs.getInt("referral_md");
					System.out.println(doc);
					notes = rs.getString("notes");
					System.out.println(notes);
					modality = rs.getInt("modality");
					System.out.println(modality);
					status = rs.getInt("status");
					System.out.println(status);
					ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
					while(rs2.next()) {
						patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
					}
					System.out.println(patientName);
					rs2 = con.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
					while(rs2.next()) {
						modalityName = rs2.getString("name");
					}
					System.out.println(modalityName);
					rs2 = con.createStatement().executeQuery("select * from users where user_id=" + doc);
					while(rs2.next()) {
						docName = rs2.getString("full_name");
					}
					System.out.println(docName);
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
			
			allOrdersOrderIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
			allOrdersPatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
			allOrdersReferralDocCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
			allOrdersModalityCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
			allOrdersNotesCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
			allOrdersStatusCol.setCellValueFactory(new PropertyValueFactory<>("s5"));
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
								AdminButton.setDisable(true);
								AppointmentButton.setDisable(true);
								InvoiceButton.setDisable(true);
								ReferralsButton.setDisable(true);
								LogOut.setDisable(true);
								
								System.out.println(m.getNum1());
								allOrdersIDTextField.setText(m.getNum1()+"");

							});

							setGraphic(modButton);
							setText(null);
						}
					}
				};

				return cell;
			};
			allOrdersDeleteCol.setCellFactory(cellFactory);

			allOrdersTable.setItems(orders);
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
				AdminButton.setDisable(false);
				AppointmentButton.setDisable(false);
				InvoiceButton.setDisable(false);
				ReferralsButton.setDisable(false);
				LogOut.setDisable(false);
				updateAlert.setHeaderText("Success");
				updateAlert.setContentText("Order has been successfully deleted.");
				updateAlert.showAndWait();
				populateOrders();
			}
			catch(SQLException e) {
				errorAlert.setHeaderText("Error");
				errorAlert.setContentText("Unable to delete order.");
				errorAlert.showAndWait();
			}
		}
		
		public void cancelButton(ActionEvent event) throws IOException {
			HomeButton.setDisable(false);
			AdminButton.setDisable(false);
			AppointmentButton.setDisable(false);
			InvoiceButton.setDisable(false);
			ReferralsButton.setDisable(false);
			LogOut.setDisable(false);
			allOrdersDeleteConfirmationPane.setVisible(false);
			
		}

}

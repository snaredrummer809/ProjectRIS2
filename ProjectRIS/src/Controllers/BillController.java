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

public class BillController implements Initializable{
	
	// Nav buttons
		@FXML
		Button LogOut;
		
		// Appointment Pane
		@FXML
		TextField searchCompletedAppsTextField;
		@FXML
		TableView<ModelTable> completedAppsTable;
		@FXML
		TableColumn<ModelTable, String> appPatientNameColumn;
		@FXML
		TableColumn<ModelTable, String> appModalityColumn;
		@FXML
		TableColumn<ModelTable, String> appDateAndTimeColumn;
		@FXML
		TableColumn<ModelTable, String> appTechNameColumn;
		@FXML
		TableColumn<ModelTable, String> appRadiologistColumn;
		@FXML
		TableColumn<ModelTable, String> appDeleteColumn;
		ObservableList<ModelTable> appointments = FXCollections.observableArrayList();
		ObservableList<ModelTable> searchApps = FXCollections.observableArrayList();
		
		// createInvoicePane
		@FXML
		Pane createInvoicePane;
		@FXML
		TextField appointmentIDTextField;
		@FXML
		TextField costTextField;
		
		// invoicePane
		@FXML
		Pane invoicePane;
		@FXML
		TextField searchInvoicesTextField;
		@FXML
		TableView<ModelTable> invoiceTable;
		@FXML
		TableColumn<ModelTable, Integer> invoiceIDCol;
		@FXML
		TableColumn<ModelTable, Integer> invoiceAppIDCol;
		@FXML
		TableColumn<ModelTable, String> invoicePatientCol;
		@FXML
		TableColumn<ModelTable, String> invoiceCostCol;
		ObservableList<ModelTable> invoices = FXCollections.observableArrayList();
		ObservableList<ModelTable> searchInvoices = FXCollections.observableArrayList();
		
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
			m.changeScene("../Views/AdminAppointment.fxml");
		}

		public void OrderButton(ActionEvent event) throws IOException {
			Main m = new Main();
			m.changeScene("../Views/AdminOrders.fxml");
		}

		public void ReferralsButton(ActionEvent event) throws IOException {
			Main m = new Main();
			m.changeScene("../Views/AdminReferrals.fxml");
		}
		
		public void initialize(URL arg0, ResourceBundle arg1) {
		
			errorAlert.setHeaderText("Error");
			populateAppointments();
			populateInvoices();

		}
		
		public void populateAppointments() {
			appointments.clear();
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
				ResultSet rs = con.createStatement().executeQuery("select * from appointments where closed=0");

				while (rs.next()) {
						
					patient = rs.getInt("patient");
					modality = rs.getInt("modality");
					tech = rs.getInt("technician");
					radio = rs.getInt("radiologist");
					ResultSet rs3 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
					while(rs3.next()) {
						patientName = rs3.getString("first_name") + " " + rs3.getString("last_name");
					}
					rs3 = con.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
					while(rs3.next()) {
						modalityName = rs3.getString("name");
					}
					rs3 = con.createStatement().executeQuery("select * from users where user_id=" + tech);
					while(rs3.next()) {
						techName = rs3.getString("full_name");
					}
					rs3 = con.createStatement().executeQuery("select * from users where user_id=" + radio);
					while(rs3.next()) {
						radioName = rs3.getString("full_name");
					}
					
					appointments.add(new ModelTable(patient, rs.getInt("appointment_id"), 0, patientName,
							modalityName, rs.getString("date_time"), techName, 
							radioName, null));
							
				}
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
			
			appPatientNameColumn.setCellValueFactory(new PropertyValueFactory<>("s1"));
			appModalityColumn.setCellValueFactory(new PropertyValueFactory<>("s2"));
			appDateAndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("s3"));
			appTechNameColumn.setCellValueFactory(new PropertyValueFactory<>("s4"));
			appRadiologistColumn.setCellValueFactory(new PropertyValueFactory<>("s5"));
			Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

				final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);

						if (empty) {
							setText(null);
							setGraphic(null);
						} else {
							final Button invoiceButton = new Button("Create Invoice");
							invoiceButton.setOnAction(event -> {
								ModelTable m = getTableView().getItems().get(getIndex());

								createInvoicePane.setVisible(true);
								LogOut.setDisable(true);
								
								appointmentIDTextField.setText(m.getNum2()+"");

							});

							setGraphic(invoiceButton);
							setText(null);
						}
					}
				};

				return cell;
			};
			appDeleteColumn.setCellFactory(cellFactory);

			completedAppsTable.setItems(appointments);
		}
		
		public void populateInvoices() {
			invoices.clear();
			int ID = 0;
			int app = 0;
			int patient = 0;
			String patientName = null;
			String cost = null;
			
			try {
				Connection con = DatabaseConnection.getConnection();
				ResultSet rs = con.createStatement().executeQuery("select * from invoices");
				
				while(rs.next()) {
					ID = rs.getInt("invoice_number");
					app = rs.getInt("appointment");
					cost = rs.getString("cost");
					
					ResultSet rs2 = con.createStatement().executeQuery("select * from appointments where appointment_id="
							+ app);
					while(rs2.next()) {
						patient = rs2.getInt("patient");
					}
					
					rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
					while(rs2.next()) {
						patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
					}
					
					invoices.add(new ModelTable(ID, app, 0, patientName, cost, null, null, null, null));
					
				}
				
				invoiceIDCol.setCellValueFactory(new PropertyValueFactory<>("num1"));
				invoiceAppIDCol.setCellValueFactory(new PropertyValueFactory<>("num2"));
				invoicePatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
				invoiceCostCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
				
				invoiceTable.setItems(invoices);
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		
		public void searchApps() {
			searchApps.clear();
			String userSearch = searchCompletedAppsTextField.getText();
			if(!userSearch.equals("")) {
				for(int i = 0; i < appointments.size(); i++) {
					if(appointments.get(i).getS1().contains(userSearch)) {
						searchApps.add(appointments.get(i));
					}
				}
				completedAppsTable.setItems(searchApps);
			}
			else {
				populateAppointments();
			}
		}
		
		public void searchInvoices() {
			searchInvoices.clear();
			String userSearch = searchInvoicesTextField.getText();
			if(!userSearch.equals("")) {
				for(int i = 0; i < invoices.size(); i++) {
					if(invoices.get(i).getS1().contains(userSearch)) {
						searchInvoices.add(invoices.get(i));
					}
				}
				invoiceTable.setItems(searchInvoices);
			}
			else {
				populateInvoices();
			}
		}
		
		public void submitInvoice(ActionEvent event) throws IOException {
				
			int app = 0;
			int invoice = 0;
			double cost = 0.0;
			
			app = Integer.parseInt(appointmentIDTextField.getText());
			
			try {
				cost = Double.parseDouble(costTextField.getText());
			}
			catch(Exception e) {
				errorAlert.setContentText("Invalid input for cost. (ex. X.XX)");
				errorAlert.showAndWait();
			}
			
			try {
				Connection con = DatabaseConnection.getConnection();
				ResultSet rs = con.createStatement().executeQuery("select * from invoices");
				
				while(rs.next()) {
					invoice = rs.getInt("invoice_number");
				}
				invoice = invoice + 1;
				
				con.close();
			}
			catch(Exception e) {
				invoice = 1;
			}
			
			String updateInvoices = "insert into invoices(invoice_number, appointment, cost) "
					+ " values(\'" + invoice + "\', \'" + app + "\', \'" + cost + "\')";
			
			try {
				Connection con = DatabaseConnection.getConnection();
				Statement stmt = con.createStatement();
				
				stmt.executeUpdate(updateInvoices);
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			
			String updateAppointments = "update appointments set closed = 1 where appointment_id=" 
					+ app;
			try {
				Connection con = DatabaseConnection.getConnection();
				Statement stmt = con.createStatement();
				
				stmt.executeUpdate(updateAppointments);
			}
			catch(SQLException e) {
				System.out.println("Error: Unable to update appoinments.");
			}
			
			String updateOrders = "update orders set status=3 where appointment=" 
					+ app;
			try {
				Connection con = DatabaseConnection.getConnection();
				Statement stmt = con.createStatement();
				
				stmt.executeUpdate(updateOrders);
			}
			catch(SQLException e) {
				System.out.println("Error: Unable to update orders");
			}
			
			LogOut.setDisable(false);
			createInvoicePane.setVisible(false);
			appointmentIDTextField.setText("");
			costTextField.setText("");
			populateAppointments();
		}
		
		public void cancelButton(ActionEvent event) throws IOException {
			LogOut.setDisable(false);
			createInvoicePane.setVisible(false);
			appointmentIDTextField.setText("");
			costTextField.setText("");
			
		}
}

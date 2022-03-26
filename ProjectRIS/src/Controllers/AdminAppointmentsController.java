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

public class AdminAppointmentsController implements Initializable {
	
	// Nav buttons
	@FXML
	Button HomeButton;
	@FXML
	Button AdminButton;
	@FXML
	Button InvoiceButton;
	@FXML
	Button OrdersButton;
	@FXML
	Button ReferralsButton;
	@FXML
	Button LogOut;
	
	// Appointment Pane
	@FXML
	TableView<ModelTable> appointmentsTable;
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
	
	//appDeleteConfirmationPane
	@FXML
	Pane appDeleteConfirmationPane;
	@FXML
	TextField appIDTextField;
	@FXML
	Button appConfirmDeleteButton;
	
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

	public void InvoiceButton(ActionEvent event) throws IOException {

		Main m = new Main();
		m.changeScene("../Views/AdminInvoice.fxml");
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
		
		populateAppointments();

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
			ResultSet rs = con.createStatement().executeQuery("select * from appointments");

			while (rs.next()) {
				patient = rs.getInt("patient");
				System.out.println(patient);
				modality = rs.getInt("modality");
				System.out.println(modality);
				tech = rs.getInt("technician");
				System.out.println(tech);
				radio = rs.getInt("radiologist");
				System.out.println(radio);
				ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
				while(rs2.next()) {
					patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
				}
				rs2 = con.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
				while(rs2.next()) {
					modalityName = rs2.getString("name");
				}
				rs2 = con.createStatement().executeQuery("select * from users where user_id=" + tech);
				while(rs2.next()) {
					techName = rs2.getString("full_name");
				}
				rs2 = con.createStatement().executeQuery("select * from users where user_id=" + radio);
				while(rs2.next()) {
					radioName = rs2.getString("full_name");
				}				
				
				appointments.add(new ModelTable(patient, 0, 0, patientName,
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
					} else {
						final Button modButton = new Button("Delete");
						modButton.setOnAction(event -> {
							ModelTable m = getTableView().getItems().get(getIndex());

							appDeleteConfirmationPane.setVisible(true);
							HomeButton.setDisable(true);
							AdminButton.setDisable(true);
							InvoiceButton.setDisable(true);
							OrdersButton.setDisable(true);
							ReferralsButton.setDisable(true);
							LogOut.setDisable(true);
							
							System.out.println(m.getNum1());
							appIDTextField.setText(m.getNum1()+"");

						});

						setGraphic(modButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		appDeleteColumn.setCellFactory(cellFactory);

		appointmentsTable.setItems(appointments);
	}
	
	public void appConfirmDelete(ActionEvent event) throws IOException {
		try {
			Connection con = DatabaseConnection.getConnection();
			Statement stmt = con.createStatement();
			String deleteApp = "delete from appointments where appointment_id=" + appIDTextField.getText();
			
			stmt.executeUpdate(deleteApp);
			con.close();
			appDeleteConfirmationPane.setVisible(false);
			HomeButton.setDisable(false);
			AdminButton.setDisable(false);
			InvoiceButton.setDisable(false);
			OrdersButton.setDisable(false);
			ReferralsButton.setDisable(false);
			LogOut.setDisable(false);
			updateAlert.setHeaderText("Success");
			updateAlert.setContentText("Appointment has been successfully deleted.");
			updateAlert.showAndWait();
			populateAppointments();
		}
		catch(SQLException e) {
			errorAlert.setHeaderText("Error");
			errorAlert.setContentText("Unable to delete appointment.");
			errorAlert.showAndWait();
		}
	}
	
	public void cancelButton(ActionEvent event) throws IOException {
		HomeButton.setDisable(false);
		AdminButton.setDisable(false);
		InvoiceButton.setDisable(false);
		OrdersButton.setDisable(false);
		ReferralsButton.setDisable(false);
		LogOut.setDisable(false);
		appDeleteConfirmationPane.setVisible(false);
		
	}
}

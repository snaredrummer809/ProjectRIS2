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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.scene.layout.Pane;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.control.TextField;

public class TechController implements Initializable{

	//fxml elements
	//Checked-in appointments pane
	@FXML TableView<ModelTable> CIApptTable;
	@FXML TableColumn<ModelTable, String> CIApptPatientColumn;
	@FXML TableColumn<ModelTable, String> CIApptModalityColumn;
	@FXML TableColumn<ModelTable, String> CIApptDateAndTimeColumn;
	@FXML TableColumn<ModelTable, String> CIApptRadiologistColumn;
	@FXML TableColumn<ModelTable, String> CIApptTechnicianColumn;
	ObservableList<ModelTable> CIappointments = FXCollections.observableArrayList();
	@FXML TableColumn<ModelTable, String> CompleteOrderColumn;
	

	//complete order form elements
	@FXML Pane ImagingOrderPane;
	@FXML Button PatientOverviewButton;
	@FXML Button CompOrderButton;
	@FXML Button backButton;
	@FXML Button chooseFileButton;	
	@FXML Line PatientInfoLine;
	@FXML Line OrderInfoLine;
	@FXML Line ImagingLine;
	@FXML Label firstNameLabel;
	@FXML Label ImagingLabel;
	@FXML Label OrderInfoLabel;
	@FXML Label PatientInfoLabel;
	@FXML Label ImagingOrderFormLabel;
	@FXML Label LastNameLabel;
	@FXML Label modalityLabel;
	@FXML Label MDNotesLabel;
	@FXML TextField LastNameTextField;
	@FXML TextField DOBTextField;
	@FXML TextField FirstNameTextField;
	@FXML TextField ModalityTextField;
	@FXML TextArea MDNotesTextArea;


	
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
			
			CIApptPatientColumn.setCellValueFactory(new PropertyValueFactory<>("s1"));
			CIApptModalityColumn.setCellValueFactory(new PropertyValueFactory<>("s2"));
			CIApptDateAndTimeColumn.setCellValueFactory(new PropertyValueFactory<>("s3"));
			CIApptRadiologistColumn.setCellValueFactory(new PropertyValueFactory<>("s4"));
			CIApptTechnicianColumn.setCellValueFactory(new PropertyValueFactory<>("s5"));
			CompleteOrderColumn.setCellValueFactory(new PropertyValueFactory<>(""));
			
			//3/30
			
			Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

				final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

					@Override
					public void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);

						if (empty) {
							setText(null);
						} else {
							final Button CompleteImagingOrderButton = new Button("Complete Order");
							CompleteImagingOrderButton.setOnAction(event -> {
								ModelTable m = getTableView().getItems().get(getIndex());
								

								ImagingOrderPane.setVisible(true);
								/*modPatientPane.setVisible(true);
								systemUsersPane.setDisable(true);
								modalitiesPane.setDisable(true);
								patientAlertsPane.setDisable(true);
								appointmentsPane.setDisable(true);
								ordersPane.setDisable(true);
								fileUploadsPane.setDisable(true);
								diagnosticReportsPane.setDisable(true);
								HomeButton.setDisable(true);
								AppointmentButton.setDisable(true);
								InvoiceButton.setDisable(true);
								OrdersButton.setDisable(true);
								ReferralsButton.setDisable(true);
								LogOut.setDisable(true);

								modPatientFirstName.setText("" + m.getFirstName());
								modPatientLastName.setText("" + m.getLastName());
								modPatientRace.setText("" + m.getRace());
								modPatientEthnicity.setText("" + m.getEthnicity());
								modPatientIDTextField.setText(""+ m.getID());
								modPatientRace.setText("");
								modPatientEthnicity.setText("");
								*/
								System.out.println("compelte button works......");
								
							});

							setGraphic(CompleteImagingOrderButton);
							setText(null);
						}
					}
				};

				return cell;
			};
			CompleteOrderColumn.setCellFactory(cellFactory);
			
			//end 3/30
			
			CIApptTable.setItems(CIappointments);
		}
	
	public void userLogOut(ActionEvent event) throws IOException {
		
		Main m = new Main();
		
		m.changeScene("../Views/Login.fxml");
	}
	
	public void HomeButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/Tech.fxml");
	}
	
	public void AppointmentButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/TechAppointments.fxml");
	}
}

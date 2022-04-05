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
	@FXML TableColumn<ModelTable, Integer> CIApptID;
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
	@FXML TextField apptIDTextField;
	

	//patient Overview pane
	@FXML Pane patientOverviewPane;
	@FXML TextField firstNameField;
	@FXML TextField lastNameField;
	@FXML TextField DOBField;
	@FXML TextField sexField;
	@FXML TextField raceField;
	@FXML TextField ethnicityField;
	@FXML TextArea allergiesTextArea;
	@FXML Button closeButton;
	@FXML TextField phoneTextField;
	@FXML TextField emailTextField;
	
	

	
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
			int apptID = 0;
			String patientName = null;
			String modalityName = null;
			String techName = null;
			String radioName = null;
			try {
				Connection con = DatabaseConnection.getConnection();
				ResultSet rs = con.createStatement().executeQuery("select * from appointments where checked_in=1 and closed=0");

				while (rs.next()) {
					apptID = rs.getInt("appointment_id");
					//System.out.println("Appt ID: "+apptID);
					patient = rs.getInt("patient");
					//System.out.println("patient ID: "+patient);
					modality = rs.getInt("modality");
					//System.out.println("mod: "+modality);
					radio = rs.getInt("radiologist");
					//System.out.println("radio: "+radio);
					tech = rs.getInt("technician");
					//System.out.println("tech: "+tech);
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
			CIApptID.setCellValueFactory(new PropertyValueFactory<>("num1"));
			CompleteOrderColumn.setCellValueFactory(new PropertyValueFactory<>(""));
			
			//complete order button column
			
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
								//may need to disable logout & other button while this pane is open!!!!!!!!!!!!!!!!!!!!!!!!!!
								/*modPatientPane.setVisible(true);
								systemUsersPane.setDisable(true);
								modalitiesPane.setDisable(true);
								patientAlertsPane.setDisable(true);
								appointmentsPane.setDisable(true);
								ordersPane.setDisable(true);
								fileUploadsPane.setDisable(true);
								diagnosticReportsPane.setDisable(true);
								HomeButton.setDisable(true);
								AppointmentButton.setDisable(true);!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
								InvoiceButton.setDisable(true);
								OrdersButton.setDisable(true);
								ReferralsButton.setDisable(true);
								LogOut.setDisable(true);

								*//*
								String patientName = m.getS1();
								String[] name = patientName.split(" ");
								
								String firstName= name[0];
								String lastName = name[1];
								//System.out.println(name[0]);
								//System.out.println(name[1]);
								
								FirstNameTextField.setText(firstName);
								LastNameTextField.setText(lastName);
								firstNameField.setText(firstName);
								lastNameField.setText(lastName);
								
								modPatientFirstName.setText("" + m.getFirstName());
								modPatientLastName.setText("" + m.getLastName());
								modPatientRace.setText("" + m.getRace());
								modPatientEthnicity.setText("" + m.getEthnicity());
								modPatientIDTextField.setText(""+ m.getID());
								modPatientRace.setText("");
								modPatientEthnicity.setText("");
								*/
								int patient = 0;
								int modality = 0;
								int tech = 0;
								int radio = 0;
								int apptID =0;
								String patientName = null;
								String modalityName = null;
								String techName = null;
								String radioName = null;
								
								//stuff i need from patients table for imaging order pane
								String firstName = null;
								String lastName = null;
								String DOB = null;
								
								//stuff i need from orders table for imaging order pane
								//modalityName declared above
								String MDNotes = null;
								
								
								
								try {
									Connection con = DatabaseConnection.getConnection();
									ResultSet rs = con.createStatement().executeQuery("select * from appointments where checked_in=1 and closed=0");

									while (rs.next()) {
										patient = rs.getInt("patient");
										//System.out.println("patient ID: "+patient);
										modality = rs.getInt("modality");
										//System.out.println("modality: "+modality);
										radio = rs.getInt("radiologist");
										//System.out.println("Radiologist: "+radio);
										tech = rs.getInt("technician");
										//System.out.println("Tech: "+tech);
										ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
										while(rs2.next()) {
											patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
											firstName = rs2.getString("first_name");
											lastName = rs2.getString("last_name");
											DOB = rs2.getString("dob");
											
																				
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
										
										rs2 = con.createStatement().executeQuery("select * from orders where patient=" + patient);
										while(rs2.next()) {
											MDNotes = rs2.getString("notes");
											
										}				
										
									}
								} catch (SQLException e) {
									e.printStackTrace();
								}
								
								try {
									Connection con = DatabaseConnection.getConnection();
									ResultSet rs = con.createStatement().executeQuery("select * from patients where patient_id="+patient+";");

									while (rs.next()) {
										patient = rs.getInt("patient_id");
										//System.out.println("patient ID: "+patient);
										lastName = rs.getString("last_name");
										//System.out.println("modality: "+modality);
										firstName = rs.getString("first_name");
										//System.out.println("Radiologist: "+radio);
										DOB = rs.getString("dob");
										//System.out.println("Tech: "+tech);
										/*ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
										while(rs2.next()) {
											patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
											firstName = rs2.getString("first_name");
											lastName = rs2.getString("last_name");
											DOB = rs2.getString("dob");
											
																				
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
										
										rs2 = con.createStatement().executeQuery("select * from orders where patient=" + patient);
										while(rs2.next()) {
											MDNotes = rs2.getString("notes");
											
										}				
										*/
									}
								} catch (SQLException e) {
									e.printStackTrace();
								}
								
								FirstNameTextField.setText(firstName);
								LastNameTextField.setText(lastName);
								firstNameField.setText(firstName);
								lastNameField.setText(lastName);
								DOBTextField.setText(DOB);	
								ModalityTextField.setText(modalityName);
								MDNotesTextArea.setText(MDNotes);
								
								//int apptID = Integer.parseInt(apptIDTextField.getText());
								
								/*try {
									Connection con = DatabaseConnection.getConnection();
									ResultSet rs = con.createStatement().executeQuery("select * from appointments where appointment_id="+apptID+";");
									System.out.println(rs);
									while (rs.next()) {
										patient = rs.getInt("patient");
										//System.out.println("patient ID: "+patient);
										modality = rs.getInt("modality");
										//System.out.println("modality: "+modality);
										radio = rs.getInt("radiologist");
										//System.out.println("Radiologist: "+radio);
										tech = rs.getInt("technician");
										//System.out.println("Tech: "+tech);
										ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
										while(rs2.next()) {
											patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
											firstName = rs2.getString("first_name");
											lastName = rs2.getString("last_name");
											DOB = rs2.getString("dob");
											
											FirstNameTextField.setText(firstName);
											LastNameTextField.setText(lastName);
											firstNameField.setText(firstName);
											lastNameField.setText(lastName);
											DOBTextField.setText(DOB);										
										}
										rs2 = con.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
										while(rs2.next()) {
											modalityName = rs2.getString("name");
											
											ModalityTextField.setText(modalityName);
										}
										rs2 = con.createStatement().executeQuery("select * from users where user_id=" + radio);
										while(rs2.next()) {
											techName = rs2.getString("full_name");
										}
										rs2 = con.createStatement().executeQuery("select * from users where user_id=" + tech);
										while(rs2.next()) {
											radioName = rs2.getString("full_name");
										}				
										
										rs2 = con.createStatement().executeQuery("select * from orders where patient=" + patient);
										while(rs2.next()) {
											MDNotes = rs2.getString("notes");
											MDNotesTextArea.setText(MDNotes);
										}				
										
									}
								} catch (SQLException e) {
									e.printStackTrace();
								}
								*/
								apptIDTextField.setText(Integer.toString(apptID));
								
								
							});

							setGraphic(CompleteImagingOrderButton);
							setText(null);
						}
					}
				};

				return cell;
			};
			CompleteOrderColumn.setCellFactory(cellFactory);
			
			CIApptTable.setItems(CIappointments);
		}
		
	//cancel order method
				public void backButton(ActionEvent event) throws IOException {
					ImagingOrderPane.setVisible(false);
					LastNameTextField.clear();
					DOBTextField.clear();
					FirstNameTextField.clear();
					ModalityTextField.clear();
				}
				
				
	//complete order method
				public void completeOrderButton(ActionEvent event) throws IOException {
					ImagingOrderPane.setVisible(false);
					//add method to upload image!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
					int patient = 0;
					int modality = 0;
					int tech = 0;
					int radio = 0;
					String patientName = null;
					String modalityName = null;
					String techName = null;
					String radioName = null;
					
					//stuff i need from patients table for imaging order pane
					String firstName = null;
					String lastName = null;
					String DOB = null;
					
					//stuff i need from orders table for imaging order pane
					//modalityName declared above
					String MDNotes = null;
					/*88888888
					int apptID = Integer.parseInt(apptIDTextField.getText());
					
					try {
						Connection con = DatabaseConnection.getConnection();
						ResultSet rs = con.createStatement().executeQuery("select * from appointments where appointment_id="+apptID+";");
						System.out.println(rs);
						while (rs.next()) {
							patient = rs.getInt("patient");
							//System.out.println("patient ID: "+patient);
							modality = rs.getInt("modality");
							//System.out.println("modality: "+modality);
							radio = rs.getInt("radiologist");
							//System.out.println("Radiologist: "+radio);
							tech = rs.getInt("technician");
							//System.out.println("Tech: "+tech);
							ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
							while(rs2.next()) {
								patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
								firstName = rs2.getString("first_name");
								lastName = rs2.getString("last_name");
								DOB = rs2.getString("dob");
								
								FirstNameTextField.setText(firstName);
								LastNameTextField.setText(lastName);
								firstNameField.setText(firstName);
								lastNameField.setText(lastName);
								DOBTextField.setText(DOB);										
							}
							rs2 = con.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
							while(rs2.next()) {
								modalityName = rs2.getString("name");
								
								ModalityTextField.setText(modalityName);
							}
							rs2 = con.createStatement().executeQuery("select * from users where user_id=" + radio);
							while(rs2.next()) {
								techName = rs2.getString("full_name");
							}
							rs2 = con.createStatement().executeQuery("select * from users where user_id=" + tech);
							while(rs2.next()) {
								radioName = rs2.getString("full_name");
							}				
							
							rs2 = con.createStatement().executeQuery("select * from orders where patient=" + patient);
							while(rs2.next()) {
								MDNotes = rs2.getString("notes");
								MDNotesTextArea.setText(MDNotes);
							}				
							
						}
					} catch (SQLException e) {
						e.printStackTrace();
					}
					
					
					*///88888888888888888888888888
					//LastNameTextField.clear();
					//DOBTextField.clear();
					//FirstNameTextField.clear();
					//ModalityTextField.clear();
				}
				
//patient overview method
				public void patientOverview()
				{
					patientOverviewPane.setVisible(true);
					ImagingOrderPane.setVisible(false);
					//fill out method to fill in fields
					//querying patients table for patient info
						

				}

	//patient overview close button
				public void closeOverview()
				{
					patientOverviewPane.setVisible(false);
					ImagingOrderPane.setVisible(true);
					firstNameField.clear();
					lastNameField.clear();
					DOBField.clear();
					sexField.clear();
					raceField.clear();
					ethnicityField.clear();
					allergiesTextArea.clear();
					phoneTextField.clear();
					emailTextField.clear();
					
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

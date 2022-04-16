package Controllers;

import java.awt.Desktop;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class TechController implements Initializable{
	
	// Nav buttons
	@FXML
	Button LogOut;
	
	// Checked In Apps Pane
	@FXML
	TableView<ModelTable> checkedInAppsTable;
	@FXML
	TableColumn<ModelTable, String> checkedInPatientCol;
	@FXML
	TableColumn<ModelTable, String> checkedInModalityCol;
	@FXML 
	TableColumn<ModelTable, String> checkedInDateAndTimeCol;
	@FXML 
	TableColumn<ModelTable, String> checkedInRadioCol;
	@FXML 
	TableColumn<ModelTable, String> checkedInTechCol;
	@FXML 
	TableColumn<ModelTable, String> checkedInCompleteOrderCol;
	ObservableList<ModelTable> checkedInApps = FXCollections.observableArrayList();


	// Imaging Order Pane
	@FXML
	Pane ImagingOrderPane;
	@FXML 
	TextField imagingFirstNameTextField;
	@FXML
	TextField imagingLastNameTextField;
	@FXML 
	TextField imagingModalityTextField;
	@FXML 
	TextArea imagingMDNotesTextArea;
	@FXML Button chooseFileButton;
	private FileChooser fileChooser;
	private File file;
	private Stage stage;
	private final Desktop deskTop = Desktop.getDesktop();
	@FXML private ImageView scanImageView;
	private Image image;
	private FileInputStream fis;

	// Patient Overview pane
	@FXML Pane patientOverviewPane;
	@FXML TextField overviewFirstNameTextField;
	@FXML TextField overviewLastNameTextField;
	@FXML TextField overviewDOBTextField;
	@FXML TextField overviewSexTextField;
	@FXML TextField overviewRaceTextField;
	@FXML TextField overviewEthnicityTextField;
	@FXML TextField overviewPhoneTextField;
	@FXML TextField overviewEmailTextField;
	@FXML TextArea overviewAllergiesTextArea;
	@FXML Button closeButton;
	ModelTable instance = new ModelTable();
	ModelTable m;
	

	

	public void initialize(URL arg0, ResourceBundle arg1) {
		populateCheckedInApps();
		
		fileChooser = new FileChooser();
		fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("All files", "*.*"),
					new FileChooser.ExtensionFilter("Images", "*.png", "*.jpg")
		);
	}
	
	// Populate checkedInAppsTable
	public void populateCheckedInApps() {
		checkedInApps.clear();
		int patient = 0;
		int modality = 0;
		int tech = 0;
		int radio = 0;
		int order = 0;
		int status = 0;
		int checkedIn = 0;
		String patientName = null;
		String modalityName = null;
		String techName = null;
		String radioName = null;
		String statusName = null;
		int closed = 0;
		
		// Get all appointment data
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from appointments");

			while (rs.next()) {
				patient = rs.getInt("patient");
				modality = rs.getInt("modality");
				tech = rs.getInt("technician");
				radio = rs.getInt("radiologist");
				order = rs.getInt("order_id");
				checkedIn = rs.getInt("checked_in");
				closed = rs.getInt("closed");
				
				
				// Check to make sure appointment has been checked in
				//added check to make sure appt is not closed
				if(checkedIn == 1  && closed==0) {
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
					rs2 = con.createStatement().executeQuery("select * from orders where order_id=" + order);
					while(rs2.next()) {
						status = rs2.getInt("status");
					}
					rs2 = con.createStatement().executeQuery("select * from order_status where order_status_id=" + status);
					while(rs2.next()) {
						statusName = rs2.getString("name");
					}
					
					checkedInApps.add(new ModelTable(patient, modality, order, patientName,
							modalityName, rs.getString("date_time"), radioName, 
							techName, null));
				}
			}
			
			con.close();
		} 
		catch (SQLException e) {
			System.out.println("Error: Could not get checked in appointment data.");
		}
		
		checkedInPatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		checkedInModalityCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		checkedInDateAndTimeCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		checkedInRadioCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
		checkedInTechCol.setCellValueFactory(new PropertyValueFactory<>("s5"));
		
		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
					} else {
						final Button compOrderButton = new Button("Complete Order");
						compOrderButton.setOnAction(event -> {
							m = getTableView().getItems().get(getIndex());
							String firstName = null;
							String lastName = null;
							String DOB = null;
							String sex = null;
							String race = null;
							String ethnicity = null;
							String phone = null;
							String email = null;
							String allergies = null;
							String modalityName = null;
							
											
							try {
								Connection con = DatabaseConnection.getConnection();
								ResultSet rs = con.createStatement().executeQuery("select * from patients where patient_id=" + m.getNum1());
								
								while(rs.next()) {
									firstName = rs.getString("first_name");
									lastName = rs.getString("last_name");
									DOB = rs.getString("DOB");
									sex = rs.getString("sex");
									race = rs.getString("race");
									ethnicity = rs.getString("ethnicity");
									phone = rs.getString("phone_number");
									email = rs.getString("email_address");
									allergies = rs.getString("patientNotes");
									
								}
								con.close();
							}
							catch(SQLException e) {
								e.printStackTrace();
							}
							
							try {
								Connection con = DatabaseConnection.getConnection();
								//System.out.println("select * from orders where status=1 and patient=" + m.getNum1());
								ResultSet rs = con.createStatement().executeQuery("select * from orders where status=1 and patient=" + m.getNum1());
								
								while(rs.next()) {
									int order_id = rs.getInt("order_id");
									instance.setNum5(order_id);
									//System.out.println("order id is "+order_id);								
								}
								con.close();
							}
							catch(SQLException e) {
								e.printStackTrace();
							}
							
							try {
								Connection con = DatabaseConnection.getConnection();
								ResultSet rs = con.createStatement().executeQuery("select * from modalities where modality_id=" + m.getNum2());
								
								while(rs.next()) {
									modalityName = rs.getString("name");
								}
								con.close();
							}
							catch(SQLException e) {
								e.printStackTrace();
							}
							
							//find appt_id where patient_id = m.getNum1() and checked_in=1 and closed = 0
													
							try {
								Connection con = DatabaseConnection.getConnection();
								ResultSet rs = con.createStatement().executeQuery("select * from appointments where patient=" + m.getNum1()+" and checked_in=1 and closed=0");
								
								while(rs.next()) {
									int appt_id = rs.getInt("appointment_id");
									int patient = rs.getInt("patient");
									int modality = rs.getInt("modality");
									int tech = rs.getInt("technician");
																	
									instance.setNum1(modality);
									instance.setNum2(patient);
									instance.setNum3(tech);
								}
								con.close();
								
							}
							catch(SQLException e) {
								e.printStackTrace();
							}
							
							//get order number to get notes
							try {
								Connection con = DatabaseConnection.getConnection();
								ResultSet rs = con.createStatement().executeQuery("select * from orders where order_id=" + instance.getNum5());
								
								while(rs.next()) {
									String notes = rs.getString("notes");						
									instance.setS1(notes);
								}
								con.close();
							}
							catch(SQLException e) {
								e.printStackTrace();
							}


							overviewFirstNameTextField.setText(firstName);
							overviewLastNameTextField.setText(lastName);
							overviewDOBTextField.setText(DOB);
							overviewSexTextField.setText(sex);
							overviewRaceTextField.setText(race);
							overviewEthnicityTextField.setText(ethnicity);
							overviewPhoneTextField.setText(phone);
							overviewEmailTextField.setText(email);
							overviewAllergiesTextArea.setText(allergies);
							
							imagingFirstNameTextField.setText(firstName);
							imagingLastNameTextField.setText(lastName);
							imagingModalityTextField.setText(modalityName);
							
							LogOut.setDisable(true);
							patientOverviewPane.setVisible(true);

						});

						setGraphic(compOrderButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		checkedInCompleteOrderCol.setCellFactory(cellFactory);

		// Set observable list data in the table
		checkedInAppsTable.setItems(checkedInApps);
	}

	//complete order method
	public void completeOrderButton(ActionEvent event) throws IOException {
		ImagingOrderPane.setVisible(false);
		patientOverviewPane.setVisible(false);
		//add method to upload image!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!	
		//modality = instance.getModality()
		//patient = instance.getPatient()
		//tech = instance.getTech()		
		
		//query info from imaging_info table to find last used imaging_id
				String sql = "insert into imaging_info(imaging_id,image_url,imaging,modality,notes,patient,technician) VALUES (?,?,?,?,?,?,?)";
				int imaging_id = 0;
				String image_url = file.getAbsolutePath();
				int modality = instance.getNum1();
				String notes  = instance.getS1();
				int patient =instance.getNum2();
				int technician = instance.getNum3();
							
				try {
					Connection con = DatabaseConnection.getConnection();
					
					//set imaging_id
					Statement stmt = con.createStatement();
					String IDCheck = "select * from imaging_info";
					ResultSet rs = stmt.executeQuery(IDCheck);
					
					int c = 0;

					while (rs.next()) {
						c = rs.getInt("imaging_id");
					}
					imaging_id = c + 1;
				
					
					
					PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);		
					pst.setInt(1, imaging_id);
					pst.setString(2, image_url);
					fis = new FileInputStream(file);
					pst.setBinaryStream(3,fis,file.length());
					pst.setInt(4, modality);
					pst.setString(5, notes);
					pst.setInt(6, patient);
					pst.setInt(7, technician);
					
					pst.executeUpdate();
					//rs.pst.getGeneratedKeys();
					//rs.next();
					
					
					
					//System.out.println("image inserted successfully");
					
					//update order status to imaging complete
					String updateOrderStatus = "UPDATE orders SET status=2,image="+imaging_id +" where order_id="+instance.getNum5()+";";
					//System.out.println(updateOrderStatus);
					try{
						stmt.executeUpdate(updateOrderStatus);
					}
					catch(SQLException e){
						System.out.println("Order status update failed...");
					}
				}
				catch(SQLException e) {
					System.out.println("image failed to insert");
					e.printStackTrace();
				}
	}
	
	//cancel order method
	public void backButton(ActionEvent event) throws IOException {
		ImagingOrderPane.setVisible(false);
		
	}


	//patient overview method
	public void patientOverview() {
		ImagingOrderPane.setVisible(false);
		patientOverviewPane.setVisible(true);
		//fill out method to fill in fields
		//querying patients table for patient info
	}

	//patient overview close button
	public void closeOverview() {
		LogOut.setDisable(false);
		patientOverviewPane.setVisible(false);
		ImagingOrderPane.setVisible(true);
		overviewFirstNameTextField.setText("");
		overviewLastNameTextField.setText("");
		overviewDOBTextField.setText("");
		overviewSexTextField.setText("");
		overviewRaceTextField.setText("");
		overviewEthnicityTextField.setText("");
		overviewPhoneTextField.setText("");
		overviewEmailTextField.setText("");
		overviewAllergiesTextArea.setText("");

	}
	
	
	public void selectFile(ActionEvent event) {
		stage = (Stage) ImagingOrderPane.getScene().getWindow();
		file = fileChooser.showOpenDialog(stage);
		
		/*try {
			deskTop.open(file);
		} catch (IOException e) {
			System.out.println("File failed to open.");
			e.printStackTrace();
		}
		*/
		if(file != null) {
			//System.out.println(""+file.getAbsolutePath());
			image = new Image(file.getAbsoluteFile().toURI().toString(), scanImageView.getFitWidth(), scanImageView.getFitHeight(), true, true);
			scanImageView.setImage(image);
			scanImageView.setPreserveRatio(true);
		}
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

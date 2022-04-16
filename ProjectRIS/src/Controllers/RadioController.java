package Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Logger;

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
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;

public class RadioController implements Initializable{

	@FXML TextField searchOrdersTextField;
	@FXML TableColumn<ModelTable, String> revOrdersPatientCol;
	@FXML TableColumn<ModelTable, String> revOrdersDocCol;
	@FXML TableColumn<ModelTable, String> revOrdersModalCol;
	@FXML TableColumn<ModelTable, String> revOrdersNotesCol;
	@FXML TableColumn<ModelTable, String> reviewButtonCol;
	@FXML Pane ReviewOrdersPane;
	@FXML TableView<ModelTable> revOrdersTable;
	ObservableList<ModelTable> reviewOrders = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchReviewOrders = FXCollections.observableArrayList();
	
	ModelTable m = new ModelTable();
	ModelTable instance = new ModelTable();
	@FXML Pane diagnosticReportPane;
	@FXML Button reportCloseButton;
	@FXML Button compReportButton;
	@FXML Button viewImageButton;
	@FXML TextField FirstNameTextField;
	@FXML TextField LastNameTextField;
	@FXML TextField DOBTextField;
	@FXML TextField ModalityTextField;
	@FXML TextArea reportTextArea;
	@FXML TextArea MDNotesTextArea;
	@FXML Pane diagnosticPane;
	@FXML Button patientOverviewButton;
	@FXML Label MDNotesLabel2;
	@FXML Label modalityLabel2;
	@FXML Label LastNameLabel;
	@FXML Button LogOut;
	@FXML Pane patientOverviewPane;
	@FXML Label PatientOverviewPaneLabel;
	@FXML TextField overviewFirstNameTextField;
	@FXML TextField overviewLastNameTextField;
	@FXML TextField overviewDOBTextField;
	@FXML TextField overviewSexTextField;
	@FXML TextField overviewRaceTextField;
	@FXML TextField overviewEthnicityTextField;
	@FXML TextArea overviewAllergiesTextArea;
	@FXML Button closeButton;
	@FXML TextField overviewPhoneTextField;
	@FXML TextField overviewEmailTextField;
	private Image image;
	@FXML Pane scanPane;
	@FXML Button closeImage;
	@FXML ImageView imageView;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		populateReviewOrders();
		
	}
	private void populateReviewOrders() {
		reviewOrders.clear();
		int patient = 0;
		int modality = 0;
		int refDoc = 0;
		int order = 0;
		int radio = 0;
		String patientName = null;
		String modalityName = null;
		String docName = null;
		String notes = null;
		int checked_in=0;
		//int closed = 0;
		int status = 0;
		
		// Get all appointment data
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from orders;");
			//get all orders that have imaging complete

			while (rs.next()) {
				patient = rs.getInt("patient");
				modality = rs.getInt("modality");
				refDoc = rs.getInt("referral_md");
				order = rs.getInt("order_id");
				//checkedIn = rs.getInt("checked_in");
				//closed = rs.getInt("closed");
				status = rs.getInt("status");
				//System.out.println(patient);
				//System.out.println(modality);
				//System.out.println(refDoc);
				//System.out.println(order);
				//System.out.println(closed);
				//System.out.println(status);
				
				// Check to make sure appointment has been checked in
				//added check to make sure appt is not closed
				if(status==2) {
					ResultSet rs2 = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
					while(rs2.next()) {
						patientName = rs2.getString("first_name") + " " + rs2.getString("last_name");
						//System.out.println(patientName);
					}
					rs2 = con.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
					while(rs2.next()) {
						modalityName = rs2.getString("name");
						//System.out.println(modalityName);
					}
					rs2 = con.createStatement().executeQuery("select * from users where user_id=" + refDoc);
					while(rs2.next()) {
						docName = rs2.getString("full_name");
						//System.out.println(docName);
					}
					rs2 = con.createStatement().executeQuery("select * from orders where order_id=" + order);
					while(rs2.next()) {
						status = rs2.getInt("status");
						//System.out.println(status);
					}
					rs2 = con.createStatement().executeQuery("select * from orders where order_id=" + order);
					while(rs2.next()) {
						notes = rs2.getString("notes");
						//System.out.println(notes);
					}
					rs2 = con.createStatement().executeQuery("select * from appointments where order_id=" + order);
					while(rs2.next()) {
						radio = rs2.getInt("radiologist");
						//System.out.println("radio is: "+radio);
						instance.setNum1(radio);
					}
					
					
					reviewOrders.add(new ModelTable(patient,refDoc,modality,patientName, modalityName, docName, notes, null, null));
				}
			}
			
			con.close();
		} 
		catch (SQLException e) {
			System.out.println("Error: Could not get checked in appointment data.");
		}
		
		revOrdersPatientCol.setCellValueFactory(new PropertyValueFactory<>("s1"));
		revOrdersModalCol.setCellValueFactory(new PropertyValueFactory<>("s2"));
		revOrdersDocCol.setCellValueFactory(new PropertyValueFactory<>("s3"));
		revOrdersNotesCol.setCellValueFactory(new PropertyValueFactory<>("s4"));
		reviewButtonCol.setCellValueFactory(new PropertyValueFactory<>(""));
		
		Callback<TableColumn<ModelTable, String>, TableCell<ModelTable, String>> cellFactory = (param) -> {

			final TableCell<ModelTable, String> cell = new TableCell<ModelTable, String>() {

				@Override
				public void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);

					if (empty) {
						setText(null);
					} else {
						final Button reviewOrderButton = new Button("Review Order");
						reviewOrderButton.setOnAction(event -> {
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
							int order_id =0;
							int image_id=0;
							
											
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
								ResultSet rs = con.createStatement().executeQuery("select * from orders where status=2 and patient=" + m.getNum1());
								
								while(rs.next()) {
									order_id = rs.getInt("order_id");
									image_id = rs.getInt("image");
									//System.out.println("Setting order_id; it is "+order_id);
									instance.setNum4(order_id);
									instance.setNum5(image_id);
									//System.out.println("Setting image_id; it is "+image_id);
								}
								con.close();
							}
							catch(SQLException e) {
								e.printStackTrace();
							}
							
							try {
								Connection con = DatabaseConnection.getConnection();
								ResultSet rs = con.createStatement().executeQuery("select * from modalities where modality_id=" + m.getNum3());
								
								while(rs.next()) {
									modalityName = rs.getString("name");
								}
								con.close();
							}
							catch(SQLException e) {
								e.printStackTrace();
							}
							
							
							
							//System.out.println("order id is:"+ instance.getNum4());
							//get order number to get notes
							try {
								Connection con = DatabaseConnection.getConnection();
								ResultSet rs = con.createStatement().executeQuery("select * from orders where order_id=" + instance.getNum4()+";");
								
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
							
							FirstNameTextField.setText(firstName);
							LastNameTextField.setText(lastName);
							DOBTextField.setText(DOB);
							//System.out.println(modalityName);
							ModalityTextField.setText(modalityName);
							MDNotesTextArea.setText(instance.getS1());
							
							LogOut.setDisable(true);
							patientOverviewPane.setVisible(true);

						});

						setGraphic(reviewOrderButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		reviewButtonCol.setCellFactory(cellFactory);

		// Set observable list data in the table
		revOrdersTable.setItems(reviewOrders);
		
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

	@FXML public void compReport() {
		
		String sql = "insert into diagnostic_reports(diagnostic_report_id,order_id,patient,radiologist,diagnostic) VALUES (?,?,?,?,?)";
		int report_id = 0;
		int order_id = instance.getNum4();
		int patient = m.getNum1();
		int modality = m.getNum3();
		String report = reportTextArea.getText();
		int radio = instance.getNum1();
		
					
		try {
			Connection con = DatabaseConnection.getConnection();
			
			//set diagnostic_report_id
			Statement stmt = con.createStatement();
			String IDCheck = "select * from diagnostic_reports";
			ResultSet rs = stmt.executeQuery(IDCheck);
			
			int c = 0;

			while (rs.next()) {
				c = rs.getInt("diagnostic_report_id");
			}
			report_id = c + 1;
		
			
			
			PreparedStatement pst = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);		
			pst.setInt(1, report_id);
			pst.setInt(2, order_id);
			pst.setInt(3, patient);
			pst.setInt(4, radio);
			pst.setString(5, report);
			
			
			pst.executeUpdate();
			//rs.pst.getGeneratedKeys();
			//rs.next();
			
			
			
			
			
			//update order status to imaging complete
			String updateOrderStatus = "UPDATE orders SET status=3 where order_id="+order_id+";";
			//System.out.println(updateOrderStatus);
			try{
				stmt.executeUpdate(updateOrderStatus);
			}
			catch(SQLException e){
				System.out.println("Order status update failed...");
			}
			
			//close appointment
			String closeAppt = "UPDATE appointments SET closed=1 where order_id="+order_id+";";
			
			try{
				stmt.executeUpdate(closeAppt);
			}
			catch(SQLException e){
				System.out.println("Close appt update failed...");
			}
		}
		catch(SQLException e) {
			System.out.println("image failed to insert");
			e.printStackTrace();
		}
		
		diagnosticPane.setVisible(false);
		populateReviewOrders();
	}
	
	public void closeOverview() {
		LogOut.setDisable(false);
		patientOverviewPane.setVisible(false);
		diagnosticPane.setVisible(true);
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
	public void reportClose() {
		LogOut.setDisable(false);
		diagnosticPane.setVisible(false);
		patientOverviewPane.setVisible(false);
		
		FirstNameTextField.setText("");
		LastNameTextField.setText("");
		DOBTextField.setText("");

	}
	
	public void viewImage() {
		try{
			Connection con = DatabaseConnection.getConnection();
			PreparedStatement pst = con.prepareStatement("Select imaging from imaging_info where imaging_id=?");
			pst.setInt(1, instance.getNum5());
			ResultSet rs = pst.executeQuery();
			if(rs.next()) {
				InputStream is = rs.getBinaryStream(1);
				OutputStream os = new FileOutputStream(new File("photo.jpg"));
				byte[] contents = new byte[1024];
				int size = 0;
				while((size=is.read(contents)) != -1) {
					os.write(contents, 0, size);
				}
				image = new Image("file:photo.jpg", imageView.getFitWidth(),imageView.getFitHeight(), true, true);
				imageView.setImage(image);
				scanPane.setVisible(true);
			}
		}catch(SQLException e) {
			System.out.println("failed to retrieve image from database");
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@FXML public void closeImagePane() {
		scanPane.setVisible(false);
	}
	
	public void searchOrders() {
		searchReviewOrders.clear();
		String userSearch = searchOrdersTextField.getText();
		if(!userSearch.equals("")) {
			for(int i = 0; i < reviewOrders.size(); i++) {
				if(reviewOrders.get(i).getS1().contains(userSearch)) {
					searchReviewOrders.add(reviewOrders.get(i));
				}
			}
			revOrdersTable.setItems(searchReviewOrders);
		}
		else {
			populateReviewOrders();
		}
	}
}

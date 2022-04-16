package Controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;
import javafx.scene.layout.Pane;


public class DoctorController implements Initializable{
	//orders
	@FXML
	private TextField OrderPatientName;
	@FXML
	private TextField OrderReferralMD;
	@FXML
	private TextField OrderModalityNeeded;
	@FXML
	private TextArea OrderNotes;
	
	@FXML
	TextField searchOrdersTextField;
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
	TableColumn<ModelTable, String> allOrdersReportCol;
	ObservableList<ModelTable> orders = FXCollections.observableArrayList();
	ObservableList<ModelTable> searchOrders = FXCollections.observableArrayList();
	
	ModelTable m = new ModelTable();
	private Image image;
	@FXML Pane scanPane;
	@FXML Button closeImage;
	@FXML ImageView imageView;
	ModelTable instance = new ModelTable();
	
	//report pane
	@FXML Pane reportPane;
	@FXML TextField reportRadioTextField;
	@FXML TextField reportPatientTextField;
	@FXML TextField reportOrderIDTextField;
	@FXML Button closeReportButton;
	@FXML Button viewImageButton;
	@FXML TextArea reportTextArea;
	@FXML Button LogOut;
	
	public void userLogOut(ActionEvent event) throws IOException {
		
		Main m = new Main();
		
		m.changeScene("../Views/Login.fxml");
	}
	
	public void HomeButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/Doctor.fxml");
}
	
	public void ReferralsButton(ActionEvent event) throws IOException{
		Main m = new Main();
		m.changeScene("../Views/DoctorReferrals.fxml");
}
	
	public void newOrderButton(ActionEvent event) throws IOException{
		
		Main m = new Main();
		m.changeScene("../Views/DoctorNewOrder.fxml");
	}
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		populateOrders();

	}
	
	public void populateOrders() {
		orders.clear();
		int orderID = 0;
		int patient_id = 0;
		int doc_id=0;
		int status = 0;
		int modality = 0;
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
				patient_id = rs.getInt("patient");
				doc_id = rs.getInt("referral_md");
				notes = rs.getString("notes");
				modality = rs.getInt("modality");
				status = rs.getInt("status");
				// getting values we need to populate table with textual info
				
				
				// get patient name
				try {
					Connection con1 = DatabaseConnection.getConnection();
					ResultSet rs1 = con1.createStatement().executeQuery("select * from patients where patient_id=" + patient_id);
					
					while(rs1.next()) {
						String firstName = rs1.getString("first_name");
						String lastName = rs1.getString("last_name");
						patientName = firstName +" "+ lastName;
						
					}
					con1.close();
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
				
				
				//get referral doc
				try {
					Connection con2 = DatabaseConnection.getConnection();
					ResultSet rs2 = con2.createStatement().executeQuery("select * from users where user_id=" + doc_id);
					
					while(rs2.next()) {
						docName = rs2.getString("full_name");						
					}
					con2.close();
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
				
				//get modality name
				try {
					Connection con3 = DatabaseConnection.getConnection();
					ResultSet rs3 = con3.createStatement().executeQuery("select * from modalities where modality_id=" + modality);
					
					while(rs3.next()) {
						modalityName = rs3.getString("name");						
					}
					con3.close();
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
				
				//get status name
				//get patient name
				try {
					Connection con4 = DatabaseConnection.getConnection();
					ResultSet rs4 = con4.createStatement().executeQuery("select * from order_status where name=" + status);
					
					while(rs4.next()) {
						statusName = rs4.getString("name");
						
						
					}
					con4.close();
				}
				catch(SQLException e) {
					e.printStackTrace();
				}
				
				orders.add(new ModelTable(orderID, 0, 0, patientName, docName, modalityName, notes, statusName, null));
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
						final Button reviewReportButton = new Button("Report");
						reviewReportButton.setOnAction(event -> {
							
							m = getTableView().getItems().get(getIndex());
							
							String patientName = null;
							String radioName = null;
							String radioReport = null;
							int patient = 0;
							int radio = 0;
							int order = 0;
							
							order = m.getNum1();
							//System.out.println("order is "+order);
							//getting patient and radio IDs
							//getting notes
							try {
								
								Connection con = DatabaseConnection.getConnection();
								ResultSet rs = con.createStatement().executeQuery("select * from diagnostic_reports where order_id=" + order+";");
								
								
								while(rs.next()) {
									patient = rs.getInt("patient");
									radio = rs.getInt("radiologist");
									radioReport = rs.getString("diagnostic");
																		
								}
								con.close();
							}
							catch(SQLException e) {
								e.printStackTrace();
							}
							
							
							//setting patientName variable
							try {
								Connection con = DatabaseConnection.getConnection();
								ResultSet rs = con.createStatement().executeQuery("select * from patients where patient_id=" + patient);
								//System.out.println("patient is: "+patient);
								while(rs.next()) {
									String firstName = rs.getString("first_name");
									String lastName = rs.getString("last_name");
									patientName = firstName + " " +lastName;								
								}
								con.close();
							}
							catch(SQLException e) {
								e.printStackTrace();
							}
							
							//setting radioName variable
							try {
								Connection con2 = DatabaseConnection.getConnection();
								ResultSet rs2 = con2.createStatement().executeQuery("select * from users where user_id=" + radio);
								
								while(rs2.next()) {
									radioName = rs2.getString("full_name");						
								}
								con2.close();
							}
							catch(SQLException e) {
								e.printStackTrace();
							}
							String orderString = ""+order;
							
							reportRadioTextField.setText(radioName);
							reportPatientTextField.setText(patientName);
							reportOrderIDTextField.setText(orderString);
							reportTextArea.setText(radioReport);
							
							LogOut.setDisable(true);
							reportPane.setVisible(true);


						});

						setGraphic(reviewReportButton);
						setText(null);
					}
				}
			};

			return cell;
		};
		allOrdersReportCol.setCellFactory(cellFactory);

		allOrdersTable.setItems(orders);
	}
	
	public void addNewOrder(ActionEvent event) throws IOException {
		 
	      Statement stmt = null;
	      Connection con;
	      
	      try {
	    	  con = DatabaseConnection.getConnection();
	          try {
	             Class.forName("com.mysql.cj.jdbc.Driver");
	          } catch (Exception e) {
	             System.out.println(e);
	       }
	       
	       System.out.println("Connection is created successfully:");
	       stmt = (Statement) con.createStatement();
	       
	       
	       String query1 = "INSERT INTO orders(patient, referral_md, modality, notes) VALUES('"+OrderPatientName.getText()+"', '"+OrderReferralMD.getText()+"', '"+OrderModalityNeeded.getText()+"', '"+OrderNotes.getText()+"');";
	       System.out.println(query1);
	       stmt.executeUpdate(query1);
	       } catch (SQLException excep) {
	          excep.printStackTrace();
	       } catch (Exception excep) {
	          excep.printStackTrace();
	       } finally {
	          try {
	        	  con = DatabaseConnection.getConnection();
	             if (stmt != null)
	                con.close();
	          } catch (SQLException se) {}
	          try {
	        	  con = DatabaseConnection.getConnection();
	             if (con != null)
	                con.close();
	          } catch (SQLException se) {
	             se.printStackTrace();
	          }  
	       }
	       newOrderButton(event);
	      }
	public void viewImage() {
		try{
			Connection con = DatabaseConnection.getConnection();
			int imaging_id=0;
			//get imaging_id
			try {
				Connection con1 = DatabaseConnection.getConnection();
				int order_id = Integer.parseInt(reportOrderIDTextField.getText());
				ResultSet rs1 = con1.createStatement().executeQuery("select * from orders where order_id=" + order_id);
				
				while(rs1.next()) {
					imaging_id = rs1.getInt("image");
									
				}
				con1.close();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
			
			PreparedStatement pst = con.prepareStatement("Select imaging from imaging_info where imaging_id=?");
			pst.setInt(1, imaging_id);
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

	@FXML public void closeReport() {
		reportPane.setVisible(false);
		LogOut.setDisable(false);
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
			allOrdersTable.setItems(searchOrders);
		}
		else {
			populateOrders();
		}
	}
}

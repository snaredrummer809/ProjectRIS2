package Controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import application.DatabaseConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ImageController implements Initializable {

	
	@FXML
	Button open,load;
	@FXML
	ImageView imageView;
	@FXML
	Pane ImagesPane;
	@FXML
	Label label;
	
	private PreparedStatement store, retrieve;
	private String storeStatment = "INSERT INTO photos(image) VALUES (?)";
	private String retrieveStatement = "SELECT image FROM photos WHERE photo_id = 15";
	
	public void initialize() throws IOException {
		
		try {
			Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_ris", "root","Aa8091670");
			
			store = connection.prepareStatement(storeStatment);
			retrieve = connection.prepareStatement(retrieveStatement);
		}catch(SQLException e) {
			System.out.println(e.getMessage());
		}

	}


	
	public void chooseFile(ActionEvent event) throws SQLException {
		
		
		
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new ExtensionFilter("PNG image", "*.png"));
		List<File> f = fc.showOpenMultipleDialog(null);
		for(File file : f) {
			
			try {
				
				Connection con = DatabaseConnection.getConnection();
				PreparedStatement ps = con.prepareStatement("insert into photos(Image) values(?)");
				
				
				InputStream is = new FileInputStream(new File(file.getAbsolutePath()));
				ps.setBlob(1,is);
				ps.executeUpdate();
				
				label.setText(file.getAbsolutePath() + " selected and inserted into database ");
				
			}
				
			
			catch (FileNotFoundException e) {
				
				e.printStackTrace();
			}
			
		}
	}
	
	
	public void loadFile(ActionEvent event) {
		
		try {
			
			
			
			Connection con = DatabaseConnection.getConnection();
			//PreparedStatement ps = con.prepareStatement("Select * from photos where photo_id = 10");
			retrieve = con.prepareStatement(retrieveStatement);
			ResultSet rs = retrieve.executeQuery();
			while(rs.next()) {
				
				Blob blob = rs.getBlob(1);
			    InputStream inputStream = blob.getBinaryStream();
				Image image = new Image(inputStream);
				imageView.setImage(image);
				System.out.println(inputStream);
			}
			
				
				label.setText("Image retrieved");
			
			
			
		} 
		
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// TODO Auto-generated method stub
		
		
	}
	
	
	}

	


	
	


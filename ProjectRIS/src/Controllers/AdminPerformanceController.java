package Controllers;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;

public class AdminPerformanceController implements Initializable{
	
	// Nav Buttons
	@FXML
	Button LogOut;
	@FXML
	Button HomeButton;
	
	// Performance Report
	@FXML
	Pane performanceReportPane;
	@FXML
	PieChart patientsSeenPieChart;
	@FXML
	BarChart<String,Number> systemDataBarChart;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		populateReport();
	}
	
	public void userLogOut(ActionEvent event) throws IOException {

		Main m = new Main();

		m.changeScene("../Views/Login.fxml");
	}

	public void HomeButton(ActionEvent event) throws IOException {

		Main m = new Main();
		m.changeScene("../Views/Admin.fxml");
	}
	
	public void populateReport() {
		int patientsSeen = 0;
		int noShows = 0;
		int images = 0;
		int reports = 0;
		int invoices = 0;
		
		try {
			Connection con = DatabaseConnection.getConnection();
			ResultSet rs = con.createStatement().executeQuery("select * from appointments");
			
			while(rs.next()) {
				if(rs.getInt("closed") == 1 && rs.getInt("checked_in") == 1) {
					patientsSeen++;
				}
				if(rs.getInt("closed") == 1 && rs.getInt("checked_in") == 0) {
					noShows++;
				}
			}
			con.close();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                new PieChart.Data("Patients Seen: " + patientsSeen, patientsSeen),
                new PieChart.Data("No Shows: " + noShows, noShows));
                
        patientsSeenPieChart.setData(pieChartData);
        
        try {
        	Connection con = DatabaseConnection.getConnection();
        	ResultSet rs = con.createStatement().executeQuery("select * from orders");
        	
        	while(rs.next()) {
        		if(rs.getInt("image") > 0) {
        			images++;
        		}
        	}
        	
        	rs = con.createStatement().executeQuery("select * from diagnostic_reports");
        	
        	while(rs.next()) {
        		if(rs.getInt("diagnostic_report_id") > 0) {
        			reports++;
        		}
        	}
        	
        	rs = con.createStatement().executeQuery("select * from invoices");
        	
        	while(rs.next()) {
        		if(rs.getInt("invoice_number") > 0) {
        			invoices++;
        		}
        	}
        }
        catch(SQLException e) {
        	e.printStackTrace();
        }
        
        XYChart.Series series1 = new XYChart.Series();
        series1.setName("Images");
        series1.getData().add(new XYChart.Data("Patients Imaged", images));
        
        XYChart.Series series2 = new XYChart.Series();
        series2.setName("Reports");
        series2.getData().add(new XYChart.Data("Reports Written", reports));
        
        XYChart.Series series3 = new XYChart.Series();
        series3.setName("Invoices");
        series3.getData().add(new XYChart.Data("Patients Invoiced", invoices));
        
        systemDataBarChart.getData().addAll(series1, series2, series3);
	}
}

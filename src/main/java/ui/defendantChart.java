package ui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import util.DBUtils;

import java.util.List;

public class defendantChart extends Application {
	private DBUtils dbUtils = new DBUtils();
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("defendantChart");
		Scene scene = new Scene(new Group());
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		primaryStage.setWidth(1000);
		primaryStage.setHeight(1000);

		PieChart genderChart = generatePieChart("gender","性别");
		PieChart provinceChart = generatePieChart("birthProvince","省份");
		PieChart educationChart = generatePieChart("education","文化程度");
		PieChart careerChart = generatePieChart("career","职业");


		gridPane.add(genderChart,0,0);
		gridPane.add(provinceChart,0,1);
		gridPane.add(educationChart,1,0);
		gridPane.add(careerChart,1,1);

		((Group)scene.getRoot()).getChildren().addAll(gridPane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	private PieChart generatePieChart( String column, String title){
		List<String> keys = dbUtils.generateKeys("Defendant",column);
		ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
		for (String key:keys){
			chartData.add(new PieChart.Data(key,dbUtils.getValue("Defendant",column,key)));
		}
		PieChart pieChart = new PieChart(chartData);
		pieChart.setTitle(title);
		return pieChart;
	}
}

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

public class judgmentChart extends Application {
	private DBUtils dbUtils = new DBUtils();
	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("judgmentChart");
		Scene scene = new Scene(new Group());
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		primaryStage.setWidth(1000);
		primaryStage.setHeight(1000);

		PieChart drugChart = generatePieChart("drugType","毒品种类");
		PieChart crimeChart = generatePieChart("crime","罪名种类");
		PieChart penaltyChart = generatePieChart("penaltyType","财产刑种类");
		PieChart propertyPunishmentChart = generatePieChart("propertyPunishment","刑法种类");

		gridPane.add(drugChart,0,0);
		gridPane.add(crimeChart,0,1);
		gridPane.add(penaltyChart,1,0);
		gridPane.add(propertyPunishmentChart,1,1);

		((Group)scene.getRoot()).getChildren().addAll(gridPane);
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private PieChart generatePieChart( String column, String title){
		List<String> keys = dbUtils.generateKeys("Judgment",column);
		ObservableList<PieChart.Data> chartData = FXCollections.observableArrayList();
		for (String key:keys){
			chartData.add(new PieChart.Data(key,dbUtils.getValue("Judgment",column,key)));
		}
		PieChart pieChart = new PieChart(chartData);
		pieChart.setTitle(title);
		return pieChart;
	}
}

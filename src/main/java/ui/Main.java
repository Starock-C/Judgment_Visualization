package ui;

import com.sun.xml.bind.v2.TODO;
import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Defendant;
import model.Judgment;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;
import util.*;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Main extends Application {
	private tableJudgment tablejudgment;
	private DBUtils dbUtils = new DBUtils();
	private static Extract extract = new Extract();
	private static List<Defendant> allDefendants = new ArrayList<>();
	private static List<Judgment> allJudgments = new ArrayList<>();
	private DatePicker startDatePicker;
	private DatePicker finishDatePicker;

	private List<Judgment> judgments;
	private ObservableList<tableJudgment> tableJudgments = FXCollections.observableArrayList();
	private List<Defendant> defendants;
	private ObservableList<tableDefendant> tableDefendants = FXCollections.observableArrayList();
	private final TableView<tableJudgment> tableViewJudgment = new TableView<>();
	private final TableView<tableDefendant> tableViewDefendant = new TableView<>();
	public static void main(String[] args) {
		Locale.setDefault(Locale.CHINA);launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Judgment Visualization");

		MenuBar menuBar = new MenuBar();
		Menu menuFile = new Menu("文件");
		Menu menuEdit = new Menu("编辑");
		Menu menuView = new Menu("视图");

		MenuItem menuItemImportFile = new MenuItem("从文件导入");
		MenuItem menuItemImportDirectory = new MenuItem("从文件夹导入");
		MenuItem menuItemExit = new MenuItem("Exit");
		MenuItem menuItemExportJudgment = new MenuItem("导出判决信息");
		MenuItem menuItemExportDefendant = new MenuItem("导出被告人信息");
		menuItemExit.setOnAction((ActionEvent e) -> {
			System.exit(0);
		});
		FileChooser fileChooser = new FileChooser();    configureFileChooser(fileChooser);
		menuItemImportFile.setOnAction((ActionEvent t) -> {
			List<File> fileList = fileChooser.showOpenMultipleDialog(primaryStage);
			if (fileList != null){
				fileList.stream().forEach(file -> {
					if (file.exists()) {
						try {
							extract.extract(file.getPath(),allJudgments,allDefendants);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
				for (Judgment judgment:allJudgments) {
					dbUtils.addJudgment(judgment);
				}
				for (Defendant defendant:allDefendants) {
					dbUtils.addDefendant(defendant);
				}
			}
			refreshTableJudgment();
		});
		DirectoryChooser directoryChooser = new DirectoryChooser(); configureDirectoryChooser(directoryChooser);
		menuItemImportDirectory.setOnAction((ActionEvent t) -> {
			File file = directoryChooser.showDialog(primaryStage);
			if (file.exists())
				ExtractFiles(file);
			DBUtils dbUtils = new DBUtils();
			for (Judgment judgment:allJudgments
			) {
				dbUtils.addJudgment(judgment);
			}
			for (Defendant defendant:allDefendants
			) {
				dbUtils.addDefendant(defendant);
			}
			refreshTableJudgment();
		});
		FileChooser fileChooserCSV = new FileChooser(); configureCSVFileChoose(fileChooserCSV);
		menuItemExportJudgment.setOnAction((ActionEvent t) -> {
			fileChooserCSV.setInitialFileName("Judgments.csv");
			File file = fileChooserCSV.showSaveDialog(primaryStage);
			if (file != null){
				CSVUtils csvUtils = new CSVUtils();
				try {
					allJudgments.clear();
					allJudgments.addAll(dbUtils.listAllJudgments());
					csvUtils.CSV_Write_Judgement(allJudgments,file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		menuItemExportDefendant.setOnAction((ActionEvent t) -> {
			fileChooserCSV.setInitialFileName("Defendants.csv");
			File file = fileChooserCSV.showSaveDialog(primaryStage);
			if (file != null){
				CSVUtils csvUtils = new CSVUtils();
				try {
					allDefendants.clear();;
					allDefendants.addAll(dbUtils.listAllDefendants());
					csvUtils.CSV_Write_defendants(allDefendants,file);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		MenuItem menuItemJudgmentPieChart = new MenuItem("JudgmentsChart");
		menuItemJudgmentPieChart.setOnAction((ActionEvent e) -> {
			Stage judgmentChartStage = new Stage();
			judgmentChart judgmentchart = new judgmentChart();
			judgmentchart.start(judgmentChartStage);
		});
		MenuItem menuItemDefendantsPieChart = new MenuItem("DefendantsChart");
		menuItemDefendantsPieChart.setOnAction((ActionEvent e) -> {
			Stage defendantChartStage = new Stage();
			defendantChart defendantchart = new defendantChart();
			defendantchart.start(defendantChartStage);
		});


		menuFile.getItems().addAll(menuItemImportFile, menuItemImportDirectory, menuItemExportJudgment, menuItemExportDefendant, menuItemExit);
		menuView.getItems().addAll(menuItemJudgmentPieChart,menuItemDefendantsPieChart);
		menuBar.getMenus().addAll(menuFile,menuEdit,menuView);

		Label labelJudgmentTitle = new Label("判决一览");
		labelJudgmentTitle.setFont(new Font("Arial",20));
		Label labelDefendantTitle = new Label("涉案被告人");
		labelDefendantTitle.setFont(new Font("Arial",20));

		initTableView();

		Button buttonSearch = new Button("搜索");
		TextField searchField = new TextField();
		buttonSearch.setOnAction((ActionEvent e) -> {
			loadFoundJudgments(searchField.getText());
		});

		Label labelTo = new Label("  To  ");
		startDatePicker = new DatePicker();
		finishDatePicker = new DatePicker(LocalDate.now());
		System.out.println(finishDatePicker.getValue());
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(0,0,0,10));
		hBox.getChildren().addAll(searchField,buttonSearch);

		final VBox vbox = new VBox();
		vbox.setSpacing(5);
		vbox.setPadding(new Insets(0, 0, 0, 0));
		vbox.getChildren().addAll(menuBar,labelJudgmentTitle, hBox, tableViewJudgment,labelDefendantTitle,tableViewDefendant);

		Scene scene = new Scene(new Group());
		((Group)scene.getRoot()).getChildren().addAll(vbox);
		primaryStage.setScene(scene);
		scene.getStylesheets().add(Main.class.getResource("/DarkTheme.css").toExternalForm());
		primaryStage.show();
	}

	public static class tableJudgment{
		private SimpleStringProperty caseNumber;  //案号
		private SimpleStringProperty courtName;   //法院名称
		private SimpleStringProperty firstDefendant;
		private SimpleIntegerProperty defendantsCount;
		private SimpleStringProperty crime;       //罪名
		private SimpleStringProperty propertyPunishment;      //刑法种类
		private SimpleStringProperty sentence;        //刑期
		private SimpleStringProperty penaltyType;     //财产刑种类
		private SimpleIntegerProperty penalty;     //财产刑金额
		private SimpleStringProperty drugType;    //毒品种类
		private SimpleStringProperty quantity;    //毒品数量
		private SimpleStringProperty contact;     //联系方式
		private SimpleStringProperty payment;     //支付方式
		private SimpleStringProperty trade;       //交易方式
		private SimpleStringProperty transport;   //运输方式

		private tableJudgment(Judgment judgment){
			this.caseNumber = new SimpleStringProperty(judgment.getCaseNumber());
			this.courtName = new SimpleStringProperty(judgment.getCourtName());
			this.firstDefendant = new SimpleStringProperty(judgment.getFirstDefendant());
			this.defendantsCount = new SimpleIntegerProperty(judgment.getDefendantsCount());
			this.crime = new SimpleStringProperty(judgment.getCrime());
			this.propertyPunishment = new SimpleStringProperty(judgment.getPropertyPunishment());
			this.sentence = new SimpleStringProperty(judgment.getSentence());
			this.penaltyType = new SimpleStringProperty(judgment.getPenaltyType());
			this.penalty = new SimpleIntegerProperty(judgment.getPenalty());
			this.drugType = new SimpleStringProperty(judgment.getDrugType());
			this.quantity = new SimpleStringProperty(judgment.getQuantity());
			this.contact = new SimpleStringProperty(judgment.getContact());
			this.payment = new SimpleStringProperty(judgment.getPayment());
			this.trade = new SimpleStringProperty(judgment.getTrade());
			this.transport = new SimpleStringProperty(judgment.getTransport());
		}

		public String getCaseNumber() {
			return caseNumber.get();
		}

		public SimpleStringProperty caseNumberProperty() {
			return caseNumber;
		}

		public void setCaseNumber(String caseNumber) {
			this.caseNumber.set(caseNumber);
		}

		public String getCourtName() {
			return courtName.get();
		}

		public SimpleStringProperty courtNameProperty() {
			return courtName;
		}

		public void setCourtName(String courtName) {
			this.courtName.set(courtName);
		}

		public String getFirstDefendant() {
			return firstDefendant.get();
		}

		public SimpleStringProperty firstDefendantProperty() {
			return firstDefendant;
		}

		public void setFirstDefendant(String firstDefendant) {
			this.firstDefendant.set(firstDefendant);
		}

		public int getDefendantsCount() {
			return defendantsCount.get();
		}

		public SimpleIntegerProperty defendantsCountProperty() {
			return defendantsCount;
		}

		public void setDefendantsCount(int defendantsCount) {
			this.defendantsCount.set(defendantsCount);
		}

		public String getCrime() {
			return crime.get();
		}

		public SimpleStringProperty crimeProperty() {
			return crime;
		}

		public void setCrime(String crime) {
			this.crime.set(crime);
		}

		public String getPropertyPunishment() {
			return propertyPunishment.get();
		}

		public SimpleStringProperty propertyPunishmentProperty() {
			return propertyPunishment;
		}

		public void setPropertyPunishment(String propertyPunishment) {
			this.propertyPunishment.set(propertyPunishment);
		}

		public String getSentence() {
			return sentence.get();
		}

		public SimpleStringProperty sentenceProperty() {
			return sentence;
		}

		public void setSentence(String sentence) {
			this.sentence.set(sentence);
		}

		public String getPenaltyType() {
			return penaltyType.get();
		}

		public SimpleStringProperty penaltyTypeProperty() {
			return penaltyType;
		}

		public void setPenaltyType(String penaltyType) {
			this.penaltyType.set(penaltyType);
		}

		public int getPenalty() {
			return penalty.get();
		}

		public SimpleIntegerProperty penaltyProperty() {
			return penalty;
		}

		public void setPenalty(int penalty) {
			this.penalty.set(penalty);
		}

		public String getDrugType() {
			return drugType.get();
		}

		public SimpleStringProperty drugTypeProperty() {
			return drugType;
		}

		public void setDrugType(String drugType) {
			this.drugType.set(drugType);
		}

		public String getQuantity() {
			return quantity.get();
		}

		public SimpleStringProperty quantityProperty() {
			return quantity;
		}

		public void setQuantity(String quantity) {
			this.quantity.set(quantity);
		}

		public String getContact() {
			return contact.get();
		}

		public SimpleStringProperty contactProperty() {
			return contact;
		}

		public void setContact(String contact) {
			this.contact.set(contact);
		}

		public String getPayment() {
			return payment.get();
		}

		public SimpleStringProperty paymentProperty() {
			return payment;
		}

		public void setPayment(String payment) {
			this.payment.set(payment);
		}

		public String getTrade() {
			return trade.get();
		}

		public SimpleStringProperty tradeProperty() {
			return trade;
		}

		public void setTrade(String trade) {
			this.trade.set(trade);
		}

		public String getTransport() {
			return transport.get();
		}

		public SimpleStringProperty transportProperty() {
			return transport;
		}

		public void setTransport(String transport) {
			this.transport.set(transport);
		}
	}

	public static class tableDefendant{
		private SimpleStringProperty name;
		private SimpleStringProperty gender;
		private SimpleStringProperty birthDay;
		private SimpleStringProperty birthProvince;
		private SimpleStringProperty birthCity;
		private SimpleStringProperty nation;
		private SimpleStringProperty education;
		private SimpleStringProperty career;
		private SimpleStringProperty householdReg;    //户籍地
		private SimpleStringProperty residence;       //居住地
		private SimpleStringProperty judgment;

		private tableDefendant(Defendant defendant){
			this.name = new SimpleStringProperty(defendant.getName());
			this.gender = new SimpleStringProperty(defendant.getGender());
			this.birthDay = new SimpleStringProperty(defendant.getBirthDay());
			this.birthProvince = new SimpleStringProperty(defendant.getBirthProvince());
			this.birthCity = new SimpleStringProperty(defendant.getBirthCity());
			this.nation = new SimpleStringProperty(defendant.getNation());
			this.education = new SimpleStringProperty(defendant.getEducation());
			this.career = new SimpleStringProperty(defendant.getCareer());
			this.residence = new SimpleStringProperty(defendant.getResidence());
			this.judgment = new SimpleStringProperty(defendant.getJudgment().getCaseNumber());
		}

		public String getName() {
			return name.get();
		}

		public SimpleStringProperty nameProperty() {
			return name;
		}

		public void setName(String name) {
			this.name.set(name);
		}

		public String getGender() {
			return gender.get();
		}

		public SimpleStringProperty genderProperty() {
			return gender;
		}

		public void setGender(String gender) {
			this.gender.set(gender);
		}

		public String getBirthDay() {
			return birthDay.get();
		}

		public SimpleStringProperty birthDayProperty() {
			return birthDay;
		}

		public void setBirthDay(String birthDay) {
			this.birthDay.set(birthDay);
		}

		public String getBirthProvince() {
			return birthProvince.get();
		}

		public SimpleStringProperty birthProvinceProperty() {
			return birthProvince;
		}

		public void setBirthProvince(String birthProvince) {
			this.birthProvince.set(birthProvince);
		}

		public String getBirthCity() {
			return birthCity.get();
		}

		public SimpleStringProperty birthCityProperty() {
			return birthCity;
		}

		public void setBirthCity(String birthCity) {
			this.birthCity.set(birthCity);
		}

		public String getNation() {
			return nation.get();
		}

		public SimpleStringProperty nationProperty() {
			return nation;
		}

		public void setNation(String nation) {
			this.nation.set(nation);
		}

		public String getEducation() {
			return education.get();
		}

		public SimpleStringProperty educationProperty() {
			return education;
		}

		public void setEducation(String education) {
			this.education.set(education);
		}

		public String getCareer() {
			return career.get();
		}

		public SimpleStringProperty careerProperty() {
			return career;
		}

		public void setCareer(String career) {
			this.career.set(career);
		}

		public String getHouseholdReg() {
			return householdReg.get();
		}

		public SimpleStringProperty householdRegProperty() {
			return householdReg;
		}

		public void setHouseholdReg(String householdReg) {
			this.householdReg.set(householdReg);
		}

		public String getResidence() {
			return residence.get();
		}

		public SimpleStringProperty residenceProperty() {
			return residence;
		}

		public void setResidence(String residence) {
			this.residence.set(residence);
		}

		public String getJudgment() {
			return judgment.get();
		}

		public SimpleStringProperty judgmentProperty() {
			return judgment;
		}

		public void setJudgment(String judgment) {
			this.judgment.set(judgment);
		}
	}

	private List<tableJudgment> J2tJ(List<Judgment> judgments){
		List<tableJudgment> tableJudgments = new ArrayList<>();
		for (int i = 0; i < judgments.size(); i++)
			tableJudgments.add(new tableJudgment(judgments.get(i)));
		return tableJudgments;
	}
	private List<tableDefendant> D2tD(List<Defendant> defendants){
		List<tableDefendant> tableDefendants = new ArrayList<>();
		for (int i = 0; i < defendants.size(); i++)
			tableDefendants.add(new tableDefendant(defendants.get(i)));
		return tableDefendants;
	}

	private Judgment tJ2J(tableJudgment tableJudgment){
		Session session = HibernateUtil.getSession();
		String hql = "from Judgment where caseNumber = '"+tableJudgment.getCaseNumber()+"'";
		Query query = session.createQuery(hql);
		return (Judgment)query.uniqueResult();
	}

	private static void configureFileChooser(final FileChooser fileChooser) {
		fileChooser.setTitle("View Docs");
		fileChooser.setInitialDirectory(
				new File(System.getProperty("user.home"))
		);
		fileChooser.getExtensionFilters().addAll(
				new FileChooser.ExtensionFilter("DOC", "*.doc"),
				new FileChooser.ExtensionFilter("DOCX", "*.docx")
		);
	}
	private static void configureDirectoryChooser(final DirectoryChooser directoryChooser){
		directoryChooser.setTitle("View Directory");
		directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
	}
	private static void configureCSVFileChoose(final FileChooser fileChooser){
		fileChooser.setTitle("Save CSV");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV","*.csv"));
	}

	private static void ExtractFiles(File file){
		File[] files = file.listFiles();
		for (File f:files
		) {
			if (f.isDirectory())
				ExtractFiles(f);
			if (f.isFile()) {
				try {
					extract.extract(f.getPath(),allJudgments,allDefendants);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
	}

	private void loadFoundJudgments(String keyword){
		judgments = dbUtils.searchJudgmentsByKeyword(keyword);
		tableJudgments.clear();
		tableJudgments.addAll(J2tJ(judgments));
		tableViewJudgment.setItems(tableJudgments);
	}
	private void refreshTableJudgment(){
		judgments = dbUtils.listAllJudgments();
		tableJudgments.clear();
		tableJudgments.addAll(J2tJ(judgments));
		tableViewJudgment.setItems(tableJudgments);
	}
	private void refreshTableDefendant(tableJudgment judgment){
		defendants = dbUtils.listDefendants(tJ2J(judgment));
		tableDefendants.clear();
		tableDefendants.addAll(D2tD(defendants));
		tableViewDefendant.setItems(tableDefendants);
	}
	private void initTableView(){
		tableViewJudgment.setEditable(true);
		TableColumn col_caseNumber = new TableColumn("案号"); col_caseNumber.setCellValueFactory(new PropertyValueFactory<>("caseNumber"));
		col_caseNumber.setMinWidth(200);
		TableColumn col_courtName = new TableColumn("法院名称");    col_courtName.setCellValueFactory(new PropertyValueFactory<>("courtName"));
		TableColumn col_firstDefendant = new TableColumn("第一被告人");  col_firstDefendant.setCellValueFactory(new PropertyValueFactory<>("firstDefendant"));
		TableColumn col_defendantsCount = new TableColumn("一案人数");  col_defendantsCount.setCellValueFactory(new PropertyValueFactory<>("defendantsCount"));
		TableColumn col_crime = new TableColumn("罪名");  col_crime.setCellValueFactory(new PropertyValueFactory<>("crime"));
		TableColumn col_propertyPunishment = new TableColumn("刑法种类");   col_propertyPunishment.setCellValueFactory(new PropertyValueFactory<>("propertyPunishment"));
		TableColumn col_sentence = new TableColumn("刑期");   col_sentence.setCellValueFactory(new PropertyValueFactory<>("sentence"));
		TableColumn col_penaltyType = new TableColumn("财产刑种类"); col_penaltyType.setCellValueFactory(new PropertyValueFactory<>("penaltyType"));
		TableColumn col_penalty = new TableColumn("财产刑金额"); col_penalty.setCellValueFactory(new PropertyValueFactory<>("penalty"));
		TableColumn col_drugType = new TableColumn("毒品种类"); col_drugType.setCellValueFactory(new PropertyValueFactory<>("drugType"));
		TableColumn col_quantity = new TableColumn("毒品数量"); col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		TableColumn col_contact = new TableColumn("联系方式");  col_contact.setCellValueFactory(new PropertyValueFactory<>("contact"));
		TableColumn col_payment = new TableColumn("支付方式");  col_payment.setCellValueFactory(new PropertyValueFactory<>("payment"));
		TableColumn col_trade = new TableColumn("交易方式");    col_trade.setCellValueFactory(new PropertyValueFactory<>("trade"));
		TableColumn col_transport = new TableColumn("运输方式");    col_transport.setCellValueFactory(new PropertyValueFactory<>("transport"));
		tableViewJudgment.getColumns().addAll(col_caseNumber,col_courtName,col_firstDefendant,col_defendantsCount,col_crime,
				col_propertyPunishment,col_sentence,col_penaltyType, col_penalty,col_drugType,
				col_quantity,col_contact,col_payment,col_trade,col_transport);
		refreshTableJudgment();

		tableViewDefendant.setEditable(true);
		TableColumn col_name = new TableColumn("姓名");   col_name.setCellValueFactory(new PropertyValueFactory<>("name"));
		TableColumn col_gender = new TableColumn("性别"); col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
		TableColumn col_birthDay = new TableColumn("出生日");  col_birthDay.setCellValueFactory(new PropertyValueFactory<>("birthDay"));
		TableColumn col_birthProvince = new TableColumn("省份");  col_birthProvince.setCellValueFactory(new PropertyValueFactory<>("birthProvince"));
		TableColumn col_birthCity = new TableColumn("城市");  col_birthCity.setCellValueFactory(new PropertyValueFactory<>("birthCity"));
		TableColumn col_nation = new TableColumn("民族"); col_nation.setCellValueFactory(new PropertyValueFactory<>("nation"));
		TableColumn col_education = new TableColumn("文化程度");    col_education.setCellValueFactory(new PropertyValueFactory<>("education"));
		TableColumn col_career = new TableColumn("职业"); col_career.setCellValueFactory(new PropertyValueFactory<>("career"));
		TableColumn col_residence = new TableColumn("居住地"); col_residence.setCellValueFactory(new PropertyValueFactory<>("residence"));
		col_residence.setMinWidth(300);
		TableColumn col_judgment = new TableColumn("案号");   col_judgment.setCellValueFactory(new PropertyValueFactory<>("judgment"));
		col_judgment.setMinWidth(200);
		tableViewDefendant.getColumns().addAll(col_name,col_gender,col_birthDay,col_birthProvince,col_birthCity,
				col_nation,col_education,col_career,col_residence,col_judgment);

		tableViewJudgment.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<tableJudgment>() {
			@Override
			public void changed(ObservableValue<? extends tableJudgment> observable, tableJudgment oldValue, tableJudgment newValue) {
				refreshTableDefendant(newValue);
				initD3byJudgment(newValue);
			}
		});
		tableViewDefendant.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<tableDefendant>() {
			@Override
			public void changed(ObservableValue<? extends tableDefendant> observable, tableDefendant oldValue, tableDefendant newValue) {

			}
		});
	}
	private void initD3byJudgment(tableJudgment tablejudgment){

		Session session = HibernateUtil.getSession();
		String hql = "from Judgment where caseNumber ='"+tablejudgment.getCaseNumber()+"'";
		Query query = session.createQuery(hql);
		Judgment judgment = (Judgment) query.uniqueResult();
		hql = "from Defendant where judgment.caseNumber ='"+tablejudgment.getCaseNumber()+"'";
		query = session.createQuery(hql);
		List<Defendant> defendants = query.list();
		PrintHtml printHtml = new PrintHtml();
		String path = printHtml.MakeHtml(defendants,judgment);

		Stage d3Stage = new Stage();
		D3View d3View = new D3View();
		d3View.url = path;
		d3View.start(d3Stage);
	}
	private void initD3byDefendant(tableDefendant tabledefendant){
		Session session = HibernateUtil.getSession();
		String hql = "from Defendant where name = '"+tabledefendant.getName()+"'";
		Query query = session.createQuery(hql);
		Defendant defendant = (Defendant) query.list().get(0);

		hql = "select judgment.caseNumber from Defendant where name = '"+tabledefendant.getName()+"'";
		query = session.createQuery(hql);
		List<String> caseList = query.list();
		List<Judgment> judgmentList = new ArrayList<>();
		for (String caseNumber:caseList
		     ) {
			hql = "from Judgment where caseNumber ='"+caseNumber+"'";
			query = session.createQuery(hql);
			judgmentList.add((Judgment) query.uniqueResult());
		}
		PrintHtml printHtml = new PrintHtml();

//		TODO
//		String path = method(judgmentList, defendant);

		Stage d3Stage = new Stage();
		D3View d3View = new D3View();
//		d3View.url = path;
		d3View.start(d3Stage);
	}
}

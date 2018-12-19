package util;

import model.Defendant;
import model.Judgment;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

public class CSVUtils{
	public void CSV_Write_defendants(List<Defendant> defendants, File file) throws Exception{
//		FileOutputStream fos = new FileOutputStream("CSV/Defendants.csv");
		FileOutputStream fos = new FileOutputStream(file.getPath());
		OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");

		CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("姓名","性别","出生年月","省份","城市","民族","文化程度","职业","居住地","案号");
		CSVPrinter csvPrinter = new CSVPrinter(osw,csvFormat);

		for (int i = 0; i < defendants.size(); i++){
			csvPrinter.printRecord(defendants.get(i).getName(),defendants.get(i).getGender(),defendants.get(i).getBirthDay(),
					defendants.get(i).getBirthProvince(),defendants.get(i).getBirthCity(),defendants.get(i).getNation(),
					defendants.get(i).getEducation(),defendants.get(i).getCareer(),defendants.get(i).getHouseholdReg(),
					defendants.get(i).getJudgment().getCaseNumber());
		}
		
		csvPrinter.flush();
		csvPrinter.close();
	}

	public void CSV_Write_Judgement(List<Judgment> judgments, File file) throws Exception{
//		FileOutputStream fos = new FileOutputStream("CSV/Judgments.csv");
		FileOutputStream fos = new FileOutputStream(file.getPath());
		OutputStreamWriter osw = new OutputStreamWriter(fos,"UTF-8");

		CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("案号","法院名称","一案人数","第一被告人","罪名","刑法种类","刑期","财产刑种类","财产刑金额","毒品种类","毒品数量","联系方式","支付方式","交易方式","运输方式");
		CSVPrinter csvPrinter = new CSVPrinter(osw,csvFormat);
		List<String> stringList= new ArrayList<>();
		for (int i = 0; i < judgments.size(); i++){
			if (judgments.get(i).getContact()==null)
				stringList.add(judgments.get(i).getCaseNumber());
			csvPrinter.printRecord(judgments.get(i).getCaseNumber(),judgments.get(i).getCourtName(),judgments.get(i).getDefendantsCount(),
					judgments.get(i).getFirstDefendant(),judgments.get(i).getCrime(),judgments.get(i).getPropertyPunishment(),
					judgments.get(i).getSentence(),judgments.get(i).getPenaltyType(),judgments.get(i).getPenalty(),
					judgments.get(i).getDrugType(),judgments.get(i).getQuantity(),judgments.get(i).getContact(),
					judgments.get(i).getPayment(),judgments.get(i).getTrade(),judgments.get(i).getTransport());
		}
		csvPrinter.flush();
		csvPrinter.close();
		for (String casenumber:stringList
		     ) {
			System.out.println(casenumber);

		}
	}
}

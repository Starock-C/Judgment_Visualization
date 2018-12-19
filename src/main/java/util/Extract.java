package util;

import model.Defendant;
import model.Judgment;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Extract {
	public void extract(String path, List<Judgment> allJudgments, List<Defendant> allDefendants) throws Exception {
		POI poi = new POI();
		RegexUtils regexUtils = new RegexUtils();

		String rawString = POI.readWord(path);

		Judgment judgment = new Judgment();
		List<Defendant> defendants = new ArrayList<>();

//		String pattern = "(被告人[A-z0-9\\u4e00-\\u9fa5\\，\\。\\、\\；\\（\\）\\.]+\\s+辩护人[A-z0-9\\u4e00-\\u9fa5\\，\\。]+)";
        String pattern = "\\s+(被告人[\\u4e00-\\u9fa5]+(?:，|,|\\(|（)[A-z0-9\\u4e00-\\u9fa5\\，\\。\\○\\、\\；\\：\\(\\)\\（\\）\\.\\·\\,\\“\\”\\-\\－\\;\\s^\\x00-\\xff]+" +
		        "(?:看守所|逮捕|候审|在家|治疗|居住|戒毒|服刑|执行）|日|年|地)。\\s+(?:(?:指定辩护人|辩护人)[A-z0-9\\u4e00-\\u9fa5\\，\\。\\、\\（\\）]+\\。|))";
//		        "(?:日|看守所|候审|在家|治疗|地|居住|戒毒|逮捕|服刑|执行）|年)。\\s+(?:(?:指定辩护人|辩护人)[A-z0-9\\u4e00-\\u9fa5\\，\\。\\、\\（\\）]+\\。|))";

//        String pattern = "(被告人[\\u4e00-\\u9fa5\\w\\W\\s]+(?:(?:指定辩护人|委托辩护人|辩护人)[A-z0-9\\u4e00-\\u9fa5\\，\\。\\、\\（\\）]+\\。|))";

		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(rawString);
		String defendantinfo = "";
		if (m.find()) {
			defendantinfo = m.group(1);
		}else {
			return;
		}

		regexUtils.extractJudgment(judgment,rawString);
		judgment.setCaseNumber(judgment.getCaseNumber().replaceAll("(?:\\（|\\(|\\〔)","("));
		judgment.setCaseNumber(judgment.getCaseNumber().replaceAll("(?:\\）|\\)|\\〕)",")"));
		regexUtils.batchExtractDefendants(defendants,defendantinfo);
		for (Defendant defendanta1:defendants
		     ) {
			defendanta1.setJudgment(judgment);
		}
		judgment.setDefendants(defendants);
		judgment.setFirstDefendant(judgment.getDefendants().get(0).getName());
		judgment.setDefendantsCount(judgment.getDefendants().size());
		allJudgments.add(judgment);
		allDefendants.addAll(defendants);

	}
}

package util;

import model.Defendant;
import model.Judgment;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtils {
	public void extractDefendant(Defendant defendant, String line){
		//汉字字符:[\\u4e00-\\u9fa5]
		//提取被告人信息
		String pattern = "(?:被告人)([\\u4e00-\\u9fa5]+)(?:（|\\(|，|,)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);

		if (m.find()) {
			defendant.setName(m.group(1));
//			System.out.println(m.group(1));
//			System.out.println(m.group(2));
		}

		pattern= "，((?:男|女))，";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()){
			defendant.setGender(m.group(1));
//			System.out.println(m.group(1));
		}


		pattern = "([\\u4e00-\\u9fa5]+族)，";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()){
			defendant.setNation(m.group(1));
//			System.out.println(m.group(1));
		}

		pattern = "((?:高中|初中|小学|大学|大专|中专|半文盲|文盲|专科|本科|研究生|博士生|博士|硕士|职业技术))";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find())
			defendant.setEducation(m.group(1));
		else defendant.setEducation("其他");


		pattern = "((?:无业|农民工|农民|经商|工人|务工|个体户|职工|劳务人员|打工|驾驶员|个体经营|医生|经理|居民|学生|司机))";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find())
			defendant.setCareer(m.group(1));
		else defendant.setCareer("其他");


		pattern = "(\\d+(?:年|月|日)\\d+(?:年|月|日)\\d+(?:年|月|日))(?:生|出生)";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()){
			defendant.setBirthDay(m.group(1));
//			System.out.println(m.group(1));
		}

		pattern = "(?:暂|现暂|现|)(?:住地|住所地|住)([A-z0-9\\u4e00-\\u9fa5]+)";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()){
			defendant.setResidence(m.group(1));
//			System.out.println(m.group(1));
		}else defendant.setResidence("未知");


		pattern = "(?:户籍地|户籍所在地|户籍所在|户籍)(?:：|)([A-z0-9\\u4e00-\\u9fa5]+)";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()){
			defendant.setHouseholdReg(m.group(1));
//			System.out.println(m.group(1));
		}
		if (defendant.getHouseholdReg() == null){
			defendant.setHouseholdReg(defendant.getResidence());
		}

		pattern = "(?:，|于)((?:北京|天津|上海|重庆|河北|山西|辽宁|吉林|黑龙江|江苏|浙江|安徽|福建|江西|山东|河南|湖北|湖南|广东|海南|四川|贵州|云南|陕西|甘肃|青海|台湾|内蒙古|广西壮族|西藏|宁夏回族|新疆维吾尔|香港|澳门)(?:省|自治区|市|特别行政区))([\\u4e00-\\u9fa5]+(?:市|县|区))(?:人，|，)";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()){
			defendant.setBirthProvince(m.group(1));
			defendant.setBirthCity(m.group(2));
//			System.out.println(m.group(1));
//			System.out.println(m.group(2));
		}
		if (defendant.getBirthProvince() == null){
			pattern = "((?:北京|天津|上海|重庆|河北|山西|辽宁|吉林|黑龙江|江苏|浙江|安徽|福建|江西|山东|河南|湖北|湖南|广东|海南|四川|贵州|云南|陕西|甘肃|青海|台湾|内蒙古|广西壮族|西藏|宁夏回族|新疆维吾尔|香港|澳门)(?:省|市|自治区|特别行政区|))";
			r = Pattern.compile(pattern);
			m = r.matcher(line);
			if (m.find())
				defendant.setBirthProvince(m.group(1));
		}
		if (defendant.getBirthCity() == null && defendant.getHouseholdReg() != null){
			pattern = "(?:省|自治区)([\\u4e00-\\u9fa5]+(?:市|县|区))";
			r = Pattern.compile(pattern);
			m = r.matcher(defendant.getHouseholdReg());
			if (m.find())
				defendant.setBirthCity(m.group(1));
			else{
				m = r.matcher(line);
				if (m.find())
					defendant.setBirthCity(m.group(1));
				else defendant.setBirthCity("未知");
			}
			pattern = "((?:北京|天津|上海|重庆|河北|山西|辽宁|吉林|黑龙江|江苏|浙江|安徽|福建|江西|山东|河南|湖北|湖南|广东|海南|四川|贵州|云南|陕西|甘肃|青海|台湾|内蒙古|广西壮族|西藏|宁夏回族|新疆维吾尔|香港|澳门)(?:省|市|自治区|特别行政区|))";
			r = Pattern.compile(pattern);
			m = r.matcher(defendant.getHouseholdReg());
			if (m.find())
				defendant.setBirthProvince(m.group(1));
			else defendant.setBirthProvince("其他");
		}
	}

	public void batchExtractDefendants(List<Defendant> defendants, String lines) {
		if (lines.equals(""))
			return;
		String[] stringList = lines.split("\\n");
		for (String line:stringList
		     ) {
			if (line.equals("") || line.contains("辩护人"))
				continue;
			if (lines.startsWith("被告人")){
				System.out.println(line);
				Defendant defendant = new Defendant();
				extractDefendant(defendant,line);
				if(defendant.getName() != null && defendant.getGender() != null)
					defendants.add(defendant);
			}
		}
		System.out.println(defendants.size());

	}

	public void extractJudgment(Judgment judgment, String line){
		//提取判决书信息（未完）
		String pattern = "((?:\\（|\\(|\\〔)\\d+(?:\\）|\\)|\\〕)[0-9\\u4e00-\\u9fa5]+号)";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(line);
		if (m.find( )) {
			judgment.setCaseNumber(m.group(1));
//			System.out.println(m.group(1));
		}

		pattern = "公诉机关([\\u4e00-\\u9fa5]+)";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			judgment.setCourtName(m.group(1));
//			System.out.println(m.group(1));
		}
//		pattern = "被告人[\\u4e00-\\u9fa5]+犯([\u4e00-\u9fa5]+罪)";
		pattern = "被告人[\\u4e00-\\u9fa5\\w\\W]+((?:贩卖毒品罪|制造毒品罪|容留他人吸毒罪|聚众斗殴罪|盗窃罪|开设赌场罪|运输毒品罪|寻衅滋事罪|非法持有毒品罪|非法种植毒品原植物罪|引诱他人吸毒罪|故意伤害罪|教唆他人吸毒罪|欺骗他人吸毒罪|非法买卖制毒物品罪))";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			judgment.setCrime(m.group(1));
//			System.out.println(m.group(1));
		}else judgment.setCrime("其他");

//		pattern = "判处([\\u4e00-\\u9fa5]+徒刑)([A-z0-9\\u4e00-\\u9fa5]+年)";
		pattern = "((?:有期徒刑|拘役|有期徒|拘投))([A-z0-9\\u4e00-\\u9fa5]+(?:月|年))";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			judgment.setPropertyPunishment(m.group(1));
			judgment.setSentence(m.group(2));
//			System.out.println(m.group(1));
//			System.out.println(m.group(2));
		}else {
			pattern = "(管制([A-z0-9\\u4e00-\\u9fa5]+(?:月|年)))";
			r = Pattern.compile(pattern);
			m = r.matcher(line);
			if (m.find()){
				judgment.setPropertyPunishment(m.group(1));
				judgment.setSentence(m.group(2));
			}
			else{
				judgment.setPropertyPunishment("其他");
				judgment.setSentence("未知");
			}
		}

		pattern = "((?:罚金|没收财产|没收个人财产|没受个人财产))(?:人民币|人民元|人民|美元|欧元|日元|港币|英镑|)([0-9一二两三四五六七八九十百千万\\.\\s]+)(?:元|至[A-z0-9\\u4e00-\\u9fa5\\s]+元|到[A-z0-9\\u4e00-\\u9fa5\\s]+元|)";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			judgment.setPenaltyType(m.group(1));
			judgment.setPenalty(Integer.parseInt(amountConversion(m.group(2))));
//			System.out.println(m.group(1));
			System.out.println(m.group(2));
		}
		else
			judgment.setPenaltyType("其他");


		pattern = "((?:甲基苯丙胺|冰毒|大麻叶|大麻|可卡因|海洛因|吗啡|卡西酮|鸦片|K粉|摇头丸|杜冷丁|古柯|咖啡因|三唑仑|羟基丁酸|氯胺酮|可待因|丙酮|地西泮|美沙酮))(?:约|大约|共计|总计|共|)([A-z0-9一二三四五六七八九十百千万]+(?:克|千克|公斤|斤|吨|毫克|微克|g|kg|mg|ug|t|粒|小粒|小包|包|袋|小袋|块|小块|瓶))";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			judgment.setDrugType(m.group(1));
			judgment.setQuantity(m.group(2));
//			System.out.println(m.group(1));
//			System.out.println(m.group(2));
		}
		else{
			pattern = "([A-z0-9一二两三四五六七八九十百千万]+(?:克|千克|公斤|斤|吨|毫克|微克|g|kg|mg|ug|t|粒|小粒|小包|包|袋|小袋|块|小块|瓶))((?:甲基苯丙胺|冰毒|大麻叶|大麻|可卡因|海洛因|吗啡|卡西酮|鸦片|K粉|摇头丸|杜冷丁|古柯|咖啡因|三唑仑|羟基丁酸|氯胺酮|可待因|丙酮|地西泮|美沙酮))";
			r = Pattern.compile(pattern);
			m = r.matcher(line);
			if (m.find()){
				judgment.setDrugType(m.group(2));
				judgment.setQuantity(m.group(1));
			}
			else {
				pattern = "((?:甲基苯丙胺|冰毒|大麻叶|大麻|可卡因|海洛因|吗啡|卡西酮|鸦片|K粉|摇头丸|杜冷丁|古柯|咖啡因|三唑仑|羟基丁酸|氯胺酮|可待因|丙酮|地西泮|美沙酮))";
				r = Pattern.compile(pattern);
				m = r.matcher(line);
				if (m.find()){
					judgment.setDrugType(m.group(1));
					if (judgment.getDrugType().equals("甲基苯丙胺"))
						judgment.setDrugType("冰毒");
				}else judgment.setDrugType("其他");
			}
		}
		if (judgment.getQuantity() == null)
			judgment.setQuantity("未知");



		pattern = "((?:电话|手机|微信|QQ|Skype|MSN|msn))";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			judgment.setContact(m.group(1));
//			System.out.println(m.group(1));
		}else judgment.setContact("其他");

		pattern = "((?:支付宝|微信转账|现金|银行转账|微信支付))";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			judgment.setPayment(m.group(1));
//			System.out.println(m.group(1));
		}
		else
			judgment.setPayment("现金");

		pattern = "((?:见面交易|快递|埋地雷))";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			judgment.setTrade(m.group(1));
//			System.out.println(m.group(1));
		}else judgment.setTrade("未知");

		pattern = "((?:空运|大巴|物流快递))";
		r = Pattern.compile(pattern);
		m = r.matcher(line);
		if (m.find()) {
			judgment.setTransport(m.group(1));
//			System.out.println(m.group(1));
		}else judgment.setTransport("未知");
	}

	private String amountConversion(String amount){
		String convertedAmount = amount;
		convertedAmount = convertedAmount.replace(" ","");
		if (convertedAmount.matches("[0-9]+"))
			return convertedAmount;
		if (convertedAmount.matches("[\\u4e00-\\u9fa5]+[0-9]+"))
			return convertedAmount.replaceAll("[\\u4e00-\\u9fa5]","");
		if (convertedAmount.matches("[0-9]+至[0-9]+"))
			return convertedAmount.replaceAll("至[0-9]+","");

		String pattern = "((?:[0-9一二两三四五六七八九十百千\\.]+万|))((?:[0-9\\u4e00-\\u9fa5]千|))((?:[0-9\\u4e00-\\u9fa5]百|))";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(convertedAmount);
		if (m.find()){
			int[] num = new int[3];
			for (int i = 2; i <= 3; i++) {
				String str;
				if (m.group(i) != null){
					str = m.group(i).replace("千","");
					str = str.replaceAll("百","");
					switch (str) {
						case "一": num[i - 1] = 1;break;
						case "1": num[i - 1] = 1;break;
						case "二": num[i - 1] = 2;break;
						case "2": num[i - 1] = 2;break;
						case "两": num[i - 1] = 2;break;
						case "三": num[i - 1] = 3;break;
						case "3": num[i - 1] = 3;break;
						case "四": num[i - 1] = 4;break;
						case "4": num[i - 1] = 4;break;
						case "五": num[i - 1] = 5;break;
						case "5": num[i - 1] = 5;break;
						case "六": num[i - 1] = 6;break;
						case "6": num[i - 1] = 6;break;
						case "七": num[i - 1] = 7;break;
						case "7": num[i - 1] = 7;break;
						case "八": num[i - 1] = 8;break;
						case "8": num[i - 1] = 8;break;
						case "九": num[i - 1] = 9;break;
						case "9": num[i - 1] = 9;break;
					}
				} else
					num[i - 1] = 0;
			}
				if (m.group(1) != null){
					pattern = "((?:[0-9\\u4e00-\\u9fa5]千|))((?:[0-9\\u4e00-\\u9fa5]百|))((?:[0-9\\u4e00-\\u9fa5]十|))((?:[0-9\\u4e00-\\u9fa5]万))";
					r = Pattern.compile(pattern);
					m = r.matcher(m.group(1));
					if (m.find()){
						int[] num2 = new int[4];
						for (int j = 1; j <= 4; j++){
							String str;
							if (m.group(j) != null){
								str = m.group(j).replace("万","");
								str = str.replace("千","");
								str = str.replace("百","");
								if (j != 4)
									str = str.replace("十","");
								switch (str){
									case "一": num2[j - 1] = 1;break;
									case "1": num2[j - 1] = 1;break;
									case "二": num2[j - 1] = 2;break;
									case "2": num2[j - 1] = 2;break;
									case "两": num2[j - 1] = 2;break;
									case "三": num2[j - 1] = 3;break;
									case "3": num2[j - 1] = 3;break;
									case "四": num2[j - 1] = 4;break;
									case "4": num2[j - 1] = 4;break;
									case "五": num2[j - 1] = 5;break;
									case "5": num2[j - 1] = 5;break;
									case "六": num2[j - 1] = 6;break;
									case "6": num2[j - 1] = 6;break;
									case "七": num2[j - 1] = 7;break;
									case "7": num2[j - 1] = 7;break;
									case "八": num2[j - 1] = 8;break;
									case "8": num2[j - 1] = 8;break;
									case "九": num2[j - 1] = 9;break;
									case "9": num2[j - 1] = 9;break;
									case "十": num2[j-1] = 10;break;
									case "10": num2[j-1] = 10;break;
								}
							}
							else num2[j-1] = 0;
						}
						num[0] = num2[0]*1000+num2[1]*100+num2[2]*10+num2[3];
					}
				}else num[0] = 0;
			convertedAmount = Integer.toString(num[0]*10000+num[1]*1000+num[2]*100);
			}
		return convertedAmount;
	}
}

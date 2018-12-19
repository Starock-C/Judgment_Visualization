package neo4j;

import model.Defendant;

import java.util.List;

public class JudgmentNode {
	private String relation = "have";
	private String label = "Judgment";
	private String caseNumber;  //案号
	private String courtName;   //法院名称
	private String firstDefendant;
	private int defendantsCount;
	private String crime;       //罪名
	private String propertyPunishment;      //刑法种类
	private String sentence;        //刑期
	private String penaltyType;     //财产刑种类
	private int penalty;     //财产刑金额
	private String drugType;    //毒品种类
	private String quantity;    //毒品数量
	private String contact;     //联系方式
	private String payment;     //支付方式
	private String trade;       //交易方式
	private String transport;   //运输方式

	public String getRelation() {
		return relation;
	}

	public void setRelation(String relation) {
		this.relation = relation;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	public String getCourtName() {
		return courtName;
	}

	public void setCourtName(String courtName) {
		this.courtName = courtName;
	}

	public String getFirstDefendant() {
		return firstDefendant;
	}

	public void setFirstDefendant(String firstDefendant) {
		this.firstDefendant = firstDefendant;
	}

	public int getDefendantsCount() {
		return defendantsCount;
	}

	public void setDefendantsCount(int defendantsCount) {
		this.defendantsCount = defendantsCount;
	}

	public String getCrime() {
		return crime;
	}

	public void setCrime(String crime) {
		this.crime = crime;
	}

	public String getPropertyPunishment() {
		return propertyPunishment;
	}

	public void setPropertyPunishment(String propertyPunishment) {
		this.propertyPunishment = propertyPunishment;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String getPenaltyType() {
		return penaltyType;
	}

	public void setPenaltyType(String penaltyType) {
		this.penaltyType = penaltyType;
	}

	public int getPenalty() {
		return penalty;
	}

	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}

	public String getDrugType() {
		return drugType;
	}

	public void setDrugType(String drugType) {
		this.drugType = drugType;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}
}

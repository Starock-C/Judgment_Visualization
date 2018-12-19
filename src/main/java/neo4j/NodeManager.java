package neo4j;

import model.Judgment;
import org.neo4j.driver.v1.*;

import java.util.ArrayList;
import java.util.List;

public class NodeManager {
	public static void InitNode(Judgment judgment){
		Driver driver = GraphDatabase.driver("bolt://localhost;11001", AuthTokens.basic("neo4j","123456"));
		JudgmentNode judgmentNode = new JudgmentNode();
		judgmentNode.setCaseNumber(judgment.getCaseNumber());
		judgmentNode.setCourtName(judgment.getCourtName());
		judgmentNode.setCrime(judgment.getCrime());
		judgmentNode.setFirstDefendant(judgment.getFirstDefendant());
		judgmentNode.setDefendantsCount(judgment.getDefendantsCount());
		judgmentNode.setPropertyPunishment(judgment.getPropertyPunishment());
		judgmentNode.setSentence(judgment.getSentence());
		judgmentNode.setPenaltyType(judgment.getPenaltyType());
		judgmentNode.setPenalty(judgment.getPenalty());
		judgmentNode.setDrugType(judgment.getDrugType());
		judgmentNode.setContact(judgment.getContact());
		judgmentNode.setPayment(judgment.getPayment());
		judgmentNode.setQuantity(judgment.getQuantity());
		judgmentNode.setTrade(judgment.getTrade());
		judgmentNode.setTransport(judgment.getTransport());

		List<DefendantNode> defendantNodeList = new ArrayList<>();
		for (int i = 0; i < judgment.getDefendants().size(); i++){
			DefendantNode defendantNode = new DefendantNode();
			defendantNode.setName(judgment.getDefendants().get(i).getName());
			defendantNode.setGender(judgment.getDefendants().get(i).getGender());
			defendantNode.setBirthDay(judgment.getDefendants().get(i).getBirthDay());
			defendantNode.setBirthProvince(judgment.getDefendants().get(i).getBirthProvince());
			defendantNode.setBirthCity(judgment.getDefendants().get(i).getBirthCity());
			defendantNode.setEducation(judgment.getDefendants().get(i).getEducation());
			defendantNode.setCareer(judgment.getDefendants().get(i).getCareer());
			defendantNode.setHouseholdReg(judgment.getDefendants().get(i).getHouseholdReg());
			defendantNode.setNation(judgment.getDefendants().get(i).getNation());
			defendantNode.setCaseNumber(judgment.getDefendants().get(i).getJudgment().getCaseNumber());
			defendantNodeList.add(defendantNode);

		}
		Session session = driver.session();
		Transaction transaction = session.beginTransaction();
//		transaction.run("create(judgment:"+judgmentNode.getLabel()+"{CaseNumber:{CaseNumber}," +
//				"CourtName:{CourtName}, Crime:{Crime}, FirstDefendant:{FirstDefendant},DefendantsCount:{DefendantsCount}," +
//				"})
	}
}

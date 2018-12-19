package util;

import model.Defendant;
import model.Judgment;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.*;

public class DBUtils {
	public void addDefendant(Defendant defendant){
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.save(defendant);
			transaction.commit();
		}catch (Exception e){
			e.printStackTrace();
			transaction.rollback();
		}
		session.close();
	}

	public void addJudgment(Judgment judgment){
		Session session = HibernateUtil.getSession();
		Transaction transaction = session.beginTransaction();
		try {
			session.save(judgment);
			transaction.commit();
		}catch (Exception e){
			e.printStackTrace();
			transaction.rollback();
		}
		session.close();
	}

	public List<Judgment> listAllJudgments(){
		Session session = HibernateUtil.getSession();
		String hql = "from Judgment";
		Query query = session.createQuery(hql);
		return query.list();

	}

	public List<Defendant> listAllDefendants(){
		Session session = HibernateUtil.getSession();
		String hql = "from Defendant";
		Query query = session.createQuery(hql);
		return query.list();
	}

	public List<Defendant> listDefendants(Judgment judgment){
		Session session = HibernateUtil.getSession();
		String hql = "from Defendant where judgment = '"+judgment.getCaseNumber()+"'";
		Query query = session.createQuery(hql);
		return query.list();
	}

	public List<Judgment> searchJudgmentsByKeyword(String keyword){
		Session session = HibernateUtil.getSession();
		String hql = "from Judgment where concat(caseNumber, courtName, firstDefendant, defendantsCount, crime," +
				" propertyPunishment, sentence, penaltyType, penalty, drugType," +
				" quantity, contact, payment, trade, transport) like '%"+keyword+"%'";
		Query query = session.createQuery(hql);
		return query.list();
	}

	public List<String> generateKeys(String table, String column){
		List<String> list = new ArrayList<>();
		Session session = HibernateUtil.getSession();
		String hql = "select distinct "+column+" from "+table;
		Query query = session.createQuery(hql);
		list = query.list();
		return list;
	}
	public int getValue(String table, String column, String key){
		Session session = HibernateUtil.getSession();
		String hql = "select count("+column+") from "+table+" where "+column+" = '"+key+"'";
		Query query = session.createQuery(hql);
		return Integer.parseInt(query.uniqueResult().toString());
	}

	public Map<String, Integer> generateMap(String table, String column){
		Map<String, Integer> map = new Map<String, Integer>() {
			@Override
			public int size() {
				return 0;
			}

			@Override
			public boolean isEmpty() {
				return false;
			}

			@Override
			public boolean containsKey(Object key) {
				return false;
			}

			@Override
			public boolean containsValue(Object value) {
				return false;
			}

			@Override
			public Integer get(Object key) {
				return null;
			}

			@Override
			public Integer put(String key, Integer value) {
				return null;
			}

			@Override
			public Integer remove(Object key) {
				return null;
			}

			@Override
			public void putAll(Map<? extends String, ? extends Integer> m) {

			}

			@Override
			public void clear() {

			}

			@Override
			public Set<String> keySet() {
				return null;
			}

			@Override
			public Collection<Integer> values() {
				return null;
			}

			@Override
			public Set<Entry<String, Integer>> entrySet() {
				return null;
			}

			@Override
			public boolean equals(Object o) {
				return false;
			}

			@Override
			public int hashCode() {
				return 0;
			}
		};
		Session session = HibernateUtil.getSession();
		String hql = "select distinct "+column+" from "+table;
		Query query = session.createQuery(hql);
		for (String key:(List<String>)query.list()) {
			hql = "select count("+column+") from "+table+" where "+column+" = '"+key+"'";
			query = session.createQuery(hql);
			map.put(key,Integer.parseInt(query.uniqueResult().toString()));
		}
		return map;
	}
}

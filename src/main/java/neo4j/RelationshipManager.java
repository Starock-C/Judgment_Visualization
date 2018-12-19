package neo4j;

import org.neo4j.driver.v1.*;

public class RelationshipManager {
	public static void createRelationShip(){
		Driver driver = GraphDatabase.driver("bolt://localhost;11001", AuthTokens.basic("neo4j","123456"));
		Session session = driver.session();
		Transaction transaction = session.beginTransaction();
	}
}

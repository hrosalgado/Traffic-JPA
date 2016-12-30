package SI_ESEI.Traffic;

import static SI_ESEI.Traffic.TransactionUtils.doTransaction;
import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class KmPointTest extends SQLBasedTest{
	private static EntityManagerFactory emf;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		emf = Persistence.createEntityManagerFactory("traffic");
	}
	
	@After
	public void renewConnectionAfterTest() throws ClassNotFoundException, SQLException{
		super.renewConnection();
	}
	
	// Create
	@Test
	public void testCreateKmPoint() throws SQLException{
		// Prepare database for test
		final KmPoint kmpoint = new KmPoint();
		
		doTransaction(emf, em -> {
			kmpoint.setStart(12);
			kmpoint.setEnd(13);
			kmpoint.setblackPoint(true);
			kmpoint.setSignposting(true);
			kmpoint.setRadar(false);
			
			em.persist(kmpoint);
		});
		
		// Check
		Statement statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement
				.executeQuery("SELECT COUNT(*) as total FROM kmpoint WHERE id = " + kmpoint.getId());
		resultSet.next();
		
		assertEquals(1, resultSet.getInt("total"));
	}
	
	// Read
	@Test
	public void testFindById() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement
				.executeUpdate("INSERT INTO kmpoint(start, end, black_point, signposting, radar)"
						+ "VALUES(12, 13, 1, 1, 0)", Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		// Test code
		KmPoint kmpoint = emf.createEntityManager().find(KmPoint.class, id);
		
		// Check
		assertEquals(id, kmpoint.getId());
		assertEquals(12, kmpoint.getStart());
	}
	
	// Update
	@Test
	public void testUpdateKmPoint() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO kmpoint(start, end, black_point, signposting, radar)"
				+ "VALUES(12, 13, 1, 1, 0)", Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		// Test code
		doTransaction(emf, em -> {
			KmPoint kmpoint = em.find(KmPoint.class, id);
			kmpoint.setStart(13);
			kmpoint.setEnd(14);
			kmpoint.setblackPoint(true);
			kmpoint.setSignposting(true);
			kmpoint.setRadar(true);
		});
		
		// Check
		statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM kmpoint WHERE id = " + id);
		resultSet.next();
		
		assertEquals(id, resultSet.getInt("id"));
		assertEquals(14, resultSet.getInt("end"));
		assertEquals(5, resultSet.getInt("radar"));
	}
	
	// Delete
	@Test
	public void testDeleteKmPoint() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO kmpoint(start, end, black_point, signposting, radar)"
				+ "VALUES(12, 13, 1, 1, 0)", Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		// Test code
		doTransaction(emf, em -> {
			KmPoint kmpoint = em.find(KmPoint.class, id);
			em.remove(kmpoint);
		});
		
		// Check
		statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT count(*) as total FROM kmpoint WHERE id = " + id);
		resultSet.next();
		
		assertEquals(0, resultSet.getInt("total"));
	}
	
	// List
	@Test
	public void testListKmPoint() throws SQLException{
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO kmpoint(start, end, black_point, signposting, radar)"
				+ "VALUES(12, 13, 1, 1, 0)", Statement.RETURN_GENERATED_KEYS);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate("INSERT INTO kmpoint(start, end, black_point, signposting, radar)"
				+ "VALUES(22, 23, 2, 2, 6)", Statement.RETURN_GENERATED_KEYS);
		
		List<KmPoint> kmpoints = emf.createEntityManager()
				.createQuery("SELECT kmpoint FROM KmPoint kmpoint ORDER BY kmpoint.start", KmPoint.class)
				.getResultList();
		
		assertEquals(2, kmpoints.size());
		assertEquals(12, kmpoints.get(0).getStart());
		assertEquals(22, kmpoints.get(1).getStart());
	}
}
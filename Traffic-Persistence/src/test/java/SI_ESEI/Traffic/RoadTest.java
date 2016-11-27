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
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RoadTest extends SQLBasedTest{
	private static EntityManagerFactory emf;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("traffic");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(emf != null && emf.isOpen()){
			emf.close();
		}
	}
	
	@After
	public void renewConnectionAfterTest() throws ClassNotFoundException, SQLException {
		super.renewConnection();
	}
	
	// Create
	@Test
	public void testCreateRoad() throws SQLException{
		// Prepare database for test
		final Road road = new Road();
		
		doTransaction(emf, em -> {
			road.setMaxSpeed(120);
			road.setName("A-52");
			road.setToll(false);
			road.setType("Highway");
			
			em.persist(road);
		});
		
		// Check
		Statement statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT COUNT(*) as total FROM road WHERE id = " + road.getId());
		resultSet.next();
		
		assertEquals(1, resultSet.getInt("total"));
	}
	
	// Read
	@Test
	public void testFindRoadById() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO road(max_speed, name, toll, type) "
				+ "VALUES(120, 'AP-9', 1, 'Motorway')",
				Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		// Test code
		Road road = emf.createEntityManager().find(Road.class, id);
		
		// Check
		assertEquals(id, road.getId());
		assertEquals(120, road.getMaxSpeed());
		assertEquals("AP-9", road.getName());
	}
	
	// Update
	@Test
	public void testUpdateRoad() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO road(max_speed, name, toll, type) "
				+ "VALUES(120, 'AP-9', 1, 'Motorway')",
				Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		// Test code
		doTransaction(emf, em -> {
			Road road = em.find(Road.class, id);
			road.setMaxSpeed(100);
		});
		
		// Check
		statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT * FROM road WHERE id = " + id);
		resultSet.next();
		
		assertEquals(id, resultSet.getInt("id"));
		assertEquals(100, resultSet.getInt("max_speed"));
	}
	
	// Delete
	@Test
	public void testDeleteRoad() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO road(max_speed, name, toll, type) "
				+ "VALUES(120, 'AP-9', 1, 'Motorway')",
				Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		// Test code
		doTransaction(emf, em -> {
			Road road = em.find(Road.class, id);
			em.remove(road);
		});
		
		// Check
		statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT COUNT(*) as total FROM road WHERE id = " + id);
		resultSet.next();
		
		assertEquals(0, resultSet.getInt("total"));
	}
	
	// List
	@Test
	public void testListRoad() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("DELETE FROM road");
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO road(max_speed, name, toll, type) "
				+ "VALUES(120, 'A-52', 0, 'Highway')",
				Statement.RETURN_GENERATED_KEYS);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO road(max_speed, name, toll, type) "
				+ "VALUES(120, 'AP-9', 1, 'Motorway')",
				Statement.RETURN_GENERATED_KEYS);
		
		List<Road> roads = emf.createEntityManager()
				.createQuery("SELECT road FROM Road road ORDER BY road.type", Road.class)
				.getResultList();
		
		// Check
		assertEquals(2, roads.size());
		assertEquals("A-52", roads.get(0).getName());
		assertEquals("Motorway", roads.get(1).getType());
	}
}
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

public class DriverTest extends SQLBasedTest{
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
	public void testCreateDriver() throws SQLException{
		// Prepare database for test
		final Driver driver = new Driver();
		
		doTransaction(emf, em -> {
			driver.setSex("Female");
			driver.setAge(22);
			driver.setExperience(0);
			driver.setPreviousInfractions(0);
			driver.setIll("No");
			em.persist(driver);
		});
		
		// Check
		Statement statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT COUNT(*) as total FROM driver WHERE id = " + driver.getId());
		resultSet.next();
		
		assertEquals(1, resultSet.getInt("total"));
	}
	
	// Read
	@Test
	public void testFindDriverById() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
					"INSERT INTO driver(age, sex, experience, previous_infractions, ill) "
					+ "VALUES(30, 0, 7, 2, 0)",
					Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);

		// Test code
		Driver driver = emf.createEntityManager().find(Driver.class, id);
		
		// Check
		assertEquals(id, driver.getId());
		assertEquals(30, driver.getAge());
		assertEquals(7, driver.getExperience());
	}
	
	// Update
	@Test
	public void testUpdateDriver() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
					"INSERT INTO driver(age, sex, experience, previous_infractions, ill) "
					+ "VALUES(30, 0, 7, 2, 0)",
					Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		// Test code
		doTransaction(emf, em -> {
			Driver driver = em.find(Driver.class, id);
			driver.setAge(24);
			driver.setExperience(4);
		});
		
		// Check
		statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT * FROM driver WHERE id = " + id);
		resultSet.next();
		
		assertEquals(id, resultSet.getInt("id"));
		assertEquals(24, resultSet.getInt("age"));
		assertEquals(4, resultSet.getInt("experience"));
	}
	
	// Delete
	@Test
	public void testDeleteDriver() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
					"INSERT INTO driver(age, sex, experience, previous_infractions, ill) "
					+ "VALUES(30, 0, 7, 2, 0)",
					Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		// Test code
		doTransaction(emf, em -> {
			Driver driver = em.find(Driver.class, id);
			em.remove(driver);
		});
		
		// Check
		statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT count(*) as total FROM driver WHERE id = " + id);
		resultSet.next();
		
		assertEquals(0, resultSet.getInt("total"));
	}
	
	// List
	@Test
	public void testListDriver() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate("DELETE FROM driver");
		statement.executeUpdate(
					"INSERT INTO driver(age, sex, experience, previous_infractions, ill) "
					+ "VALUES(30, 0, 7, 2, 0)",
					Statement.RETURN_GENERATED_KEYS);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
					"INSERT INTO driver(age, sex, experience, previous_infractions, ill) "
					+ "VALUES(24, 1, 4, 0, 0)",
					Statement.RETURN_GENERATED_KEYS);
		
		List<Driver> drivers = emf.createEntityManager()
				.createQuery("SELECT driver FROM Driver driver ORDER BY driver.age", Driver.class)
				.getResultList();
		
		// Check
		assertEquals(2, drivers.size());
		assertEquals(24, drivers.get(0).getAge());
		assertEquals(30, drivers.get(1).getAge());
	}
}

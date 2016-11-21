package SI_ESEI.Traffic;

import static SI_ESEI.Traffic.TransactionUtils.doTransaction;
import static org.junit.Assert.assertEquals;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
		emf = Persistence.createEntityManagerFactory("si-database");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		if(emf!=null && emf.isOpen()) emf.close();
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
			driver.setSex(false);
			driver.setAge(22);
			driver.setExperience(0);
			driver.setPreviousInfractions(0);
			driver.setIll(false);
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
	public void testFindById() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
					"INSERT INTO driver(age, sex, experience, previousInfractions, illness) "
					+ "VALUES(30, 0, 7, 2, 0)",
					Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);

		// Test code
		Driver driver = emf.createEntityManager().find(Driver.class, id);
		System.out.println(driver);
		
		// Check
		assertEquals(30, driver.getAge());
		assertEquals(id, driver.getId());
	}
}

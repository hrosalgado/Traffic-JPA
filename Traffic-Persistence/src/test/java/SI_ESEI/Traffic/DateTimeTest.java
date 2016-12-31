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

public class DateTimeTest extends SQLBasedTest{
	private static EntityManagerFactory emf;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		emf = Persistence.createEntityManagerFactory("traffic");
	}
	
	@AfterClass
	public static void tearDoownAfterClass() throws Exception{
		if(emf != null && emf.isOpen()){
			emf.close();
		}
	}
	
	@After
	public void renewConnectionAfterTest() throws ClassNotFoundException, SQLException{
		super.renewConnection();
	}
	
	// Create
	@Test
	public void testCreateDateTime() throws SQLException{
		// Prepare database for test
		final DateTime dateTime = new DateTime();
		
		doTransaction(emf, em-> {
			dateTime.setDay(2);
			dateTime.setMonth(5);
			dateTime.setYear(2010);
			dateTime.setHour(5);
			dateTime.setSeason("autumn");
			dateTime.setWeather("sunny");
			dateTime.setWeekend("Yes");
			dateTime.setHoliday("Yes");
			
			em.persist(dateTime);
		});
		
		// Check
		Statement statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT COUNT(*) as total FROM datetime WHERE id = " + dateTime.getId());
		resultSet.next();
		
		assertEquals(1, resultSet.getInt("total"));
	}
	
	
	// Read
	@Test
	public void testFindById() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO datetime(day, month, year, hour, season, weather, weekend, holiday)"
				+ "VALUES(20, 7, 2007, 20, 'summer', 'rainy', 0, 0)",
				Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		// Test code
		DateTime date = emf.createEntityManager().find(DateTime.class, id);
		
		// Check
		assertEquals(id, date.getId());
		assertEquals(20, date.getDay());
	}
	
	// Update
	@Test
	public void testUpdateDriver() throws SQLException{
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO datetime(day, month, year, hour, season, weather, weekend, holiday)"
				+ "VALUES(20, 7 ,2007, 17, 'summer', 'rainy', 0, 0)",
				Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			DateTime dateTime = em.find(DateTime.class, id);
			dateTime.setDay(20);
			dateTime.setMonth(10);
			dateTime.setYear(2015);
			dateTime.setHour(9);
			dateTime.setSeason("summer");
			dateTime.setWeather("sunny");
			dateTime.setWeekend("Yes");
			dateTime.setHoliday("No");
		});
		
		statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT * FROM datetime WHERE id = "+id);
		resultSet.next();
		
		assertEquals(id, resultSet.getInt("id"));
		assertEquals(10, resultSet.getInt("month"));
		assertEquals(2015, resultSet.getInt("year"));
	}
	
	// Delete
	@Test
	public void testDeleteDateTime() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO datetime(day, month, year, hour, season, weather, weekend, holiday)"
				+ "VALUES(20, 7, 2007, 8, 'summer', 'rainy', 0, 0)",
				Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		// Test code
		doTransaction(emf, em-> {
			DateTime dateTime = em.find(DateTime.class, id);
			em.remove(dateTime);
		});
		
		// Check
		statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT count(*) as total FROM datetime WHERE id = "+id);
		resultSet.next();
		
		assertEquals(0, resultSet.getInt("total"));
	}
	
	// List
	@Test
	public void testListDateTime() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO datetime(day, month, year, hour, season, weather, weekend, holiday)"
				+ "VALUES(20, 7, 2007, 4, 'summer', 'rainy', 0, 0)",
				Statement.RETURN_GENERATED_KEYS
				);
		
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO datetime(day, month, year, hour, season, weather, weekend, holiday)"
				+ "VALUES(28, 12, 2010, 16, 'autumn', 'sunny', 1, 0)",
				Statement.RETURN_GENERATED_KEYS
				);
		
		List<DateTime> dateTimes = emf.createEntityManager()
				.createQuery("SELECT datetime FROM DateTime datetime ORDER BY datetime.year", DateTime.class)
				.getResultList();
		
		// Check
		assertEquals(2, dateTimes.size());
		assertEquals(20, dateTimes.get(0).getDay());
		assertEquals(28, dateTimes.get(1).getDay());
		
	}
}

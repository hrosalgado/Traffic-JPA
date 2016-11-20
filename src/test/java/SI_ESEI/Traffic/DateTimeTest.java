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

public class DateTimeTest extends SQLBasedTest{

	private static EntityManagerFactory emf;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		emf = Persistence.createEntityManagerFactory("si-database");
	}
	
	@AfterClass
	public static void tearDoownAfterClass() throws Exception {
		if(emf!=null && emf.isOpen()) emf.close();
	}
	
	@After
	public void renewConnectionAfterTest() throws ClassNotFoundException, SQLException {
		super.renewConnection();
	}
	
	//Create
	@Test
	public void testCreateDateTime() throws SQLException{
		final DateTime dateTime = new DateTime();
		
		doTransaction(emf, em-> {
			dateTime.setDay(2);
			dateTime.setMonth(5);
			dateTime.setYear(2010);
			dateTime.setHour(5);
			dateTime.setSeason("autum");
			dateTime.setWeather("sunny");
			dateTime.setWeekend("Si");
			dateTime.setHoliday("Si");
		});
		
		//Check
		Statement statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT COUNT(*) as total FROM datetime WHERE id = " + dateTime.getIdDateTime());
		resultSet.next();
		
		assertEquals(1, resultSet.getInt("total"));
	}
	
	
	//Read
	@Test
	public void testFindById() throws SQLException{
		
		Statement statement = jdbcConnection.createStatement();
		int id = statement.executeUpdate(
				"INSERT INTO datetime(day, month, year, hour, season, weather, weekend, holiday)"
				+ "VALUES(20,7,7,'summer', 'rainy', 'No', 'No')",
				Statement.RETURN_GENERATED_KEYS);
		
		System.out.println("ID: " + id);
		
		//Test code
		DateTime date = emf.createEntityManager().find(DateTime.class, id);
		System.out.println(date);
		
		//Check
		assertEquals(20, date.getDay());
		assertEquals(id, date.getIdDateTime());
	}
}

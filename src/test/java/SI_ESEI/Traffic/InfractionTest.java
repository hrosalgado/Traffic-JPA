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

public class InfractionTest extends SQLBasedTest{

	private static EntityManagerFactory emf;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		emf = Persistence.createEntityManagerFactory("traffic");
	}
	
	@After
	public void renewConnectionAfterTest() throws ClassNotFoundException, SQLException{
		super.renewConnection();
	}
	
	//Create
	@Test
	public void testCreateInfraction() throws SQLException{
		final Infraction infraction = new Infraction();
		
		doTransaction(emf, em-> {
			infraction.setType("high");
			infraction.setDescription("No se detuvo en un stop");
			infraction.setPenalty(100);
			
			em.persist(infraction);
		});
		
		//Check
		Statement statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT COUNT(*) as total FROM infraction WHERE id = " + infraction.getIdInfraction());
		resultSet.next();
		
		assertEquals(1, resultSet.getInt("total"));
	}
	
	//Read
	@Test
	public void testFindById() throws SQLException{
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO infraction(type, description, penalty)"
				+ "VALUES('high', 'No se detuvo en un stop', 100)",
				Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em-> {
			Infraction infraction = em.find(Infraction.class, id);
			infraction.setType("high");
			infraction.setDescription("No se detuvo en un stop");
			infraction.setPenalty(100);
		});
		
		statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT * FROM infraction WHERE id = "+ id);
		resultSet.next();
		
		assertEquals(id, resultSet.getInt("id"));
		assertEquals("high", resultSet.getInt("type"));
		assertEquals(100, resultSet.getInt("penalty"));
	}
	
	//Delete
	@Test
	public void testDeleteInfraction() throws SQLException{
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO infraction(type, description, penalty)"
				+ "VALUES('high', 'No se detuvo en un stop', 100)",
				Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		doTransaction(emf, em -> {
			Infraction infraction = em.find(Infraction.class, id);
			em.remove(infraction);
		});
		
		statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT count(*) as total FROM infraction WHERE id = "+id);
		resultSet.next();
		
		assertEquals(0, resultSet.getInt("total"));
	}
	
	//List
	@Test
	public void testListInfraction() throws SQLException{
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO infraction(type, description, penalty)"
				+ "VALUES('high', 'No se detuvo en un stop', 100)",
				Statement.RETURN_GENERATED_KEYS
				);
		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO infraction(type, description, penalty)"
				+ "VALUES('low', 'No se detuvo en un semaforo', 100)",
				Statement.RETURN_GENERATED_KEYS
				);
		
		List<Infraction> infractions = emf.createEntityManager()
				.createQuery("SELECT infraction FROM Infraction infraction ORDER BY infraction.type", Infraction.class)
				.getResultList();
		
		assertEquals(2, infractions.size());
		assertEquals("high", infractions.get(0).getType());
		assertEquals(100, infractions.get(1).getPenalty());
	}
}

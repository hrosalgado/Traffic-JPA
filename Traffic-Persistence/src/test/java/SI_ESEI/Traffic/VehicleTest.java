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

public class VehicleTest extends SQLBasedTest{
private static EntityManagerFactory emf;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception{
		emf = Persistence.createEntityManagerFactory("traffic");
	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception{
		if(emf!=null && emf.isOpen()) emf.close();
	}
	
	@After
	public void renewConnectionAfterTest() throws ClassNotFoundException, SQLException{
		super.renewConnection();
	}
	
	// Create
	@Test
	public void testCreateVehicle() throws SQLException{
		// Prepare database for test
		final Vehicle vehicle = new Vehicle();
		
		doTransaction(emf, em -> {
			vehicle.setAntiquity(4);
			vehicle.setBrand("BMW");
			vehicle.setCarSpaces(2);
			vehicle.setDrivePermission(true);
			vehicle.setElectric(false);
			vehicle.setPassengers(2);
			vehicle.setType("Car");
			
			em.persist(vehicle);
		});
		
		// Check
		Statement statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT COUNT(*) as total FROM vehicle WHERE id = " + vehicle.getId());
		resultSet.next();
		
		assertEquals(1, resultSet.getInt("total"));
	}
	
	// Read
	@Test
	public void testFindVehicleById() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO vehicle(type, brand, car_spaces, passengers, antiquity, drive_permission, electric) "
				+ "VALUES('Car', 'Audi', 5, 2, 1, 0, 0)",
				Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		// Test code
		Vehicle vehicle = emf.createEntityManager().find(Vehicle.class, id);
		
		// Check
		assertEquals(id, vehicle.getId());
		assertEquals("Audi", vehicle.getBrand());
		assertEquals(2, vehicle.getPassengers());
	}
	
	// Update
	@Test
	public void testUpdateVehicle() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO vehicle(type, brand, car_spaces, passengers, antiquity, drive_permission, electric) "
				+ "VALUES('Car', 'Audi', 5, 2, 1, 1, 0)",
				Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		// Test code
		doTransaction(emf, em -> {
			Vehicle vehicle = em.find(Vehicle.class, id);
			vehicle.setPassengers(1);
		});
		
		// Check
		statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(""
				+ "SELECT * FROM vehicle WHERE id = " + id);
		resultSet.next();
		
		assertEquals(id, resultSet.getInt("id"));
		assertEquals(1, resultSet.getInt("passengers"));
	}
	
	// Delete
	@Test
	public void testDeleteVehicle() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO vehicle(type, brand, car_spaces, passengers, antiquity, drive_permission, electric) "
				+ "VALUES('Car', 'Audi', 5, 2, 1, 1, 0)",
				Statement.RETURN_GENERATED_KEYS);
		
		int id = getLastInsertedId(statement);
		
		// Test code
		doTransaction(emf, em -> {
			Vehicle vehicle = em.find(Vehicle.class, id);
			em.remove(vehicle);
		});
		
		// Check
		statement = jdbcConnection.createStatement();
		ResultSet resultSet = statement.executeQuery(
				"SELECT COUNT(*) as total FROM vehicle WHERE id = " + id);
		resultSet.next();
		
		assertEquals(0, resultSet.getInt("total"));
	}
	
	// List
	@Test
	public void testListVehicle() throws SQLException{
		// Prepare database for test
		Statement statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO vehicle(type, brand, car_spaces, passengers, antiquity, drive_permission, electric) "
				+ "VALUES('Car', 'Audi', 5, 2, 1, 1, 0)",
				Statement.RETURN_GENERATED_KEYS);

		statement = jdbcConnection.createStatement();
		statement.executeUpdate(
				"INSERT INTO vehicle(type, brand, car_spaces, passengers, antiquity, drive_permission, electric) "
				+ "VALUES('Car', 'Lamborgini', 2, 1, 4, 1, 0)",
					Statement.RETURN_GENERATED_KEYS);
		
		List<Vehicle> vehicles = emf.createEntityManager()
				.createQuery("SELECT vehicle FROM Vehicle vehicle ORDER BY vehicle.type", Vehicle.class)
				.getResultList();
		
		// Check
		assertEquals(2, vehicles.size());
		assertEquals("Audi", vehicles.get(0).getBrand());
		assertEquals("Lamborgini", vehicles.get(1).getBrand());
	}
}

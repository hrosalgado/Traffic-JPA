package SI_ESEI.Traffic.webapp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transaction;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import SI_ESEI.Traffic.Driver;
import SI_ESEI.Traffic.TransactionUtils;
import SI_ESEI.Traffic.Vehicle;
import SI_ESEI.Traffic.webapp.util.DesktopEntityManagerManager;

public class VehiclesVM{
	// Vehicle under edition...
	private Vehicle currentVehicle = null;
	
	public Vehicle getCurrentVehicle(){
		return currentVehicle;
	}
	
	public List<Driver> getDrivers(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT d FROM Driver d", Driver.class).getResultList();
	}
	
	public List<Vehicle> getVehicles(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT v FROM Vehicle v", Vehicle.class).getResultList();
	}
	
	@Command
	@NotifyChange("vehicles")
	public void delete(@BindingParam("v") Vehicle vehicle){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(vehicle);
		});
	}
	
	@Command
	@NotifyChange("currentVehicle")
	public void newVehicle(){
		this.currentVehicle = new Vehicle();
	}
	
	@Command
	@NotifyChange({"vehicles", "currentVehicle"})
	public void save(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentVehicle);
		});
		this.currentVehicle = null;
	}
	
	@Command
	@NotifyChange("currentVehicle")
	public void cancel(){
		this.currentVehicle = null;
	}
	
	@Command
	@NotifyChange("currentVehicle")
	public void edit(@BindingParam("v") Vehicle vehicle){
		this.currentVehicle = vehicle;
	}
}
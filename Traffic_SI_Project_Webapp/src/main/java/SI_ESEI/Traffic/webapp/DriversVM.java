package SI_ESEI.Traffic.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import SI_ESEI.Traffic.Driver;
import SI_ESEI.Traffic.TransactionUtils;
import SI_ESEI.Traffic.webapp.util.DesktopEntityManagerManager;

public class DriversVM{
	private Driver currentDriver = null;
	
	public Driver getCurrentDriver(){
		return currentDriver;
	}
	
	public List<Driver> getDrivers(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT d FROM Driver d", Driver.class).getResultList();
	}
	
	@Command
	@NotifyChange("drivers")
	public void delete(@BindingParam("d") Driver driver){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(driver);
		});
	}
	
	@Command
	@NotifyChange("currentDriver")
	public void newDriver(){
		this.currentDriver = new Driver();
	}
	
	@Command
	@NotifyChange({"drivers", "currentDriver"})
	public void save(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentDriver);
		});
		this.currentDriver = null;
	}
	
	@Command
	@NotifyChange("currentDriver")
	public void cancel(){
		this.currentDriver = null;
	}
	
	@Command
	@NotifyChange("currentDriver")
	public void edit(@BindingParam("d") Driver driver){
		this.currentDriver = driver;
	}
}

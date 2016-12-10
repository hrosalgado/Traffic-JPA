package SI_ESEI.Traffic.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import SI_ESEI.Traffic.DateTime;
import SI_ESEI.Traffic.Driver;
import SI_ESEI.Traffic.Infraction;
import SI_ESEI.Traffic.Road;
import SI_ESEI.Traffic.TransactionUtils;
import SI_ESEI.Traffic.webapp.util.DesktopEntityManagerManager;

public class InfractionsVM{
	private Infraction currentInfraction = null;

	public Infraction getCurrentInfraction(){
		return currentInfraction;
	}
	
	public List<Driver> getDrivers(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT d FROM Driver d", Driver.class).getResultList();
	}
	
	public List<DateTime> getDateTimes(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT dt FROM DateTime dt", DateTime.class).getResultList();
	}
	
	public List<Road> getRoads(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT r FROM Road r", Road.class).getResultList();
	}
	
	public List<Infraction> getInfractions(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT i FROM Infraction i", Infraction.class).getResultList();
	}
	
	@Command
	@NotifyChange("infractions")
	public void delete(@BindingParam("i") Infraction infraction){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(infraction);
		});
	}
	
	@Command
	@NotifyChange("currentInfraction")
	public void newInfraction(){
		this.currentInfraction = new Infraction();
	}
	
	@Command
	@NotifyChange({"infractions", "currentInfraction"})
	public void save(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentInfraction);
		});
		this.currentInfraction = null;
	}
	
	@Command
	@NotifyChange("currentInfraction")
	public void cancel(){
		this.currentInfraction = null;
	}
	
	@Command
	@NotifyChange("currentInfraction")
	public void edit(@BindingParam("i") Infraction infraction){
		this.currentInfraction = infraction;
	}
}
package SI_ESEI.Traffic.webapp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transaction;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import SI_ESEI.Traffic.DateTime;
import SI_ESEI.Traffic.Infraction;
import SI_ESEI.Traffic.TransactionUtils;
import SI_ESEI.Traffic.webapp.util.DesktopEntityManagerManager;

public class DateTimesVM {
	
	//DateTime under edition...
	private DateTime currentDateTime = null;
	
	public DateTime getCurrentDateTime(){
		return currentDateTime;
	}
	
	public List<Infraction> getInfractions(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT i FROM Infraction i", Infraction.class).getResultList();
	}
	
	public List<DateTime> getDateTimes(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT dt FROM DateTime dt", DateTime.class).getResultList();
	}
	
	@Command
	@NotifyChange("datetimes")
	
	public void delete(@BindingParam("dt") DateTime dateTime){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ ->{
			em.remove(dateTime);
		});
	}
	
	@Command
	@NotifyChange("currentDateTime")
	public void newDateTime(){
		this.currentDateTime = new DateTime();
	}
	
	@Command
	@NotifyChange({"dateTimes", "currentDateTime"})
	public void save(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentDateTime);
		});
		this.currentDateTime = null;
	}
	
	@Command
	@NotifyChange("currentDateTime")
	public void cancel(){
		this.currentDateTime = null;
	}
	
	@Command
	@NotifyChange("currentDateTime")
	public void edit(@BindingParam("dt") DateTime dateTime){
		this.currentDateTime = dateTime;
	}
	
}

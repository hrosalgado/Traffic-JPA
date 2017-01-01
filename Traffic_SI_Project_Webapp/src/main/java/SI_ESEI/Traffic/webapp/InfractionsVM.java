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
	@NotifyChange({"infractions", "currentInfraction", "typeError",
				   "descriptionError", "penaltyError"})
	public void save(){
		boolean flag;
		int cont = 0;
		
		// Type
		if(!this.currentInfraction.getType().toLowerCase().equals("low")
			&& !this.currentInfraction.getType().toLowerCase().equals("medium")
			&& !this.currentInfraction.getType().toLowerCase().equals("high")){
			this.typeError = "Incorrect format! Choose between low, medium or high.";
			cont++;
		}else{
			this.typeError = "-";
		}
		
		// Description
		
		
		// Penalty
		if(this.currentInfraction.getPenalty() < 0){
			this.penaltyError = "Incorrect value! It must be equal or more than 0";
			cont++;
		}else{
			this.penaltyError = "-";
		}
		
		if(cont == 0){
			flag = true;
		}else{
			flag = false;
		}
		
		if(flag){
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			TransactionUtils.doTransaction(em, __ -> {
				em.persist(this.currentInfraction);
			});
			this.currentInfraction = null;
			
			this.typeError = "-";
			this.descriptionError = "-";
			this.penaltyError = "-";
		}
	}
	
	@Command
	@NotifyChange({"currentInfraction", "typeError",
			   	   "descriptionError", "penaltyError"})
	public void cancel(){
		this.currentInfraction = null;
		
		this.typeError = "-";
		this.descriptionError = "-";
		this.penaltyError = "-";
	}
	
	@Command
	@NotifyChange("currentInfraction")
	public void edit(@BindingParam("i") Infraction infraction){
		this.currentInfraction = infraction;
	}
	
	// Error messages
	private String typeError = "-";
	private String descriptionError = "-";
	private String penaltyError = "-";
	
	public String getTypeError(){
		return this.typeError;
	}
	
	public String getDescriptionError(){
		return this.descriptionError;
	}
	
	public String getPenaltyError(){
		return this.penaltyError;
	}
}
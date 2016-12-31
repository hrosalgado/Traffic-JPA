package SI_ESEI.Traffic.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import SI_ESEI.Traffic.Road;
import SI_ESEI.Traffic.TransactionUtils;
import SI_ESEI.Traffic.webapp.util.DesktopEntityManagerManager;

public class RoadsVM{
	private Road currentRoad = null;
	
	public Road getcurrentRoad(){
		return currentRoad;
	}
	
	public List<Road> getRoads(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT r FROM Road r", Road.class).getResultList();
	}
	
	@Command
	@NotifyChange("roads")
	public void delete(@BindingParam("r") Road road){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(road);
		});
	}
	
	@Command
	@NotifyChange("currentRoad")
	public void newRoad(){
		this.currentRoad = new Road();
	}
	
	@Command
	@NotifyChange({"roads", "currentRoad", "nameError", "typeError",
				   "tollError", "maxSpeedError"})
	public void save(){
		boolean flag;
		int cont = 0;
		
		// Name
		
		
		// Type
		
		
		// Toll
		if(!this.currentRoad.getToll().toLowerCase().equals("yes")
			&& !this.currentRoad.getToll().toLowerCase().equals("no")){
			this.tollError = "Incorrect value. Choose between Yes or No.";
			cont++;
		}else{
			this.tollError = "-";
		}
		
		// Max speed
		if(this.currentRoad.getMaxSpeed() < 10){
			this.maxSpeedError = "Incorrect value!";
			cont++;
		}else{
			this.maxSpeedError = "-";
		}
		
		if(cont == 0){
			flag = true;
		}else{
			flag = false;
		}
		
		if(flag){
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			TransactionUtils.doTransaction(em, __ -> {
				em.persist(this.currentRoad);
			});
			this.currentRoad = null;
			
			this.nameError = "-";
			this.typeError = "-";
			this.tollError = "-";
			this.maxSpeedError = "-";
		}
	}
	
	@Command
	@NotifyChange({"currentRoad", "nameError", "typeError",
			   	   "tollError", "maxSpeedError"})
	public void cancel(){
		this.currentRoad = null;
		
		this.nameError = "-";
		this.typeError = "-";
		this.tollError = "-";
		this.maxSpeedError = "-";
	}
	
	@Command
	@NotifyChange("currentRoad")
	public void edit(@BindingParam("r") Road road){
		this.currentRoad = road;
	}
	
	// Error messages
	private String nameError = "-";
	private String typeError = "-";
	private String tollError = "-";
	private String maxSpeedError = "-";
	
	public String getNameError(){
		return this.nameError;
	}
	
	public String getTypeError(){
		return this.typeError;
	}
	
	public String getTollError(){
		return this.tollError;
	}
	
	public String getMaxSpeedError(){
		return this.maxSpeedError;
	}
}
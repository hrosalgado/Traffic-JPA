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
	@NotifyChange({"drivers", "currentDriver", "ageError", "sexError",
				   "experienceError", "previousInfractionsError", "illError"})
	public void save(){
		boolean flag;
		int cont = 0;
		
		// Age
		if(this.currentDriver.getAge() < 18){
			this.ageError = "Age must be more or equal than 18.";
			cont++;
		}else if(this.currentDriver.getAge() > 85){
			this.ageError = "Age must be less or equal than 85.";
			cont++;
		}else{
			this.ageError = "-";
		}
		
		// Sex
		if(!this.currentDriver.getSex().toLowerCase().equals("male")
			&& !this.currentDriver.getSex().toLowerCase().equals("female")){
			this.sexError = "Incorrect format! Choose between Male of Female.";
			cont++;
		}else{
			this.sexError = "-";
		}
		
		// Experience
		if(this.currentDriver.getExperience() > this.currentDriver.getAge() - 18
			&& this.currentDriver.getExperience() < 0){
			this.experienceError = "Incorrect value. You can't have more experience "
					+ "than years you are less minimum age to get a license driver.";
			cont++;
		}else{
			this.experienceError = "-";
		}
		
		// Previous infractions
		if(this.currentDriver.getPreviousInfractions() < 0){
			this.previousInfractionsError = "Incorrect value.";
			cont++;
		}else{
			this.previousInfractionsError = "-";
		}
		
		// Ill
		if(!this.currentDriver.getIll().toLowerCase().equals("yes")
			&& !this.currentDriver.getIll().toLowerCase().equals("no")){
			this.illError = "Incorrect format! Choose between Yes or No.";
			cont++;
		}else{
			this.illError = "-";
		}
		
		if(cont == 0){
			flag = true;
		}else{
			flag = false;
		}
		
		if(flag){
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			TransactionUtils.doTransaction(em, __ -> {
				em.persist(this.currentDriver);
			});
			this.currentDriver = null;
			
			this.ageError = "-";
			this.sexError = "-";
			this.experienceError = "-";
			this.previousInfractionsError = "-";
			this.illError = "-";
		}
	}
	
	@Command
	@NotifyChange({"currentDriver",  "currentDriver", "ageError", "sexError",
		   			"experienceError", "previousInfractionsError", "illError"})
	public void cancel(){
		this.currentDriver = null;
		
		this.ageError = "-";
		this.sexError = "-";
		this.experienceError = "-";
		this.previousInfractionsError = "-";
		this.illError = "-";
	}
	
	@Command
	@NotifyChange("currentDriver")
	public void edit(@BindingParam("d") Driver driver){
		this.currentDriver = driver;
	}
	
	// Error messages
	private String ageError = "-";
	private String sexError = "-";
	private String experienceError = "-";
	private String previousInfractionsError = "-";
	private String illError = "-";
	
	public String getAgeError(){
		return this.ageError;
	}
	
	public String getSexError(){
		return this.sexError;
	}
	
	public String getExperienceError(){
		return this.experienceError;
	}
	
	public String getPreviousInfractionsError(){
		return this.previousInfractionsError;
	}
	
	public String getIllError(){
		return this.illError;
	}
}

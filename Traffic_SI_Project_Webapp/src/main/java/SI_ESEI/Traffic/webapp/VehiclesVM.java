package SI_ESEI.Traffic.webapp;

import java.util.List;

import javax.persistence.EntityManager;
import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import SI_ESEI.Traffic.Driver;
import SI_ESEI.Traffic.TransactionUtils;
import SI_ESEI.Traffic.Vehicle;
import SI_ESEI.Traffic.webapp.util.DesktopEntityManagerManager;

public class VehiclesVM{
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
	@NotifyChange({"vehicles", "currentVehicle", "typeError", "brandError",
				   "carSpacesError", "passengersError", "antiquityError",
				   "drivePermissionError", "electricError"})
	public void save(){
		boolean flag;
		int cont = 0;
		
		// Car spaces
		if(this.currentVehicle.getCarSpaces() < 1){
			this.carSpacesError = "Incorrect value. It must be more than 0.";
			cont++;
		}else{
			this.carSpacesError = "-";
		}
		
		// Passengers
		if(this.currentVehicle.getPassengers() < 1){
			this.passengersError = "Incorrect value. It must be more than 0.";
			cont++;
		}else if(this.currentVehicle.getPassengers() > this.currentVehicle.getCarSpaces()){
			this.passengersError = "Incorrect value. It must be equal or less than vehicle spaces.";
			cont++;
		}else{
			this.passengersError = "-";
		}
		
		// Antiquity
		if(this.currentVehicle.getAntiquity() < 0){
			this.antiquityError = "Incorrect value. It must be equal or more than 0.";
			cont++;
		}else{
			this.antiquityError = "-";
		}
		
		// Drive permission
		if(!this.currentVehicle.getDrivePermission().toLowerCase().equals("yes")
			&& !this.currentVehicle.getDrivePermission().toLowerCase().equals("no")){
			this.drivePermissionError = "Incorrect format! Choose between Yes or No";
			cont++;
		}else{
			this.drivePermissionError = "-";
		}
		
		// Electric
		if(!this.currentVehicle.getElectric().toLowerCase().equals("yes")
			&& !this.currentVehicle.getElectric().toLowerCase().equals("no")){
			this.electricError = "Incorrect format! Choose between Yes or No";
			cont++;
		}else{
			this.electricError = "-";
		}
		
		if(cont == 0){
			flag = true;
		}else{
			flag = false;
		}
		
		if(flag){
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			TransactionUtils.doTransaction(em, __ -> {
				em.persist(this.currentVehicle);
			});
			this.currentVehicle = null;
			
			this.typeError = "-";
			this.brandError = "-";
			this.carSpacesError = "-";
			this.passengersError = "-";
			this.antiquityError = "-";
			this.drivePermissionError = "-";
			this.electricError = "-";
		}
	}
	
	@Command
	@NotifyChange({"currentVehicle", "typeError", "brandError",
			       "carSpacesError", "passengersError", "antiquityError",
			       "drivePermissionError", "electricError"})
	public void cancel(){
		this.currentVehicle = null;
		
		this.typeError = "-";
		this.brandError = "-";
		this.carSpacesError = "-";
		this.passengersError = "-";
		this.antiquityError = "-";
		this.drivePermissionError = "-";
		this.electricError = "-";
	}
	
	@Command
	@NotifyChange("currentVehicle")
	public void edit(@BindingParam("v") Vehicle vehicle){
		this.currentVehicle = vehicle;
	}
	
	// Error messages
	private String typeError = "-";
	private String brandError = "-";
	private String carSpacesError = "-";
	private String passengersError = "-";
	private String antiquityError = "-";
	private String drivePermissionError = "-";
	private String electricError = "-";
	
	public String getTypeError(){
		return this.typeError;
	}
	
	public String getBrandError(){
		return this.brandError;
	}
	
	public String getCarSpacesError(){
		return this.carSpacesError;
	}
	
	public String getPassengersError(){
		return this.passengersError;
	}
	
	public String getAntiquityError(){
		return this.antiquityError;
	}
	
	public String getDrivePermissionError(){
		return this.drivePermissionError;
	}
	
	public String getElectricError(){
		return this.electricError;
	}
}
package SI_ESEI.Traffic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Driver{
	// Id
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// Age
	@Column(name = "age")
	@Digits(integer = 2, fraction = 0)
	@Min(18)
	@NotNull
	private int age;
	
	// Sex (true -> male, false -> female)
	@Column(name = "sex")
	@NotNull
	private Boolean sex;
	
	// Experience
	@Column(name = "experience")
	@Digits(integer = 2, fraction = 0)
	@NotNull
	private int experience;
	
	// Previous infractions
	@Column(name = "previous_infractions")
	@Min(0)
	@NotNull
	private int previousInfractions;
	
	// Ill
	@Column(name = "ill")
	@NotNull
	private Boolean ill;
	
	public int getId(){
		return id;
	}

	public int getAge(){
		return age;
	}

	public void setAge(int age){
		this.age = age;
	}

	public Boolean getSex(){
		return sex;
	}

	public void setSex(Boolean sex){
		this.sex = sex;
	}

	public int getExperience(){
		return experience;
	}

	public void setExperience(int experience){
		this.experience = experience;
	}

	public int getPreviousInfractions(){
		return previousInfractions;
	}

	public void setPreviousInfractions(int previousInfractions){
		this.previousInfractions = previousInfractions;
	}
	
	public Boolean getIll(){
		return ill;
	}

	public void setIll(Boolean ill){
		this.ill = ill;
	}
	
	// Relation with Vehicle
	@OneToMany(mappedBy = "driver")
	private Set<Vehicle> vehicles = new HashSet<>();
	
	public Set<Vehicle> getVehicles(){
		return Collections.unmodifiableSet(vehicles);
	}
	
	public void addVehicle(Vehicle vehicle){
		vehicle.setDriver(this);
	}
	
	public void removeVehicle(Vehicle vehicle){
		vehicle.setDriver(null);
	}
	
	public void internalAddVehicle(Vehicle vehicle){
		this.vehicles.add(vehicle);
	}
	
	public void internalRemoveVehicle(Vehicle vehicle){
		this.vehicles.remove(vehicle);
	}
	
	// Relation with Infraction
	@OneToMany(mappedBy = "driver")
	private Set<Infraction> infractions = new HashSet<>();
	
	public Set<Infraction> getInfractions(){
		return Collections.unmodifiableSet(infractions);
	}
	
	public void addInfraction(Infraction infraction){
		infraction.setDriver(this);
	}
	
	public void removeInfraction(Infraction infraction){
		infraction.setDriver(null);
	}
	
	void internalRemoveInfraction(Infraction infraction){
		this.infractions.remove(infraction);
	}

	void internalAddInfraction(Infraction infraction){
		this.infractions.add(infraction);
	}
}

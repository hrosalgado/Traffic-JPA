package SI_ESEI.Traffic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
	
	// Sex (Male or Female)
	@Column(name = "sex")
	@Size(min = 1, max = 6)
	@NotNull
	private String sex;
	
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
	@Size(min = 1, max = 3)
	@NotNull
	private String ill;
	
	public int getId(){
		return id;
	}

	public int getAge(){
		return age;
	}

	public void setAge(int age){
		this.age = age;
	}

	public String getSex(){
		return sex;
	}

	public void setSex(String sex){
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
	
	public String getIll(){
		return ill;
	}

	public void setIll(String ill){
		this.ill = ill;
	}
	
	// Relation with Vehicle
	@ManyToMany(mappedBy = "drivers")
	private Set<Vehicle> vehicles = new HashSet<>();
	
	public Set<Vehicle> getVehicles(){
		return Collections.unmodifiableSet(this.vehicles);
	}
	
	public void addVehicle(Vehicle v){
		v.internalAddDriver(this);
		this.vehicles.add(v);
	}
	
	public void internalAddVehicle(Vehicle v){
		this.vehicles.add(v);
	}
	
	public void internalRemoveVehicle(Vehicle v){
		this.vehicles.remove(v);
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

package SI_ESEI.Traffic;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Vehicle{
	// Id
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// Type
	@Column(name = "type")
	@Size(min = 1, max = 20)
	@NotNull
	private String type;
	
	// Brand
	@Column(name = "brand")
	@Size(min = 1, max = 20)
	@NotNull
	private String brand;
	
	// Car spaces
	@Column(name = "car_spaces")
	@Digits(integer = 1, fraction = 0)
	@Min(1)
	@NotNull
	private int carSpaces;
	
	// Passengers
	@Column(name = "passengers")
	@Digits(integer = 1, fraction = 0)
	@Min(1)
	@NotNull
	private int passengers;
	
	// Antiquity
	@Column(name = "antiquity")
	@Digits(integer = 3, fraction = 0)
	@NotNull
	private int antiquity;
	
	// Drive permission
	@Column(name = "drive_permission")
	@NotNull
	private Boolean drivePermission;
	
	// Electric
	@Column(name = "electric")
	@NotNull
	private Boolean electric;
	
	public int getId(){
		return id;
	}
	
	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	public String getBrand(){
		return brand;
	}

	public void setBrand(String brand){
		this.brand = brand;
	}

	public int getCarSpaces(){
		return carSpaces;
	}

	public void setCarSpaces(int carSpaces){
		this.carSpaces = carSpaces;
	}

	public int getPassengers(){
		return passengers;
	}

	public void setPassengers(int passengers){
		this.passengers = passengers;
	}

	public int getAntiquity(){
		return antiquity;
	}

	public void setAntiquity(int antiquity){
		this.antiquity = antiquity;
	}

	public Boolean getDrivePermission(){
		return drivePermission;
	}

	public void setDrivePermission(Boolean drivePermission){
		this.drivePermission = drivePermission;
	}

	public Boolean getElectric(){
		return electric;
	}

	public void setElectric(Boolean electric){
		this.electric = electric;
	}
	
	// Relation with Driver
	@ManyToMany
	private Set<Driver> drivers = new HashSet<>();
	
	public Set<Driver> getDrivers(){
		return Collections.unmodifiableSet(drivers);
	}
	
	public void setDrivers(Collection<Driver> drivers){
		Set<Driver> myDriversCopy = new HashSet<>(this.drivers);
		for(Driver driver : myDriversCopy){
			if(!drivers.contains(driver)){
				this.removeDriver(driver);
			}
		}
		
		for(Driver driver : drivers){
			this.addDriver(driver);
		}
	}
	
	public void addDriver(Driver d){
		d.internalAddVehicle(this);
		this.drivers.add(d);
	}
	
	public void removeDriver(Driver d){
		d.internalRemoveVehicle(this);
		this.drivers.remove(d);
	}
	
	public void internalAddDriver(Driver driver){
		this.drivers.add(driver);
	}
}

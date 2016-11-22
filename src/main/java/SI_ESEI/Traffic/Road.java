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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Road{
	// Id
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// Name
	@Column(name = "name")
	@Size(min = 1, max = 10)
	@NotNull
	private String name;
	
	// Type
	@Column(name = "type")
	@Size(min = 1, max = 20)
	@NotNull
	private String type;
	
	// Toll
	@Column(name = "toll")
	@NotNull
	private Boolean toll;
	
	// Max speed
	@Column(name = "max_speed")
	@NotNull
	private int maxSpeed;
	
	public int getId(){
		return id;
	}

	public String getName(){
		return name;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getType(){
		return type;
	}

	public void setType(String type){
		this.type = type;
	}

	public Boolean getToll(){
		return toll;
	}

	public void setToll(Boolean toll){
		this.toll = toll;
	}

	public int getMaxSpeed(){
		return maxSpeed;
	}

	public void setMaxSpeed(int maxSpeed){
		this.maxSpeed = maxSpeed;
	}
	
	// Relation with Infraction
	@OneToMany(mappedBy = "road")
	private Set<Infraction> infractions = new HashSet<>();
	
	public Set<Infraction> getInfractions(){
		return Collections.unmodifiableSet(infractions);
	}
	
	public void addInfraction(Infraction infraction){
		infraction.setRoad(this);
	}
	
	public void removeInfraction(Infraction infraction){
		infraction.setRoad(null);
	}
	
	public void internalAddInfraction(Infraction infraction){
		this.infractions.add(infraction);
	}
	
	public void internalRemoveInfraction(Infraction infraction){
		this.infractions.remove(infraction);
	}
	
	// Relation with KmPoint
	@OneToMany(mappedBy = "road")
	private Set<KmPoint> kmPoints = new HashSet<>();
	
	public Set<KmPoint> getKmPoints(){
		return Collections.unmodifiableSet(kmPoints);
	}
	
	public void addKmPoint(KmPoint kmPoint){
		kmPoint.setRoad(this);
	}
	
	public void deleteKmPoint(KmPoint kmPoint){
		kmPoint.setRoad(null);
	}
	
	public void internalRemoveKmPoint(KmPoint kmPoint){
		this.kmPoints.remove(kmPoint);
	}

	public void interalAddVehicleKmPoint(KmPoint kmPoint){
		this.kmPoints.add(kmPoint);
	}
}

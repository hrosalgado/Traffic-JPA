package SI_ESEI.Traffic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Infraction{
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int idInfraction;
	
	// idDriver
	// una infraction solo puede tener un conductor, pero un conductor puede
	// tener varias infracciones
	@ManyToOne
	private Driver driver;
	
	// idDatetime
	// una infraccion solo puede tener una fecha, pero una fecha puede tener
	// varias infracciones
	@ManyToOne
	private DateTime datetime;
	
	// idKmPoint
	// idVehicle
	
	// type
	@Column(name = "type")
	@Size(min = 1, max = 10)
	@NotNull
	private String type;
	
	// description
	@Column(name = "description")
	@Size(min = 1, max = 40)
	@NotNull
	private String description;
	
	// penalty
	@Column(name = "penalty")
	@Digits(integer = 4, fraction = 0)
	@NotNull
	private int penalty;
	
	public int getIdInfraction(){
		return idInfraction;
	}
	
	public String getType(){
		return type;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getDescription(){
		return description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public int getPenalty(){
		return penalty;
	}
	
	public void setPenalty(int penalty){
		this.penalty = penalty;
	}
	
	// relation with Driver
	public void setDriver(Driver d){
		if(this.driver != null){
			this.driver.internalRemoveInfraction(this);
		}
		
		this.driver = d;
		
		if(this.driver != null){
			this.driver.internalAddInfraction(this);
		}
	}
	
	public Driver getDriver(){
		return this.driver;
	}
	
	// relation whit DateTime
	public void setDateTime(DateTime dt){
		if(this.datetime != null){
			this.datetime.internalRemoveInfraction(this);
		}
		
		this.datetime = dt;
		
		if(this.datetime != null){
			this.datetime.internalAddInfraction(this);
		}
	}
	
	public DateTime getDateTime(){
		return this.datetime;
	}
	
	// Relation with Road
	@ManyToOne
	private Road road;
	
	public Road getRoad(){
		return this.road;
	}
	
	public void setRoad(Road road){
		if(this.road != null){
			this.road.internalRemoveInfraction(this);
		}
		
		this.road = road;
		
		if(this.road != null){
			this.road.internalAddInfraction(this);
		}
	}
	
	// Relation with KmPoint
	@ManyToOne
	private KmPoint kmpoint;
	
	public KmPoint getKmPoint(){
		return this.kmpoint;
	}
	
	public void setKmPoint(KmPoint kmpoint){
		if(this.kmpoint != null){
			this.kmpoint.internalRemoveInfraction(this);
		}
		
		this.kmpoint = kmpoint;
		
		if(this.kmpoint != null){
			this.kmpoint.internalAddInfraction(this);
		}
	}
}

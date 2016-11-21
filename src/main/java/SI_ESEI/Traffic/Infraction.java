package SI_ESEI.Traffic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;


@Entity
public class Infraction {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idInfraction;
	
	//idDriver
	//una infraction solo puede tener un conductor, pero un conductor puede tener varias infracciones
	@ManyToOne
	private Driver driver;
	
	
	//idDatetime
	//una infraccion solo puede tener una fecha, pero una fecha puede tener varias infracciones
	@ManyToOne
	private DateTime datetime;
	
	//idKmPoint
	//idVehicle
	
	//type
	private String type;
	
	//description
	private String description;
	
	//penalty
	private int penalty;
	
	
	
	public int getIdInfraction(){
		return idInfraction;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getPenalty() {
		return penalty;
	}

	public void setPenalty(int penalty) {
		this.penalty = penalty;
	}
	
	//relation with Driver
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
	
	
	//relation whit DateTime
	public void setDate(DateTime dt){
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
	
}

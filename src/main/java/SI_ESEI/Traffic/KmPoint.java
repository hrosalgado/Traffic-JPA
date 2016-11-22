package SI_ESEI.Traffic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

@Entity
public class KmPoint{
	// idKmPoint
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// start
	@Column(name = "start")
	@NotNull
	private int start;
	
	// end
	@Column(name = "end")
	@NotNull
	private int end;
	
	// blackPoint
	@Column(name = "black_point")
	@NotNull
	private int blackPoint;
	
	// signposting
	@Column(name = "signposting")
	@NotNull
	private int signposting;
	
	// radar
	@Column(name = "radar")
	@NotNull
	private int radar;
	
	public int getId(){
		return id;
	}
	
	public int getStart(){
		return start;
	}
	
	public void setStart(int start){
		this.start = start;
	}
	
	public int getEnd(){
		return end;
	}
	
	public void setEnd(int end){
		this.end = end;
	}
	
	public int getblackPoint(){
		return blackPoint;
	}
	
	public void setblackPoint(int blackPoint){
		this.blackPoint = blackPoint;
	}
	
	public int getSignposting(){
		return signposting;
	}
	
	public void setSignposting(int signposting){
		this.signposting = signposting;
	}
	
	public int getRadar(){
		return radar;
	}
	
	public void setRadar(int radar){
		this.radar = radar;
	}
	
	// Relation with Infraction
	@OneToMany(mappedBy = "kmpoint")
	private Set<Infraction> infractions = new HashSet<>();
	
	public Set<Infraction> getInfractions(){
		return Collections.unmodifiableSet(infractions);
	}
	
	public void addInfraction(Infraction infraction){
		infraction.setKmPoint(this);
	}
	
	public void removeInfraction(Infraction infraction){
		infraction.setKmPoint(this);
	}
	
	void internalRemoveInfraction(Infraction infraction){
		this.infractions.remove(infraction);
	}
	
	void internalAddInfraction(Infraction infraction){
		this.infractions.add(infraction);
	}
	
	// Relation with Road
	@ManyToOne
	private Road road;
	
	public Road getRoad(){
		return this.road;
	}
	
	public void setRoad(Road road){
		if(this.road != null){
			this.road.internalRemoveKmPoint(this);
		}
		
		this.road = road;
		
		if(this.road != null){
			this.road.interalAddVehicleKmPoint(this);
		}
	}
	
}

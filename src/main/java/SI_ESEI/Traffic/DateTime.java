package SI_ESEI.Traffic;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;


@Entity
public class DateTime {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int idDateTime;
	
	@OneToMany(mappedBy="datetime")
	private Set<Infraction> infractions = new HashSet<>();
	
	
	//day
	private int day;
	
	//month
	private int month;
	
	//year
	private int year;
	
	//hour
	private int hour;
	
	//season
	private String season;
	
	//weather
	private String weather;
	
	//weekend
	private String weekend;
	
	//holiday
	private String holiday;

	
	public int getIdDateTime(){
		return idDateTime;
	}
	
	
	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
	}


	public int getMonth() {
		return month;
	}


	public void setMonth(int month) {
		this.month = month;
	}


	public int getYear() {
		return year;
	}


	public void setYear(int year) {
		this.year = year;
	}


	public int getHour() {
		return hour;
	}


	public void setHour(int hour) {
		this.hour = hour;
	}


	public String getSeason() {
		return season;
	}


	public void setSeason(String season) {
		this.season = season;
	}


	public String getWeather() {
		return weather;
	}


	public void setWeather(String weather) {
		this.weather = weather;
	}


	public String getWeekend() {
		return weekend;
	}


	public void setWeekend(String weekend) {
		this.weekend = weekend;
	}


	public String getHoliday() {
		return holiday;
	}


	public void setHoliday(String holiday) {
		this.holiday = holiday;
	}
	
	
	//relation whit Infraction
	public Set<Infraction> getInfractions(){
		return Collections.unmodifiableSet(infractions);
	}

	public void addInfraction(Infraction infraction){
		infraction.setDate(this);
	}

	public void removeInfraction(Infraction infraction){
		infraction.setDate(null);
	}
	
	public void internalRemoveInfraction(Infraction infraction) {
		this.infractions.remove(infraction);
	}


	public void internalAddInfraction(Infraction infraction) {
		this.infractions.add(infraction);
	}
}

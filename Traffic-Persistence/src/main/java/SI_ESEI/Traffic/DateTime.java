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
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class DateTime{
	// Id
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	// Day
	@Column(name = "day")
	@Min(1)
	@Max(31)
	@NotNull
	private int day;
	
	// Month
	@Column(name = "month")
	@Min(1)
	@Max(12)
	@NotNull
	private int month;
	
	// Year
	@Column(name = "year")
	@Digits(integer = 4, fraction = 0)
	@NotNull
	private int year;
	
	// Hour
	@Column(name = "hour")
	@Min(0)
	@Max(23)
	@NotNull
	private int hour;
	
	// Season
	@Column(name = "season")
	@Size(min = 1, max = 20)
	@NotNull
	private String season;
	
	// Weather
	@Column(name = "weather")
	@Size(min = 1, max = 20)
	@NotNull
	private String weather;
	
	// Weekend
	@Column(name = "weekend")
	@NotNull
	private String weekend;
	
	// Holiday
	@Column(name = "holiday")
	@NotNull
	private String holiday;

	public int getId(){
		return id;
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
	
	// Relation with Infraction
	@OneToMany(mappedBy = "datetime")
	private Set<Infraction> infractions = new HashSet<>();
	
	public Set<Infraction> getInfractions(){
		return Collections.unmodifiableSet(infractions);
	}

	public void addInfraction(Infraction infraction){
		infraction.setDateTime(this);
	}

	public void removeInfraction(Infraction infraction){
		infraction.setDateTime(null);
	}
	
	public void internalRemoveInfraction(Infraction infraction) {
		this.infractions.remove(infraction);
	}

	public void internalAddInfraction(Infraction infraction) {
		this.infractions.add(infraction);
	}
}

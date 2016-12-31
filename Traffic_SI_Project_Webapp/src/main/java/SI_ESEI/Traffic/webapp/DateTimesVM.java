package SI_ESEI.Traffic.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import SI_ESEI.Traffic.DateTime;
import SI_ESEI.Traffic.TransactionUtils;
import SI_ESEI.Traffic.webapp.util.DesktopEntityManagerManager;

public class DateTimesVM{
	private DateTime currentDateTime = null;
	
	public DateTime getCurrentDateTime(){
		return currentDateTime;
	}
	
	public List<DateTime> getDateTimes(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT dt FROM DateTime dt", DateTime.class).getResultList();
	}
	
	@Command
	@NotifyChange("datetimes")
	public void delete(@BindingParam("dt") DateTime dateTime){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ ->{
			em.remove(dateTime);
		});
	}
	
	@Command
	@NotifyChange("currentDateTime")
	public void newDateTime(){
		this.currentDateTime = new DateTime();
	}
	
	@Command
	@NotifyChange({"dateTimes", "currentDateTime", "dayError", "monthError",
				   "yearError", "hourError", "seasonError", "weatherError",
				   "weekendError", "holidayError"})
	public void save(){
		boolean flag;
		int cont = 0;
		
		// Day
		if(this.currentDateTime.getDay() < 1
			|| this.currentDateTime.getDay() > 31){
			this.dayError = "Incorrect value!";
			cont++;
		}else{
			switch(this.currentDateTime.getMonth()){
				case 4:
				case 6:
				case 9:
				case 11:
					if(this.currentDateTime.getDay() > 30){
						this.dayError = "Incorrect value! This month doesn't have 31 days.";
						cont++;
					}
					break;
				
				case 2:
					if(this.currentDateTime.getDay() > 28){
						this.dayError = "Incorrect value! This month doesn't have more than 28 days";
						cont++;
					}
					
				default:
					this.dayError = "-";
					break;
			}
		}
		
		// Month
		if(this.currentDateTime.getMonth() < 1
			|| this.currentDateTime.getMonth() > 12){
			this.monthError = "Incorrect value!";
			cont++;
		}else{
			this.monthError = "-";
		}
		
		// Year
		if(this.currentDateTime.getYear() < 1900){
			this.yearError = "Incorrect value!";
			cont++;
		}else{
			this.yearError = "-";
		}
		
		// Hour
		if(this.currentDateTime.getHour() < 0
			|| this.currentDateTime.getHour() > 23){
			this.hourError = "Incorrect value!";
			cont++;
		}else{
			this.hourError = "-";
		}
		
		// Season
		if(!this.currentDateTime.getSeason().toLowerCase().equals("summer")
			&& !this.currentDateTime.getSeason().toLowerCase().equals("spring")
			&& !this.currentDateTime.getSeason().toLowerCase().equals("autumn")
			&& !this.currentDateTime.getSeason().toLowerCase().equals("winter")){
			this.seasonError = "Incorrect value!";
			cont++;
		}else{
			this.seasonError = "-";
		}
		
		// Weather
		if(!this.currentDateTime.getWeather().toLowerCase().equals("sunny")
			&& !this.currentDateTime.getWeather().toLowerCase().equals("cloudy")
			&& !this.currentDateTime.getWeather().toLowerCase().equals("rainy")
			&& !this.currentDateTime.getWeather().toLowerCase().equals("foggy")){
			this.weatherError = "Incorrect value!";
			cont++;
		}else{
			this.weatherError = "-";
		}
		
		// Weekend
		if(!this.currentDateTime.getWeekend().toLowerCase().equals("yes")
			&& !this.currentDateTime.getWeekend().toLowerCase().equals("no")){
			this.weekendError = "Incorrect value!";
			cont++;
		}else{
			this.weekendError = "-";
		}
		
		// Holiday
		if(!this.currentDateTime.getHoliday().toLowerCase().equals("yes")
			&& !this.currentDateTime.getHoliday().toLowerCase().equals("no")){
			this.holidayError = "Incorrect value!";
			cont++;
		}else{
			this.holidayError = "-";
		}
		
		if(cont == 0){
			flag = true;
		}else{
			flag = false;
		}
		
		if(flag){			
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			TransactionUtils.doTransaction(em, __ -> {
				em.persist(this.currentDateTime);
			});
			this.currentDateTime = null;
			
			this.dayError = "-";
			this.monthError = "-";
			this.yearError = "-";
			this.hourError = "-";
			this.seasonError = "-";
			this.weatherError = "-";
			this.weekendError = "-";
			this.holidayError = "-";
		}
	}
	
	@Command
	@NotifyChange({"currentDateTime", "dayError", "monthError",
			   	   "yearError", "hourError", "seasonError", "weatherError",
			   	   "weekendError", "holidayError"})
	public void cancel(){
		this.currentDateTime = null;
		
		this.dayError = "-";
		this.monthError = "-";
		this.yearError = "-";
		this.hourError = "-";
		this.seasonError = "-";
		this.weatherError = "-";
		this.weekendError = "-";
		this.holidayError = "-";
	}
	
	@Command
	@NotifyChange("currentDateTime")
	public void edit(@BindingParam("dt") DateTime dateTime){
		this.currentDateTime = dateTime;
	}
	
	// Error messages
	private String dayError = "-";
	private String monthError = "-";
	private String yearError = "-";
	private String hourError = "-";
	private String seasonError = "-";
	private String weatherError = "-";
	private String weekendError = "-";
	private String holidayError = "-";
	
	public String getDayError(){
		return this.dayError;
	}
	
	public String getMonthError(){
		return this.monthError;
	}
	
	public String getYearError(){
		return this.yearError;
	}
	
	public String getHourError(){
		return this.hourError;
	}
	
	public String getSeasonError(){
		return this.seasonError;
	}
	
	public String getWeatherError(){
		return this.weatherError;
	}
	
	public String getWeekendError(){
		return this.weekendError;
	}
	
	public String getHolidayError(){
		return this.holidayError;
	}
}

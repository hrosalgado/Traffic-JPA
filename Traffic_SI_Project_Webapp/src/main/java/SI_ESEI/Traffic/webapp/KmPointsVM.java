package SI_ESEI.Traffic.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import SI_ESEI.Traffic.KmPoint;
import SI_ESEI.Traffic.Road;
import SI_ESEI.Traffic.TransactionUtils;
import SI_ESEI.Traffic.webapp.util.DesktopEntityManagerManager;

public class KmPointsVM{
	private KmPoint currentKmPoint = null;
	
	public KmPoint getCurrentKmPoint(){
		return currentKmPoint;
	}
	
	public List<Road> getRoads(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT r FROM Road r", Road.class).getResultList();
	}
	
	public List<KmPoint> getKmPoints(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT km FROM KmPoint km", KmPoint.class).getResultList();
	}
	
	@Command
	@NotifyChange("kmpoints")
	public void detele(@BindingParam("kmp") KmPoint kmPoint){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ ->{
			em.remove(kmPoint);
		});
	}
	
	@Command
	@NotifyChange("currentKmPoint")
	public void newKmPoint(){
		this.currentKmPoint = new KmPoint();
	}
	
	@Command
	@NotifyChange({"kmpoints", "currentKmPoint", "startError", "endError",
				   "blackPointError", "signpostingError", "radarError"})
	public void save(){
		boolean flag;
		int cont = 0;
		
		// Start
		if(this.currentKmPoint.getStart() < 0){
			this.startError = "Incorrect value! It must be equal or more than 0";
			cont++;
		}else{
			this.startError = "-";
		}
		
		// End
		if(this.currentKmPoint.getEnd() < 1){
			this.endError = "Incorrect value! It must be more than 0.";
			cont++;
		}else if(this.currentKmPoint.getEnd() <= this.currentKmPoint.getStart()){
			this.endError = "Incorrect value! It must be more than start.";
			cont++;
		}else{
			this.endError = "-";
		}
		
		// Black point
		if(!this.currentKmPoint.getblackPoint().toLowerCase().equals("yes")
			&& !this.currentKmPoint.getblackPoint().toLowerCase().equals("no")){
			this.blackPointError = "Incorrect format! Choose between Yes or No.";
			cont++;
		}else{
			this.blackPointError = "-";
		}
		
		// Signposting
		if(!this.currentKmPoint.getSignposting().toLowerCase().equals("yes")
			&& !this.currentKmPoint.getSignposting().toLowerCase().equals("no")){
			this.signpostingError = "Incorrect format! Choose between Yes or No.";
			cont++;
		}else{
			this.signpostingError = "-";
		}
		
		// Radar
		if(!this.currentKmPoint.getRadar().toLowerCase().equals("yes")
			&& !this.currentKmPoint.getRadar().toLowerCase().equals("no")){
			this.radarError = "Incorrect format! Choose between Yes or No.";
			cont++;
		}else{
			this.radarError = "-";
		}
		
		if(cont == 0){
			flag = true;
		}else{
			flag = false;
		}
		
		if(flag){
			EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
			TransactionUtils.doTransaction(em, __ ->{
				em.persist(this.currentKmPoint);
			});
			this.currentKmPoint = null;
			
			this.startError = "-";
			this.endError = "-";
			this.blackPointError = "-";
			this.signpostingError = "-";
			this.radarError = "-";
		}		
	}
	
	@Command
	@NotifyChange({"currentKmPoint", "startError", "endError",
			   	   "blackPointError", "signpostingError", "radarError"})
	public void cancel(){
		this.currentKmPoint = null;
		
		this.startError = "-";
		this.endError = "-";
		this.blackPointError = "-";
		this.signpostingError = "-";
		this.radarError = "-";
	}
	
	@Command
	@NotifyChange("currentKmPoint")
	public void edit(@BindingParam("kmp") KmPoint kmPoint){
		this.currentKmPoint = kmPoint;
	}
	
	// Error messages
	private String startError = "-";
	private String endError = "-";
	private String blackPointError = "-";
	private String signpostingError = "-";
	private String radarError = "-";
	
	public String getStartError(){
		return this.startError;
	}
	
	public String getEndError(){
		return this.endError;
	}
	
	public String getBlackPointError(){
		return this.blackPointError;
	}
	
	public String getSignpostingError(){
		return this.signpostingError;
	}
	
	public String getRadarError(){
		return this.radarError;
	}
}
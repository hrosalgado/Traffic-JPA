package SI_ESEI.Traffic.webapp;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transaction;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import SI_ESEI.Traffic.KmPoint;
import SI_ESEI.Traffic.Road;
import SI_ESEI.Traffic.TransactionUtils;
import SI_ESEI.Traffic.webapp.util.DesktopEntityManagerManager;

public class KmPointsVM {

	//Kmpoint under edition
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
	public void detele(@BindingParam("km") KmPoint kmPoint){
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
	@NotifyChange({"kmpoints", "currentKmPoint"})
	public void save(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ ->{
			em.persist(this.currentKmPoint);
		});
		this.currentKmPoint = null;
	}
	
	@Command
	@NotifyChange("currentKmPoint")
	public void cancel(){
		this.currentKmPoint = null;
	}
	
	@Command
	@NotifyChange("currentKmPoint"){
		public void edit(@BindingParam("km") KmPoint kmPoint){
			this.currentKmPoint = kmPoint;
		}
	}
}

package SI_ESEI.Traffic.webapp;

import java.util.List;

import javax.persistence.EntityManager;

import org.zkoss.bind.annotation.BindingParam;
import org.zkoss.bind.annotation.Command;
import org.zkoss.bind.annotation.NotifyChange;

import SI_ESEI.Traffic.Road;
import SI_ESEI.Traffic.TransactionUtils;
import SI_ESEI.Traffic.webapp.util.DesktopEntityManagerManager;

public class RoadsVM{
	private Road currentRoad = null;
	
	public Road getcurrentRoad(){
		return currentRoad;
	}
	
	public List<Road> getRoads(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		return em.createQuery("SELECT r FROM Road r", Road.class).getResultList();
	}
	
	@Command
	@NotifyChange("roads")
	public void delete(@BindingParam("r") Road road){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.remove(road);
		});
	}
	
	@Command
	@NotifyChange("currentRoad")
	public void newRoad(){
		this.currentRoad = new Road();
	}
	
	@Command
	@NotifyChange({"roads", "currentRoad"})
	public void save(){
		EntityManager em = DesktopEntityManagerManager.getDesktopEntityManager();
		TransactionUtils.doTransaction(em, __ -> {
			em.persist(this.currentRoad);
		});
		this.currentRoad = null;
	}
	
	@Command
	@NotifyChange("currentRoad")
	public void cancel(){
		this.currentRoad = null;
	}
	
	@Command
	@NotifyChange("currentRoad")
	public void edit(@BindingParam("r") Road road){
		this.currentRoad = road;
	}
}
package SI_ESEI.Traffic;

import javax.persistence.EntityManager;

public interface Transaction {
	public void run(EntityManager em);
}

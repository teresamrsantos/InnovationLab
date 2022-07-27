package pt.uc.dei.paj.DAO;

import java.io.Serializable;
import java.util.List;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public abstract class AbstractDao<T extends Serializable> implements Serializable {

	private static final long serialVersionUID = 1L;
//se for algo generico colocar o metodo no abstracDao se nao colocar no product type dao
	protected final Class<T> clazz;

	@PersistenceContext(unitName = "Backend") // persistance unit name, pode ser consultado no
	// ficheiro persistance xml
	protected EntityManager em;

	public String getIdColumn(String string) {
		switch (string) {
		case "id":
			return "id";
		case "deleted":
			return "deleted";
		}
		return string;
	}

	public AbstractDao(Class<T> clazz) {
		this.clazz = clazz;
	}

	public T find(Object id) {
		return em.find(clazz, id);
	}

	public void persist(final T entity) {
		em.persist(entity);
	}

	public void merge(final T entity) {
		em.merge(entity);
	}

	public void remove(final T entity) {
		em.remove(em.contains(entity) ? entity : em.merge(entity));
	}

	public List<T> findAll() {
		final CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(clazz);
		criteriaQuery.select(criteriaQuery.from(clazz));
		return em.createQuery(criteriaQuery).getResultList();
	}

	public void deleteAll() {
		final CriteriaDelete<T> criteriaDelete = em.getCriteriaBuilder().createCriteriaDelete(clazz);
		criteriaDelete.from(clazz);
		em.createQuery(criteriaDelete).executeUpdate();
	}

	public List<T> findAllNotDeleted() {
		final CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(clazz);
		Root<T> c = criteriaQuery.from(clazz);
		criteriaQuery.select(c).where(em.getCriteriaBuilder().equal(c.get(getIdColumn("deleted")), false));
		return em.createQuery(criteriaQuery).getResultList();
	}

		
	public List<T> findAllDeleted() {
		final CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(clazz);
		Root<T> c = criteriaQuery.from(clazz);
		criteriaQuery.select(c).where(em.getCriteriaBuilder().equal(c.get(getIdColumn("deleted")), true));
		return em.createQuery(criteriaQuery).getResultList();
	}

	public  List<T> findEntityIfNotDeleted(Object id) {
		try {

			final CriteriaQuery<T> criteriaQuery = em.getCriteriaBuilder().createQuery(clazz);
     		Root<T> c = criteriaQuery.from(clazz);

			criteriaQuery.select(c)
					.where(em.getCriteriaBuilder().and(em.getCriteriaBuilder().equal(c.get(getIdColumn("id")), id),
							em.getCriteriaBuilder().equal(c.get(getIdColumn("deleted")), false)));

			return em.createQuery(criteriaQuery).getResultList();

		} catch (Exception e) {
			System.out.println("Error in: findEntityIfNotDeleted");
			e.printStackTrace();
			return null;
		}
	}


	
}

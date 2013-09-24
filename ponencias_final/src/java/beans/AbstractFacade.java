/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import java.util.List;
import javax.persistence.EntityManager;
//import javax.persistence.NamedQuery;
//import javax.persistence.criteria.Order;
//import javax.persistence.Query;

/**
 *
 * @author Carmen
 */
public abstract class AbstractFacade<T> {
    private Class<T> entityClass;    

    public AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    public void create(T entity) {
        getEntityManager().persist(entity);
    }

    public void edit(T entity) {
        getEntityManager().merge(entity);
    }

    public void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
    }

    public T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }
    
    
//    método para obtener las ciudades ordentadas alfabeticamente
//    hacer este método en otra clase en esta clase no queda bien por el alto acoplamiento
//    public List<T> findAllOrdered(int[] range){
//        NamedQuery(name = "Evento.findAll", query = "SELECT e FROM Evento e"),
//                javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
//
////Query query = getEntityManager().createQuery("SELECT e FROM Evento e order by e.fecha");
////List<Evento> c = (List<Evento>)(query.getResultList());
// javax.persistence.Query q = getEntityManager().createQuery("SELECT e FROM Evento e order by e.fechaInicial desc");
//        q.setMaxResults(range[1] - range[0]);
//        q.setFirstResult(range[0]);
//        return q.getResultList();
//    }

    public List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        /*Hacer pruebas para sacar las ciudades en orden alfabetico*/
        /*crear un nuevo query tomar como ejemplo el ejercicio de abajo, donde podamos ordenar la lista alfabeticamente*/
        
        //cq
        return getEntityManager().createQuery(cq).getResultList();
    }
//    public List<T> lista_eventos1(int[] range)
//    {
//                @NamedQuery(name = "Evento.findAll", query = "SELECT e FROM Evento e"),
//                javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
//
//Query query = getEntityManager().createQuery("SELECT e FROM Evento e order by e.fecha");
//List<Evento> c = (List<Evento>)(query.getResultList());
// javax.persistence.Query q = getEntityManager().createQuery("SELECT e FROM Evento e order by e.fechaInicial desc");
//        q.setMaxResults(range[1] - range[0]);
//        q.setFirstResult(range[0]);
//        return q.getResultList();
//
//}
    public List<T> findRange(int[] range) {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));        
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.criteria.Root<T> rt = cq.from(entityClass);
        cq.select(getEntityManager().getCriteriaBuilder().count(rt));
        javax.persistence.Query q = getEntityManager().createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }

}

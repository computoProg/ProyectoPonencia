/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

/**
 *
 * @author Aux de Programacion
 */
@Stateless
public class ApoyoFacade {
    @PersistenceContext(unitName = "ponenciasPU")
    private EntityManager em;

    public void create(Apoyo apoyo) {
        em.persist(apoyo);
    }

    public void edit(Apoyo apoyo) {
        em.merge(apoyo);
    }

    public void remove(Apoyo apoyo) {
        em.remove(em.merge(apoyo));
    }

    public Apoyo find(Object id) {
        return em.find(Apoyo.class, id);
    }

    public List<Apoyo> findAll() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Apoyo.class));
        return em.createQuery(cq).getResultList();
    }
    
    public List<Proyecto> findAll_Except_NoActivos(){
//        final String estado="Activo";              
        Query q = em.createQuery("select a from Apoyo a where"
//                + " (a.estado = :estado)");    
                + " (a.estado = 'Activo')");   
//        q.setParameter("estado", estado);
        return q.getResultList();
    }

    public List<Apoyo> findRange(int[] range) {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Apoyo.class));
        Query q = em.createQuery(cq);
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();
    }

    public int count() {
        CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        Root<Apoyo> rt = cq.from(Apoyo.class);
        cq.select(em.getCriteriaBuilder().count(rt));
        Query q = em.createQuery(cq);
        return ((Long) q.getSingleResult()).intValue();
    }
    
    
    public List<Apoyo> apoyo_filtro(String apoyo, String estado){
        Query q = em.createQuery("select p from Apoyo p where"               
                + "(p.estado like :estado or :estado = '') "
                + "and (p.apoyo like :apoyo or :apoyo = '')");
        q.setParameter("apoyo", apoyo).setParameter("estado", estado);
        return q.getResultList();
    }
    
    public List<Apoyo> apoyo_filtro_rango(int [] rango, String apoyo, String estado){
        Query q = em.createQuery("select p from Apoyo p where"                
                + "(p.estado like :estado or :estado = '')"
                + "and (p.apoyo like :apoyo or :apoyo = '')");
        
        q.setMaxResults(rango[1]-rango[0]);
        q.setFirstResult(rango[0]);
        
        q.setParameter("apoyo", apoyo).setParameter("estado", estado);
        return q.getResultList();
    }

}

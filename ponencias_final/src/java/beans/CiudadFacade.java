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


/**
 *
 * @author Aux de Programacion
 */
@Stateless
public class CiudadFacade extends AbstractFacade<Ciudad> {
    @PersistenceContext(unitName = "ponenciasPU")
    private EntityManager em;    
    private Query query;  

    public CiudadFacade(){
        super(Ciudad.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Ciudad> getCiudades(){
        query = em.createQuery("SELECT c FROM Ciudad c");
        List<Ciudad> c = (List<Ciudad>)query.getResultList();
        return c;
    }
    public List<Ciudad> getCiudadesOrdered(){
        Query q= em.createQuery("select c.nombre from Ciudad c order by c.nombre");
        return q.getResultList();
    }

}

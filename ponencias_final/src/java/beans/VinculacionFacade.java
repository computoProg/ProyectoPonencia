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
 * @author Carmen
 */
@Stateless
public class VinculacionFacade extends AbstractFacade<Vinculacion> {
    @PersistenceContext(unitName = "ponenciasPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public VinculacionFacade() {
        super(Vinculacion.class);
    }
     public List<Participantes> participantesVinculacion(int vinculacion)
    {

Query query = em.createQuery("SELECT r FROM Participantes r where r.vinculacion.idvinculacion = :vinculacion");
//ParticipantesController pc = null;
query.setParameter("vinculacion",vinculacion);

 List<Participantes> c = (List<Participantes>)(query.getResultList());
  return (List<Participantes>) c;

  // return getEntityManager().createQuery("SELECT c FROM lista_estados c WHERE c.codigo_pais : = codigoPais").getResultList();
    }

}

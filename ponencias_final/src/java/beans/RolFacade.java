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
public class RolFacade extends AbstractFacade<Rol> {
    @PersistenceContext(unitName = "ponenciasPU")
    private EntityManager em;

    protected EntityManager getEntityManager() {
        return em;
    }

    public RolFacade() {
        super(Rol.class);
    }
     public List<Rol> rolesUsuario()
    {

        Query query = em.createQuery(" SELECT r FROM Rol r where r.idrol = :cedula");
        //ParticipantesController pc = null;
        query.setParameter("cedula",2);

         List<Rol> c = (List<Rol>)(query.getResultList());
          return (List<Rol>) c;

          // return getEntityManager().createQuery("SELECT c FROM lista_estados c WHERE c.codigo_pais : = codigoPais").getResultList();
    }
  public List<Participantes> participantesRol(int rol)
    {

Query query = em.createQuery(" SELECT r FROM Participantes r where r.rol.idrol = :cedula");
//ParticipantesController pc = null;
query.setParameter("cedula",rol);

 List<Participantes> c = (List<Participantes>)(query.getResultList());
  return (List<Participantes>) c;

  // return getEntityManager().createQuery("SELECT c FROM lista_estados c WHERE c.codigo_pais : = codigoPais").getResultList();
    }
}

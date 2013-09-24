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
public class GrupoInvestigacionFacade extends AbstractFacade<GrupoInvestigacion>{
    @PersistenceContext(unitName = "ponenciasPU")
    private EntityManager em;
    /** Creates a new instance of GrupoInvestigacionFacade */
    public GrupoInvestigacionFacade() {
        super(GrupoInvestigacion.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Participantes> participantesGrupoInvestigacion(int grupoInvestigacion){
        Query query = em.createQuery("Select p FROM Participantes p WHERE p.grupoInvestigacionidGrupoInvestigacion.idGrupoInvestigacion = :grupoInvestigacion");
        query.setParameter("grupoInvestigacion", grupoInvestigacion);
        return query.getResultList();
    }
    
    public List<GrupoInvestigacion>grupoI_filtro(String nombre, String estado, String clasificacion){
        Query q = em.createQuery("select p from GrupoInvestigacion p where"
                + " (p.clasificacion = :clasificacion or :clasificacion = '') "
                + "and (p.estado = :estado or :estado = '') "
                + "and (p.nombre = :nombre or :nombre = '')");
        q.setParameter("clasificacion",clasificacion).setParameter("nombre", nombre).setParameter("estado", estado);
        return q.getResultList();
    }
    
    public List<GrupoInvestigacion>grupoI_filtro_rango(int [] rango, String nombre, String estado, String clasificacion){
        Query q = em.createQuery("select p from GrupoInvestigacion p where"
                + " (p.clasificacion = :clasificacion or :clasificacion = '') "
                + "and (p.estado = :estado or :estado = '') "
                + "and (p.nombre = :nombre or :nombre = '')");
        
        q.setMaxResults(rango[1]-rango[0]);
        q.setFirstResult(rango[0]);
        
        q.setParameter("clasificacion",clasificacion).setParameter("nombre", nombre).setParameter("estado", estado);
        return q.getResultList();
    }
    
    public List<Proyecto> findAll_Except_NoActivos(){
//        final String estado="Activo";              
        Query q = em.createQuery("select g from GrupoInvestigacion g where"
//                + " (g.estado = :estado)");    
                + " (g.estado = 'Activo')");   
//        q.setParameter("estado", estado);
        return q.getResultList();
    }
}

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
public class ProyectoFacade extends AbstractFacade<Proyecto> {
    @PersistenceContext(unitName = "ponenciasPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ProyectoFacade() {
        super(Proyecto.class);
    }
    

    /*public List<Proyecto> proyectosParticipante(int cedula){
        Query query = em.createQuery(" SELECT p FROM Proyecto p where p.idparticipantes.cedula = :cedula");
        query.setParameter("cedula",cedula);
        List<Proyecto> c = (List<Proyecto>)(query.getResultList());
        return (List<Proyecto>) c;
    }*/


    /*public List<Participantes> participantesProyecto(int proyecto){
        Query query = em.createQuery(" SELECT p FROM Participantes p where p.cedula = Proyecto.proyecto.idparticipante and Proyecto.idproyecto = :proyecto  ");
        query.setParameter("proyecto",proyecto);
        List<Participantes> c = (List<Participantes>)(query.getResultList());
        return (List<Participantes>) c;
    }*/
    
//    public List<Proyecto> proyecto_filtro(int cedula, String nombre, String estado){
//        Query q = em.createQuery("select p from Proyecto p, Ponencia ponencia where"
//                + " (p.idproyecto = ponencia.proyectoIdproyecto.idproyecto and "
//                + " ponencia.participantesCedula.cedula = :cedula or :cedula = 0) "
//                + "and (p.estado = :estado or :estado = '') "
//                + "and (p.nombre = :nombre or :nombre = '')");
//        q.setParameter("cedula", cedula).setParameter("nombre", nombre).setParameter("estado", estado);
//        return q.getResultList();
//    }    
    
    public List<Proyecto> proyecto_filtro(String nombre, String estado){
        Query q = em.createQuery("select p from Proyecto p where"               
                + "(p.estado = :estado or :estado = '') "
                + "and (p.nombre = :nombre or :nombre = '')");
        q.setParameter("nombre", nombre).setParameter("estado", estado);
        return q.getResultList();
    }
    
//    public List<Proyecto> proyecto_filtro_rango(int [] rango, int cedula, String nombre, String estado){
//        Query q = em.createQuery("select p from Proyecto p, Ponencia ponencia where"
//                + " (p.idproyecto = ponencia.proyectoIdproyecto.idproyecto and "
//                + "ponencia.participantesCedula.cedula = :cedula or :cedula = 0)"
//                + "and (p.estado = :estado or :estado = '')"
//                + "and (p.nombre = :nombre or :nombre = '')");
//        
//        q.setMaxResults(rango[1]-rango[0]);
//        q.setFirstResult(rango[0]);
//        
//        q.setParameter("cedula", cedula).setParameter("nombre", nombre).setParameter("estado", estado);
//        return q.getResultList();
//    }
    public List<Proyecto> proyecto_filtro_rango(int [] rango, String nombre, String estado){
        Query q = em.createQuery("select p from Proyecto p where"                
                + "(p.estado = :estado or :estado = '')"
                + "and (p.nombre = :nombre or :nombre = '')");
        
        q.setMaxResults(rango[1]-rango[0]);
        q.setFirstResult(rango[0]);
        
        q.setParameter("nombre", nombre).setParameter("estado", estado);
        return q.getResultList();
    }
    
    public List<Proyecto> findAll_Except_NoActivos(){
//        final String estado="Activo";              
        Query q = em.createQuery("select p from Proyecto p where"
//                + " (p.estado = :estado)");    
                + " (p.estado = 'Activo')");   
//        q.setParameter("estado", estado);
        return q.getResultList();
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Root;

/**
 *
 * @author Aux de Programacion
 */
@Stateless
public class PonenciaFacade extends AbstractFacade<Ponencia>{
    @PersistenceContext(unitName = "ponenciasPU")
    private EntityManager em;
    private Query query;    
    public PonenciaFacade(){
        super(Ponencia.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Ponencia> getPonencias(){
        query = em.createQuery("SELECT p FROM Ponencia p");
//        List<Ponencia> p = (List<Ponencia>)query.getResultList();
//        return p;
        return query.getResultList();
    }

    public List<Ponencia> busca_ponencia(String nombre){
        query = em.createQuery("SELECT p FROM Ponencia p WHERE p.nombre LIKE :nombre%");
        query.setParameter("nombre", nombre);
//        List<Ponencia> p = (List<Ponencia>)query.getResultList();
//        return p;
        return query.getResultList();
    }

    public List<Ponencia> ponencias_fecha(Date fecha1, Date fecha2){
        query = em.createQuery("SELECT p FROM Ponencia p WHERE p.fechaInicial >= :fecha1 " +
                "AND p.fechaInicial <= :fecha2" +
                "ORDER BY p.fechaInicial DESC");
//        List<Ponencia> p = (List<Ponencia>)query.getResultList();
//        return p;
        return query.getResultList();
    }
    
    public void setRango(int[]rango){
        //Query query;
        if(rango!=null){
            query.setMaxResults(rango[1]-rango[0]);
            query.setFirstResult(rango[0]);
        }
        //return query.getResultList();
    }
    
    public List<Ponencia> lista_next_prev(int[]rango){
        //Query query;
        setRango(rango);
//        if(rango!=null){
//            query.setMaxResults(rango[1]-rango[0]);
//            query.setFirstResult(rango[0]);
//        }
        return query.getResultList();
    }
            
    public List<Ponencia> lista_ponencias(int[] rango, int participante,int rol){
        //Query query;           
        if(rol!=2){
            query = em.createQuery("SELECT p FROM Ponencia p ORDER BY p.fechaInicial DESC");
        }
        else{
            query = em.createQuery("SELECT p FROM Ponencia p where " 
                    + "p.participantesCedula.cedula = :participante");
            query.setParameter("participante", participante);
        }        
//        if(rango!= null){
//            query.setMaxResults(rango[1]-rango[0]);
//            query.setFirstResult(rango[0]);
//        }                
////        List<Ponencia> p = (List<Ponencia>)query.getResultList();
////        return p;
        setRango(rango);
        return query.getResultList();
    }
    
    public List <Ponencia> ponencia_filtro_rango(int []range, int participante, int ciudad, int tipo, int apoyo, int proyecto, int rol){
        /*agregar condicional if rol = 1, para admin else para usuario-standar*/
        //javax.persistence.Query query;
        if(rol==1){
            query = getEntityManager().createQuery("SELECT c FROM Ponencia c where  (c.apoyoIdapoyo.idapoyo = :apoyo or :apoyo=0)"
                    
                    + "and (c.proyectoIdproyecto.idproyecto = :proyecto or :proyecto = 0) and (c.tipoIdtipo.idtipo = :tipo or :tipo = 0)"
                    + "and (c.ciudadidCiudad.idCiudad = :ciudad or :ciudad = 0)"
                    );
            query.setParameter("proyecto",proyecto).setParameter("ciudad",ciudad).setParameter("tipo", tipo).setParameter("apoyo", apoyo);
            //query.setParameter("proyecto",proyecto).setParameter("tipo", tipo).setParameter("apoyo", apoyo);
        }
        else{
            query = getEntityManager().createQuery("SELECT c FROM Ponencia c where  (c.apoyoIdapoyo.idapoyo = :apoyo or :apoyo=0)"
                    + "and (c.participantesCedula.cedula = :participante)"
                    + "and (c.proyectoIdproyecto.idproyecto = :proyecto or :proyecto = 0) and (c.tipoIdtipo.idtipo = :tipo or :tipo = 0)"
                    + "and (c.ciudadidCiudad.idCiudad = :ciudad or :ciudad = 0)"
                    );
            query.setParameter("proyecto",proyecto).setParameter("participante",participante).setParameter("ciudad",ciudad).setParameter("tipo", tipo).setParameter("apoyo", apoyo);
            
        }                
              
        setRango(range);        
        return query.getResultList();
    }
    
    public List<Ponencia> ponencias_fecha1_fecha2(int [] range, Date fecha1, Date fecha2, int participante,int ciudad, int tipo, int apoyo, int proyecto, int rol) {
                
        if(rol==1){
            query = getEntityManager().createQuery("SELECT c FROM Ponencia c where  "
                +"c.fechaInicial >= :fecha1 and c.fechaInicial <= :fecha2 and"
                + "(c.apoyoIdapoyo.idapoyo = :apoyo or :apoyo=0)" 
                
                + "and (c.proyectoIdproyecto.idproyecto = :proyecto or :proyecto = 0) and (c.tipoIdtipo.idtipo = :tipo or :tipo = 0)"
                + "and (c.ciudadidCiudad.idCiudad = :ciudad or :ciudad = 0)"
                    );
            query.setParameter("proyecto",proyecto).setParameter("ciudad",ciudad).setParameter("tipo", tipo).setParameter("apoyo", apoyo).setParameter("fecha1", fecha1).setParameter("fecha2", fecha2);
            
        }
        else{
            query = getEntityManager().createQuery("SELECT c FROM Ponencia c where  "
                +"c.fechaInicial >= :fecha1 and c.fechaInicial <= :fecha2 and"
                + "(c.apoyoIdapoyo.idapoyo = :apoyo or :apoyo=0)" 
                + "and (c.participantesCedula.cedula = :participante or :participante = 0)"
                + "and (c.proyectoIdproyecto.idproyecto = :proyecto or :proyecto = 0) and (c.tipoIdtipo.idtipo = :tipo or :tipo = 0)"
                + "and (c.ciudadidCiudad.idCiudad = :ciudad or :ciudad = 0)"
                    );
            query.setParameter("proyecto",proyecto).setParameter("participante", participante).setParameter("ciudad",ciudad).setParameter("tipo", tipo).setParameter("apoyo", apoyo).setParameter("fecha1", fecha1).setParameter("fecha2", fecha2);
            
        }               
                       
        setRango(range);
        return query.getResultList();
    }    
   
    public List<Ponencia> ponencias_fecha1(int [] range, Date fecha1, int participante,int ciudad, int tipo, int apoyo, int proyecto, int rol){
                
        if(rol==1){
            query = getEntityManager().createQuery("SELECT c FROM Ponencia c where  "
                +"c.fechaInicial >= :fecha1 and"
                + "(c.apoyoIdapoyo.idapoyo = :apoyo or :apoyo=0)"
                
                + "and (c.proyectoIdproyecto.idproyecto = :proyecto or :proyecto = 0) and (c.tipoIdtipo.idtipo = :tipo or :tipo = 0)"
                + "and (c.ciudadidCiudad.idCiudad = :ciudad or :ciudad = 0)"
                    );   
            query.setParameter("proyecto",proyecto).setParameter("ciudad",ciudad).setParameter("tipo", tipo).setParameter("apoyo", apoyo).setParameter("fecha1", fecha1);
            
        }
        else{
            query = getEntityManager().createQuery("SELECT c FROM Ponencia c where  "
                +"c.fechaInicial >= :fecha1 and"
                + "(c.apoyoIdapoyo.idapoyo = :apoyo or :apoyo=0)"
                + "and (c.participantesCedula.cedula = :participante or :participante = 0)"
                + "and (c.proyectoIdproyecto.idproyecto = :proyecto or :proyecto = 0) and (c.tipoIdtipo.idtipo = :tipo or :tipo = 0)"
                + "and (c.ciudadidCiudad.idCiudad = :ciudad or :ciudad = 0)"
                    );        
            query.setParameter("proyecto",proyecto).setParameter("participante", participante).setParameter("ciudad",ciudad).setParameter("tipo", tipo).setParameter("apoyo", apoyo).setParameter("fecha1", fecha1);
            
        }
     
        setRango(range);
        return query.getResultList();      
    }    
  
    public List<Ponencia> ponencias_fecha2(int [] range, Date fecha2, int participante, int ciudad, int tipo, int apoyo, int proyecto,int rol){
        
        if(rol==1){
            query = getEntityManager().createQuery("SELECT c FROM Ponencia c where  "
                +"c.fechaInicial <= :fecha2 and"
                + "(c.apoyoIdapoyo.idapoyo = :apoyo or :apoyo=0)"
                
                + "and (c.proyectoIdproyecto.idproyecto = :proyecto or :proyecto = 0) and (c.tipoIdtipo.idtipo = :tipo or :tipo = 0)"
                + "and (c.ciudadidCiudad.idCiudad = :ciudad or :ciudad = 0)"
                    ); 
            query.setParameter("proyecto",proyecto).setParameter("ciudad",ciudad).setParameter("tipo", tipo).setParameter("apoyo", apoyo).setParameter("fecha2", fecha2);           
        }
        else{
            query = getEntityManager().createQuery("SELECT c FROM Ponencia c where  "
                +"c.fechaInicial <= :fecha2 and"
                + "(c.apoyoIdapoyo.idapoyo = :apoyo or :apoyo=0)"
                + "and (c.participantesCedula.cedula = :participante or :participante = 0)"
                + "and (c.proyectoIdproyecto.idproyecto = :proyecto or :proyecto = 0) and (c.tipoIdtipo.idtipo = :tipo or :tipo = 0)"
                + "and (c.ciudadidCiudad.idCiudad = :ciudad or :ciudad = 0)"
                    );        
            query.setParameter("proyecto",proyecto).setParameter("participante", participante).setParameter("ciudad",ciudad).setParameter("tipo", tipo).setParameter("apoyo", apoyo).setParameter("fecha2", fecha2);            
        }
 
        setRango(range);
        return query.getResultList();        
    }
}


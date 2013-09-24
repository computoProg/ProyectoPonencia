/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;
//import jsf.ParticipantesController;
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
public class ParticipantesFacade extends AbstractFacade<Participantes> {
    @PersistenceContext(unitName = "ponenciasPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParticipantesFacade() {
        super(Participantes.class);
    }
     public List<Participantes> participantesEvento(int id)
    {

Query query = em.createQuery(" SELECT c FROM Participantes c where c.cedula = :cedula");
//ParticipantesController pc = null;
query.setParameter("cedula",id);

 List<Participantes> c = (List<Participantes>)(query.getResultList());
  return (List<Participantes>) c;

  // return getEntityManager().createQuery("SELECT c FROM lista_estados c WHERE c.codigo_pais : = codigoPais").getResultList();
    }

     //listado de eventos por participante





//     public List<Evento> eventosParticipante(int id)
//    {
//
//Query query = em.createQuery(" SELECT c FROM EventoParticipantes c where c.participantes.cedula = :cedula");
////ParticipantesController pc = null;
//query.setParameter("cedula",id);
//
// List<Evento> c = (List<Evento>)(query.getResultList());
//  return (List<Evento>) c;
//
//  // return getEntityManager().createQuery("SELECT c FROM lista_estados c WHERE c.codigo_pais : = codigoPais").getResultList();
//    }





     //listado de proyectos por participante
     public List<Proyecto> proyectosParticipante(int id)
    {

Query query = em.createQuery(" SELECT c FROM Proyecto c where c.idparticipante.cedula = :cedula");
//ParticipantesController pc = null;
query.setParameter("cedula",id);

 List<Proyecto> c = (List<Proyecto>)(query.getResultList());
  return (List<Proyecto>) c;

  
    }
       public List<Participantes> participantes_lista(int[] range){
        javax.persistence.Query q = getEntityManager().createQuery("SELECT e FROM Participantes e");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        return q.getResultList();

    }
         public List<Participantes> participantes_cedula(int[] range,int cedula)
    {

 javax.persistence.Query q = getEntityManager().createQuery("SELECT e FROM Participantes e where e.cedula= :cedula");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("cedula",cedula);

        return q.getResultList();

}
         public List<Participantes> participantes_cedula_rol(int[] range,int cedula, int rol)
    {

 javax.persistence.Query q = getEntityManager().createQuery("SELECT e FROM Participantes e where e.cedula= :cedula and e.rol.idrol= :rol");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("cedula",cedula).setParameter("rol",rol);

        return q.getResultList();

}
           public List<Participantes> participantes_nombre(int[] range,String nombre)
    {

 javax.persistence.Query q = getEntityManager().createQuery("SELECT c FROM Participantes c where c.nombre like :nombre%");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("nombre",nombre);

        return q.getResultList();

}
           public List<Participantes> participantes_nombre_rol(int[] range,String nombre,int rol)
    {

 javax.persistence.Query q = getEntityManager().createQuery("SELECT c FROM Participantes c where c.nombre like :nombre% and c.rol.idrol= :rol");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("nombre",nombre).setParameter("rol",rol);

        return q.getResultList();

}
            public List<Participantes> participantes_rol(int[] range, int rol)
    {

 javax.persistence.Query q = getEntityManager().createQuery("SELECT e FROM Participantes e where e.rol.idrol= :rol");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("rol",rol);

        return q.getResultList();

}

             public List<Participantes> participantes_cedula_nombre(int[] range,int cedula,String nombre)
    {

 javax.persistence.Query q = getEntityManager().createQuery("SELECT c FROM Participantes c where c.nombre like :nombre% and c.cedula= :cedula");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("nombre",nombre).setParameter("cedula",cedula);

        return q.getResultList();

}
              public List<Participantes> participantes_cedula_nombre_rol(int[] range,int cedula,String nombre, int rol)
    {

 javax.persistence.Query q = getEntityManager().createQuery("SELECT c FROM Participantes c where c.nombre like :nombre% and c.cedula= :cedula and c.rol.idrol");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("nombre",nombre).setParameter("cedula",cedula).setParameter("rol",rol);

        return q.getResultList();

}
              public List<Participantes> participantes_vinculacion(int[] range,int vinculacion)
    {

 javax.persistence.Query q = getEntityManager().createQuery("SELECT c FROM Participantes c where c.vinculacion.idvinculacion= :vinculacion");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("vinculacion",vinculacion);

        return q.getResultList();

}
              public List<Participantes> participantes_vinculacion_rol(int[] range,int vinculacion, int rol)
    {

 javax.persistence.Query q = getEntityManager().createQuery("SELECT e FROM Participantes e where e.rol.idrol= :rol and e.vinculacion.idvinculacion= :vinculacion");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("rol",rol).setParameter("vinculacion",vinculacion);

        return q.getResultList();

}
               public List<Participantes> participantes_nombre_vinculacion(int[] range,String nombre,int vinculacion)
    {

 javax.persistence.Query q = getEntityManager().createQuery("SELECT c FROM Participantes c where c.vinculacion.idvinculacion= :vinculacion and c.nombre like :nombre%");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("vinculacion",vinculacion).setParameter("nombre", nombre);

        return q.getResultList();

}
                public List<Participantes> participantes_nombre_vinculacion_rol(int[] range,String nombre,int vinculacion, int rol)
    {

 javax.persistence.Query q = getEntityManager().createQuery("SELECT c FROM Participantes c where c.vinculacion.idvinculacion= :vinculacion and c.nombre like :nombre% and c.rol.idrol= :rol");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("vinculacion",vinculacion).setParameter("nombre", nombre).setParameter("rol", rol);

        return q.getResultList();

}
                  public List<Participantes> participantes_cedula_vinculacion(int[] range,int cedula,int vinculacion)
    {

 javax.persistence.Query q = getEntityManager().createQuery(" SELECT c FROM Participantes c where c.vinculacion.idvinculacion= :vinculacion and c.cedula= :cedula");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("vinculacion",vinculacion).setParameter("cedula",cedula);

        return q.getResultList();

}
                    public List<Participantes> participantes_cedula_vinculacion_rol(int[] range,int cedula,int vinculacion, int rol)
    {

 javax.persistence.Query q = getEntityManager().createQuery(" SELECT c FROM Participantes c where c.vinculacion.idvinculacion= :vinculacion and c.cedula= :cedula and c.rol.idrol= :rol");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("vinculacion",vinculacion).setParameter("cedula",cedula).setParameter("rol",rol);

        return q.getResultList();

}
        public List<Participantes> participantes_prueba(int[] range,int apoyo)
    {

 javax.persistence.Query q = getEntityManager().createQuery(" SELECT c FROM Participantes c where c.apoyo.idapoyo= :apoyo or c.apoyo= null");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("apoyo",apoyo);

        return q.getResultList();

}
    public List<Participantes> participantes_cedula_nombre_vinculacion(int[] range,int cedula,String nombre,int vinculacion){
       javax.persistence.Query q = getEntityManager().createQuery(" SELECT c FROM Participantes c where c.vinculacion.idvinculacion= :vinculacion and"
               + " c.nombre like :nombre% and c.cedula= :cedula");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("vinculacion",vinculacion).setParameter("nombre", nombre).setParameter("cedula", cedula);
        return q.getResultList();
    }

//    public List<Participantes> participantes_cedula_nombre_vinculacion_rol(int[] range,int cedula,String nombre,int vinculacion, int rol, int gi){
//        javax.persistence.Query q = getEntityManager().createQuery(" SELECT c FROM Participantes c where (c.vinculacion.idvinculacion = :vinculacion or :vinculacion =0)"
//                + " and (c.nombre like :nombre or :nombre=' ' )and "
//                + "(c.cedula = :cedula or :cedula =0) and"
//                + " (c.rol.idrol= :rol or :rol =0) and "
//                + "(c.grupoInvestigacionidGrupoInvestigacion.idGrupoInvestigacion= :gi or :gi =0) order by c.cedula");
//        q.setMaxResults(range[1] - range[0]);
//        q.setFirstResult(range[0]);
//        q.setParameter("vinculacion",vinculacion).setParameter("nombre", nombre).setParameter("cedula", cedula).setParameter("rol", rol).setParameter("gi", gi);
//        return q.getResultList();
//    }
    public List<Participantes> participantes_cedula_nombre_vinculacion_rol(int[] range,int cedula,String nombre,int vinculacion, int rol, int gi, String estado){
        javax.persistence.Query q = getEntityManager().createQuery(" SELECT c FROM Participantes c where (c.vinculacion.idvinculacion = :vinculacion or :vinculacion =0)"
                + " and (c.nombre like :nombre or :nombre=' ' )and "
                + "(c.cedula = :cedula or :cedula =0) and"
                + " (c.rol.idrol= :rol or :rol =0) and "
                + " (c.estado like :estado or :estado=' ' )and "
                + "(c.grupoInvestigacionidGrupoInvestigacion.idGrupoInvestigacion= :gi or :gi =0) order by c.cedula");
        q.setMaxResults(range[1] - range[0]);
        q.setFirstResult(range[0]);
        q.setParameter("vinculacion",vinculacion).setParameter("nombre", nombre).setParameter("cedula", cedula).setParameter("rol", rol).setParameter("gi", gi).setParameter("estado", estado);
        return q.getResultList();
    }


//    public List<Participantes> participantes_para_num(int cedula,String nombre,int vinculacion, int rol, int gi){
//        javax.persistence.Query q = getEntityManager().createQuery(" SELECT c FROM Participantes c where (c.vinculacion.idvinculacion = :vinculacion or :vinculacion =0) and (c.nombre like :nombre or :nombre=' ' )and (c.cedula = :cedula or :cedula =0) and (c.rol.idrol= :rol or :rol =0)" +
//        "and (c.grupoInvestigacionidGrupoInvestigacion.idGrupoInvestigacion = :gi or :gi = 0) order by c.cedula");
//        q.setParameter("vinculacion",vinculacion).setParameter("nombre", nombre).setParameter("cedula", cedula).setParameter("rol", rol).setParameter("gi", gi);
//        return q.getResultList();
//     }
     public List<Participantes> participantes_para_num(int cedula,String nombre,int vinculacion, int rol, int gi, String estado){
        javax.persistence.Query q = getEntityManager().createQuery(" SELECT c FROM Participantes c where (c.vinculacion.idvinculacion = :vinculacion or :vinculacion =0) and (c.nombre like :nombre or :nombre=' ' )and (c.cedula = :cedula or :cedula =0) and (c.rol.idrol= :rol or :rol =0)" +
                "and (c.estado like :estado or :estado=' ')" +
        "and (c.grupoInvestigacionidGrupoInvestigacion.idGrupoInvestigacion = :gi or :gi = 0) order by c.cedula");
        q.setParameter("vinculacion",vinculacion).setParameter("nombre", nombre).setParameter("cedula", cedula).setParameter("rol", rol).setParameter("gi", gi).setParameter("estado", estado);
        return q.getResultList();
     }

      public int contar() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        javax.persistence.Query q = getEntityManager().createQuery(" SELECT c FROM Participantes c where (c.vinculacion.idvinculacion = :vinculacion or :vinculacion =0) and (c.nombre like :nombre% or :nombre=' ' )and (c.cedula = :cedula or :cedula =0) and (c.rol.idrol= :rol or :rol =0) order by c.cedula");
        return ((Long) q.getSingleResult()).intValue();
    }


}

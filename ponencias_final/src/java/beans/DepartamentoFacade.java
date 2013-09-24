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
public class DepartamentoFacade extends AbstractFacade<Departamento>{
    @PersistenceContext(unitName = "ponenciasPU")
    private EntityManager em;

    public DepartamentoFacade(){
        super(Departamento.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Departamento> departamentosPais(int idPais){
        Query query = em.createQuery("SELECT d FROM Departamento d WHERE d.paisidPais := idPais");
        query.setParameter("idPais", idPais);
        List<Departamento> d = (List<Departamento>)query.getResultList();
        return d;
    }

    public List<Departamento> departamentos(){
        Query query = em.createQuery("SELECT d FROM Departamento d");
        List<Departamento> d = (List<Departamento>)query.getResultList();
        return d;
    }

    public List<Ciudad> ciudadesDepartamento(int idDep){
        Query query = em.createQuery("SELECT c FROM Ciudad c WHERE c.departamentoidDepartamento.idDepartamento = :idDep");
        query.setParameter("idDep", idDep);
        List<Ciudad> c = (List<Ciudad>)query.getResultList();
        return c;
    }

}

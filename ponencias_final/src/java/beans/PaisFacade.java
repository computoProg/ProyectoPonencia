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
public class PaisFacade extends AbstractFacade<Pais>{
    @PersistenceContext(unitName = "ponenciasPU")
    private EntityManager em;

    public PaisFacade(){
        super(Pais.class);
    }

    @Override
    protected EntityManager getEntityManager() {
       return em;
    }

    public List<Pais> paises(){
        Query query = em.createQuery("SELECT p FROM Pais p");
        List<Pais> p = (List<Pais>)query.getResultList();
        return p;
    }

   public List<Departamento> departamentosPais(Integer idPais) {
        Query query = em.createQuery("SELECT d FROM Departamento d WHERE d.paisidPais.idPais = :idPais");
        query.setParameter("idPais", idPais);
        List<Departamento> c = (List<Departamento>)query.getResultList();
        return c;
    }



}

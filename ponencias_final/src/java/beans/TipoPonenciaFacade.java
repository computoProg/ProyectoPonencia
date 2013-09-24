
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
public class TipoPonenciaFacade extends AbstractFacade<TipoPonencia>{

    @PersistenceContext(unitName = "ponenciasPU")
    private EntityManager em;

    public TipoPonenciaFacade(){
        super(TipoPonencia.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public List<Ponencia> ponenciasTipo(int ponencia){
        Query query = em.createQuery(" SELECT p FROM Ponencia p where p.tipoIdtipo.idtipo = :ponencia");
        query.setParameter("ponencia",ponencia);
        List<Ponencia> c = (List<Ponencia>)(query.getResultList());
        return (List<Ponencia>) c;
    }
}


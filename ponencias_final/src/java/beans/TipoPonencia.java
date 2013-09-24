/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author Aux de Programacion
 */
@Entity
@Table(name = "tipoponencia")
@NamedQueries({
    @NamedQuery(name = "TipoPonencia.findAll", query = "SELECT t FROM TipoPonencia t"),
    @NamedQuery(name = "TipoPonencia.findByIdtipo", query = "SELECT t FROM TipoPonencia t WHERE t.idtipo = :idtipo"),
    @NamedQuery(name = "TipoPonencia.findByTipo", query = "SELECT t FROM TipoPonencia t WHERE t.tipo = :tipo")})
public class TipoPonencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    
    @Column(name = "idtipo")
    private Integer idtipo;
    @Basic(optional = false)
    
    @Column(name = "tipo")
    private String tipo;

    public TipoPonencia() {
    }

    public TipoPonencia(Integer idtipo) {
        this.idtipo = idtipo;
    }

    public TipoPonencia(Integer idtipo, String tipo) {
        this.idtipo = idtipo;
        this.tipo = tipo;
    }

    public Integer getIdtipo() {
        return idtipo;
    }

    public void setIdtipo(Integer idtipo) {
        this.idtipo = idtipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipo != null ? idtipo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoPonencia)) {
            return false;
        }
        TipoPonencia other = (TipoPonencia) object;
        if ((this.idtipo == null && other.idtipo != null) || (this.idtipo != null && !this.idtipo.equals(other.idtipo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return tipo;
    }

}

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
@Table(name = "vinculacion")
@NamedQueries({
    @NamedQuery(name = "Vinculacion.findAll", query = "SELECT v FROM Vinculacion v"),
    @NamedQuery(name = "Vinculacion.findByIdvinculacion", query = "SELECT v FROM Vinculacion v WHERE v.idvinculacion = :idvinculacion"),
    @NamedQuery(name = "Vinculacion.findByVinculacion", query = "SELECT v FROM Vinculacion v WHERE v.vinculacion = :vinculacion")})
public class Vinculacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idvinculacion")
    private Integer idvinculacion;
    @Basic(optional = false)
    @Column(name = "vinculacion")
    private String vinculacion;

    public Vinculacion() {
    }

    public Vinculacion(Integer idvinculacion) {
        this.idvinculacion = idvinculacion;
    }

    public Vinculacion(Integer idvinculacion, String vinculacion) {
        this.idvinculacion = idvinculacion;
        this.vinculacion = vinculacion;
    }

    public Integer getIdvinculacion() {
        return idvinculacion;
    }

    public void setIdvinculacion(Integer idvinculacion) {
        this.idvinculacion = idvinculacion;
    }

    public String getVinculacion() {
        return vinculacion;
    }

    public void setVinculacion(String vinculacion) {
        this.vinculacion = vinculacion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idvinculacion != null ? idvinculacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vinculacion)) {
            return false;
        }
        Vinculacion other = (Vinculacion) object;
        if ((this.idvinculacion == null && other.idvinculacion != null) || (this.idvinculacion != null && !this.idvinculacion.equals(other.idvinculacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return vinculacion;
    }

}

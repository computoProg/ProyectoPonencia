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
@Table(name = "apoyo", catalog = "ponencias", schema = "")
@NamedQueries({
    @NamedQuery(name = "Apoyo.findAll", query = "SELECT a FROM Apoyo a"),
    @NamedQuery(name = "Apoyo.findByIdapoyo", query = "SELECT a FROM Apoyo a WHERE a.idapoyo = :idapoyo"),
    @NamedQuery(name = "Apoyo.findByApoyo", query = "SELECT a FROM Apoyo a WHERE a.apoyo = :apoyo"),
    @NamedQuery(name = "Apoyo.findByEstado", query = "SELECT a FROM Apoyo a WHERE a.estado = :estado"),
    @NamedQuery(name = "Apoyo.findByDescripcion", query = "SELECT a FROM Apoyo a WHERE a.descripcion = :descripcion")})
public class Apoyo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idapoyo")
    private Integer idapoyo;
    @Basic(optional = false)
    @Column(name = "apoyo")
    private String apoyo;
    @Basic(optional = false)
    @Column(name = "estado")
    private String estado;
    @Column(name = "descripcion")
    private String descripcion;

    public Apoyo() {
    }

    public Apoyo(Integer idapoyo) {
        this.idapoyo = idapoyo;
    }

    public Apoyo(Integer idapoyo, String apoyo, String estado) {
        this.idapoyo = idapoyo;
        this.apoyo = apoyo;
        this.estado = estado;
    }

    public Integer getIdapoyo() {
        return idapoyo;
    }

    public void setIdapoyo(Integer idapoyo) {
        this.idapoyo = idapoyo;
    }

    public String getApoyo() {
        return apoyo;
    }

    public void setApoyo(String apoyo) {
        this.apoyo = apoyo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idapoyo != null ? idapoyo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Apoyo)) {
            return false;
        }
        Apoyo other = (Apoyo) object;
        if ((this.idapoyo == null && other.idapoyo != null) || (this.idapoyo != null && !this.idapoyo.equals(other.idapoyo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return apoyo;
    }

}

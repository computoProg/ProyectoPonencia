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
@Table(name = "grupoinvestigacion")
@NamedQueries({
    @NamedQuery(name = "GrupoInvestigacion.findAll", query = "SELECT g FROM GrupoInvestigacion g"),
    @NamedQuery(name = "GrupoInvestigacion.findByIdGrupoInvestigacion", query = "SELECT g FROM GrupoInvestigacion g WHERE g.idGrupoInvestigacion = :idGrupoInvestigacion"),
    @NamedQuery(name = "GrupoInvestigacion.findByNombre", query = "SELECT g FROM GrupoInvestigacion g WHERE g.nombre = :nombre"),
    @NamedQuery(name = "GrupoInvestigacion.findByDescripcion", query = "SELECT g FROM GrupoInvestigacion g WHERE g.descripcion = :descripcion"),
    @NamedQuery(name = "GrupoInvestigacion.findByClasificacion", query = "SELECT g FROM GrupoInvestigacion g WHERE g.clasificacion = :clasificacion"),
    @NamedQuery(name = "GrupoInvestigacion.findByEstado", query = "SELECT g FROM GrupoInvestigacion g WHERE g.estado = :estado")})
public class GrupoInvestigacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idGrupoInvestigacion")
    private Integer idGrupoInvestigacion;
    @Basic(optional = false)
    @Column(name = "Nombre")
    private String nombre;
    @Column(name = "Descripcion")
    private String descripcion;
    @Basic(optional = false)
    @Column(name = "Clasificacion")
    private String clasificacion;
    @Basic(optional = false)
    @Column(name = "estado")
    private String estado;

    public GrupoInvestigacion() {
    }

    public GrupoInvestigacion(Integer idGrupoInvestigacion) {
        this.idGrupoInvestigacion = idGrupoInvestigacion;
    }

    public GrupoInvestigacion(Integer idGrupoInvestigacion, String nombre, String clasificacion, String estado) {
        this.idGrupoInvestigacion = idGrupoInvestigacion;
        this.nombre = nombre;
        this.clasificacion = clasificacion;
        this.estado = estado;
    }

    public Integer getIdGrupoInvestigacion() {
        return idGrupoInvestigacion;
    }

    public void setIdGrupoInvestigacion(Integer idGrupoInvestigacion) {
        this.idGrupoInvestigacion = idGrupoInvestigacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getClasificacion() {
        return clasificacion;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idGrupoInvestigacion != null ? idGrupoInvestigacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GrupoInvestigacion)) {
            return false;
        }
        GrupoInvestigacion other = (GrupoInvestigacion) object;
        if ((this.idGrupoInvestigacion == null && other.idGrupoInvestigacion != null) || (this.idGrupoInvestigacion != null && !this.idGrupoInvestigacion.equals(other.idGrupoInvestigacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

}

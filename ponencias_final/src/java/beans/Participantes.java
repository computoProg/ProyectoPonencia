/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Aux de Programacion
 */
@Entity
@Table(name = "participantes")
@NamedQueries({
    @NamedQuery(name = "Participantes.findAll", query = "SELECT p FROM Participantes p"),
    @NamedQuery(name = "Participantes.findByCedula", query = "SELECT p FROM Participantes p WHERE p.cedula = :cedula"),
    @NamedQuery(name = "Participantes.findByNombre", query = "SELECT p FROM Participantes p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Participantes.findByEmail", query = "SELECT p FROM Participantes p WHERE p.email = :email"),
    @NamedQuery(name = "Participantes.findByContraseña", query = "SELECT p FROM Participantes p WHERE p.contraseña = :contraseña"),
    @NamedQuery(name = "Participantes.findByContraseñav", query = "SELECT p FROM Participantes p WHERE p.contraseñav = :contraseñav"),
    @NamedQuery(name = "Participantes.findByApellido", query = "SELECT p FROM Participantes p WHERE p.apellido = :apellido"),
    @NamedQuery(name = "Participantes.findByEstado", query = "SELECT p FROM Participantes p WHERE p.estado = :estado")})
public class Participantes implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cedula")
    private Integer cedula;
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "email")
    private String email;
    @Basic(optional = false)
    @Column(name = "contraseña")
    private String contraseña;
    @Basic(optional = false)
    @Column(name = "contraseñav")
    private String contraseñav;
    @Basic(optional = false)
    @Column(name = "apellido")
    private String apellido;
    @Basic(optional = false)
    @Column(name = "estado")
    private String estado;
    @JoinColumn(name = "GrupoInvestigacion_idGrupoInvestigacion", referencedColumnName = "idGrupoInvestigacion")
    @ManyToOne(optional = false)
    private GrupoInvestigacion grupoInvestigacionidGrupoInvestigacion;
    @JoinColumn(name = "rol", referencedColumnName = "idrol")
    @ManyToOne(optional = false)
    private Rol rol;
    @JoinColumn(name = "vinculacion", referencedColumnName = "idvinculacion")
    @ManyToOne(optional = false)
    private Vinculacion vinculacion;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "participantesCedula")
    private Collection<Ponencia> ponenciaCollection;

    public Participantes() {
    }

    public Participantes(Integer cedula) {
        this.cedula = cedula;
    }

    public Participantes(Integer cedula, String nombre, String email, String contraseña, String contraseñav, String apellido, String estado) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.email = email;
        this.contraseña = contraseña;
        this.contraseñav = contraseñav;
        this.apellido = apellido;
        this.estado = estado;
    }

    public Integer getCedula() {
        return cedula;
    }

    public void setCedula(Integer cedula) {
        this.cedula = cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getContraseñav() {
        return contraseñav;
    }

    public void setContraseñav(String contraseñav) {
        this.contraseñav = contraseñav;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public GrupoInvestigacion getGrupoInvestigacionidGrupoInvestigacion() {
        return grupoInvestigacionidGrupoInvestigacion;
    }

    public void setGrupoInvestigacionidGrupoInvestigacion(GrupoInvestigacion grupoInvestigacionidGrupoInvestigacion) {
        this.grupoInvestigacionidGrupoInvestigacion = grupoInvestigacionidGrupoInvestigacion;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public Vinculacion getVinculacion() {
        return vinculacion;
    }

    public void setVinculacion(Vinculacion vinculacion) {
        this.vinculacion = vinculacion;
    }

    public Collection<Ponencia> getPonenciaCollection() {
        return ponenciaCollection;
    }

    public void setPonenciaCollection(Collection<Ponencia> ponenciaCollection) {
        this.ponenciaCollection = ponenciaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cedula != null ? cedula.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Participantes)) {
            return false;
        }
        Participantes other = (Participantes) object;
        if ((this.cedula == null && other.cedula != null) || (this.cedula != null && !this.cedula.equals(other.cedula))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre + apellido;
    }

}

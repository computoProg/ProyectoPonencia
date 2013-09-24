/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package beans;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Aux de Programacion
 */
@Entity
@Table(name = "ponencia")
@NamedQueries({
    @NamedQuery(name = "Ponencia.findAll", query = "SELECT p FROM Ponencia p"),
    @NamedQuery(name = "Ponencia.findByIdponencia", query = "SELECT p FROM Ponencia p WHERE p.idponencia = :idponencia"),
    @NamedQuery(name = "Ponencia.findByNombre", query = "SELECT p FROM Ponencia p WHERE p.nombre = :nombre"),
    @NamedQuery(name = "Ponencia.findByDescripcion", query = "SELECT p FROM Ponencia p WHERE p.descripcion = :descripcion"),
    @NamedQuery(name = "Ponencia.findByFechaInicial", query = "SELECT p FROM Ponencia p WHERE p.fechaInicial = :fechaInicial"),
    @NamedQuery(name = "Ponencia.findByEvento", query = "SELECT p FROM Ponencia p WHERE p.evento = :evento")})
public class Ponencia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idponencia")
    private Integer idponencia;
    @Basic(optional = false)    
    @Column(name = "nombre")
    private String nombre;    
    @Column(name = "descripcion")
    private String descripcion;
    @Basic(optional = false)    
    @Column(name = "fechaInicial")
    @Temporal(TemporalType.DATE)
    private Date fechaInicial;
    @Basic(optional = false)    
    @Column(name = "evento")
    private String evento;    
    @JoinColumn(name = "apoyo_idapoyo", referencedColumnName = "idapoyo")
    @ManyToOne(optional = false)
    private Apoyo apoyoIdapoyo;    
    @JoinColumn(name = "Ciudad_idCiudad", referencedColumnName = "idCiudad")
    @ManyToOne(optional = false)
    private Ciudad ciudadidCiudad;    
    @JoinColumn(name = "participantes_cedula", referencedColumnName = "cedula")
    @ManyToOne(optional = false)
    private Participantes participantesCedula;    
    @JoinColumn(name = "proyecto_idproyecto", referencedColumnName = "idproyecto")
    @ManyToOne(optional = false)
    private Proyecto proyectoIdproyecto;    
    @JoinColumn(name = "tipo_idtipo", referencedColumnName = "idtipo")
    @ManyToOne(optional = false)
    private TipoPonencia tipoIdtipo;

    public Ponencia() {
    }
        
    public Ponencia(Integer idponencia) {
        this.idponencia = idponencia;
    }
    
    public Ponencia(Integer idponencia, String nombre, Date fechaInicial, String evento) {
        this.idponencia = idponencia;
        this.nombre = nombre;
        this.fechaInicial = fechaInicial;
        this.evento = evento;
    }

    public Integer getIdponencia() {
        return idponencia;
    }

    public void setIdponencia(Integer idponencia) {
        this.idponencia = idponencia;
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

    public Date getFechaInicial() {
        return fechaInicial;
    }

    public void setFechaInicial(Date fechaInicial) {
        this.fechaInicial = fechaInicial;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public Apoyo getApoyoIdapoyo() {
        return apoyoIdapoyo;
    }

    public void setApoyoIdapoyo(Apoyo apoyoIdapoyo) {
        this.apoyoIdapoyo = apoyoIdapoyo;
    }

    public Ciudad getCiudadidCiudad() {     
        return ciudadidCiudad;
    }

    public void setCiudadidCiudad(Ciudad ciudadidCiudad) {        
        this.ciudadidCiudad = ciudadidCiudad;
    }

    public Participantes getParticipantesCedula() {
        return participantesCedula;
    }

    public void setParticipantesCedula(Participantes participantesCedula) {
        this.participantesCedula = participantesCedula;
    }

    public Proyecto getProyectoIdproyecto() {        
        return proyectoIdproyecto;
    }

    public void setProyectoIdproyecto(Proyecto proyectoIdproyecto) {        
        this.proyectoIdproyecto = proyectoIdproyecto;
    }

    public TipoPonencia getTipoIdtipo() {        
        return tipoIdtipo;
    }

    public void setTipoIdtipo(TipoPonencia tipoIdtipo) {        
        this.tipoIdtipo = tipoIdtipo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idponencia != null ? idponencia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Ponencia)) {
            return false;
        }
        Ponencia other = (Ponencia) object;
        if ((this.idponencia == null && other.idponencia != null) || (this.idponencia != null && !this.idponencia.equals(other.idponencia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return nombre;
    }

}

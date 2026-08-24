package org.example.model;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "persona_id")
    private Persona persona;

    @Column(nullable = false, unique = true)
    private String nombreDeUsuario;

    @Column(nullable = false, unique = true)
    private String correo;

    @Column(nullable = false)
    private String contrasena;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "rol")
    private Set<Rol> roles = new HashSet<>();

    public Usuario() {}

    public Usuario(Persona persona, String nombreDeUsuario, String correo, String contrasena) {
        this.persona = persona;
        this.nombreDeUsuario = nombreDeUsuario;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public Usuario(Persona persona, String nombreDeUsuario, String correo, String contrasena, Set<Rol> roles) {
        this.persona = persona;
        this.nombreDeUsuario = nombreDeUsuario;
        this.correo = correo;
        this.contrasena = contrasena;
        this.roles = (roles != null) ? roles : new HashSet<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}
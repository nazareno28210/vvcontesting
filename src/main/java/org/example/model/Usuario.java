package org.example.model;

import jakarta.persistence.*;

@Entity
public class Usuario {
    @Id //no usa generated, usa el id de la persona
    private Long id;

    @OneToOne
    @MapsId // Copia el id de la persona a la que pertenece el usuario
    @JoinColumn(name = "persona_id")
    private Persona persona;
    private String nombreDeUsuario;
    private String correo;
    private String contrasena;

    public Usuario(Persona persona, String nombreDeUsuario, String correo, String contrasena) {
        this.persona = persona;
        this.nombreDeUsuario = nombreDeUsuario;
        this.correo = correo;
        this.contrasena = contrasena;
    }

    public Long getId() {
        return id;
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
}
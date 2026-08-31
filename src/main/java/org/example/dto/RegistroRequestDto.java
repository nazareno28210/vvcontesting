package org.example.dto;

import org.example.model.Rol;
import java.util.Set;

public class RegistroRequestDto {
    private String nombre;
    private String apellido;
    private String nombreDeUsuario;
    private String correo;
    private String contrasena;
    private String confirmarContrasena;
    private Set<Rol> roles;

    public RegistroRequestDto() {}

    public RegistroRequestDto(String nombre, String apellido, String nombreDeUsuario, String correo, String contrasena, String confirmarContrasena, Set<Rol> roles) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.nombreDeUsuario = nombreDeUsuario;
        this.correo = correo;
        this.contrasena = contrasena;
        this.confirmarContrasena = confirmarContrasena;
        this.roles = roles;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
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

    public String getConfirmarContrasena() {
        return confirmarContrasena;
    }

    public void setConfirmarContrasena(String confirmarContrasena) {
        this.confirmarContrasena = confirmarContrasena;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}

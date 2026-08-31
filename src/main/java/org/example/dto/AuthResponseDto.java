package org.example.dto;

import java.util.Set;

public class AuthResponseDto {
    private String token;
    private String tokenType = "Bearer";
    private String correo;
    private String nombreDeUsuario;
    private String nombre;
    private String apellido;
    private Set<String> roles;
    private String mensaje;

    public AuthResponseDto() {}

    public AuthResponseDto(String token, String correo, String nombreDeUsuario, String nombre, String apellido, Set<String> roles, String mensaje) {
        this.token = token;
        this.correo = correo;
        this.nombreDeUsuario = nombreDeUsuario;
        this.nombre = nombre;
        this.apellido = apellido;
        this.roles = roles;
        this.mensaje = mensaje;
    }

    public AuthResponseDto(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreDeUsuario() {
        return nombreDeUsuario;
    }

    public void setNombreDeUsuario(String nombreDeUsuario) {
        this.nombreDeUsuario = nombreDeUsuario;
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

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}

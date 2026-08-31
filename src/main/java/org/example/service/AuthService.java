package org.example.service;

import org.example.dto.AuthResponseDto;
import org.example.dto.LoginRequestDto;
import org.example.dto.RegistroRequestDto;
import org.example.model.Persona;
import org.example.model.Rol;
import org.example.model.Usuario;
import org.example.repository.UsuarioRepository;
import org.example.security.JwtUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(UsuarioRepository usuarioRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager,
                       JwtUtils jwtUtils) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public AuthResponseDto registrar(RegistroRequestDto request) {
        if (request.getNombre() == null || request.getNombre().matches(".*\\d.*")) {
            throw new IllegalArgumentException("El nombre no debe contener números.");
        }

        if (request.getApellido() == null || request.getApellido().matches(".*\\d.*")) {
            throw new IllegalArgumentException("El apellido no debe contener números.");
        }

        if (request.getNombreDeUsuario() == null || request.getNombreDeUsuario().trim().length() < 5) {
            throw new IllegalArgumentException("El nombre de usuario debe tener al menos 5 caracteres.");
        }

        String pass = request.getContrasena();
        if (pass == null || pass.length() < 8) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 8 caracteres.");
        }
        if (!pass.matches(".*[A-Z].*")) {
            throw new IllegalArgumentException("La contraseña debe incluir al menos una letra mayúscula.");
        }
        if (!pass.matches(".*[a-z].*")) {
            throw new IllegalArgumentException("La contraseña debe incluir al menos una letra minúscula.");
        }
        if (!pass.matches(".*\\d.*")) {
            throw new IllegalArgumentException("La contraseña debe incluir al menos un número.");
        }
        if (!pass.matches(".*[^a-zA-Z0-9].*")) {
            throw new IllegalArgumentException("La contraseña debe incluir al menos un carácter especial.");
        }
        if (!pass.equals(request.getConfirmarContrasena())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden.");
        }

        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo '" + request.getCorreo() + "' ya se encuentra registrado.");
        }

        if (usuarioRepository.existsByNombreDeUsuario(request.getNombreDeUsuario())) {
            throw new IllegalArgumentException("El nombre de usuario '" + request.getNombreDeUsuario() + "' ya está en uso.");
        }

        Persona persona = new Persona(request.getNombre(), request.getApellido());
        String contrasenaEncriptada = passwordEncoder.encode(request.getContrasena());

        Set<Rol> roles = new HashSet<>();
        if (request.getRoles() != null && !request.getRoles().isEmpty()) {
            roles.addAll(request.getRoles());
        } else {
            roles.add(Rol.USUARIO);
        }

        Usuario usuario = new Usuario(persona, request.getNombreDeUsuario(), request.getCorreo(), contrasenaEncriptada, roles);
        usuarioRepository.save(usuario);

        Set<String> rolesString = roles.stream().map(Enum::name).collect(Collectors.toSet());

        return new AuthResponseDto(
                null,
                usuario.getCorreo(),
                usuario.getNombreDeUsuario(),
                persona.getNombre(),
                persona.getApellido(),
                rolesString,
                "Usuario registrado exitosamente."
        );
    }

    public AuthResponseDto login(LoginRequestDto request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getCorreo(), request.getContrasena())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generarToken(authentication);

        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        Set<String> rolesString = usuario.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        String nombre = (usuario.getPersona() != null) ? usuario.getPersona().getNombre() : "";
        String apellido = (usuario.getPersona() != null) ? usuario.getPersona().getApellido() : "";

        return new AuthResponseDto(
                token,
                usuario.getCorreo(),
                usuario.getNombreDeUsuario(),
                nombre,
                apellido,
                rolesString,
                "Inicio de sesión exitoso."
        );
    }

    public AuthResponseDto obtenerPerfil(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        Set<String> rolesString = usuario.getRoles().stream().map(Enum::name).collect(Collectors.toSet());
        String nombre = (usuario.getPersona() != null) ? usuario.getPersona().getNombre() : "";
        String apellido = (usuario.getPersona() != null) ? usuario.getPersona().getApellido() : "";

        return new AuthResponseDto(
                null,
                usuario.getCorreo(),
                usuario.getNombreDeUsuario(),
                nombre,
                apellido,
                rolesString,
                "Perfil obtenido con éxito."
        );
    }
}

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
            roles.add(Rol.VISITANTE);
        }

        Usuario usuario = new Usuario(persona, request.getNombreDeUsuario(), request.getCorreo(), contrasenaEncriptada, roles);
        usuarioRepository.save(usuario);

        Set<String> rolesString = roles.stream().map(Enum::name).collect(Collectors.toSet());

        return new AuthResponseDto(
                null,
                usuario.getCorreo(),
                usuario.getNombreDeUsuario(),
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

        return new AuthResponseDto(
                token,
                usuario.getCorreo(),
                usuario.getNombreDeUsuario(),
                rolesString,
                "Inicio de sesión exitoso."
        );
    }
}

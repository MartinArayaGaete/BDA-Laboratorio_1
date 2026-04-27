package com.example.demo.services;

import com.example.demo.dtos.UserInfoDTO;
import com.example.demo.dtos.UsuarioRegistroDTO;
import com.example.demo.models.Usuario;
import com.example.demo.repositories.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Usuario> findAll() {
        return usuarioRepository.obtenerTodos();
    }

    public Optional<Usuario> findByRut(String rut) {
        return usuarioRepository.buscarPorRut(rut);
    }

    public void registerUser(UsuarioRegistroDTO dto) {
        if (usuarioRepository.existePorRut(dto.getRut())) {
            throw new IllegalArgumentException("El RUT ya está registrado");
        }
        String hash = passwordEncoder.encode(dto.getContrasena());
        usuarioRepository.crearUsuario(dto.getRut(), dto.getNombre(), dto.getCorreo(), hash, dto.getRol());
    }

    public void update(String rut, UsuarioRegistroDTO dto) {
        if (!usuarioRepository.existePorRut(rut)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        String hash = passwordEncoder.encode(dto.getContrasena());
        usuarioRepository.actualizarUsuario(rut, dto.getNombre(), dto.getCorreo(), hash, dto.getRol());
    }

    public void deleteByRut(String rut) {
        if (usuarioRepository.existePorRut(rut)) {
            usuarioRepository.eliminarPorRut(rut);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
    }

    // Aca se puede poner AESUtils mas adelante cuando hagamos el front
    public Optional<Usuario> validarLogin(String rut, String rawPassword) {
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorRut(rut);

        if (usuarioOpt.isEmpty()) {
            return Optional.empty();
        }

        Usuario usuario = usuarioOpt.get();
        String passwordEncriptadaBD = usuario.getContrasena();

        // compara la contraseña guardada encriptada con la contraseña entregada por el usuario
        if (passwordEncoder.matches(rawPassword, passwordEncriptadaBD)) {
            return Optional.of(usuario);
        }

        return Optional.empty();
    }

    public UserInfoDTO toUserInfoDTO(Usuario usuario) {
        return new UserInfoDTO(
                usuario.getIdUsuario(),
                usuario.getRut(),
                usuario.getRol(),
                usuario.getCorreo(),
                usuario.getNombre()
        );
    }
}
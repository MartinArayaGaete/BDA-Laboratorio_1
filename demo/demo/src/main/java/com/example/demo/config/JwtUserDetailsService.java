package com.example.demo.config;

/*
import com.example.demo.entities.LoginEntity;
import com.example.demo.repositories.LoginRepository;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final LoginRepository loginRepository;

    @Override
    public UserDetails loadUserByUsername(String rut) throws UsernameNotFoundException {
        // Busca al usuario por su RUT (en lugar de "username")
        Optional<LoginEntity> usuarioOpt = loginRepository.findByRutUsuario(rut);

        if (usuarioOpt.isEmpty()) {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }

        LoginEntity usuario = usuarioOpt.get();

        // Devuelve un UserDetails con el RUT, contraseña hasheada y rol como autoridad
        return new org.springframework.security.core.userdetails.User(
                usuario.getRutUsuario(),
                usuario.getPasswordUsuario(),
                AuthorityUtils.createAuthorityList(usuario.getRolUsuario()) // Devuelve ["ROLE_PACIENTE"]
        );
    }
}

*/
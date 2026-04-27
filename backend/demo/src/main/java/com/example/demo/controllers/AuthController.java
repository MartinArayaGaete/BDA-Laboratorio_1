package com.example.demo.controllers;

import com.example.demo.config.JwtUtils;
import com.example.demo.dtos.UserInfoDTO;
import jakarta.servlet.http.HttpServletRequest;
import com.example.demo.dtos.ValidarLoginDTO;
import com.example.demo.models.Usuario;
import com.example.demo.services.UsuarioService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;
    private final JwtUtils jwtUtils;

    public AuthController(UsuarioService usuarioService, JwtUtils jwtUtils) {
        this.usuarioService = usuarioService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/login")
    public ResponseEntity<?> iniciarSesion(@RequestBody ValidarLoginDTO credenciales, HttpServletResponse response) {
        try {
            Optional<Usuario> usuarioOpt = usuarioService.validarLogin(credenciales.getRut(), credenciales.getPassword());

            if (usuarioOpt.isPresent()) {
                Usuario usuario = usuarioOpt.get();
                String token = jwtUtils.generateToken(usuario.getRut(), usuario.getRol());

                // Crear la cookie
                Cookie jwtCookie = new Cookie("token_acceso", token);
                jwtCookie.setHttpOnly(true); // Evita que JavaScript lea la cookie (protección XSS)
                jwtCookie.setSecure(false);  // localhost
                jwtCookie.setPath("/");      // Para que la cookie se envíe en todas las rutas de la API
                jwtCookie.setMaxAge(60 * 60); // 1 hora

                // Añade la cookie
                response.addCookie(jwtCookie);

                // Devuelve info sin contraseña
                UserInfoDTO userInfo = usuarioService.toUserInfoDTO(usuario);
                return ResponseEntity.ok(userInfo);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestHeader(value = "Authorization", required = false) String authorizationHeader, HttpServletRequest request) {
        String token = null;

        // Primero intentar sacar el token de la cookie
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("token_acceso".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        // Si no hay cookie, intentar sacar del header
        if (token == null && authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
        }

        if (token == null || !jwtUtils.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String rut = jwtUtils.extractRut(token);
        Optional<Usuario> usuarioOpt = usuarioService.findByRut(rut);

        if (usuarioOpt.isPresent()) {
            String nuevoToken = jwtUtils.generateToken(usuarioOpt.get().getRut(), usuarioOpt.get().getRol());
            return ResponseEntity.ok(nuevoToken);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
package com.example.demo.entities;

import lombok.Data;

/*

@Data
@Table(name = "login")
public class LoginEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLogin;

    // Campos básicos
    @Column(name = "rut_usuario", length = 10, nullable = false, unique = true)
    private String rutUsuario;

    @Column(name = "password_usuario", length = 255, nullable = false)
    private String passwordUsuario;  // Almacenar el hash de la contraseña

    @Column(name = "rol_usuario", length = 15, nullable = false)
    private String rolUsuario;

    @Column(name = "correo_usuario", length = 50, nullable = true)
    private String correoUsuario;

    @Column(name = "nombre_usuario", length = 20, nullable = true)
    private String nombreUsuario;
}

*/
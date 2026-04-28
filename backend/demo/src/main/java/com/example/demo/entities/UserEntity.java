package com.example.demo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {

    private Long id_usuario;

    private String rut;

    private String correo;

    private String nombre;

    private String contraseña;

    private String rol;

    private String keycloak_id;

}

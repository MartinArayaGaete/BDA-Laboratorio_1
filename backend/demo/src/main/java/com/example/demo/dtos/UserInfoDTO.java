package com.example.demo.dtos;

public class UserInfoDTO {
    private Long idUsuario;
    private String rut;
    private String rol;
    private String correo;
    private String nombre;

    public UserInfoDTO(Long idUsuario, String rut, String rol, String correo, String nombre) {
        this.idUsuario = idUsuario;
        this.rut = rut;
        this.rol = rol;
        this.correo = correo;
        this.nombre = nombre;
    }

    // Getters y Setters
    public Long getIdUsuario() { return idUsuario; }
    public String getRut() { return rut; }
    public String getRol() { return rol; }
    public String getCorreo() { return correo; }
    public String getNombre() { return nombre; }
}
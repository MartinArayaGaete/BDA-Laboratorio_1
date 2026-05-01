package com.example.demo.dtos;

public class InscritoDTO {
    private Long idParticipacion;
    private Long idUsuario;
    private String rut;
    private String nombre;

    public InscritoDTO() {}

    // Getters y Setters
    public Long getIdParticipacion() { return idParticipacion; }
    public void setIdParticipacion(Long idParticipacion) { this.idParticipacion = idParticipacion; }
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public String getRut() { return rut; }
    public void setRut(String rut) { this.rut = rut; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
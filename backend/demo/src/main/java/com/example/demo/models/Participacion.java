package com.example.demo.models;

public class Participacion {
    private Long idParticipacion;
    private Long idUsuario;
    private Long idTorneo;

    public Participacion() {}

    // Getters y Setters
    public Long getIdParticipacion() { return idParticipacion; }
    public void setIdParticipacion(Long idParticipacion) { this.idParticipacion = idParticipacion; }
    public Long getIdUsuario() { return idUsuario; }
    public void setIdUsuario(Long idUsuario) { this.idUsuario = idUsuario; }
    public Long getIdTorneo() { return idTorneo; }
    public void setIdTorneo(Long idTorneo) { this.idTorneo = idTorneo; }
}
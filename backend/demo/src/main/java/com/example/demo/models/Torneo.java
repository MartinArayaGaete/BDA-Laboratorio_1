package com.example.demo.models;

import java.time.LocalDate;

public class Torneo {
    private Long idTorneo;
    private Long idCategoria;
    private String nombreTorneo;
    private String estadoTorneo; // CREADO, EN_CURSO, FINALIZADO
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;

    public Torneo() {}

    // Getters y Setters
    public Long getIdTorneo() { return idTorneo; }
    public void setIdTorneo(Long idTorneo) { this.idTorneo = idTorneo; }
    public Long getIdCategoria() { return idCategoria; }
    public void setIdCategoria(Long idCategoria) { this.idCategoria = idCategoria; }
    public String getNombreTorneo() { return nombreTorneo; }
    public void setNombreTorneo(String nombreTorneo) { this.nombreTorneo = nombreTorneo; }
    public String getEstadoTorneo() { return estadoTorneo; }
    public void setEstadoTorneo(String estadoTorneo) { this.estadoTorneo = estadoTorneo; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public LocalDate getFechaTermino() { return fechaTermino; }
    public void setFechaTermino(LocalDate fechaTermino) { this.fechaTermino = fechaTermino; }
}
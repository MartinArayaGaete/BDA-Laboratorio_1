package com.example.demo.dtos;

import java.time.LocalDate;

public class TorneoDisponibleDTO {
    private Long idTorneo;
    private String nombreTorneo;
    private String estadoTorneo;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;
    private Long idCategoria;
    private String nombreCategoria;

    public TorneoDisponibleDTO() {}

    public TorneoDisponibleDTO(Long idTorneo, String nombreTorneo, String estadoTorneo,
                             LocalDate fechaInicio, LocalDate fechaTermino,
                             Long idCategoria, String nombreCategoria) {
        this.idTorneo = idTorneo;
        this.nombreTorneo = nombreTorneo;
        this.estadoTorneo = estadoTorneo;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
        this.idCategoria = idCategoria;
        this.nombreCategoria = nombreCategoria;
    }

    public Long getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(Long idTorneo) {
        this.idTorneo = idTorneo;
    }

    public String getNombreTorneo() {
        return nombreTorneo;
    }

    public void setNombreTorneo(String nombreTorneo) {
        this.nombreTorneo = nombreTorneo;
    }

    public String getEstadoTorneo() {
        return estadoTorneo;
    }

    public void setEstadoTorneo(String estadoTorneo) {
        this.estadoTorneo = estadoTorneo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(LocalDate fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public void setNombreCategoria(String nombreCategoria) {
        this.nombreCategoria = nombreCategoria;
    }
}

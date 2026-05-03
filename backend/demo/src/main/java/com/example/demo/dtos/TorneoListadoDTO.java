package com.example.demo.dtos;

import java.time.LocalDate;

public class TorneoListadoDTO {
    private Long idTorneo;
    private Long idCategoria;
    private String nombreTorneo;
    private String estadoTorneo;
    private LocalDate fechaInicio;
    private LocalDate fechaTermino;

    public TorneoListadoDTO() {}

    public TorneoListadoDTO(Long idTorneo, Long idCategoria, String nombreTorneo, String estadoTorneo,
                            LocalDate fechaInicio, LocalDate fechaTermino) {
        this.idTorneo = idTorneo;
        this.idCategoria = idCategoria;
        this.nombreTorneo = nombreTorneo;
        this.estadoTorneo = estadoTorneo;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
    }

    public Long getIdTorneo() {
        return idTorneo;
    }

    public void setIdTorneo(Long idTorneo) {
        this.idTorneo = idTorneo;
    }

    public Long getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Long idCategoria) {
        this.idCategoria = idCategoria;
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
}

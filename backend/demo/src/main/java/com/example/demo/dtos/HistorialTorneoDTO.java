package com.example.demo.dtos;

import java.time.LocalDate;
import java.util.List;

public class HistorialTorneoDTO {
    private Long idTorneo;
    private String nombreTorneo;
    private Integer puntajeFinal;
    private Integer posicionFinal;
    private LocalDate fechaInicio;
    private String estadoTorneo;
    private List<HistorialRondaDTO> rondas;

    public HistorialTorneoDTO() {}

    public HistorialTorneoDTO(Long idTorneo, String nombreTorneo, Integer puntajeFinal, 
                             Integer posicionFinal, LocalDate fechaInicio, String estadoTorneo,
                             List<HistorialRondaDTO> rondas) {
        this.idTorneo = idTorneo;
        this.nombreTorneo = nombreTorneo;
        this.puntajeFinal = puntajeFinal;
        this.posicionFinal = posicionFinal;
        this.fechaInicio = fechaInicio;
        this.estadoTorneo = estadoTorneo;
        this.rondas = rondas;
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

    public Integer getPuntajeFinal() {
        return puntajeFinal;
    }

    public void setPuntajeFinal(Integer puntajeFinal) {
        this.puntajeFinal = puntajeFinal;
    }

    public Integer getPosicionFinal() {
        return posicionFinal;
    }

    public void setPosicionFinal(Integer posicionFinal) {
        this.posicionFinal = posicionFinal;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getEstadoTorneo() {
        return estadoTorneo;
    }

    public void setEstadoTorneo(String estadoTorneo) {
        this.estadoTorneo = estadoTorneo;
    }

    public List<HistorialRondaDTO> getRondas() {
        return rondas;
    }

    public void setRondas(List<HistorialRondaDTO> rondas) {
        this.rondas = rondas;
    }
}

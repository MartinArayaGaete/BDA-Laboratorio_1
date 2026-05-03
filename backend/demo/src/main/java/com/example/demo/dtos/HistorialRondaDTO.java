package com.example.demo.dtos;

import java.util.List;

public class HistorialRondaDTO {
    private Integer numeroRonda;
    private Integer puntajeRonda;
    private List<HistorialFlechaDTO> flechas;

    public HistorialRondaDTO() {}

    public HistorialRondaDTO(Integer numeroRonda, Integer puntajeRonda, List<HistorialFlechaDTO> flechas) {
        this.numeroRonda = numeroRonda;
        this.puntajeRonda = puntajeRonda;
        this.flechas = flechas;
    }

    public Integer getNumeroRonda() {
        return numeroRonda;
    }

    public void setNumeroRonda(Integer numeroRonda) {
        this.numeroRonda = numeroRonda;
    }

    public Integer getPuntajeRonda() {
        return puntajeRonda;
    }

    public void setPuntajeRonda(Integer puntajeRonda) {
        this.puntajeRonda = puntajeRonda;
    }

    public List<HistorialFlechaDTO> getFlechas() {
        return flechas;
    }

    public void setFlechas(List<HistorialFlechaDTO> flechas) {
        this.flechas = flechas;
    }
}

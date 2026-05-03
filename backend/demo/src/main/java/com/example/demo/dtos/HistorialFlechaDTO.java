package com.example.demo.dtos;

public class HistorialFlechaDTO {
    private Long idFlecha;
    private Integer puntaje;

    public HistorialFlechaDTO() {}

    public HistorialFlechaDTO(Long idFlecha, Integer puntaje) {
        this.idFlecha = idFlecha;
        this.puntaje = puntaje;
    }

    public Long getIdFlecha() {
        return idFlecha;
    }

    public void setIdFlecha(Long idFlecha) {
        this.idFlecha = idFlecha;
    }

    public Integer getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(Integer puntaje) {
        this.puntaje = puntaje;
    }
}

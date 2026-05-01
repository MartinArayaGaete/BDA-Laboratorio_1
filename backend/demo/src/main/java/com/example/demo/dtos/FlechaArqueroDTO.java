package com.example.demo.dtos;

public class FlechaArqueroDTO {
    private Integer numeroRonda;
    private Long idFlecha;
    private Integer puntaje;

    public FlechaArqueroDTO() {}

    public Integer getNumeroRonda() { return numeroRonda; }
    public void setNumeroRonda(Integer numeroRonda) { this.numeroRonda = numeroRonda; }

    public Long getIdFlecha() { return idFlecha; }
    public void setIdFlecha(Long idFlecha) { this.idFlecha = idFlecha; }

    public Integer getPuntaje() { return puntaje; }
    public void setPuntaje(Integer puntaje) { this.puntaje = puntaje; }
}
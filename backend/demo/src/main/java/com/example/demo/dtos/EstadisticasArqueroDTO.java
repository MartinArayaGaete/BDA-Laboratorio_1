package com.example.demo.dtos;

public class EstadisticasArqueroDTO {
    private Integer torneosTotales;
    private Integer totalFlechas;
    private Integer flechasAcertadas;
    private Integer porcentajeAcierto;
    private Integer totalPuntos;
    private Double promedioPuntos;

    public EstadisticasArqueroDTO() {}

    public EstadisticasArqueroDTO(Integer torneosTotales, Integer totalFlechas, Integer flechasAcertadas,
                                  Integer porcentajeAcierto, Integer totalPuntos, Double promedioPuntos) {
        this.torneosTotales = torneosTotales;
        this.totalFlechas = totalFlechas;
        this.flechasAcertadas = flechasAcertadas;
        this.porcentajeAcierto = porcentajeAcierto;
        this.totalPuntos = totalPuntos;
        this.promedioPuntos = promedioPuntos;
    }

    public Integer getTorneosTotales() {
        return torneosTotales;
    }

    public void setTorneosTotales(Integer torneosTotales) {
        this.torneosTotales = torneosTotales;
    }

    public Integer getTotalFlechas() {
        return totalFlechas;
    }

    public void setTotalFlechas(Integer totalFlechas) {
        this.totalFlechas = totalFlechas;
    }

    public Integer getFlechasAcertadas() {
        return flechasAcertadas;
    }

    public void setFlechasAcertadas(Integer flechasAcertadas) {
        this.flechasAcertadas = flechasAcertadas;
    }

    public Integer getPorcentajeAcierto() {
        return porcentajeAcierto;
    }

    public void setPorcentajeAcierto(Integer porcentajeAcierto) {
        this.porcentajeAcierto = porcentajeAcierto;
    }

    public Integer getTotalPuntos() {
        return totalPuntos;
    }

    public void setTotalPuntos(Integer totalPuntos) {
        this.totalPuntos = totalPuntos;
    }

    public Double getPromedioPuntos() {
        return promedioPuntos;
    }

    public void setPromedioPuntos(Double promedioPuntos) {
        this.promedioPuntos = promedioPuntos;
    }
}
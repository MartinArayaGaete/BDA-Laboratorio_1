package com.example.demo.dtos;

public class ResumenTorneoArqueroDTO {
    private Integer puntajeFinal;
    private Integer posicionFinal;
    private Integer totalFlechas;
    private Double promedioPuntos;
    private Integer rondasJugadas;

    public ResumenTorneoArqueroDTO() {}

    public ResumenTorneoArqueroDTO(Integer puntajeFinal, Integer posicionFinal, Integer totalFlechas,
                                   Double promedioPuntos, Integer rondasJugadas) {
        this.puntajeFinal = puntajeFinal;
        this.posicionFinal = posicionFinal;
        this.totalFlechas = totalFlechas;
        this.promedioPuntos = promedioPuntos;
        this.rondasJugadas = rondasJugadas;
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

    public Integer getTotalFlechas() {
        return totalFlechas;
    }

    public void setTotalFlechas(Integer totalFlechas) {
        this.totalFlechas = totalFlechas;
    }

    public Double getPromedioPuntos() {
        return promedioPuntos;
    }

    public void setPromedioPuntos(Double promedioPuntos) {
        this.promedioPuntos = promedioPuntos;
    }

    public Integer getRondasJugadas() {
        return rondasJugadas;
    }

    public void setRondasJugadas(Integer rondasJugadas) {
        this.rondasJugadas = rondasJugadas;
    }
}

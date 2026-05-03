package com.example.demo.dtos;

public class LeaderboardDTO {
    private Long idUsuario;
    private String nombre;
    private Double promedioPuntosFlecha;

    public LeaderboardDTO(Long idUsuario, String nombre, Double promedioPuntosFlecha) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.promedioPuntosFlecha = promedioPuntosFlecha;
    }

    public Long getIdUsuario() { return idUsuario; }
    public String getNombre() { return nombre; }
    public Double getPromedioPuntosFlecha() { return promedioPuntosFlecha; }
}
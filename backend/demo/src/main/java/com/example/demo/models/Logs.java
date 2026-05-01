package com.example.demo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Logs {
    private Long idLogs;
    private Long idUsuario;      // Quien edita
    private Long idAfectado;      // usuario afectado
    private LocalDateTime fechaEditado;
    private BigDecimal torneoAfectado;
    private BigDecimal rondaAfectada;
    private BigDecimal puntajeAnterior;
    private BigDecimal puntajeNuevo;
}
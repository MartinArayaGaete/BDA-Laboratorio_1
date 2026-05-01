package com.example.demo.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Participacion {
    private Long idParticipacion;
    private Long idUsuario; // FK
    private Long idRonda;   // FK
    private BigDecimal puntajeFinal;
}
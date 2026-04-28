package com.example.demo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlechaEntity {

    private Long id_flecha;

    private RondaEntity ronda;

    private BigDecimal puntaje;

}



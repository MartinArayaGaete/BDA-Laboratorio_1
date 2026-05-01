package com.example.demo.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flecha {
    private Long idFlecha;
    private Long idParticipacion; // FK
    private BigDecimal puntaje;
}



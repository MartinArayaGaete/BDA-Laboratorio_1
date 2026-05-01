package com.example.demo.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Flecha {
    private Long idFlecha;
    private Long idParticipacion; // FK
    private Long idRonda;         // FK
    private Integer puntaje;
}
package com.example.demo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;   
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TorneoEntity {

    private Long id_torneo;

    private CategoriaEntity categoria;

    private String nombre_torneo;

    private String estado_torneo;

    private LocalDate fecha_inicio;

    private LocalDate fecha_termino;

}



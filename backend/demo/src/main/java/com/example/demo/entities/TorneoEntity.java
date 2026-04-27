package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;   
import lombok.AllArgsConstructor;
import java.time.LocalDate;

@Data
@Entity
@Table(name="torneo")
@NoArgsConstructor
@AllArgsConstructor
public class TorneoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private Long id_torneo;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaEntity categoria;

    @Column(length = 80)
    private String nombre_torneo;

    @Column(length = 80)
    private String estado_torneo;

    private LocalDate fecha_inicio;

    private LocalDate fecha_termino;

}



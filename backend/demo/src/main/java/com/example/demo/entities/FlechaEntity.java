package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;

@Data
@Entity
@Table(name="flecha")
@NoArgsConstructor
@AllArgsConstructor
public class FlechaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private Long id_flecha;

    @ManyToOne
    @JoinColumn(name = "id_ronda", nullable = false)
    private RondaEntity ronda;

    private BigDecimal puntaje;

}



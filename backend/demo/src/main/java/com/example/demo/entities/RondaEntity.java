package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;   
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name="ronda")
@NoArgsConstructor
@AllArgsConstructor

public class RondaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private Long id_ronda;

    @ManyToOne
    @JoinColumn(name = "id_participacion", nullable = false)
    private ParticipacionEntity participacion;

    private String num_ronda;

}



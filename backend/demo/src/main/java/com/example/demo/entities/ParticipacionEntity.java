package com.example.demo.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Entity
@Table(name="participacion")
@NoArgsConstructor
@AllArgsConstructor
public class ParticipacionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique=true, nullable=false)
    private Long id_participacion;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UserEntity usuario;

    @ManyToOne
    @JoinColumn(name = "id_torneo", nullable = false)
    private TorneoEntity torneo;

}
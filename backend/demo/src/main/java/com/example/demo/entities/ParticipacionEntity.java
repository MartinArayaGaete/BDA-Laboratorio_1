package com.example.demo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipacionEntity {

    private Long id_participacion;

    private UserEntity usuario;

    private TorneoEntity torneo;

}
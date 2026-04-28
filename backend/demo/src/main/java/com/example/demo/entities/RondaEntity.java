package com.example.demo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;   
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RondaEntity {

    private Long id_ronda;

    private ParticipacionEntity participacion;

    private String num_ronda;

}



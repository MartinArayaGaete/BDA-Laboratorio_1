package com.example.demo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LogsEntity {

    private Long id_logs;

    private UserEntity usuario;

    private String tipo_movimiento;

    private String descripcion;

    private String fecha;

}
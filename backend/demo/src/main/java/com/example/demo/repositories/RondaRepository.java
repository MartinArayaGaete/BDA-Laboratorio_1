package com.example.demo.repositories;

import com.example.demo.models.Ronda;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class RondaRepository {

    private final JdbcTemplate jdbcTemplate;

    public RondaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Obtiene todas las rondas asociadas a un torneo
    public List<Ronda> buscarPorTorneo(Long idTorneo) {
        String sql = "SELECT * FROM ronda WHERE id_torneo = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            Ronda r = new Ronda();
            r.setIdRonda(rs.getLong("id_ronda"));
            r.setIdTorneo(rs.getLong("id_torneo"));
            r.setNumeroRonda(rs.getInt("numero_ronda"));
            return r;
        }, idTorneo);
    }

    // Crea una ronda para un torneo especifico
    public void crearRonda(Long idTorneo, Integer numeroRonda) {
        String sql = "INSERT INTO ronda (id_torneo, numero_ronda) VALUES (?, ?)";
        jdbcTemplate.update(sql, idTorneo, numeroRonda);
    }

    // Verifica si una ronda ya existe en un torneo
    public boolean existeRonda(Long idTorneo, Integer numeroRonda) {
        String sql = "SELECT COUNT(*) FROM ronda WHERE id_torneo = ? AND numero_ronda = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idTorneo, numeroRonda);
        return count != null && count > 0;
    }

    // Obtiene el id de la ronda para un torneo y numero dados
    public Optional<Long> obtenerIdRonda(Long idTorneo, Integer numeroRonda) {
        String sql = "SELECT id_ronda FROM ronda WHERE id_torneo = ? AND numero_ronda = ?";
        try {
            Long id = jdbcTemplate.queryForObject(sql, Long.class, idTorneo, numeroRonda);
            return Optional.ofNullable(id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
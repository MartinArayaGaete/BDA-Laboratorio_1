package com.example.demo.repositories;

import com.example.demo.models.Torneo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class TorneoRepository {

    private final JdbcTemplate jdbcTemplate;

    public TorneoRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Torneo mapRowToTorneo(ResultSet rs) throws SQLException {
        Torneo t = new Torneo();
        t.setIdTorneo(rs.getLong("id_torneo"));
        t.setIdCategoria(rs.getLong("id_categoria"));
        t.setNombreTorneo(rs.getString("nombre_torneo"));
        t.setEstadoTorneo(rs.getString("estado_torneo"));
        t.setFechaInicio(rs.getObject("fecha_inicio", LocalDate.class));
        t.setFechaTermino(rs.getObject("fecha_termino", LocalDate.class));
        return t;
    }

    public void crearTorneo(Long idCategoria, String nombre, String estado, LocalDate inicio, LocalDate termino) {
        jdbcTemplate.update("INSERT INTO torneo (id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino) VALUES (?, ?, ?, ?, ?)", idCategoria, nombre, estado, inicio, termino);
    }

    public List<Torneo> obtenerTodos() {
        return jdbcTemplate.query("SELECT * FROM torneo", (rs, rowNum) -> mapRowToTorneo(rs));
    }

    public Optional<Torneo> buscarPorId(Long idTorneo) {
        try { return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM torneo WHERE id_torneo = ?", (rs, rowNum) -> mapRowToTorneo(rs), idTorneo)); } catch (EmptyResultDataAccessException e) { return Optional.empty(); }
    }
}
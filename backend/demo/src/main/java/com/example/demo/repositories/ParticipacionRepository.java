package com.example.demo.repositories;

import com.example.demo.dtos.InscritoDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public class ParticipacionRepository {

    private final JdbcTemplate jdbcTemplate;

    public ParticipacionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void inscribirUsuario(Long idUsuario, Long idTorneo) {
        String sql = "INSERT INTO participacion (id_usuario, id_torneo) VALUES (?, ?)";
        jdbcTemplate.update(sql, idUsuario, idTorneo);
    }

    public boolean existeParticipacion(Long idUsuario, Long idTorneo) {
        String sql = "SELECT COUNT(*) FROM participacion WHERE id_usuario = ? AND id_torneo = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idUsuario, idTorneo);
        return count != null && count > 0;
    }

    public List<InscritoDTO> obtenerInscritosPorTorneo(Long idTorneo) {
        String sql = """
                SELECT p.id_participacion, u.id_usuario, u.rut, u.nombre
                FROM participacion p
                INNER JOIN usuario u ON p.id_usuario = u.id_usuario
                WHERE p.id_torneo = ?
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            InscritoDTO dto = new InscritoDTO();
            dto.setIdParticipacion(rs.getLong("id_participacion"));
            dto.setIdUsuario(rs.getLong("id_usuario"));
            dto.setRut(rs.getString("rut"));
            dto.setNombre(rs.getString("nombre"));
            return dto;
        }, idTorneo);
    }

    public Optional<Long> obtenerIdParticipacion(Long idUsuario, Long idTorneo) {
        String sql = "SELECT id_participacion FROM participacion WHERE id_usuario = ? AND id_torneo = ?";
        try {
            Long id = jdbcTemplate.queryForObject(sql, Long.class, idUsuario, idTorneo);
            return Optional.ofNullable(id);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
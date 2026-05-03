package com.example.demo.repositories;

import com.example.demo.dtos.InscritoDTO;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.jdbc.core.ConnectionCallback;
import java.sql.PreparedStatement;


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

    public void actualizarPuntajeFinalConTrigger(Long idAdmin, Long idRondaAfectada, Integer puntajeFinal, Long idUsuario, Long idTorneo) {
        jdbcTemplate.execute((ConnectionCallback<Void>) conn -> {
            
            // Usamos try-with-resources para asegurar que los PreparedStatements se cierren
            try (
                PreparedStatement ps1 = conn.prepareStatement("SET LOCAL my.user_id = ?");
                PreparedStatement ps2 = conn.prepareStatement("SET LOCAL my.ronda_id = ?");
                PreparedStatement ps3 = conn.prepareStatement(
                    "UPDATE participacion SET puntaje_final = ? WHERE id_usuario = ? AND id_torneo = ?"
                )
            ) {
                // Seteamos el ID de quien hace el cambio (Para el Trigger)
                ps1.setLong(1, idAdmin);
                ps1.execute();

                // Seteamos el ID de la ronda afectada (Para el Trigger)
                ps2.setLong(1, idRondaAfectada);
                ps2.execute();

                // Ejecutamos el Update real de la tabla
                ps3.setInt(1, puntajeFinal);
                ps3.setLong(2, idUsuario);
                ps3.setLong(3, idTorneo);
                ps3.executeUpdate();
            }
            return null; // El callback requiere retornar algo, mandamos null
        });
    }

    public List<Map<String, Object>> obtenerTodas() {
        String sql = """
            SELECT p.id_participacion, p.id_usuario, u.nombre, p.id_torneo, t.nombre_torneo, p.puntaje_final
            FROM participacion p
            JOIN usuario u ON p.id_usuario = u.id_usuario
            JOIN torneo t ON p.id_torneo = t.id_torneo
            ORDER BY p.id_participacion ASC
            """;
        return jdbcTemplate.queryForList(sql);
    }
}
package com.example.demo.repositories;

import com.example.demo.dtos.FlechaArqueroDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class FlechaRepository {

    private final JdbcTemplate jdbcTemplate;

    public FlechaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<FlechaArqueroDTO> obtenerFlechasDeArqueroEnTorneo(Long idUsuario, Long idTorneo) {
        String sql = """
                SELECT r.numero_ronda, f.id_flecha, f.puntaje
                FROM flecha f
                INNER JOIN participacion p ON f.id_participacion = p.id_participacion
                INNER JOIN ronda r ON f.id_ronda = r.id_ronda
                WHERE p.id_usuario = ? AND p.id_torneo = ?
                ORDER BY r.numero_ronda ASC, f.id_flecha ASC
                """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            FlechaArqueroDTO dto = new FlechaArqueroDTO();
            dto.setNumeroRonda(rs.getInt("numero_ronda"));
            dto.setIdFlecha(rs.getLong("id_flecha"));
            dto.setPuntaje(rs.getInt("puntaje"));
            return dto;
        }, idUsuario, idTorneo);
    }

    public void guardarFlecha(Long idParticipacion, Long idRonda, Integer puntaje) {
        String sql = "INSERT INTO flecha (id_participacion, id_ronda, puntaje) VALUES (?, ?, ?)";
        jdbcTemplate.update(sql, idParticipacion, idRonda, puntaje);
    }
}
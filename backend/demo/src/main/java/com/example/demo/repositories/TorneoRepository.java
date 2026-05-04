package com.example.demo.repositories;

import com.example.demo.dtos.InscritoDTO;
import com.example.demo.models.Torneo;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Map;

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

    public Long contarTorneos() {
        String sql = "SELECT COUNT(*) FROM torneo";
        Long count = jdbcTemplate.queryForObject(sql, Long.class);
        return count != null ? count : 0;
    }

    public Long contarTorneosPorEstado(String estadoTorneo) {
        String sql = "SELECT COUNT(*) FROM torneo WHERE estado_torneo = ?";
        Long count = jdbcTemplate.queryForObject(sql, Long.class, estadoTorneo);
        return count != null ? count : 0;
    }

    public List<Map<String, Object>> obtenerTorneosPaginados(int page, int size) {
        String sql = """
            SELECT id_torneo, id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino
            FROM torneo
            ORDER BY fecha_inicio DESC, id_torneo DESC
            LIMIT ? OFFSET ?
            """;
        int offset = page * size;
        return jdbcTemplate.queryForList(sql, size, offset);
    }

    public List<Map<String, Object>> obtenerTorneosPorEstadoPaginados(String estadoTorneo, int page, int size) {
        String sql = """
            SELECT id_torneo, id_categoria, nombre_torneo, estado_torneo, fecha_inicio, fecha_termino
            FROM torneo
            WHERE estado_torneo = ?
            ORDER BY fecha_inicio DESC, id_torneo DESC
            LIMIT ? OFFSET ?
            """;
        int offset = page * size;
        return jdbcTemplate.queryForList(sql, estadoTorneo, size, offset);
    }

    public Optional<Torneo> buscarPorId(Long idTorneo) {
        try { return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM torneo WHERE id_torneo = ?", (rs, rowNum) -> mapRowToTorneo(rs), idTorneo)); } catch (EmptyResultDataAccessException e) { return Optional.empty(); }
    }

    //  cambiar el estado del torneo
    public void finalizarTorneo(Long idTorneo) {
        jdbcTemplate.update("UPDATE torneo SET estado_torneo = 'COMPLETED' WHERE id_torneo = ?", idTorneo);
    }

    //  ejecuta el Procedimiento Almacenado 2
    public void actualizarPosicionesSP(Long idTorneo) {
        jdbcTemplate.update("CALL actualizar_posiciones(?)", idTorneo);
    }


    public List<InscritoDTO> obtenerPodio(Long idTorneo) {
        String sql = """
            SELECT p.id_participacion, u.id_usuario, u.rut, u.nombre 
            FROM participacion p 
            JOIN usuario u ON p.id_usuario = u.id_usuario 
            WHERE p.id_torneo = ? AND p.posicion_final <= 3 
            ORDER BY p.posicion_final ASC
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


    // Pasa el torneo a estado 'IN_COURSE'
    public int iniciarTorneo(Long idTorneo) {
        String sql = "UPDATE torneo SET estado_torneo = 'IN_COURSE' WHERE id_torneo = ? AND estado_torneo = 'NOT_STARTED'";
        // Retorna el número de filas afectadas. Si es 0, significa que el torneo no existía o ya había empezado.
        return jdbcTemplate.update(sql, idTorneo);
    }
    /**
     * Cuenta la cantidad total de torneos disponibles para un usuario.
     * Torneos disponibles = estado NOT_STARTED y usuario NO está inscrito.
     */
    public Long contarTorneosDisponibles(Long idUsuario) {
        String sql = """
            SELECT COUNT(DISTINCT t.id_torneo) as total
            FROM torneo t
            LEFT JOIN participacion p ON t.id_torneo = p.id_torneo AND p.id_usuario = ?
            WHERE t.estado_torneo = 'NOT_STARTED' AND p.id_participacion IS NULL
            """;
        Long count = jdbcTemplate.queryForObject(sql, Long.class, idUsuario);
        return count != null ? count : 0;
    }

    /**
     * Obtiene torneos disponibles de forma paginada para un usuario.
     * Retorna Map con keys: id_torneo, nombre_torneo, estado_torneo, 
     * fecha_inicio, fecha_termino, id_categoria, nombre_categoria
     */
    public List<Map<String, Object>> obtenerTorneosDisponiblesPaginados(Long idUsuario, int page, int size) {
        String sql = """
            SELECT DISTINCT t.id_torneo, t.nombre_torneo, t.estado_torneo, 
                   t.fecha_inicio, t.fecha_termino, t.id_categoria, c.nombre_categoria
            FROM torneo t
            LEFT JOIN participacion p ON t.id_torneo = p.id_torneo AND p.id_usuario = ?
            LEFT JOIN categoria c ON t.id_categoria = c.id_categoria
            WHERE t.estado_torneo = 'NOT_STARTED' AND p.id_participacion IS NULL
            ORDER BY t.fecha_inicio ASC
            LIMIT ? OFFSET ?
            """;
        int offset = page * size;
        return jdbcTemplate.queryForList(sql, idUsuario, size, offset);
    }
}
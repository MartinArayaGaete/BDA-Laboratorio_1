package com.example.demo.repositories;

import com.example.demo.models.Usuario;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/*
TODO: agregar paginación

 */

@Repository
public class UsuarioRepository {

    private final JdbcTemplate jdbcTemplate;

    public UsuarioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // toma un objeto, le mete un usuario y devuelve un objeto con los datos de usuario
    private Usuario mapRowToUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setIdUsuario(rs.getLong("id_usuario"));
        u.setRut(rs.getString("rut"));
        u.setNombre(rs.getString("nombre"));
        u.setCorreo(rs.getString("correo"));
        u.setContrasena(rs.getString("contrasena"));
        u.setRol(rs.getString("rol"));
        return u;
    }

    public List<Usuario> obtenerTodos() {
        String sql = "SELECT id_usuario, rut, nombre, correo, contrasena, rol FROM usuario";
        return jdbcTemplate.query(sql, (rs, rowNum) -> mapRowToUsuario(rs));
    }

    public Optional<Usuario> buscarPorRut(String rut) {
        String sql = "SELECT id_usuario, rut, nombre, correo, contrasena, rol FROM usuario WHERE rut = ?";
        try {
            Usuario usuario = jdbcTemplate.queryForObject(sql, (rs, rowNum) -> mapRowToUsuario(rs), rut);
            return Optional.ofNullable(usuario);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public boolean existePorRut(String rut) {
        String sql = "SELECT COUNT(*) FROM usuario WHERE rut = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, rut);
        return count != null && count > 0;
    }

    public void crearUsuario(String rut, String nombre, String correo, String contrasena, String rol) {
        String sql = "INSERT INTO usuario (rut, nombre, correo, contrasena, rol) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, rut, nombre, correo, contrasena, rol);
    }

    public void actualizarUsuario(String rut, String nombre, String correo, String contrasena, String rol) {
        String sql = "UPDATE usuario SET nombre = ?, correo = ?, contrasena = ?, rol = ? WHERE rut = ?";
        jdbcTemplate.update(sql, nombre, correo, contrasena, rol, rut);
    }

    public void eliminarPorRut(String rut) {
        String sql = "DELETE FROM usuario WHERE rut = ?";
        jdbcTemplate.update(sql, rut);
    }
}
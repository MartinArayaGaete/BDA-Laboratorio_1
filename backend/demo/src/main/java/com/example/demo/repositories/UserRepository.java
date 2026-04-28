package com.example.demo.repositories;

import com.example.demo.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //Definir un RowMapper para convertir los registros de la DB a tu POJO
    private RowMapper<UserEntity> userRowMapper = new RowMapper<UserEntity>() {
        @Override
        public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserEntity user = new UserEntity();
            user.setId_usuario(rs.getLong("id_usuario"));
            user.setRut(rs.getString("rut"));
            user.setCorreo(rs.getString("correo"));
            user.setNombre(rs.getString("nombre"));
            user.setContraseña(rs.getString("contraseña"));
            user.setRol(rs.getString("rol"));
            user.setKeycloak_id(rs.getString("keycloak_id"));
            return user;
        }
    };

    //Metodo para buscar todos los usuarios
    public List<UserEntity> findAll() {
        String sql = "SELECT * FROM usuario";
        return jdbcTemplate.query(sql, userRowMapper);
    }

    //Metodo para buscar un usuario por id
    public Optional<UserEntity> findById(Long id) {
        String sql = "SELECT * FROM usuario WHERE id_usuario = ?";
        try {
            UserEntity user = jdbcTemplate.queryForObject(sql, userRowMapper, id);
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty(); // Retorna vacío si no lo encuentra en la DB
        }
    }

    //Metodo para guardar un usuario
    public int save(UserEntity user) {
        String sql = "INSERT INTO usuario (rut, correo, nombre, contraseña, rol, keycloak_id) VALUES (?, ?, ?, ?, ?, ?)";
        return jdbcTemplate.update(sql, 
                user.getRut(), 
                user.getCorreo(), 
                user.getNombre(), 
                user.getContraseña(), 
                user.getRol(), 
                user.getKeycloak_id());
    }

    //Metodo para actualizar los datos de un usuario
    public int update(UserEntity user) {
        String sql = "UPDATE usuario SET rut=?, correo=?, nombre=?, contraseña=?, rol=?, keycloak_id=? WHERE id_usuario=?";
        return jdbcTemplate.update(sql, 
                user.getRut(), 
                user.getCorreo(), 
                user.getNombre(), 
                user.getContraseña(), 
                user.getRol(), 
                user.getKeycloak_id(), 
                user.getId_usuario());
    }

    //Metodo para borrar un usuario
    public int deleteById(Long id) {
        String sql = "DELETE FROM usuario WHERE id_usuario = ?";
        return jdbcTemplate.update(sql, id);
    }
}
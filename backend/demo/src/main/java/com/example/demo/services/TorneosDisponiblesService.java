package com.example.demo.services;

import com.example.demo.dtos.TorneoDisponibleDTO;
import com.example.demo.dtos.TorneosDisponiblesResponse;
import com.example.demo.repositories.TorneoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TorneosDisponiblesService {

    private final TorneoRepository torneoRepository;

    public TorneosDisponiblesService(TorneoRepository torneoRepository) {
        this.torneoRepository = torneoRepository;
    }

    /**
     * Obtiene los torneos disponibles para un usuario de forma paginada.
     * Los torneos disponibles son aquellos en estado NOT_STARTED donde el usuario no está inscrito.
     */
    public TorneosDisponiblesResponse obtenerTorneosDisponibles(Long idUsuario, int page, int size) {
        // Validar parámetros de paginación
        if (page < 0 || size <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Los parámetros de paginación son inválidos");
        }

        // Obtener total de torneos disponibles para este usuario
        Long totalElements = torneoRepository.contarTorneosDisponibles(idUsuario);

        // Si no hay torneos disponibles
        if (totalElements == 0) {
            return new TorneosDisponiblesResponse(
                    new ArrayList<>(),
                    page,
                    size,
                    0L,
                    0
            );
        }

        // Calcular total de páginas
        Integer totalPages = (int) Math.ceil((double) totalElements / size);

        // Validar que la página solicitada existe
        if (page >= totalPages) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, 
                    "La página solicitada no existe. Máximo: " + (totalPages - 1));
        }

        // Obtener torneos disponibles paginados
        List<Map<String, Object>> torneosData = torneoRepository.obtenerTorneosDisponiblesPaginados(idUsuario, page, size);

        // Convertir Maps a DTOs
        List<TorneoDisponibleDTO> torneos = new ArrayList<>();
        for (Map<String, Object> torneo : torneosData) {
            Long idTorneo = ((Number) torneo.get("id_torneo")).longValue();
            String nombreTorneo = (String) torneo.get("nombre_torneo");
            String estadoTorneo = (String) torneo.get("estado_torneo");
            LocalDate fechaInicio = (LocalDate) torneo.get("fecha_inicio");
            LocalDate fechaTermino = (LocalDate) torneo.get("fecha_termino");
            Long idCategoria = ((Number) torneo.get("id_categoria")).longValue();
            String nombreCategoria = (String) torneo.get("nombre_categoria");

            TorneoDisponibleDTO dto = new TorneoDisponibleDTO(
                    idTorneo,
                    nombreTorneo,
                    estadoTorneo,
                    fechaInicio,
                    fechaTermino,
                    idCategoria,
                    nombreCategoria
            );
            torneos.add(dto);
        }

        return new TorneosDisponiblesResponse(
                torneos,
                page,
                size,
                totalElements,
                totalPages
        );
    }
}

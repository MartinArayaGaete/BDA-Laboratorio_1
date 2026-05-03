package com.example.demo.services;

import com.example.demo.dtos.InscritoDTO;
import com.example.demo.dtos.TorneoCreacionDTO;
import com.example.demo.models.Torneo;
import com.example.demo.repositories.RondaRepository;
import com.example.demo.repositories.TorneoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final RondaRepository rondaRepository;

    public TorneoService(TorneoRepository torneoRepository, RondaRepository rondaRepository) {
        this.torneoRepository = torneoRepository;
        this.rondaRepository = rondaRepository;
    }

    // Vuelve a ser 100% manual, sin auto-crear rondas.
    @Transactional
    public void crearTorneo(TorneoCreacionDTO dto) {
        torneoRepository.crearTorneo(
                dto.getIdCategoria(),
                dto.getNombreTorneo(),
                "NOT_STARTED",
                dto.getFechaInicio(),
                dto.getFechaTermino()
        );
    }

    public List<Torneo> obtenerTodos() {
        return torneoRepository.obtenerTodos();
    }

    public void agregarRondaManual(Long idTorneo, Integer numeroRonda) {
        if (rondaRepository.existeRonda(idTorneo, numeroRonda)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esa ronda ya existe en este torneo");
        }
        rondaRepository.crearRonda(idTorneo, numeroRonda);
    }

    @Transactional
    public void finalizarTorneo(Long idTorneo) {
        // Validar que el torneo exista y revisar su estado actual
        Torneo torneo = torneoRepository.buscarPorId(idTorneo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneo no encontrado"));

        // Solo torneos EN CURSO pueden finalizarse
        if (!"IN_COURSE".equals(torneo.getEstadoTorneo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Solo se pueden finalizar torneos que estén en curso (IN_COURSE). Estado actual: " + torneo.getEstadoTorneo());
        }

        //  Cambiar el estado a COMPLETED
        torneoRepository.finalizarTorneo(idTorneo);

        //  Ejecutar el Procedimiento Almacenado para calcular el ranking
        try {
            torneoRepository.actualizarPosicionesSP(idTorneo);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al calcular posiciones: " + e.getMessage());
        }
    }


    public List<InscritoDTO> obtenerPodio(Long idTorneo) {
        if (torneoRepository.buscarPorId(idTorneo).isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneo no encontrado");
        }
        return torneoRepository.obtenerPodio(idTorneo);
    }

    @Transactional
    public void iniciarTorneo(Long idTorneo) {
        int filasAfectadas = torneoRepository.iniciarTorneo(idTorneo);

        if (filasAfectadas == 0) {
            // Si no se actualizó nada, lanzamos un error claro para el frontend
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "No se pudo iniciar el torneo. Verifica que el torneo exista y esté en estado no haya iniciado."
            );
        }
    }
}
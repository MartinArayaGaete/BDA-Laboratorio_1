package com.example.demo.services;

import com.example.demo.dtos.InscritoDTO;
import com.example.demo.dtos.TorneoCreacionDTO;
import com.example.demo.models.Torneo;
import com.example.demo.repositories.ParticipacionRepository;
import com.example.demo.repositories.TorneoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final ParticipacionRepository participacionRepository;

    public TorneoService(TorneoRepository torneoRepository, ParticipacionRepository participacionRepository) {
        this.torneoRepository = torneoRepository;
        this.participacionRepository = participacionRepository;
    }

    public void crearTorneo(TorneoCreacionDTO dto) {
        // Por defecto, un torneo nuevo nace en estado "CREADO"
        String estadoInicial = "CREADO";
        torneoRepository.crearTorneo(
                dto.getIdCategoria(),
                dto.getNombreTorneo(),
                estadoInicial,
                dto.getFechaInicio(),
                dto.getFechaTermino()
        );
    }

    public List<Torneo> obtenerTodos() {
        return torneoRepository.obtenerTodos();
    }

    public void inscribirArquero(Long idUsuario, Long idTorneo) {
        Optional<Torneo> torneoOpt = torneoRepository.buscarPorId(idTorneo);

        if (torneoOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneo no encontrado");
        }

        Torneo torneo = torneoOpt.get();

        // Solo se puede inscribir si el torneo no ha empezado
        if (!"CREADO".equals(torneo.getEstadoTorneo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las inscripciones están cerradas. El torneo ya inició o finalizó.");
        }

        // No doble inscripción
        if (participacionRepository.existeParticipacion(idUsuario, idTorneo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El arquero ya está inscrito en este torneo");
        }

        participacionRepository.inscribirUsuario(idUsuario, idTorneo);
    }

    public List<InscritoDTO> obtenerInscritos(Long idTorneo) {
        return participacionRepository.obtenerInscritosPorTorneo(idTorneo);
    }

}
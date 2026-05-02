package com.example.demo.services;

import com.example.demo.dtos.InscritoDTO;
import com.example.demo.repositories.ParticipacionRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ParticipacionService {

    private final ParticipacionRepository participacionRepository;

    public ParticipacionService(ParticipacionRepository participacionRepository) {
        this.participacionRepository = participacionRepository;
    }

    public void inscribirUsuario(Long idUsuario, Long idTorneo) {
        // Validación: Evitar que el mismo usuario se inscriba dos veces en el mismo torneo
        if (participacionRepository.existeParticipacion(idUsuario, idTorneo)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario ya se encuentra inscrito en este torneo.");
        }
        
        participacionRepository.inscribirUsuario(idUsuario, idTorneo);
    }

    public List<InscritoDTO> obtenerInscritosPorTorneo(Long idTorneo) {
        List<InscritoDTO> inscritos = participacionRepository.obtenerInscritosPorTorneo(idTorneo);
        
        // Retornamos la lista (si está vacía, el controlador decidirá si envía un NO_CONTENT o un OK con lista vacía)
        return inscritos;
    }

    public Long obtenerIdParticipacion(Long idUsuario, Long idTorneo) {
        return participacionRepository.obtenerIdParticipacion(idUsuario, idTorneo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró una participación para este usuario en este torneo."));
    }

    @Transactional
    public void editarPuntajeManual(Long idAdmin, Long idRondaAfectada, Long idUsuario, Long idTorneo, Integer nuevoPuntaje) {
        // Llamas al repositorio que preparará las variables y lanzará el UPDATE (que a su vez dispara tu trigger)
        participacionRepository.actualizarPuntajeFinalConTrigger(idAdmin, idRondaAfectada, nuevoPuntaje, idUsuario, idTorneo);
    }
}
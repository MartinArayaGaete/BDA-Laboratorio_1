package com.example.demo.services;

import com.example.demo.dtos.InscritoDTO;
import com.example.demo.dtos.TorneoCreacionDTO;
import com.example.demo.models.Torneo;
import com.example.demo.repositories.ParticipacionRepository;
import com.example.demo.repositories.RondaRepository;
import com.example.demo.repositories.TorneoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.Optional;

@Service
public class TorneoService {

    private final TorneoRepository torneoRepository;
    private final ParticipacionRepository participacionRepository;
    private final RondaRepository rondaRepository;

    public TorneoService(TorneoRepository torneoRepository, ParticipacionRepository participacionRepository, RondaRepository rondaRepository) {
        this.torneoRepository = torneoRepository;
        this.participacionRepository = participacionRepository;
        this.rondaRepository = rondaRepository;
    }

    @Transactional
    public void crearTorneo(TorneoCreacionDTO dto) {
        String estadoInicial = "CREADO";

        torneoRepository.crearTorneo(
                dto.getIdCategoria(),
                dto.getNombreTorneo(),
                estadoInicial,
                dto.getFechaInicio(),
                dto.getFechaTermino()
        );

        // Busca el torneo recién insertado y le asigna sus rondas globales
        List<Torneo> torneos = torneoRepository.obtenerTodos();
        if (!torneos.isEmpty()) {
            Torneo torneoCreado = torneos.get(torneos.size() - 1);
            rondaRepository.crearRonda(torneoCreado.getIdTorneo(), 1); // Ronda 1
            rondaRepository.crearRonda(torneoCreado.getIdTorneo(), 2); // Ronda 2
        }
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

        if (!"CREADO".equals(torneo.getEstadoTorneo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Las inscripciones están cerradas. El torneo ya inició o finalizó.");
        }

        // Validación directa contra el torneo, mucho más limpia
        if (participacionRepository.existeParticipacion(idUsuario, idTorneo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El arquero ya está inscrito en este torneo");
        }

        participacionRepository.inscribirUsuario(idUsuario, idTorneo);
    }

    public List<InscritoDTO> obtenerInscritos(Long idTorneo) {
        return participacionRepository.obtenerInscritosPorTorneo(idTorneo);
    }

    public void agregarRondaManual(Long idTorneo, Integer numeroRonda) {
        if (rondaRepository.existeRonda(idTorneo, numeroRonda)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Esa ronda ya existe en este torneo");
        }
        rondaRepository.crearRonda(idTorneo, numeroRonda);
    }
}
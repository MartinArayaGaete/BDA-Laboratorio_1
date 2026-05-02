package com.example.demo.services;

import com.example.demo.dtos.FlechaArqueroDTO;
import com.example.demo.repositories.FlechaRepository;
import com.example.demo.repositories.ParticipacionRepository;
import com.example.demo.repositories.RondaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FlechaService {

    private final ParticipacionRepository participacionRepository;
    private final RondaRepository rondaRepository;
    private final FlechaRepository flechaRepository;

    // Constructor que inyecta las dependencias necesarias para la gestión de flechas, participaciones y rondas.
    public FlechaService(FlechaRepository flechaRepository, ParticipacionRepository participacionRepository, RondaRepository rondaRepository) {
        this.flechaRepository = flechaRepository;
        this.participacionRepository = participacionRepository;
        this.rondaRepository = rondaRepository;
    }

    // Recupera la lista de flechas de un arquero en un torneo específico
    public List<FlechaArqueroDTO> obtenerFlechasArquero(Long idUsuario, Long idTorneo) {
        return flechaRepository.obtenerFlechasDeArqueroEnTorneo(idUsuario, idTorneo);
    }

    // Registra una ronda completa validando puntajes y llamando al Procedimiento Almacenado 1 de la base de datos.
    public void registrarRondaCompleta(Long idTorneo, Long idUsuario, Integer numeroRonda, List<Integer> flechas) {

        // Validación del TRIGGER 1
        for (Integer puntaje : flechas) {
            if (puntaje < 0 || puntaje > 10) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trampa detectada: Los puntajes deben estar entre 0 y 10");
            }
        }

        Long idParticipacion = participacionRepository.obtenerIdParticipacion(idUsuario, idTorneo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El arquero no está inscrito en este torneo"));

        Long idRonda = rondaRepository.obtenerIdRonda(idTorneo, numeroRonda)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La ronda " + numeroRonda + " no existe en este torneo"));

        // Llama al Stored Procedure
        flechaRepository.guardarRondaCompletaSP(idParticipacion, idRonda, flechas);
    }
}
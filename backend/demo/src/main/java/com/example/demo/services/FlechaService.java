package com.example.demo.services;

import com.example.demo.dtos.FlechaArqueroDTO;
import com.example.demo.dtos.PuntajeRondaDTO;
import com.example.demo.repositories.FlechaRepository;
import com.example.demo.repositories.ParticipacionRepository;
import com.example.demo.repositories.RondaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class FlechaService {

    private final ParticipacionRepository participacionRepository;
    private final RondaRepository rondaRepository;
    private final FlechaRepository flechaRepository;

    public FlechaService(FlechaRepository flechaRepository, ParticipacionRepository participacionRepository, RondaRepository rondaRepository) {
        this.flechaRepository = flechaRepository;
        this.participacionRepository = participacionRepository;
        this.rondaRepository = rondaRepository;
    }

    public List<FlechaArqueroDTO> obtenerFlechasArquero(Long idUsuario, Long idTorneo) {
        return flechaRepository.obtenerFlechasDeArqueroEnTorneo(idUsuario, idTorneo);
    }

    public void registrarRondaCompleta(Long idTorneo, Long idUsuario, Integer numeroRonda, List<Integer> flechas) {
        for (Integer puntaje : flechas) {
            if (puntaje < 0 || puntaje > 10) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Trampa detectada: Los puntajes deben estar entre 0 y 10");
            }
        }

        Long idParticipacion = participacionRepository.obtenerIdParticipacion(idUsuario, idTorneo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "El arquero no está inscrito en este torneo"));

        Long idRonda = rondaRepository.obtenerIdRonda(idTorneo, numeroRonda)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "La ronda " + numeroRonda + " no existe en este torneo"));

        flechaRepository.guardarRondaCompletaSP(idParticipacion, idRonda, flechas);
    }
    @Transactional
    public void registrarRondaCompletaDTO(PuntajeRondaDTO request) { // <--- 2. Cambiar tipo
        // Validaciones
        for (Integer puntaje : request.getFlechas()) {
            if (puntaje < 0 || puntaje > 10) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Puntaje inválido");
            }
        }

        // Llamada al repo (usando los getters del DTO)
        flechaRepository.guardarRondaCompletaSP(
            request.getIdParticipacion(), 
            request.getIdRonda(), 
            request.getFlechas()
        );

        registrarLogSistema(request);
    }

    private void registrarLogSistema(PuntajeRondaDTO request) { 
        // Aquí usas request.getIdAdmin() cuando implementes el log
        System.out.println("Log: El admin " + request.getIdAdmin() + " registró puntajes.");
    }
    
}
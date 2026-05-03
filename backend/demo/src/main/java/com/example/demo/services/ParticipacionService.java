package com.example.demo.services;

import com.example.demo.dtos.InscritoDTO;
import com.example.demo.dtos.ResumenTorneoArqueroDTO;
import com.example.demo.models.Torneo;
import com.example.demo.repositories.ParticipacionRepository;
import com.example.demo.repositories.TorneoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class ParticipacionService {

    private final ParticipacionRepository participacionRepository;
    private final TorneoRepository torneoRepository;

    public ParticipacionService(ParticipacionRepository participacionRepository, TorneoRepository torneoRepository) {
        this.participacionRepository = participacionRepository;
        this.torneoRepository = torneoRepository;
    }

    @Transactional
    public void inscribirUsuario(Long idUsuario, Long idTorneo) {
        // Obtener el torneo para revisar su estado
        Torneo torneo = torneoRepository.buscarPorId(idTorneo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Torneo no encontrado"));

        // Si no está en 'NOT_STARTED', no puede entrar
        if (!"NOT_STARTED".equals(torneo.getEstadoTorneo())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Las inscripciones están cerradas. El torneo ya está en curso o finalizado.");
        }

        // Verificar si ya estaba inscrito
        if (participacionRepository.existeParticipacion(idUsuario, idTorneo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El arquero ya está inscrito en este torneo");
        }

        // Inscribir
        participacionRepository.inscribirUsuario(idUsuario, idTorneo);
    }

    public List<InscritoDTO> obtenerInscritosPorTorneo(Long idTorneo) {
        return participacionRepository.obtenerInscritosPorTorneo(idTorneo);
    }

    public Long obtenerIdParticipacion(Long idUsuario, Long idTorneo) {
        return participacionRepository.obtenerIdParticipacion(idUsuario, idTorneo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró una participación para este usuario en este torneo."));
    }

    @Transactional
    public void editarPuntajeManual(Long idAdmin, Long idRondaAfectada, Long idUsuario, Long idTorneo, Integer nuevoPuntaje) {
        participacionRepository.actualizarPuntajeFinalConTrigger(idAdmin, idRondaAfectada, nuevoPuntaje, idUsuario, idTorneo);
    }

    public List<Map<String, Object>> obtenerTodas() {
        return participacionRepository.obtenerTodas();
    }

        public ResumenTorneoArqueroDTO obtenerResumenPorTorneoYUsuario(Long idTorneo, Long idUsuario) {
        Map<String, Object> resumen = participacionRepository.obtenerResumenPorTorneoYUsuario(idTorneo, idUsuario)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                "No se encontró participación para el usuario en este torneo."));

        Integer puntajeFinal = resumen.get("puntaje_final") == null
            ? 0
            : ((Number) resumen.get("puntaje_final")).intValue();

        Integer posicionFinal = resumen.get("posicion_final") == null
            ? null
            : ((Number) resumen.get("posicion_final")).intValue();

        Integer totalFlechas = resumen.get("total_flechas") == null
            ? 0
            : ((Number) resumen.get("total_flechas")).intValue();

        Double promedioPuntos = resumen.get("promedio_puntos") == null
            ? 0.0
            : ((Number) resumen.get("promedio_puntos")).doubleValue();

        Integer rondasJugadas = resumen.get("rondas_jugadas") == null
            ? 0
            : ((Number) resumen.get("rondas_jugadas")).intValue();

        return new ResumenTorneoArqueroDTO(
            puntajeFinal,
            posicionFinal,
            totalFlechas,
            promedioPuntos,
            rondasJugadas
        );
        }
}
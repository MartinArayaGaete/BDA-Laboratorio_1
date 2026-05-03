package com.example.demo.controllers;

import com.example.demo.dtos.InscritoDTO;
import com.example.demo.services.ParticipacionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/participaciones")
public class ParticipacionController {

    private final ParticipacionService participacionService;

    // Inyectamos ParticipacionService porque ahí reside ahora la lógica limpia
    public ParticipacionController(ParticipacionService participacionService) {
        this.participacionService = participacionService;
    }

    /**
     * Inscribe a un arquero en un torneo.
     * POST /api/participaciones/inscribir?idTorneo=1&idUsuario=5
     */
    @PostMapping("/inscribir")
    public ResponseEntity<String> inscribirArquero(
            @RequestParam Long idTorneo,
            @RequestParam Long idUsuario) {
        try {
            participacionService.inscribirUsuario(idUsuario, idTorneo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Arquero inscrito exitosamente");
        } catch (org.springframework.web.server.ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor");
        }
    }

    /**
     * Obtiene la lista de inscritos de un torneo específico.
     * GET /api/participaciones/torneo/1
     */
    @GetMapping("/torneo/{idTorneo}")
    public ResponseEntity<List<InscritoDTO>> obtenerInscritosPorTorneo(@PathVariable Long idTorneo) {
        List<InscritoDTO> inscritos = participacionService.obtenerInscritosPorTorneo(idTorneo);
        if (inscritos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(inscritos);
    }


    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> obtenerTodas() {
        List<Map<String, Object>> participaciones = participacionService.obtenerTodas();
        return participaciones.isEmpty() ?
                ResponseEntity.noContent().build() :
                ResponseEntity.ok(participaciones);
    }
}
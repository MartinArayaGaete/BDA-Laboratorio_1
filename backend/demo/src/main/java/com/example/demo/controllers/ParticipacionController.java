package com.example.demo.controllers;

import com.example.demo.dtos.InscritoDTO;
import com.example.demo.services.TorneoService; // O ParticipacionService si decides crearlo
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/participaciones")
public class ParticipacionController {

    private final TorneoService torneoService;

    // Inyectamos TorneoService porque ahí reside actualmente la lógica de inscripción
    public ParticipacionController(TorneoService torneoService) {
        this.torneoService = torneoService;
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
            torneoService.inscribirArquero(idUsuario, idTorneo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Arquero inscrito exitosamente");
        } catch (Exception e) {
            // TorneoService ya lanza ResponseStatusException, 
            // pero podemos manejar errores inesperados aquí.
            throw e;
        }
    }

    /**
     * Obtiene la lista de inscritos de un torneo específico.
     * GET /api/participaciones/torneo/1
     */
    @GetMapping("/torneo/{idTorneo}")
    public ResponseEntity<List<InscritoDTO>> obtenerInscritosPorTorneo(@PathVariable Long idTorneo) {
        List<InscritoDTO> inscritos = torneoService.obtenerInscritos(idTorneo);
        if (inscritos.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return ResponseEntity.ok(inscritos);
    }
}
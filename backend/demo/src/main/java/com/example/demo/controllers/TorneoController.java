package com.example.demo.controllers;

import com.example.demo.dtos.TorneoCreacionDTO;
import com.example.demo.dtos.FlechaArqueroDTO;
import com.example.demo.models.Torneo;
import com.example.demo.services.TorneoService;
import com.example.demo.services.FlechaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/torneos")
public class TorneoController {

    private final TorneoService torneoService;
    private final FlechaService flechaService;

    public TorneoController(TorneoService torneoService, FlechaService flechaService) {
        this.torneoService = torneoService;
        this.flechaService = flechaService;
    }

    // Obtiene la lista completa de todos los torneos registrados en el sistema
    @GetMapping
    public ResponseEntity<List<Torneo>> obtenerTodos() {
        return ResponseEntity.ok(torneoService.obtenerTodos());
    }

    // Crea un nuevo torneo y genera automáticamente sus rondas iniciales por defecto
    @PostMapping
    public ResponseEntity<String> crearTorneo(@RequestBody TorneoCreacionDTO dto) {
        torneoService.crearTorneo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Torneo creado exitosamente");
    }

    // Vincula a un usuario (arquero) con un torneo específico para permitir su participación
    @PostMapping("/{idTorneo}/inscribir/{idUsuario}")
    public ResponseEntity<String> inscribirArquero(@PathVariable Long idTorneo, @PathVariable Long idUsuario) {
        torneoService.inscribirArquero(idUsuario, idTorneo);
        return ResponseEntity.ok("Inscripción realizada con éxito");
    }

    // Recupera la lista de todos los arqueros inscritos en un torneo determinado
    @GetMapping("/{idTorneo}/inscritos")
    public ResponseEntity<List<com.example.demo.dtos.InscritoDTO>> verInscritos(@PathVariable Long idTorneo) {
        return ResponseEntity.ok(torneoService.obtenerInscritos(idTorneo));
    }

    // Obtiene el historial de flechas lanzadas por un arquero específico en un torneo, ordenadas por ronda
    @GetMapping("/{idTorneo}/arqueros/{idUsuario}/flechas")
    public ResponseEntity<List<FlechaArqueroDTO>> verFlechasDeArquero(@PathVariable Long idTorneo, @PathVariable Long idUsuario) {
        return ResponseEntity.ok(flechaService.obtenerFlechasArquero(idUsuario, idTorneo));
    }

    // Permite al administrador crear una nueva ronda específica para un torneo de forma manual
    @PostMapping("/{idTorneo}/rondas/{numeroRonda}")
    public ResponseEntity<String> crearRondaManual(@PathVariable Long idTorneo, @PathVariable Integer numeroRonda) {
        torneoService.agregarRondaManual(idTorneo, numeroRonda);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ronda " + numeroRonda + " creada con éxito");
    }

    // Registra el puntaje de un nuevo lanzamiento de flecha realizado por un arquero en una ronda específica
    @PostMapping("/{idTorneo}/arqueros/{idUsuario}/rondas/{numeroRonda}/flechas")
    public ResponseEntity<String> registrarFlecha(
            @PathVariable Long idTorneo,
            @PathVariable Long idUsuario,
            @PathVariable Integer numeroRonda,
            @RequestBody Map<String, Integer> body) {

        Integer puntaje = body.get("puntaje");
        if (puntaje == null) {
            return ResponseEntity.badRequest().body("Falta enviar el 'puntaje' en el JSON");
        }

        flechaService.registrarFlecha(idTorneo, idUsuario, numeroRonda, puntaje);
        return ResponseEntity.status(HttpStatus.CREATED).body("¡Diana! Flecha registrada con éxito");
    }
}
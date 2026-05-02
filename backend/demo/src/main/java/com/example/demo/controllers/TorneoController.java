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

    @GetMapping
    public ResponseEntity<List<Torneo>> obtenerTodos() {
        return ResponseEntity.ok(torneoService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<String> crearTorneo(@RequestBody TorneoCreacionDTO dto) {
        torneoService.crearTorneo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Torneo creado exitosamente");
    }

    @PostMapping("/{idTorneo}/inscribir/{idUsuario}")
    public ResponseEntity<String> inscribirArquero(@PathVariable Long idTorneo, @PathVariable Long idUsuario) {
        torneoService.inscribirArquero(idUsuario, idTorneo);
        return ResponseEntity.ok("Inscripción realizada con éxito");
    }

    @GetMapping("/{idTorneo}/inscritos")
    public ResponseEntity<List<com.example.demo.dtos.InscritoDTO>> verInscritos(@PathVariable Long idTorneo) {
        return ResponseEntity.ok(torneoService.obtenerInscritos(idTorneo));
    }

    @GetMapping("/{idTorneo}/arqueros/{idUsuario}/flechas")
    public ResponseEntity<List<FlechaArqueroDTO>> verFlechasDeArquero(@PathVariable Long idTorneo, @PathVariable Long idUsuario) {
        return ResponseEntity.ok(flechaService.obtenerFlechasArquero(idUsuario, idTorneo));
    }

    @PostMapping("/{idTorneo}/rondas/{numeroRonda}")
    public ResponseEntity<String> crearRondaManual(@PathVariable Long idTorneo, @PathVariable Integer numeroRonda) {
        torneoService.agregarRondaManual(idTorneo, numeroRonda);
        return ResponseEntity.status(HttpStatus.CREATED).body("Ronda " + numeroRonda + " creada con éxito");
    }

    @PostMapping("/{idTorneo}/arqueros/{idUsuario}/rondas/{numeroRonda}/flechas")
    public ResponseEntity<String> registrarRondaCompleta(
            @PathVariable Long idTorneo,
            @PathVariable Long idUsuario,
            @PathVariable Integer numeroRonda,
            @RequestBody Map<String, List<Integer>> body) {

        List<Integer> flechas = body.get("flechas");
        if (flechas == null || flechas.isEmpty()) {
            return ResponseEntity.badRequest().body("Falta enviar el arreglo 'flechas' en el JSON");
        }

        flechaService.registrarRondaCompleta(idTorneo, idUsuario, numeroRonda, flechas);
        return ResponseEntity.status(HttpStatus.CREATED).body("¡Ronda registrada con éxito mediante Procedimiento Almacenado!");
    }
}
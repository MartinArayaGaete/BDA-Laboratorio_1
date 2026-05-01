package com.example.demo.controllers;

import com.example.demo.dtos.TorneoCreacionDTO;
import com.example.demo.models.Torneo;
import com.example.demo.services.TorneoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/torneos")
public class TorneoController {

    private final TorneoService torneoService;

    public TorneoController(TorneoService torneoService) {
        this.torneoService = torneoService;
    }

    // Ver todos los torneos (Arqueros y Admins)
    @GetMapping
    public ResponseEntity<List<Torneo>> obtenerTodos() {
        return ResponseEntity.ok(torneoService.obtenerTodos());
    }

    // Crear torneo (ADMIN)
    @PostMapping
    public ResponseEntity<String> crearTorneo(@RequestBody TorneoCreacionDTO dto) {
        torneoService.crearTorneo(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Torneo creado exitosamente");
    }

    // Inscribirse en un torneo (Arquero)
    @PostMapping("/{idTorneo}/inscribir/{idUsuario}")
    public ResponseEntity<String> inscribirArquero(@PathVariable Long idTorneo, @PathVariable Long idUsuario) {
        torneoService.inscribirArquero(idUsuario, idTorneo);
        return ResponseEntity.ok("Inscripción realizada con éxito");
    }


    @GetMapping("/{idTorneo}/inscritos")
    public ResponseEntity<List<com.example.demo.dtos.InscritoDTO>> verInscritos(@PathVariable Long idTorneo) {
        return ResponseEntity.ok(torneoService.obtenerInscritos(idTorneo));
    }
}
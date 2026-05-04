package com.example.demo.controllers;

import com.example.demo.dtos.EstadisticasArqueroDTO;
import com.example.demo.dtos.HistorialArqueroResponse;
import com.example.demo.services.HistorialService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/arqueros")
public class ArquerosController {

    private final HistorialService historialService;

    public ArquerosController(HistorialService historialService) {
        this.historialService = historialService;
    }

    /**
     * Obtiene el historial paginado de un arquero con todos sus torneos, rondas y flechas.
     * GET /api/arqueros/{idUsuario}/historial?page=0&size=5
     * 
     * @param idUsuario ID del usuario (arquero)
     * @param page Número de página (0-indexado)
     * @param size Cantidad de torneos por página
     * @return HistorialArqueroResponse con lista de torneos paginada e información de paginación
     */
    @GetMapping("/{idUsuario}/historial")
    public ResponseEntity<HistorialArqueroResponse> obtenerHistorial(
            @PathVariable Long idUsuario,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {
        try {
            HistorialArqueroResponse respuesta = historialService.obtenerHistorialArquero(idUsuario, page, size);
            return ResponseEntity.ok(respuesta);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Obtiene estadisticas agregadas del arquero.
     * GET /api/arqueros/{idUsuario}/estadisticas
     */
    @GetMapping("/{idUsuario}/estadisticas")
    public ResponseEntity<EstadisticasArqueroDTO> obtenerEstadisticas(@PathVariable Long idUsuario) {
        try {
            return ResponseEntity.ok(historialService.obtenerEstadisticasArquero(idUsuario));
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

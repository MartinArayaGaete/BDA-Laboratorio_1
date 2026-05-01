package com.example.demo.services;

import com.example.demo.models.Categoria;
import com.example.demo.repositories.CategoriaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

	private final CategoriaRepository categoriaRepository;

	public CategoriaService(CategoriaRepository categoriaRepository) {
		this.categoriaRepository = categoriaRepository;
	}

	public List<Categoria> obtenerTodas() {
		return categoriaRepository.obtenerTodas();
	}

	public Optional<Categoria> buscarPorId(Long idCategoria) {
		return categoriaRepository.buscarPorId(idCategoria);
	}

	public void crearCategoria(Categoria categoria) {
		if (categoria == null || categoria.getNombreCategoria() == null || categoria.getNombreCategoria().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la categoria es obligatorio");
		}
		categoriaRepository.crearCategoria(categoria.getNombreCategoria());
	}

	public void actualizarCategoria(Long idCategoria, Categoria categoria) {
		if (categoria == null || categoria.getNombreCategoria() == null || categoria.getNombreCategoria().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de la categoria es obligatorio");
		}
		if (categoriaRepository.buscarPorId(idCategoria).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada");
		}
		categoriaRepository.actualizarCategoria(idCategoria, categoria.getNombreCategoria());
	}

	public void eliminarPorId(Long idCategoria) {
		if (categoriaRepository.buscarPorId(idCategoria).isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria no encontrada");
		}
		categoriaRepository.eliminarPorId(idCategoria);
	}
}

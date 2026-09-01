package com.umg.venta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.umg.venta.dto.CategoriaDTO;
import com.umg.venta.dto.MessageResponse;
import com.umg.venta.service.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaDTO> getAllCategorias() {
        return categoriaService.findAll();
    }

    // --- Endpoints agregados Filtros y Búsquedas ---
    @GetMapping("/mostrarActivos")
    public List<CategoriaDTO> mostrarActivos() {
        return categoriaService.mostrarActivos();
    }

    @GetMapping("/mostrarActivosFiltro")
    public List<CategoriaDTO> mostrarActivosFiltro(@RequestParam String nombre) {
        return categoriaService.mostrarActivosFiltro(nombre);
    }

    @GetMapping("/mostrarActivosFiltroTop")
    public List<CategoriaDTO> mostrarActivosFiltroTop2(@RequestParam String nombre) {
        return categoriaService.mostrarActivosFiltroTop2(nombre);
    }

    @PostMapping
    public ResponseEntity<MessageResponse> createCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        try {
            categoriaService.save(categoriaDTO);
            return ResponseEntity.ok(new MessageResponse("Categoría creada con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error: La categoría ya existe"));
        }
    }

    @PutMapping("/{id}")
    public CategoriaDTO modificarCategoria(@PathVariable Integer id, @RequestBody CategoriaDTO categoriaDTO) {
        return categoriaService.modificarCategoria(id, categoriaDTO);
    }

    @PutMapping("/anular/{id}")
    public CategoriaDTO anularCategoria(@PathVariable Integer id) {
        return categoriaService.anularCategoria(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategoria(@PathVariable Integer id) {
        categoriaService.eliminarCantegoria(id);
    }
}

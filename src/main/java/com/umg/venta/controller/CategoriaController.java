package com.umg.venta.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import com.umg.venta.dto.CategoriaDTO;
import com.umg.venta.service.CategoriaService;
import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public List<CategoriaDTO> getAllCategorias() {
        return categoriaService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoriaDTO createCategoria(@RequestBody CategoriaDTO categoriaDTO) {
        return categoriaService.save(categoriaDTO);
    }

    // Estándar REST: PUT /categorias/{id} para modificar el recurso
    @PutMapping("/{id}")
    public CategoriaDTO modificarCategoria(@PathVariable Integer id, @RequestBody CategoriaDTO categoriaDTO) {
        return categoriaService.modificarCategoria(id, categoriaDTO);
    }

    // Ruta específica para la lógica de negocio de anulación
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

package com.umg.venta.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.umg.venta.dto.MessageResponse;
import com.umg.venta.dto.ProductoDTO;
import com.umg.venta.repository.ProductoRepository;
import com.umg.venta.service.ProductoService;

@RestController
@RequestMapping("/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoRepository productoRepository;
    private final ProductoService productoService;

    public ProductoController(ProductoService productoService, ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
        this.productoService = productoService;
    }

    @GetMapping
    public List<ProductoDTO> listarTodos() {
        return productoService.listarProductos();
    }

    @PostMapping
    public ResponseEntity<ProductoDTO> crear(@RequestBody ProductoDTO productoDTO) {
        ProductoDTO nuevoProducto = productoService.guardarProducto(productoDTO);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    @PutMapping("/{idProducto}")
    public ResponseEntity<MessageResponse> actualizarProducto(@PathVariable Integer idProducto,
                                                             @RequestBody ProductoDTO productoDTO) {
        try {
            productoService.actualizar(idProducto, productoDTO);
            return ResponseEntity.ok(new MessageResponse("Producto actualizado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error al actualizar el producto"));
        }
    }

    @PutMapping("/anular/{idProducto}")
    public ResponseEntity<MessageResponse> anularProducto(@PathVariable Integer idProducto,
                                                         @RequestBody ProductoDTO productoDTO) {
        try {
            productoService.anular(idProducto, productoDTO);
            return ResponseEntity.ok(new MessageResponse("Producto anulado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error al anular el producto"));
        }
    }

    @DeleteMapping("/{idProducto}")
    public ResponseEntity<MessageResponse> eliminarProducto(@PathVariable Integer idProducto) {
        try {
            productoRepository.deleteById(idProducto);
            return ResponseEntity.ok(new MessageResponse("Producto eliminado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error al eliminar el producto"));
        }
    }
}

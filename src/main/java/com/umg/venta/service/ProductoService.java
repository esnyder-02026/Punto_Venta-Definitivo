package com.umg.venta.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.umg.venta.dto.ProductoDTO;
import com.umg.venta.entity.Producto;
import com.umg.venta.repository.ProductoRepository;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    public List<ProductoDTO> listarProductos() {
        return productoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // --- Métodos agregados Filtros y Búsquedas ---
    public List<ProductoDTO> mostrarActivos() {
        return productoRepository.findByEstadoTrue()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> mostrarActivosFiltro(String nombre) {
        return productoRepository.findByEstadoTrueAndNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ProductoDTO> mostrarActivosFiltroTop2(String nombre) {
        return productoRepository.findTop2ByEstadoTrueAndNombreContainingIgnoreCase(nombre)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ProductoDTO guardarProducto(ProductoDTO dto) {
        Producto producto = convertToEntity(dto);
        // Si no viene definido el estado, se asigna activo por defecto
        if (producto.getEstado() == null) {
            producto.setEstado(true);
        }
        Producto productoGuardado = productoRepository.save(producto);
        return convertToDTO(productoGuardado);
    }

    public ProductoDTO actualizar(Integer idProducto, ProductoDTO dto) {
        Producto productoExistente = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        if (dto.getNombre() != null) {
            productoExistente.setNombre(dto.getNombre());
        }
        if (dto.getDescripcion() != null) {
            productoExistente.setDescripcion(dto.getDescripcion());
        }
        if (dto.getPrecio() != null) {
            productoExistente.setPrecio(dto.getPrecio());
        }
        if (dto.getStock() != null) {
            productoExistente.setStock(dto.getStock());
        }
        if (dto.getEstado() != null) {
            productoExistente.setEstado(dto.getEstado());
        }

        return convertToDTO(productoRepository.save(productoExistente));
    }

    public ProductoDTO anular(Integer idProducto, ProductoDTO dto) {
        Producto productoExistente = productoRepository.findById(idProducto)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));

        productoExistente.setStock(0);
        productoExistente.setEstado(false); // Marca el registro como inactivo en la base de datos
        return convertToDTO(productoRepository.save(productoExistente));
    }

    private ProductoDTO convertToDTO(Producto c) {
        ProductoDTO dto = new ProductoDTO();
        dto.setIdProducto(c.getIdProducto());
        dto.setEstado(c.getEstado()); // Mapea el estado al DTO
        dto.setNombre(c.getNombre());
        dto.setDescripcion(c.getDescripcion());
        dto.setPrecio(c.getPrecio());
        dto.setStock(c.getStock());
        return dto;
    }

    private Producto convertToEntity(ProductoDTO dto) {
        Producto producto = new Producto();
        producto.setIdProducto(dto.getIdProducto());
        producto.setEstado(dto.getEstado()); // Asigna el estado a la entidad
        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setPrecio(dto.getPrecio());
        producto.setStock(dto.getStock());
        return producto;
    }
}

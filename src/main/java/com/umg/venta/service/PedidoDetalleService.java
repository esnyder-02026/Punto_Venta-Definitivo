package com.umg.venta.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.umg.venta.dto.PedidoDetalleDTO;
import com.umg.venta.entity.PedidoDetalle;
import com.umg.venta.entity.Producto;
import com.umg.venta.repository.PedidoDetalleRepository;
import com.umg.venta.repository.ProductoRepository;

@Service
public class PedidoDetalleService {

    private final PedidoDetalleRepository pedidoDetalleRepository;
    private final ProductoRepository productoRepository;

    public PedidoDetalleService(PedidoDetalleRepository pedidoDetalleRepository,
                                ProductoRepository productoRepository) {
        this.pedidoDetalleRepository = pedidoDetalleRepository;
        this.productoRepository = productoRepository;
    }

    public List<PedidoDetalleDTO> listarTodos() {
        return pedidoDetalleRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    // --- Métodos agregados Filtros y Búsquedas ---
    public List<PedidoDetalleDTO> mostrarPorPedido(Integer idPedido) {
        return pedidoDetalleRepository.findByIdPedido_IdPedido(idPedido)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDetalleDTO> mostrarPorPedidoYProducto(Integer idPedido, String nombreProducto) {
        return pedidoDetalleRepository.findByIdPedido_IdPedidoAndIdProducto_NombreContainingIgnoreCase(idPedido, nombreProducto)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<PedidoDetalleDTO> mostrarPorPedidoYProductoTop2(Integer idPedido, String nombreProducto) {
        return pedidoDetalleRepository.findTop2ByIdPedido_IdPedidoAndIdProducto_NombreContainingIgnoreCase(idPedido, nombreProducto)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoDetalleDTO guardar(PedidoDetalleDTO dto) {
        PedidoDetalle detalle = new PedidoDetalle();

        if (dto.getIdProducto() != null) {
            Producto producto = productoRepository.findById(dto.getIdProducto())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
            detalle.setIdProducto(producto);
            
            BigDecimal precio = dto.getPrecioUnitario() != null ? dto.getPrecioUnitario() : producto.getPrecio();
            detalle.setPrecioUnitario(precio);
        } else {
            detalle.setPrecioUnitario(dto.getPrecioUnitario());
        }

        detalle.setCantidad(dto.getCantidad());
        
        if (detalle.getPrecioUnitario() != null && dto.getCantidad() > 0) {
            detalle.setSubtotal(detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(dto.getCantidad())));
        } else {
            detalle.setSubtotal(dto.getSubtotal());
        }

        PedidoDetalle guardado = pedidoDetalleRepository.save(detalle);
        return convertToDTO(guardado);
    }

    @Transactional
    public PedidoDetalleDTO actualizar(Integer idDetalle, PedidoDetalleDTO dto) {
        PedidoDetalle detalleExistente = pedidoDetalleRepository.findById(idDetalle)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle no encontrado"));

        if (dto.getIdProducto() != null) {
            Producto producto = productoRepository.findById(dto.getIdProducto())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
            detalleExistente.setIdProducto(producto);
        }

        if (dto.getCantidad() > 0) {
            detalleExistente.setCantidad(dto.getCantidad());
        }

        if (dto.getPrecioUnitario() != null) {
            detalleExistente.setPrecioUnitario(dto.getPrecioUnitario());
        }

        if (detalleExistente.getPrecioUnitario() != null && detalleExistente.getCantidad() > 0) {
            detalleExistente.setSubtotal(detalleExistente.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalleExistente.getCantidad())));
        }

        return convertToDTO(pedidoDetalleRepository.save(detalleExistente));
    }

    @Transactional
    public void anular(Integer idDetalle) {
        PedidoDetalle detalleExistente = pedidoDetalleRepository.findById(idDetalle)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle no encontrado"));

        detalleExistente.setCantidad(0);
        detalleExistente.setSubtotal(BigDecimal.ZERO);
        pedidoDetalleRepository.save(detalleExistente);
    }

    public void eliminar(Integer idDetalle) {
        if (!pedidoDetalleRepository.existsById(idDetalle)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Detalle no encontrado");
        }
        pedidoDetalleRepository.deleteById(idDetalle);
    }

    private PedidoDetalleDTO convertToDTO(PedidoDetalle d) {
        PedidoDetalleDTO dto = new PedidoDetalleDTO();
        dto.setIdDetalle(d.getIdPedidoDetalle());
        dto.setCantidad(d.getCantidad());
        dto.setPrecioUnitario(d.getPrecioUnitario());
        dto.setSubtotal(d.getSubtotal());

        if (d.getIdProducto() != null) {
            dto.setIdProducto(d.getIdProducto().getIdProducto());
        }
        return dto;
    }
}

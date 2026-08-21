package com.umg.venta.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.umg.venta.dto.PedidoDetalleDTO;
import com.umg.venta.entity.PedidoDetalle;
import com.umg.venta.repository.PedidoDetalleRepository;

@Service
public class PedidoDetalleService {

    private final PedidoDetalleRepository pedidoDetalleRepository;

    public PedidoDetalleService(PedidoDetalleRepository pedidoDetalleRepository) {
        this.pedidoDetalleRepository = pedidoDetalleRepository;
    }

    public List<PedidoDetalleDTO> listarTodos() {
        return pedidoDetalleRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
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

package com.umg.venta.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.umg.venta.dto.PedidoDTO;
import com.umg.venta.dto.PedidoDetalleDTO;
import com.umg.venta.entity.Cliente;
import com.umg.venta.entity.Pedido;
import com.umg.venta.entity.PedidoDetalle;
import com.umg.venta.entity.Producto;
import com.umg.venta.repository.ClienteRepository;
import com.umg.venta.repository.PedidoRepository;
import com.umg.venta.repository.ProductoRepository;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;

    public PedidoService(PedidoRepository pedidoRepository, 
                         ClienteRepository clienteRepository, 
                         ProductoRepository productoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
    }

    public List<PedidoDTO> listarPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoDTO guardarPedido(PedidoDTO dto) {
        Pedido pedido = new Pedido();
        
        Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con ID: " + dto.getIdCliente()));
        
        pedido.setIdCliente(cliente);
        pedido.setFechaPedido(new Date());
        pedido.setEstado(true);
        pedido.setEstadoPedido(true);

        BigDecimal totalPedido = BigDecimal.ZERO;
        List<PedidoDetalle> detallesEntities = new ArrayList<>();

        if (dto.getDetalles() != null && !dto.getDetalles().isEmpty()) {
            for (PedidoDetalleDTO dDto : dto.getDetalles()) {
                Producto producto = productoRepository.findById(dDto.getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + dDto.getIdProducto()));

                BigDecimal precio = dDto.getPrecioUnitario() != null ? dDto.getPrecioUnitario() : producto.getPrecio();
                BigDecimal subtotal = precio.multiply(BigDecimal.valueOf(dDto.getCantidad()));

                PedidoDetalle detalle = new PedidoDetalle();
                detalle.setIdPedido(pedido);
                detalle.setIdProducto(producto);
                detalle.setCantidad(dDto.getCantidad());
                detalle.setPrecioUnitario(precio);
                detalle.setSubtotal(subtotal);

                detallesEntities.add(detalle);
                totalPedido = totalPedido.add(subtotal);
            }
        }

        pedido.setTotal(totalPedido);
        pedido.setPedidoDetalleList(detallesEntities);

        Pedido pedidoGuardado = pedidoRepository.save(pedido);
        return convertToDTO(pedidoGuardado);
    }

    @Transactional
    public PedidoDTO actualizar(Integer idPedido, PedidoDTO dto) {
        Pedido pedidoExistente = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        if (dto.getIdCliente() != null) {
            Cliente cliente = clienteRepository.findById(dto.getIdCliente())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado"));
            pedidoExistente.setIdCliente(cliente);
        }

        if (dto.getEstado() != null) {
            pedidoExistente.setEstado(dto.getEstado());
        }

        if (dto.getEstadoPedido() != null) {
            pedidoExistente.setEstadoPedido(dto.getEstadoPedido());
        }

        return convertToDTO(pedidoRepository.save(pedidoExistente));
    }

    @Transactional
    public PedidoDTO anular(Integer idPedido, PedidoDTO dto) {
        Pedido pedidoExistente = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado"));

        pedidoExistente.setEstado(false);
        pedidoExistente.setEstadoPedido(false);
        return convertToDTO(pedidoRepository.save(pedidoExistente));
    }

    public void eliminar(Integer idPedido) {
        if (!pedidoRepository.existsById(idPedido)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pedido no encontrado");
        }
        pedidoRepository.deleteById(idPedido);
    }

    private PedidoDTO convertToDTO(Pedido p) {
        PedidoDTO dto = new PedidoDTO();
        dto.setIdPedido(p.getIdPedido());
        dto.setFechaPedido(p.getFechaPedido());
        dto.setEstado(p.getEstado());
        dto.setEstadoPedido(p.getEstadoPedido());
        dto.setTotal(p.getTotal());

        if (p.getIdCliente() != null) {
            dto.setIdCliente(p.getIdCliente().getIdCliente());
        }

        if (p.getPedidoDetalleList() != null) {
            List<PedidoDetalleDTO> detallesDTO = p.getPedidoDetalleList().stream().map(d -> {
                PedidoDetalleDTO dDto = new PedidoDetalleDTO();
                dDto.setIdDetalle(d.getIdPedidoDetalle());
                if (d.getIdProducto() != null) {
                    dDto.setIdProducto(d.getIdProducto().getIdProducto());
                }
                dDto.setCantidad(d.getCantidad());
                dDto.setPrecioUnitario(d.getPrecioUnitario());
                dDto.setSubtotal(d.getSubtotal());
                return dDto;
            }).collect(Collectors.toList());

            dto.setDetalles(detallesDTO);
        }

        return dto;
    }
}

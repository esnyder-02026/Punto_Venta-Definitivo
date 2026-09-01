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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.umg.venta.dto.MessageResponse;
import com.umg.venta.dto.PedidoDetalleDTO;
import com.umg.venta.service.PedidoDetalleService;

@RestController
@RequestMapping("/pedido-detalles")
@CrossOrigin(origins = "*")
public class PedidoDetalleController {

    private final PedidoDetalleService pedidoDetalleService;

    public PedidoDetalleController(PedidoDetalleService pedidoDetalleService) {
        this.pedidoDetalleService = pedidoDetalleService;
    }

    @GetMapping
    public List<PedidoDetalleDTO> listarTodos() {
        return pedidoDetalleService.listarTodos();
    }

    // --- Endpoints agregados Filtros y Búsquedas ---
    @GetMapping("/mostrarPorPedido")
    public List<PedidoDetalleDTO> mostrarPorPedido(@RequestParam Integer idPedido) {
        return pedidoDetalleService.mostrarPorPedido(idPedido);
    }

    @GetMapping("/mostrarPorPedidoYProducto")
    public List<PedidoDetalleDTO> mostrarPorPedidoYProducto(@RequestParam Integer idPedido,
                                                           @RequestParam String nombreProducto) {
        return pedidoDetalleService.mostrarPorPedidoYProducto(idPedido, nombreProducto);
    }

    @GetMapping("/mostrarPorPedidoYProductoTop")
    public List<PedidoDetalleDTO> mostrarPorPedidoYProductoTop2(@RequestParam Integer idPedido,
                                                               @RequestParam String nombreProducto) {
        return pedidoDetalleService.mostrarPorPedidoYProductoTop2(idPedido, nombreProducto);
    }

    @PostMapping
    public ResponseEntity<PedidoDetalleDTO> crear(@RequestBody PedidoDetalleDTO dto) {
        PedidoDetalleDTO nuevoDetalle = pedidoDetalleService.guardar(dto);
        return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
    }

    @PutMapping("/{idDetalle}")
    public ResponseEntity<MessageResponse> actualizar(@PathVariable Integer idDetalle,
                                                     @RequestBody PedidoDetalleDTO dto) {
        try {
            pedidoDetalleService.actualizar(idDetalle, dto);
            return ResponseEntity.ok(new MessageResponse("Detalle de pedido actualizado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error al actualizar el detalle de pedido"));
        }
    }

    @PutMapping("/anular/{idDetalle}")
    public ResponseEntity<MessageResponse> anular(@PathVariable Integer idDetalle) {
        try {
            pedidoDetalleService.anular(idDetalle);
            return ResponseEntity.ok(new MessageResponse("Detalle de pedido anulado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error al anular el detalle de pedido"));
        }
    }

    @DeleteMapping("/{idDetalle}")
    public ResponseEntity<MessageResponse> eliminar(@PathVariable Integer idDetalle) {
        try {
            pedidoDetalleService.eliminar(idDetalle);
            return ResponseEntity.ok(new MessageResponse("Detalle de pedido eliminado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error al eliminar el detalle de pedido"));
        }
    }
}

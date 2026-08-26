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
import com.umg.venta.dto.PedidoDTO;
import com.umg.venta.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
@CrossOrigin(origins = "*")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public List<PedidoDTO> listarTodos() {
        return pedidoService.listarPedidos();
    }

    @PostMapping
    public ResponseEntity<PedidoDTO> crear(@RequestBody PedidoDTO pedidoDTO) {
        PedidoDTO nuevoPedido = pedidoService.guardarPedido(pedidoDTO);
        return new ResponseEntity<>(nuevoPedido, HttpStatus.CREATED);
    }

    @PutMapping("/{idPedido}")
    public ResponseEntity<MessageResponse> actualizarPedido(@PathVariable Integer idPedido,
                                                           @RequestBody PedidoDTO pedidoDTO) {
        try {
            pedidoService.actualizar(idPedido, pedidoDTO);
            return ResponseEntity.ok(new MessageResponse("Pedido actualizado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error al actualizar el pedido"));
        }
    }

    @PutMapping("/anular/{idPedido}")
    public ResponseEntity<MessageResponse> anularPedido(@PathVariable Integer idPedido,
                                                       @RequestBody PedidoDTO pedidoDTO) {
        try {
            pedidoService.anular(idPedido, pedidoDTO);
            return ResponseEntity.ok(new MessageResponse("Pedido anulado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error al anular el pedido"));
        }
    }

    @DeleteMapping("/{idPedido}")
    public ResponseEntity<MessageResponse> eliminarPedido(@PathVariable Integer idPedido) {
        try {
            pedidoService.eliminar(idPedido);
            return ResponseEntity.ok(new MessageResponse("Pedido eliminado con éxito"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new MessageResponse("Error al eliminar el pedido"));
        }
    }
}
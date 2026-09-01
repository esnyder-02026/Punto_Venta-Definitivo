package com.umg.venta.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.umg.venta.entity.PedidoDetalle;

@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Integer> {

    // --- Métodos agregados Filtros y Búsquedas ---
    List<PedidoDetalle> findByIdPedido_IdPedido(Integer idPedido);

    List<PedidoDetalle> findByIdPedido_IdPedidoAndIdProducto_NombreContainingIgnoreCase(Integer idPedido, String nombreProducto);

    List<PedidoDetalle> findTop2ByIdPedido_IdPedidoAndIdProducto_NombreContainingIgnoreCase(Integer idPedido, String nombreProducto);
}

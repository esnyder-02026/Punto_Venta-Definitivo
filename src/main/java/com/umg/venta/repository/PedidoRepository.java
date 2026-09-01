package com.umg.venta.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.umg.venta.entity.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    // Lista pedidos por estado (Boolean en la DB: true/false)
    List<Pedido> findByEstado(Boolean estado);

    // Usa idCliente_Nombre para navegar por la propiedad 'idCliente' de la entidad Pedido
    List<Pedido> findByIdCliente_NombreContainingIgnoreCase(String nombre);

    List<Pedido> findByEstadoAndIdCliente_NombreContainingIgnoreCase(Boolean estado, String nombre);

    List<Pedido> findTop2ByEstadoAndIdCliente_NombreContainingIgnoreCase(Boolean estado, String nombre);
}

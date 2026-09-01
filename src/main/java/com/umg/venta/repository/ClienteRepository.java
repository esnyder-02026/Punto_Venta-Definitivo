package com.umg.venta.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.umg.venta.entity.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
    
    boolean existsByNombreIgnoreCaseAndApellidoIgnoreCase(String nombre, String apellido);

    // --- Métodos agregados Filtros y Búsquedas ---
    List<Cliente> findByEstadoTrue();

    List<Cliente> findByEstadoTrueAndNombreContainingIgnoreCase(String nombre);

    List<Cliente> findTop2ByEstadoTrueAndNombreContainingIgnoreCase(String nombre);
}

package com.umg.venta.dto;

import lombok.Data;

/**
 *
 * @author madel
 */
@Data
public class CategoriaDTO {
    private Integer idCategoria;
    private Boolean estado;
    private String nombre;
    private String descripcion;
}

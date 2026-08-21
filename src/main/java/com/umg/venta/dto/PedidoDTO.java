package com.umg.venta.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PedidoDTO {
    private Integer idPedido;
    private Integer idCliente;
    private Date fechaPedido;
    private Boolean estado;
    private Boolean estadoPedido;
    private BigDecimal total;
    private List<PedidoDetalleDTO> detalles;

    public Integer getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public Date getFechaPedido() { return fechaPedido; }
    public void setFechaPedido(Date fechaPedido) { this.fechaPedido = fechaPedido; }

    public Boolean getEstado() { return estado; }
    public void setEstado(Boolean estado) { this.estado = estado; }

    public Boolean getEstadoPedido() { return estadoPedido; }
    public void setEstadoPedido(Boolean estadoPedido) { this.estadoPedido = estadoPedido; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public List<PedidoDetalleDTO> getDetalles() { return detalles; }
    public void setDetalles(List<PedidoDetalleDTO> detalles) { this.detalles = detalles; }
}

package com.graspymar.ims.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name="detalle_venta")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="venta_id")
    private Venta venta;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="insumo_id")
    private Insumo insumo;

    @Column(nullable=false)
    private Integer cantidad;

    @Column(nullable=false,precision=12,scale=2)
    private BigDecimal precioUnitario;

    @Column(nullable=false,precision=12,scale=2)
    private BigDecimal subtotal;
}

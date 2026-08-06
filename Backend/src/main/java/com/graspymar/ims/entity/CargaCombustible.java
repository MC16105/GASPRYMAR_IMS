package com.graspymar.ims.entity;

import com.graspymar.ims.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "carga_combustible")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CargaCombustible extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id", nullable = false)
    private Vehiculo vehiculo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private Double galones;

    @Column(nullable = false)
    private BigDecimal precioGalon;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private Integer kilometraje;

    @Column(nullable = false, length = 30)
    private String tipoCombustible;

    @Column(length = 50)
    private String numeroFactura;

    @Column(length = 250)
    private String observaciones;
}

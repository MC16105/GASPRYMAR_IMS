package com.graspymar.ims.entity;

import com.graspymar.ims.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Venta extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="cliente_id", nullable = false)
    private Cliente cliente;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length=50)
    private String documentoFiscal;

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal montoTotal;

    @Column(length=300)
    private String observaciones;

    @OneToMany(
            mappedBy="venta",
            cascade=CascadeType.ALL,
            orphanRemoval=true
    )
    @Builder.Default
    private List<DetalleVenta> detalles=new ArrayList<>();
}

package com.graspymar.ims.entity;

import com.graspymar.ims.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table (name = "compras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Compra extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="proveedor_id", nullable = false)
    private Proveedor proveedor;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length=50)
    private String documentoFiscal;

    @Column(nullable=false, precision=12, scale=2)
    private BigDecimal montoTotal;

    @Column(length=300)
    private String observaciones;

    @OneToMany(
            mappedBy="compra",
            cascade=CascadeType.ALL,
            orphanRemoval=true
    )
    @Builder.Default
    private List<DetalleCompra> detalles=new ArrayList<>();


}

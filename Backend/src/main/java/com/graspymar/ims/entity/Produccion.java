package com.graspymar.ims.entity;

import com.graspymar.ims.base.BaseEntity;
import com.graspymar.ims.enums.TipoProduccion;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "produccion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Produccion extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoProduccion tipoMovimiento;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false)
    private BigDecimal cantidad;

    @Column(nullable = false, length = 20)
    private String unidadMedida;

    @Column(length = 250)
    private String observaciones;

}

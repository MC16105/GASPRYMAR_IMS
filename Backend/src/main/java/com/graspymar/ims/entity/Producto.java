package com.graspymar.ims.entity;

import com.graspymar.ims.base.BaseEntity;
import com.graspymar.ims.enums.TipoMedida;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String categoria;

    @Column(nullable = false, length = 20)
    private TipoMedida unidadMedida;

    @Column(nullable = false)
    private BigDecimal stockActual;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precioVenta;
}

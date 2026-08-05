package com.graspymar.ims.entity;

import com.graspymar.ims.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_control_estanque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleControlEstanque extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "control_estanque_id", nullable = false)
    private ControlEstanque controlEstanque;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insumo_id", nullable = false)
    private Insumo insumo;

    @Column(nullable = false)
    private Integer cantidad;

}

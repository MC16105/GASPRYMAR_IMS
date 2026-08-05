package com.graspymar.ims.entity;

import com.graspymar.ims.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "control_estanque")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ControlEstanque extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estanque_id", nullable = false)
    private Estanque estanque;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 250)
    private String observaciones;

    @OneToMany(
            mappedBy = "controlEstanque",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<DetalleControlEstanque> detalles = new ArrayList<>();

}

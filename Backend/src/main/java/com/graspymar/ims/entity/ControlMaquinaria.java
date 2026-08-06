package com.graspymar.ims.entity;

import com.graspymar.ims.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "control_maquinaria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ControlMaquinaria extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "maquinaria_id", nullable = false)
    private Maquinaria maquinaria;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(length = 50)
    private String horasUso;

    @Column(length = 50)
    private String operador;

    @OneToMany(
            mappedBy = "controlMaquinaria",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<DetalleControlMaquinaria> detalles = new ArrayList<>();
}

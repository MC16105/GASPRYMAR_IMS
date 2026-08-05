package com.graspymar.ims.entity;

import com.graspymar.ims.base.BaseEntity;
import com.graspymar.ims.enums.EstadoEstanque;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estanques")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estanque extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 20)
    private String codigo;

    @Column(nullable = false)
    private Double area;

    @Column(nullable = false)
    private Double capacidad;

    @Column(length = 150)
    private String ubicacion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEstanque estado;


}

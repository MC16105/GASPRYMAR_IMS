package com.graspymar.ims.entity;

import com.graspymar.ims.base.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehiculo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vehiculo extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String placa;

    @Column(nullable = false, length = 20)
    private String marca;

    @Column(nullable = false, length = 20)
    private String modelo;

    @Column(nullable = false, length = 100)
    private String clase;

    @Column(length = 250)
    private String tipo;

    @Column(length = 20)
    private String anio;

    @Column(nullable = false, length = 100)
    private String estado;

    @Column(length = 100)
    private String observaciones;
}

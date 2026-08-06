package com.graspymar.ims.entity;

import com.graspymar.ims.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "maquinarias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Maquinaria extends BaseEntity {

    @Column(nullable = false, unique = true, length = 20)
    private String nombre;

    @Column(nullable = false, length = 20)
    private String codigo;

    @Column(nullable = false, length = 20)
    private String tipo;

    @Column(nullable = false, length = 100)
    private String marca;

    @Column(length = 250)
    private String modelo;

    @Column(length = 20)
    private String anio;

    @Column(nullable = false, length = 100)
    private String estado;

    @Column(length = 100)
    private String observaciones;
}

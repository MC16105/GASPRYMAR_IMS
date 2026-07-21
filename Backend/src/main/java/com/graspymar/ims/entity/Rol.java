package com.graspymar.ims.entity;

import com.graspymar.ims.base.BaseEntity;
import com.graspymar.ims.enums.RolNombre;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "roles")
public class Rol extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RolNombre nombre;

    @Column(length = 150)
    private String descripcion;

}

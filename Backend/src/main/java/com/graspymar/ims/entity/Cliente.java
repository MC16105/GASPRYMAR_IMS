package com.graspymar.ims.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.graspymar.ims.base.BaseEntity;

@Entity
@Table (name = "clientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente extends BaseEntity{

    @Column(nullable = false, length = 150)
    private String nombreRazonSocial;

    @Column(nullable = false, unique = true, length = 20)
    private String duiNit;

    @Column(unique = true, length = 20)
    private String nrc;

    @Column(length = 250)
    private String direccion;

    @Column(length = 20)
    private String telefono;

    @Column(length = 100)
    private String correo;

}

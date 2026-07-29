package com.graspymar.ims.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProveedorResponseDTO {
    private Long id;

    private String nombreRazonSocial;

    private String duiNit;

    private String nrc;

    private String direccion;

    private String telefono;

    private String correo;
}

package com.graspymar.ims.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VehiculoResponseDTO {

    private Long id;

    private String placa;

    private String marca;

    private String modelo;

    private String clase;

    private String tipo;

    private String anio;

    private String estado;

    private String observaciones;
}

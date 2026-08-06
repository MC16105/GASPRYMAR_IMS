package com.graspymar.ims.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaquinariaResponseDTO {

    private Long id;

    private String nombre;

    private String codigo;

    private String tipo;

    private String marca;

    private String modelo;

    private String anio;

    private String estado;

    private String observaciones;
}

package com.graspymar.ims.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaquinariaRequestDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String codigo;

    @NotBlank
    private String tipo;

    @NotBlank
    private String marca;

    @NotBlank
    private String modelo;

    private String anio;

    @NotBlank
    private String estado;

    private String observaciones;
}

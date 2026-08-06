package com.graspymar.ims.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehiculoRequestDTO {

    @NotBlank(message = "La Placa es Obligatoria")
    @Size(max = 20)
    private String placa;

    @NotBlank(message ="La Marca es Obligatoria")
    @Size(max = 20)
    private String marca;

    @NotBlank(message ="El Modelo es Obligatorio")
    @Size(max = 20)
    private String modelo;

    @Size(max = 250)
    private String clase;

    @Size(max = 20)
    private String tipo;

    @Size(max = 100)
    private String anio;

    @Size(max = 20)
    private String estado;

    @Size(max = 100)
    private String observaciones;

}

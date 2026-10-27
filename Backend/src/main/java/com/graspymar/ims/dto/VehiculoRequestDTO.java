package com.graspymar.ims.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VehiculoRequestDTO {

    @NotBlank(message = "La placa es obligatoria")
    @Size(max = 20, message = "La placa no puede superar los 20 caracteres")
    private String placa;

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 20, message = "La marca no puede superar los 20 caracteres")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 20, message = "El modelo no puede superar los 20 caracteres")
    private String modelo;

    @NotBlank(message = "La clase es obligatoria")
    @Size(max = 100, message = "La clase no puede superar los 100 caracteres")
    private String clase;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 50, message = "El tipo no puede superar los 50 caracteres")
    private String tipo;

    @Pattern(regexp = "^(19|20)\\d{2}$", message = "El año debe estar entre 1900 y 2099")
    private String anio;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 30, message = "El estado no puede superar los 30 caracteres")
    private String estado;

    @Size(max = 250, message = "Las observaciones no pueden superar los 250 caracteres")
    private String observaciones;

}

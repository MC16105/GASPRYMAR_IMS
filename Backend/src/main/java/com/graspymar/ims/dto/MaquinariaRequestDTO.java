package com.graspymar.ims.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MaquinariaRequestDTO {

    @NotBlank(message = "El nombre de la maquinaria es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[\\p{L}0-9 .()/-]+$", message = "El nombre contiene caracteres inválidos")
    private String nombre;

    @NotBlank(message = "El código de la maquinaria es obligatorio")
    @Size(min = 2, max = 30, message = "El código debe tener entre 2 y 30 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "El código solo puede contener letras, números, guiones y guiones bajos")
    private String codigo;

    @NotBlank(message = "El tipo de maquinaria es obligatorio")
    @Size(max = 50, message = "El tipo no puede superar los 50 caracteres")
    private String tipo;

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 50, message = "La marca no puede superar los 50 caracteres")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 50, message = "El modelo no puede superar los 50 caracteres")
    private String modelo;

    @Pattern(regexp = "^(19[0-9]{2}|20[0-9]{2})$", message = "El año debe contener 4 dígitos entre 1900 y 2099")
    private String anio;

    @NotBlank(message = "El estado de la maquinaria es obligatorio")
    @Size(max = 30, message = "El estado no puede superar los 30 caracteres")
    private String estado;

    @Size(max = 255, message = "Las observaciones no pueden superar los 255 caracteres")
    private String observaciones;
}

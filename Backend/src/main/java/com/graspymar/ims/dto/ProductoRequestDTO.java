package com.graspymar.ims.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoRequestDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String categoria;

    @NotBlank
    private String unidadMedida;

    @NotNull
    private Double stockActual;

    @NotNull
    private BigDecimal precioVenta;
}

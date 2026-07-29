package com.graspymar.ims.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsumoRequestDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String unidadMedida;

    @NotNull
    private Integer stockActual;

    @NotNull
    private Integer stockMinimo;

    @NotNull
    private BigDecimal precioReferencia;

}

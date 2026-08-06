package com.graspymar.ims.dto;

import com.graspymar.ims.enums.TipoMedida;
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
    private TipoMedida unidadMedida;

    @NotNull
    private BigDecimal stockActual;

    @NotNull
    private BigDecimal precioVenta;
}

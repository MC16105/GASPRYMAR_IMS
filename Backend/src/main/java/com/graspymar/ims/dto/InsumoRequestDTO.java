package com.graspymar.ims.dto;


import com.graspymar.ims.enums.TipoMedida;
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
    private TipoMedida unidadMedida;

    @NotNull
    private Integer stockActual;

    @NotNull
    private Integer stockMinimo;

    @NotNull
    private BigDecimal precioReferencia;

}

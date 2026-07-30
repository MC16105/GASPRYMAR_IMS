package com.graspymar.ims.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleVentaRequestDTO {

    @NotNull
    private Long insumoId;

    @NotNull
    private Integer cantidad;

    @NotNull
    private BigDecimal precioUnitario;
}

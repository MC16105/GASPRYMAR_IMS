package com.graspymar.ims.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCompraRequestDTO {

    @NotNull(message = "El insumo es obligatorio")
    @Positive(message = "El insumo seleccionado no es válido")
    private Long insumoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Digits(integer = 6, fraction = 2, message = "La cantidad admite hasta 2 decimales")
    private Integer cantidad;

    @NotNull(message = "El precio unitario es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio unitario debe ser mayor a cero")
    @Digits(integer = 10, fraction = 2, message = "El precio unitario admite hasta 2 decimales")
    private BigDecimal precioUnitario;
}

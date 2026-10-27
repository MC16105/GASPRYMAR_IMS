package com.graspymar.ims.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleControlEstanqueRequestDTO {

    @NotNull(message = "El insumo es obligatorio")
    @Positive(message = "El insumo seleccionado no es válido")
    private Long insumoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;
}

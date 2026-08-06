package com.graspymar.ims.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleControlMaquinariaRequestDTO {

    @NotNull
    private Long insumoId;

    @NotNull
    @Positive
    private Integer cantidad;

}

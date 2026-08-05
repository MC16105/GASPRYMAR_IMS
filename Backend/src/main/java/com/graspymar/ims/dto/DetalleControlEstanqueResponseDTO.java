package com.graspymar.ims.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleControlEstanqueResponseDTO {

    private Long id;

    private Long insumoId;

    private String nombreInsumo;

    private Integer cantidad;

}

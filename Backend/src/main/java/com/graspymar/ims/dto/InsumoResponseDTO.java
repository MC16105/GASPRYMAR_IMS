package com.graspymar.ims.dto;

import com.graspymar.ims.enums.TipoMedida;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsumoResponseDTO {

    private Long id;

    private String nombre;

    private TipoMedida unidadMedida;

    private Integer stockActual;

    private Integer stockMinimo;

    private BigDecimal precioReferencia;

}

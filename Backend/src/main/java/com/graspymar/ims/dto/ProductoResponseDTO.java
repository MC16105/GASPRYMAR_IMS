package com.graspymar.ims.dto;

import com.graspymar.ims.enums.TipoMedida;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoResponseDTO {

    private Long id;

    private String nombre;

    private String categoria;

    private TipoMedida unidadMedida;

    private BigDecimal stockActual;

    private BigDecimal precioVenta;
}

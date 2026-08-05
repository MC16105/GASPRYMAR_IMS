package com.graspymar.ims.dto;

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

    private String unidadMedida;

    private BigDecimal stockActual;

    private BigDecimal precioVenta;
}

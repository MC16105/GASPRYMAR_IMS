package com.graspymar.ims.dto;

import com.graspymar.ims.enums.TipoProduccion;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduccionResponseDTO {

    private Long id;

    private String producto;

    private TipoProduccion tipoMovimiento;

    private LocalDate fecha;

    private BigDecimal cantidad;

    private String unidadMedida;

    private String observaciones;

}

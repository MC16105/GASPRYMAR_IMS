package com.graspymar.ims.dto;

import com.graspymar.ims.enums.TipoMedida;
import com.graspymar.ims.enums.TipoProduccion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduccionRequestDTO {

    @NotNull
    private Long productoId;

    @NotNull
    private TipoProduccion tipoMovimiento;

    @NotNull
    private LocalDate fecha;

    @NotNull
    @Positive
    private BigDecimal cantidad;

    @NotBlank
    private TipoMedida unidadMedida;

    private String observaciones;
}

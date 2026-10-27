package com.graspymar.ims.dto;

import com.graspymar.ims.enums.TipoProduccion;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProduccionRequestDTO {

    @NotNull(message = "El producto es obligatorio")
    @Positive(message = "El producto seleccionado no es válido")
    private Long productoId;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoProduccion tipoMovimiento;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fecha;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a cero")
    @Digits(integer = 10, fraction = 2, message = "La cantidad admite hasta 2 decimales")
    private BigDecimal cantidad;

    @NotBlank(message = "La unidad de medida es obligatoria")
    @Size(max = 30, message = "La unidad de medida no puede superar los 30 caracteres")
    private String unidadMedida;

    @Size(max = 255, message = "Las observaciones no pueden superar los 255 caracteres")
    private String observaciones;
}

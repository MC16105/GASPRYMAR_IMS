package com.graspymar.ims.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InsumoRequestDTO {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[\\p{L}0-9 .%/'()-]+$", message = "El nombre contiene caracteres inválidos")
    private String nombre;

    @NotBlank(message = "La unidad de medida es obligatoria")
    @Size(max = 5, message = "La unidad de medida no puede superar los 30 caracteres")
    private String unidadMedida;

    @NotNull(message = "El stock actual es obligatorio")
    @DecimalMin(value = "0.0", message = "El stock actual no puede ser negativo")
    @DecimalMax(value = "100000.0", message = "Stock demasiado alto")
    @Digits(integer = 6, fraction = 2, message = "El stock admite hasta 2 decimales")
    private BigDecimal stockActual;

    @NotNull(message = "El stock mínimo es obligatorio")
    @DecimalMin(value = "0.0", message = "El stock mínimo no puede ser negativo")
    @DecimalMax(value = "100000.0", message = "Stock mínimo demasiado alto")
    @Digits(integer = 6, fraction = 2, message = "El stock mínimo admite hasta 2 decimales")
    private BigDecimal stockMinimo;

    @NotNull(message = "El precio de referencia es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de referencia debe ser mayor a cero")
    @Digits(integer = 10, fraction = 2, message = "El precio admite hasta 2 decimales")
    private BigDecimal precioReferencia;
}
package com.graspymar.ims.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoRequestDTO {

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[\\p{L}0-9 -]+$",message = "El nombre contiene caracteres inválidos")
    @Schema(example = "-")
    private String nombre;

    @NotBlank(message = "La categoría es obligatoria")
    @Size(max = 50, message = "La categoría no puede superar los 50 caracteres")
    private String categoria;

    @NotBlank(message = "La unidad de medida es obligatoria")
    @Size(max = 5, message = "La unidad de medida no puede superar los 5 caracteres")
    private String unidadMedida;

    @NotNull(message = "El stock es obligatorio")
    @DecimalMin(value = "0.0", message = "El stock no puede ser negativo")
    @DecimalMax(value = "100000.0", message = "Stock demasiado alto")
    @Digits(integer = 6, fraction = 2, message = "El stock admite hasta 2 decimales")
    private BigDecimal stockActual;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Digits(integer = 10, fraction = 2, message = "Formato de precio inválido")
    @Schema(example = "0.00")
    private BigDecimal precioVenta;
}

package com.graspymar.ims.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CargaCombustibleRequestDTO {

    @NotNull(message = "El vehículo es obligatorio")
    @Positive(message = "El vehículo seleccionado no es válido")
    private Long vehiculoId;

    @NotNull(message = "El proveedor es obligatorio")
    @Positive(message = "El proveedor seleccionado no es válido")
    private Long proveedorId;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fecha;

    @NotNull(message = "La cantidad de galones es obligatoria")
    @DecimalMin(value = "0.0", inclusive = false, message = "La cantidad de galones debe ser mayor a cero")
    @DecimalMax(value = "10000.0", message = "La cantidad de galones supera el límite permitido")
    private Double galones;

    @NotNull(message = "El precio por galón es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio por galón debe ser mayor a cero")
    @Digits(integer = 6, fraction = 2, message = "El precio por galón admite hasta 2 decimales")
    private BigDecimal precioGalon;

    @NotNull(message = "El kilometraje es obligatorio")
    @PositiveOrZero(message = "El kilometraje no puede ser negativo")
    private Integer kilometraje;

    @NotBlank(message = "El tipo de combustible es obligatorio")
    @Size(max = 30, message = "El tipo de combustible no puede superar los 30 caracteres")
    private String tipoCombustible;

    @NotBlank(message = "El número de factura es obligatorio")
    @Size(max = 50, message = "El número de factura no puede superar los 50 caracteres")
    private String numeroFactura;

    @Size(max = 250, message = "Las observaciones no pueden superar los 255 caracteres")
    private String observaciones;
}

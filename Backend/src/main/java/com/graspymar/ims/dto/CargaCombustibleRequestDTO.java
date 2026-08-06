package com.graspymar.ims.dto;

import com.graspymar.ims.entity.Proveedor;
import com.graspymar.ims.entity.Vehiculo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CargaCombustibleRequestDTO {

    @NotNull
    private Long vehiculoId;

    @NotNull
    private Long proveedorId;

    @NotNull
    private LocalDate fecha;

    @NotNull
    private Double galones;

    @NotNull
    private BigDecimal precioGalon;

    @NotNull
    private Integer kilometraje;

    @NotBlank
    private String tipoCombustible;

    @NotBlank
    private String numeroFactura;

    @NotBlank
    private String observaciones;

}

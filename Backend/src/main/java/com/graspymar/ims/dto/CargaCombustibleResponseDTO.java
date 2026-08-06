package com.graspymar.ims.dto;

import com.graspymar.ims.entity.Proveedor;
import com.graspymar.ims.entity.Vehiculo;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CargaCombustibleResponseDTO {

    private Long id;

    private String vehiculo;

    private String proveedor;

    private LocalDate fecha;

    private Double galones;

    private BigDecimal precioGalon;

    private BigDecimal total;

    private Integer kilometraje;

    private String tipoCombustible;

    private String numeroFactura;

    private String observaciones;
}

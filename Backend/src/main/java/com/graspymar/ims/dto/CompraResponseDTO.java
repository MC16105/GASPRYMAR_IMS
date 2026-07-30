package com.graspymar.ims.dto;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompraResponseDTO {

    private Long id;

    private String proveedor;

    private LocalDate fecha;

    private String documentoFiscal;

    private BigDecimal montoTotal;

    private String observaciones;

    private List<DetalleCompraResponseDTO> detalles;
}

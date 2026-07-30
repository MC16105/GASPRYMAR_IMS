package com.graspymar.ims.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequestDTO {

    @NotNull
    private Long clienteId;

    @NotNull
    private LocalDate fecha;

    private String documentoFiscal;

    private String observaciones;

    @NotEmpty
    private List<DetalleVentaRequestDTO> detalles;
}

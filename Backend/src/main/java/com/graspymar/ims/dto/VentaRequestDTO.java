package com.graspymar.ims.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VentaRequestDTO {

    @NotNull(message = "El cliente es obligatorio")
    @Positive(message = "El cliente seleccionado no es válido")
    private Long clienteId;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fecha;

    @Size(max = 50, message = "El documento fiscal no puede superar los 50 caracteres")
    private String documentoFiscal;

    @Size(max = 300, message = "Las observaciones no pueden superar los 300 caracteres")
    private String observaciones;

    @NotEmpty(message = "La venta debe contener al menos un producto")
    private List<@NotNull(message = "El detalle no puede ser nulo") @Valid DetalleVentaRequestDTO> detalles;
}

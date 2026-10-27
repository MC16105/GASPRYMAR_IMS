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
public class ControlEstanqueRequestDTO {

    @NotNull(message = "El estanque es obligatorio")
    @Positive(message = "El estanque seleccionado no es válido")
    private Long estanqueId;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fecha;

    @Size(max = 250, message = "Las observaciones no pueden superar los 250 caracteres")
    private String observaciones;

    @NotEmpty(message = "El control debe contener al menos un insumo")
    private List<@NotNull(message = "El detalle no puede ser nulo") @Valid DetalleControlEstanqueRequestDTO> detalles;
}

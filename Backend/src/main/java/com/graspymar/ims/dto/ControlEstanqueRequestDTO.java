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
public class ControlEstanqueRequestDTO {

    @NotNull
    private Long estanqueId;

    @NotNull
    private LocalDate fecha;

    private String observaciones;

    @NotEmpty
    private List<DetalleControlEstanqueRequestDTO> detalles;
}

package com.graspymar.ims.dto;

import jakarta.validation.constraints.NotBlank;
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
public class ControlMaquinariaRequestDTO {

    @NotNull
    private Long maquinariaId;

    @NotNull
    private LocalDate fecha;

    @NotBlank
    private String horasUso;

    @NotBlank
    private String operador;

    @NotEmpty
    private List<DetalleControlMaquinariaRequestDTO> detalles;
}

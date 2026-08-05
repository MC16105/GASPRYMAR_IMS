package com.graspymar.ims.dto;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ControlEstanqueResponseDTO {

    private Long id;

    private String estanque;

    private LocalDate fecha;

    private String observaciones;

    private List<DetalleControlEstanqueResponseDTO> detalles;
}

package com.graspymar.ims.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ControlMaquinariaResponseDTO {

    private Long id;

    private String maquinaria;

    private LocalDate fecha;

    private String horasUso;

    private String operador;

    private List<DetalleControlMaquinariaResponseDTO> detalles;
}

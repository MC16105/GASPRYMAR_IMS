package com.graspymar.ims.dto;

import com.graspymar.ims.enums.EstadoEstanque;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstanqueResponseDTO {

    private Long id;

    private String nombre;

    private String codigo;

    private Double area;

    private Double capacidad;

    private String ubicacion;

    private EstadoEstanque estado;

}

package com.graspymar.ims.dto;

import com.graspymar.ims.enums.EstadoEstanque;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstanqueRequestDTO {

    @NotBlank
    private String nombre;

    @NotBlank
    private String codigo;

    @NotNull
    @Positive
    private Double area;

    @NotNull
    @Positive
    private Double capacidad;

    private String ubicacion;

    @NotNull
    private EstadoEstanque estado;
}

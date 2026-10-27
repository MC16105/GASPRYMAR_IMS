package com.graspymar.ims.dto;

import com.graspymar.ims.enums.EstadoEstanque;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstanqueRequestDTO {

    @NotBlank(message = "El nombre del estanque es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[\\p{L}0-9 .-]+$", message = "El nombre contiene caracteres inválidos")
    private String nombre;

    @NotBlank(message = "El código del estanque es obligatorio")
    @Size(min = 2, max = 20, message = "El código debe tener entre 2 y 20 caracteres")
    @Pattern(regexp = "^[A-Za-z0-9_-]+$", message = "El código solo puede contener letras, números, guiones y guiones bajos")
    private String codigo;

    @NotNull(message = "El área del estanque es obligatoria")
    @Positive(message = "El área debe ser mayor a cero")
    private Double area;

    @NotNull(message = "La capacidad del estanque es obligatoria")
    @Positive(message = "La capacidad debe ser mayor a cero")
    private Double capacidad;

    @Size(max = 150, message = "La ubicación no puede superar los 150 caracteres")
    private String ubicacion;

    @NotNull(message = "El estado del estanque es obligatorio")
    private EstadoEstanque estado;
}

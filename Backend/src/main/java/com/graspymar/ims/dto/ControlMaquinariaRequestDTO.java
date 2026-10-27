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
public class ControlMaquinariaRequestDTO {

    @NotNull(message = "La maquinaria es obligatoria")
    @Positive(message = "La maquinaria seleccionada no es válida")
    private Long maquinariaId;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fecha;

    @NotBlank(message = "Las horas de uso son obligatorias")
    @Size(max = 20, message = "Las horas de uso no pueden superar los 20 caracteres")
    private String horasUso;

    @NotBlank(message = "El operador es obligatorio")
    @Size(min = 2, max = 100, message = "El operador debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El nombre del operador contiene caracteres inválidos")
    private String operador;

    @NotEmpty(message = "El control debe contener al menos un insumo")
    private List<@NotNull(message = "El detalle no puede ser nulo") @Valid DetalleControlMaquinariaRequestDTO> detalles;
}

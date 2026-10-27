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
public class CompraRequestDTO {

    @NotNull(message = "El proveedor es obligatorio")
    @Positive(message = "El proveedor seleccionado no es válido")
    private Long proveedorId;

    @NotNull(message = "La fecha es obligatoria")
    @PastOrPresent(message = "La fecha no puede ser futura")
    private LocalDate fecha;

    @Size(max = 50, message = "El documento fiscal no puede superar los 50 caracteres")
    private String documentoFiscal;

    @Size(max = 300, message = "Las observaciones no pueden superar los 300 caracteres")
    private String observaciones;


    @NotEmpty(message = "La compra debe contener al menos un insumo")
    @Valid
    private List<DetalleCompraRequestDTO> detalles;
}

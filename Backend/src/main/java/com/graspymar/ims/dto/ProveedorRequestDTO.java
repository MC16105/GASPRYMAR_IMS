package com.graspymar.ims.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProveedorRequestDTO {

    @NotBlank(message = "El Nombre es Obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[\\p{L}0-9 -]+$",message = "El nombre contiene caracteres inválidos")
    private String nombreRazonSocial;

    @NotBlank(message ="El DUI o NIT es Obligatorio")
    @Pattern(
            regexp = "^(?:[0-9]{9}|[0-9]{14})$",
            message = "Debe contener exactamente 9 dígitos (DUI) o 14 dígitos (NIT)"
    )
    @Size(min =9, max = 14)
    private String duiNit;

    @Pattern(regexp = "^[0-9]{7}$", message = "El campo debe tener exactamente 7 números")
    private String nrc;

    @NotBlank(message ="La Direccion es Obligatoria")
    @Size(max = 100)
    private String direccion;

    @NotBlank(message ="Ingresa al menos un Numero Telefonico")
    @Pattern(regexp = "^[0-9]{8}$", message = "El campo debe tener exactamente 8 números")
    private String telefono;

    @Email(message = "Correo inválido")
    @Size(max = 50)
    private String correo;
}

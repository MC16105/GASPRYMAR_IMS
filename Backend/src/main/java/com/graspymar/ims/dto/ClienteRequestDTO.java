package com.graspymar.ims.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClienteRequestDTO {

    @NotBlank(message = "El Nombre es Obligatorio")
    @Size(max = 150)
    private String nombreRazonSocial;

    @NotBlank(message ="El DUI o NIT es Obligatorio")
    @Size(max = 20)
    private String duiNit;

    @Size(max = 20)
    private String nrc;

    @Size(max = 250)
    private String direccion;

    @Size(max = 20)
    private String telefono;

    @Email(message = "Correo inválido")
    @Size(max = 100)
    private String correo;

}

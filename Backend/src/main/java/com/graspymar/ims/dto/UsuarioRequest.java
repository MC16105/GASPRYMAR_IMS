package com.graspymar.ims.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequest {

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 4, max = 30, message = "El nombre de usuario debe tener entre 4 y 30 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "El nombre de usuario solo puede contener letras, números, puntos, guiones y guiones bajos")
    private String username;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
    private String password;

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 3, max = 100, message = "El nombre completo debe tener entre 3 y 100 caracteres")
    @Pattern(regexp = "^[\\p{L} .'-]+$", message = "El nombre completo contiene caracteres inválidos")
    private String nombreCompleto;

    @NotBlank(message = "El correo electrónico es obligatorio")
    @Email(message = "El correo electrónico no tiene un formato válido")
    @Size(max = 100, message = "El correo electrónico no puede superar los 100 caracteres")
    private String correo;

    @NotNull(message = "El rol es obligatorio")
    @Positive(message = "El rol seleccionado no es válido")
    private Long rolId;

}

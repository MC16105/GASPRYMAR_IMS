package com.graspymar.ims.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioRequest {

    private String username;
    private String password;
    private String nombreCompleto;
    private String correo;
    private Long rolId;

}

package com.graspymar.ims.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsuarioResponse {
    
    private Long id;
    private String username;
    private String nombreCompleto;
    private String correo;
    private String rol;

}

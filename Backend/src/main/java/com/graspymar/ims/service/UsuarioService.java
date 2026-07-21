package com.graspymar.ims.service;

import com.graspymar.ims.dto.UsuarioRequest;
import com.graspymar.ims.dto.UsuarioResponse;
import java.util.List;

public interface UsuarioService {

    UsuarioResponse crearUsuario(UsuarioRequest request);

    List<UsuarioResponse> listarUsuarios();

    UsuarioResponse obtenerUsuario(Long id);

    UsuarioResponse actualizarUsuario(Long id, UsuarioRequest request);
 
    void eliminarUsuario(Long id);
}

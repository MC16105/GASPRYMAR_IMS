package com.graspymar.ims.service;

import com.graspymar.ims.dto.ClienteRequestDTO;
import com.graspymar.ims.dto.ClienteResponseDTO;
import java.util.List;

public interface ClienteService {

    ClienteResponseDTO crear(ClienteRequestDTO dto);

    List<ClienteResponseDTO> listar();

    ClienteResponseDTO buscarPorId(Long id);

    ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto);

    void eliminar(Long id);

}

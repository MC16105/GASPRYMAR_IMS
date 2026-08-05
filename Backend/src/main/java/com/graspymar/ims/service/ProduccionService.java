package com.graspymar.ims.service;

import com.graspymar.ims.dto.ProduccionRequestDTO;
import com.graspymar.ims.dto.ProduccionResponseDTO;

import java.util.List;

public interface ProduccionService {

    ProduccionResponseDTO crear(ProduccionRequestDTO dto);

    List<ProduccionResponseDTO> listar();

    ProduccionResponseDTO buscarPorId(Long id);

    ProduccionResponseDTO actualizar(Long id, ProduccionRequestDTO dto);

    void eliminar(Long id);

}

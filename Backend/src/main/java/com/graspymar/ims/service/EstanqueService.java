package com.graspymar.ims.service;

import com.graspymar.ims.dto.EstanqueRequestDTO;
import com.graspymar.ims.dto.EstanqueResponseDTO;

import java.util.List;

public interface EstanqueService {

    EstanqueResponseDTO crear(EstanqueRequestDTO dto);

    List<EstanqueResponseDTO> listar();

    EstanqueResponseDTO buscarPorId(Long id);

    EstanqueResponseDTO actualizar(Long id, EstanqueRequestDTO dto);

    void eliminar(Long id);

}

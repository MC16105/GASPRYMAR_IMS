package com.graspymar.ims.service;

import com.graspymar.ims.dto.ControlEstanqueRequestDTO;
import com.graspymar.ims.dto.ControlEstanqueResponseDTO;

import java.util.List;

public interface ControlEstanqueService {

    ControlEstanqueResponseDTO crear(ControlEstanqueRequestDTO dto);

    List<ControlEstanqueResponseDTO> listar();

    ControlEstanqueResponseDTO buscarPorId(Long id);

    ControlEstanqueResponseDTO actualizar(Long id, ControlEstanqueRequestDTO dto);

    void eliminar(Long id);

}

package com.graspymar.ims.service;

import com.graspymar.ims.dto.ControlMaquinariaRequestDTO;
import com.graspymar.ims.dto.ControlMaquinariaResponseDTO;
import java.util.List;

public interface ControlMaquinariaService {

    ControlMaquinariaResponseDTO crear(ControlMaquinariaRequestDTO dto);

    List<ControlMaquinariaResponseDTO> listar();

    ControlMaquinariaResponseDTO buscarPorId(Long id);

    ControlMaquinariaResponseDTO actualizar(Long id, ControlMaquinariaRequestDTO dto);

    void eliminar(Long id);
}

package com.graspymar.ims.service;

import com.graspymar.ims.dto.MaquinariaRequestDTO;
import com.graspymar.ims.dto.MaquinariaResponseDTO;

import java.util.List;

public interface MaquinariaService {

    MaquinariaResponseDTO crear(MaquinariaRequestDTO dto);

    List<MaquinariaResponseDTO> listar();

    MaquinariaResponseDTO buscarPorId(Long id);

    MaquinariaResponseDTO actualizar(Long id, MaquinariaRequestDTO dto);

    void eliminar(Long id);
}

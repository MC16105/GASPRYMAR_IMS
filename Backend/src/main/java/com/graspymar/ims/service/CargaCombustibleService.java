package com.graspymar.ims.service;

import com.graspymar.ims.dto.CargaCombustibleRequestDTO;
import com.graspymar.ims.dto.CargaCombustibleResponseDTO;

import java.util.List;

public interface CargaCombustibleService {

    CargaCombustibleResponseDTO crear(CargaCombustibleRequestDTO dto);

    List<CargaCombustibleResponseDTO> listar();

    CargaCombustibleResponseDTO buscarPorId(Long id);

    CargaCombustibleResponseDTO actualizar(Long id, CargaCombustibleRequestDTO dto);

    void eliminar(Long id);
}

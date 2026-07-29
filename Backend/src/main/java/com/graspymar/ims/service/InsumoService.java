package com.graspymar.ims.service;

import com.graspymar.ims.dto.InsumoRequestDTO;
import com.graspymar.ims.dto.InsumoResponseDTO;
import com.graspymar.ims.entity.Insumo;

import java.util.List;

public interface InsumoService {

    InsumoResponseDTO crear(InsumoRequestDTO dto);

    List<InsumoResponseDTO> listar();

    InsumoResponseDTO buscarPorId(Long id);

    InsumoResponseDTO actualizar(Long id, InsumoRequestDTO dto);

    void eliminar(Long id);
}

package com.graspymar.ims.service;

import com.graspymar.ims.dto.CompraRequestDTO;
import com.graspymar.ims.dto.CompraResponseDTO;

import java.util.List;

public interface CompraService {

    CompraResponseDTO crear(CompraRequestDTO dto);

    List<CompraResponseDTO> listar();

    CompraResponseDTO buscarPorId(Long id);

    CompraResponseDTO actualizar(Long id, CompraRequestDTO dto);

    void eliminar(Long id);
}

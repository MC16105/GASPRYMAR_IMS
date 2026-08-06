package com.graspymar.ims.service;

import com.graspymar.ims.dto.VehiculoRequestDTO;
import com.graspymar.ims.dto.VehiculoResponseDTO;

import java.util.List;

public interface VehiculoService {

    VehiculoResponseDTO crear(VehiculoRequestDTO vehiculo);

    List<VehiculoResponseDTO> listar();

    VehiculoResponseDTO buscarPorId(Long id);

    VehiculoResponseDTO actualizar(Long id, VehiculoRequestDTO dto);

    void eliminar(Long id);
}

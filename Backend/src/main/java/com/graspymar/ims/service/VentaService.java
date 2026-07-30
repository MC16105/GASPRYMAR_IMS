package com.graspymar.ims.service;

import com.graspymar.ims.dto.VentaRequestDTO;
import com.graspymar.ims.dto.VentaResponseDTO;
import java.util.List;

public interface VentaService {

    VentaResponseDTO crear(VentaRequestDTO dto);

    List<VentaResponseDTO> listar();

    VentaResponseDTO buscarPorId(Long id);

    VentaResponseDTO actualizar(Long id, VentaRequestDTO dto);

    void eliminar(Long id);
}

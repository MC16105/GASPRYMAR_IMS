package com.graspymar.ims.service;

import com.graspymar.ims.dto.ProveedorRequestDTO;
import com.graspymar.ims.dto.ProveedorResponseDTO;
import java.util.List;

public interface ProveedorService {

    ProveedorResponseDTO crear(ProveedorRequestDTO proveedor);

    List<ProveedorResponseDTO> listar();

    ProveedorResponseDTO buscarPorId(Long id);

    ProveedorResponseDTO actualizar(Long id, ProveedorRequestDTO dto);

    void eliminar(Long id);
}

package com.graspymar.ims.service;

import com.graspymar.ims.dto.ProductoRequestDTO;
import com.graspymar.ims.dto.ProductoResponseDTO;

import java.util.List;

public interface ProductoService {

    ProductoResponseDTO crear(ProductoRequestDTO dto);

    List<ProductoResponseDTO> listar();

    ProductoResponseDTO buscarPorId(Long id);

    ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto);

    void eliminar(Long id);
}

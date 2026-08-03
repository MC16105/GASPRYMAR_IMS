package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.ProductoRequestDTO;
import com.graspymar.ims.dto.ProductoResponseDTO;
import com.graspymar.ims.entity.Producto;
import com.graspymar.ims.repository.ProductoRepository;
import com.graspymar.ims.service.ProductoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    @Override
    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        productoRepository.findByNombre(dto.getNombre())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Ya existe un Productoo con ese Nombre.");
                });
        Producto producto = Producto.builder()
                .nombre(dto.getNombre())
                .categoria(dto.getCategoria())
                .unidadMedida(dto.getUnidadMedida())
                .stockActual(dto.getStockActual())
                .precioVenta(dto.getPrecioVenta())
                .build();
        producto = productoRepository.save(producto);
        return toResponse(producto);
    }

    @Override
    public List<ProductoResponseDTO> listar() {
        return productoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProductoResponseDTO buscarPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado."));
        return toResponse(producto);
    }

    @Override
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado."));
        producto.setNombre(dto.getNombre());
        producto.setCategoria(dto.getCategoria());
        producto.setUnidadMedida(dto.getUnidadMedida());
        producto.setStockActual(dto.getStockActual());
        producto.setPrecioVenta(dto.getPrecioVenta());
        producto = productoRepository.save(producto);
        return toResponse(producto);
    }

    @Override
    public void eliminar(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Producto no encontrado."));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    private ProductoResponseDTO toResponse(Producto producto) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .categoria(producto.getCategoria())
                .unidadMedida(producto.getUnidadMedida())
                .stockActual(producto.getStockActual())
                .precioVenta(producto.getPrecioVenta())
                .build();
    }
}

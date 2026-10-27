package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.ProductoRequestDTO;
import com.graspymar.ims.dto.ProductoResponseDTO;
import com.graspymar.ims.entity.Producto;
import com.graspymar.ims.repository.ProductoRepository;
import com.graspymar.ims.service.ProductoService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    // =====================================================
    // CREAR PRODUCTO
    // =====================================================

    @Override
    @Transactional
    public ProductoResponseDTO crear(ProductoRequestDTO dto) {

        // Verificar que el nombre no esté registrado
        productoRepository.findByNombre(dto.getNombre())
                .ifPresent(existente -> {
                    throw new IllegalArgumentException(
                            "Ya existe un producto con ese nombre."
                    );
                });

        // Validar stock inicial
        if (dto.getStockActual() == null ||
                dto.getStockActual().compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "El stock inicial no puede ser negativo."
            );
        }

        // Validar precio de venta
        if (dto.getPrecioVenta() == null ||
                dto.getPrecioVenta().compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "El precio de venta no puede ser negativo."
            );
        }

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

    // =====================================================
    // LISTAR PRODUCTOS ACTIVOS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listar() {

        return productoRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // BUSCAR PRODUCTO POR ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO buscarPorId(Long id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Producto no encontrado."
                        )
                );

        // Impedir consultar productos inactivos
        if (!Boolean.TRUE.equals(producto.getActivo())) {
            throw new EntityNotFoundException(
                    "El producto no se encuentra activo."
            );
        }

        return toResponse(producto);
    }

    // =====================================================
    // ACTUALIZAR PRODUCTO
    // =====================================================

    @Override
    @Transactional
    public ProductoResponseDTO actualizar(
            Long id,
            ProductoRequestDTO dto
    ) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Producto no encontrado."
                        )
                );

        // Impedir modificar productos inactivos
        if (!Boolean.TRUE.equals(producto.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede actualizar un producto inactivo."
            );
        }

        // Verificar que el nombre no pertenezca a otro producto
        productoRepository.findByNombre(dto.getNombre())
                .ifPresent(existente -> {

                    if (!existente.getId().equals(id)) {
                        throw new IllegalArgumentException(
                                "Ya existe otro producto con ese nombre."
                        );
                    }
                });

        // Validar stock
        if (dto.getStockActual() == null ||
                dto.getStockActual().compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "El stock no puede ser negativo."
            );
        }

        // Validar precio
        if (dto.getPrecioVenta() == null ||
                dto.getPrecioVenta().compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "El precio de venta no puede ser negativo."
            );
        }

        producto.setNombre(dto.getNombre());
        producto.setCategoria(dto.getCategoria());
        producto.setUnidadMedida(dto.getUnidadMedida());

        // Se conserva la actualización manual del stock
        producto.setStockActual(dto.getStockActual());

        producto.setPrecioVenta(dto.getPrecioVenta());

        producto = productoRepository.save(producto);

        return toResponse(producto);
    }

    // =====================================================
    // ELIMINAR PRODUCTO (ELIMINACIÓN LÓGICA)
    // =====================================================

    @Override
    @Transactional
    public void eliminar(Long id) {

        Producto producto = productoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Producto no encontrado."
                        )
                );

        // Evitar eliminar nuevamente un producto inactivo
        if (!Boolean.TRUE.equals(producto.getActivo())) {
            throw new IllegalArgumentException(
                    "El producto ya se encuentra inactivo."
            );
        }

        producto.setActivo(false);

        productoRepository.save(producto);
    }

    // =====================================================
    // CONVERTIR ENTIDAD A RESPONSE DTO
    // =====================================================

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
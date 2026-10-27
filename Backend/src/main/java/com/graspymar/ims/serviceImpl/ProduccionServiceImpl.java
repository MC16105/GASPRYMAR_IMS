package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.ProduccionRequestDTO;
import com.graspymar.ims.dto.ProduccionResponseDTO;
import com.graspymar.ims.entity.Produccion;
import com.graspymar.ims.entity.Producto;
import com.graspymar.ims.repository.ProduccionRepository;
import com.graspymar.ims.repository.ProductoRepository;
import com.graspymar.ims.service.ProduccionService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProduccionServiceImpl implements ProduccionService {

    private final ProduccionRepository produccionRepository;
    private final ProductoRepository productoRepository;

    // =====================================================
    // CREAR PRODUCCIÓN
    // =====================================================

    @Override
    @Transactional
    public ProduccionResponseDTO crear(ProduccionRequestDTO dto) {

        // Validar cantidad
        validarCantidad(dto.getCantidad());

        Producto producto = productoRepository.findById(dto.getProductoId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Producto no encontrado."
                        )
                );

        // Impedir producción de productos inactivos
        validarProductoActivo(producto);

        Produccion produccion = Produccion.builder()
                .producto(producto)
                .tipoMovimiento(dto.getTipoMovimiento())
                .fecha(dto.getFecha())
                .cantidad(dto.getCantidad())
                .unidadMedida(dto.getUnidadMedida())
                .observaciones(dto.getObservaciones())
                .build();

        // Incrementar existencias
        producto.setStockActual(
                producto.getStockActual().add(dto.getCantidad())
        );

        productoRepository.save(producto);

        Produccion produccionGuardada =
                produccionRepository.save(produccion);

        return toResponse(produccionGuardada);
    }

    // =====================================================
    // LISTAR PRODUCCIONES ACTIVAS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ProduccionResponseDTO> listar() {

        return produccionRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // BUSCAR PRODUCCIÓN POR ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public ProduccionResponseDTO buscarPorId(Long id) {

        Produccion produccion = produccionRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Producción no encontrada."
                        )
                );

        validarProduccionActiva(produccion);

        return toResponse(produccion);
    }

    // =====================================================
    // ACTUALIZAR PRODUCCIÓN
    // =====================================================

    @Override
    @Transactional
    public ProduccionResponseDTO actualizar(
            Long id,
            ProduccionRequestDTO dto
    ) {

        // Validar nueva cantidad
        validarCantidad(dto.getCantidad());

        Produccion produccion = produccionRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Producción no encontrada."
                        )
                );

        // Impedir modificar producciones inactivas
        validarProduccionActiva(produccion);

        Producto productoAnterior = produccion.getProducto();

        // Impedir modificar movimientos de productos inactivos
        validarProductoActivo(productoAnterior);

        Producto nuevoProducto = productoRepository.findById(
                dto.getProductoId()
        ).orElseThrow(() ->
                new EntityNotFoundException(
                        "Producto no encontrado."
                )
        );

        validarProductoActivo(nuevoProducto);

        BigDecimal cantidadAnterior = produccion.getCantidad();
        BigDecimal cantidadNueva = dto.getCantidad();

        // Verificar si se mantiene el mismo producto
        if (productoAnterior.getId().equals(nuevoProducto.getId())) {

            // Calcular el stock final sin modificarlo todavía
            BigDecimal stockFinal = productoAnterior.getStockActual()
                    .subtract(cantidadAnterior)
                    .add(cantidadNueva);

            // Evitar existencias negativas
            if (stockFinal.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException(
                        "No se puede actualizar la producción: " +
                                "el stock resultante sería negativo."
                );
            }

            productoAnterior.setStockActual(stockFinal);

            productoRepository.save(productoAnterior);

        } else {

            // Calcular stock del producto anterior
            BigDecimal stockAnteriorResultante =
                    productoAnterior.getStockActual()
                            .subtract(cantidadAnterior);

            // Impedir retirar más existencias de las disponibles
            if (stockAnteriorResultante.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException(
                        "No se puede cambiar el producto de la producción: " +
                                "el producto anterior no tiene existencias " +
                                "suficientes para revertir el movimiento."
                );
            }

            // Revertir producción del producto anterior
            productoAnterior.setStockActual(
                    stockAnteriorResultante
            );

            // Aplicar producción al nuevo producto
            nuevoProducto.setStockActual(
                    nuevoProducto.getStockActual()
                            .add(cantidadNueva)
            );

            productoRepository.save(productoAnterior);
            productoRepository.save(nuevoProducto);
        }

        // Actualizar datos de la producción
        produccion.setProducto(nuevoProducto);
        produccion.setTipoMovimiento(dto.getTipoMovimiento());
        produccion.setFecha(dto.getFecha());
        produccion.setCantidad(cantidadNueva);
        produccion.setUnidadMedida(dto.getUnidadMedida());
        produccion.setObservaciones(dto.getObservaciones());

        Produccion actualizada =
                produccionRepository.save(produccion);

        return toResponse(actualizada);
    }

    // =====================================================
    // ELIMINAR PRODUCCIÓN (ELIMINACIÓN LÓGICA)
    // =====================================================

    @Override
    @Transactional
    public void eliminar(Long id) {

        Produccion produccion = produccionRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Producción no encontrada."
                        )
                );

        // Impedir eliminar dos veces la misma producción
        validarProduccionActiva(produccion);

        Producto producto = produccion.getProducto();

        // Comprobar que el producto esté activo
        validarProductoActivo(producto);

        // Calcular existencias después de revertir la producción
        BigDecimal stockResultante = producto.getStockActual()
                .subtract(produccion.getCantidad());

        // Impedir stock negativo
        if (stockResultante.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "No se puede eliminar la producción: " +
                            "las existencias actuales son insuficientes " +
                            "para revertir el movimiento."
            );
        }

        // Revertir existencias
        producto.setStockActual(stockResultante);

        productoRepository.save(producto);

        // Eliminación lógica
        produccion.setActivo(false);

        produccionRepository.save(produccion);
    }

    // =====================================================
    // VALIDAR PRODUCTO ACTIVO
    // =====================================================

    private void validarProductoActivo(Producto producto) {

        if (!Boolean.TRUE.equals(producto.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede realizar la operación: " +
                            "el producto se encuentra inactivo."
            );
        }
    }

    // =====================================================
    // VALIDAR PRODUCCIÓN ACTIVA
    // =====================================================

    private void validarProduccionActiva(Produccion produccion) {

        if (!Boolean.TRUE.equals(produccion.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede realizar la operación: " +
                            "la producción se encuentra inactiva."
            );
        }
    }

    // =====================================================
    // VALIDAR CANTIDAD
    // =====================================================

    private void validarCantidad(BigDecimal cantidad) {

        if (cantidad == null ||
                cantidad.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "La cantidad de producción debe ser mayor que cero."
            );
        }
    }

    // =====================================================
    // CONVERTIR ENTIDAD A RESPONSE DTO
    // =====================================================

    private ProduccionResponseDTO toResponse(Produccion produccion) {

        return ProduccionResponseDTO.builder()
                .id(produccion.getId())
                .productoId(produccion.getProducto().getId())
                .producto(produccion.getProducto().getNombre())
                .tipoMovimiento(produccion.getTipoMovimiento())
                .fecha(produccion.getFecha())
                .cantidad(produccion.getCantidad())
                .unidadMedida(produccion.getUnidadMedida())
                .observaciones(produccion.getObservaciones())
                .build();
    }
}

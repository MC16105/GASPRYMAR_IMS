package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.InsumoRequestDTO;
import com.graspymar.ims.dto.InsumoResponseDTO;
import com.graspymar.ims.entity.Insumo;
import com.graspymar.ims.repository.InsumoRepository;
import com.graspymar.ims.service.InsumoService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InsumoServiceImpl implements InsumoService {

    private final InsumoRepository insumoRepository;

    // =====================================================
    // CREAR INSUMO
    // =====================================================

    @Override
    @Transactional
    public InsumoResponseDTO crear(InsumoRequestDTO dto) {

        // Verificar que el nombre no esté registrado
        insumoRepository.findByNombre(dto.getNombre())
                .ifPresent(existente -> {
                    throw new IllegalArgumentException(
                            "Ya existe un insumo con ese nombre."
                    );
                });

        // Validar cantidades y precio
        validarValores(dto);

        Insumo insumo = Insumo.builder()
                .nombre(dto.getNombre())
                .unidadMedida(dto.getUnidadMedida())
                .stockActual(dto.getStockActual())
                .stockMinimo(dto.getStockMinimo())
                .precioReferencia(dto.getPrecioReferencia())
                .build();

        insumo = insumoRepository.save(insumo);

        return toResponse(insumo);
    }

    // =====================================================
    // LISTAR INSUMOS ACTIVOS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<InsumoResponseDTO> listar() {

        return insumoRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // BUSCAR INSUMO POR ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public InsumoResponseDTO buscarPorId(Long id) {

        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Insumo no encontrado."
                        )
                );

        // Impedir consultar insumos inactivos
        validarInsumoActivo(insumo);

        return toResponse(insumo);
    }

    // =====================================================
    // ACTUALIZAR INSUMO
    // =====================================================

    @Override
    @Transactional
    public InsumoResponseDTO actualizar(
            Long id,
            InsumoRequestDTO dto
    ) {

        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Insumo no encontrado."
                        )
                );

        // Impedir modificar insumos inactivos
        validarInsumoActivo(insumo);

        // Verificar que el nombre no pertenezca a otro insumo
        insumoRepository.findByNombre(dto.getNombre())
                .ifPresent(existente -> {

                    if (!existente.getId().equals(id)) {
                        throw new IllegalArgumentException(
                                "Ya existe otro insumo con ese nombre."
                        );
                    }
                });

        // Validar cantidades y precio
        validarValores(dto);

        insumo.setNombre(dto.getNombre());
        insumo.setUnidadMedida(dto.getUnidadMedida());

        // Se conserva la actualización manual del stock
        insumo.setStockActual(dto.getStockActual());

        insumo.setStockMinimo(dto.getStockMinimo());
        insumo.setPrecioReferencia(dto.getPrecioReferencia());

        insumo = insumoRepository.save(insumo);

        return toResponse(insumo);
    }

    // =====================================================
    // ELIMINAR INSUMO (ELIMINACIÓN LÓGICA)
    // =====================================================

    @Override
    @Transactional
    public void eliminar(Long id) {

        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Insumo no encontrado."
                        )
                );

        // Impedir eliminar nuevamente un insumo inactivo
        if (!Boolean.TRUE.equals(insumo.getActivo())) {
            throw new IllegalArgumentException(
                    "El insumo ya se encuentra inactivo."
            );
        }

        // Eliminación lógica
        insumo.setActivo(false);

        insumoRepository.save(insumo);
    }

    // =====================================================
    // VALIDAR INSUMO ACTIVO
    // =====================================================

    private void validarInsumoActivo(Insumo insumo) {

        if (!Boolean.TRUE.equals(insumo.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede realizar la operación: " +
                            "el insumo se encuentra inactivo."
            );
        }
    }

    // =====================================================
    // VALIDAR STOCK Y PRECIO
    // =====================================================

    private void validarValores(InsumoRequestDTO dto) {

        // Validar stock actual
        if (dto.getStockActual() == null ||
                dto.getStockActual().compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "El stock actual no puede ser negativo."
            );
        }

        // Validar stock mínimo
        if (dto.getStockMinimo() == null ||
                dto.getStockMinimo().compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "El stock mínimo no puede ser negativo."
            );
        }

        // Validar precio de referencia
        if (dto.getPrecioReferencia() == null ||
                dto.getPrecioReferencia().compareTo(BigDecimal.ZERO) < 0) {

            throw new IllegalArgumentException(
                    "El precio de referencia no puede ser negativo."
            );
        }
    }

    // =====================================================
    // CONVERTIR ENTIDAD A RESPONSE DTO
    // =====================================================

    private InsumoResponseDTO toResponse(Insumo insumo) {

        return InsumoResponseDTO.builder()
                .id(insumo.getId())
                .nombre(insumo.getNombre())
                .unidadMedida(insumo.getUnidadMedida())
                .stockActual(insumo.getStockActual())
                .stockMinimo(insumo.getStockMinimo())
                .precioReferencia(insumo.getPrecioReferencia())
                .build();
    }
}
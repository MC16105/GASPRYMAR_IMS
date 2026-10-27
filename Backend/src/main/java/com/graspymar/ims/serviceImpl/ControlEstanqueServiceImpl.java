package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.ControlEstanqueRequestDTO;
import com.graspymar.ims.dto.ControlEstanqueResponseDTO;
import com.graspymar.ims.dto.DetalleControlEstanqueRequestDTO;
import com.graspymar.ims.dto.DetalleControlEstanqueResponseDTO;

import com.graspymar.ims.entity.ControlEstanque;
import com.graspymar.ims.entity.DetalleControlEstanque;
import com.graspymar.ims.entity.Estanque;
import com.graspymar.ims.entity.Insumo;

import com.graspymar.ims.repository.ControlEstanqueRepository;
import com.graspymar.ims.repository.EstanqueRepository;
import com.graspymar.ims.repository.InsumoRepository;

import com.graspymar.ims.service.ControlEstanqueService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ControlEstanqueServiceImpl implements ControlEstanqueService {

    private final ControlEstanqueRepository controlEstanqueRepository;
    private final EstanqueRepository estanqueRepository;
    private final InsumoRepository insumoRepository;

    // =====================================================
    // CREAR CONTROL DE ESTANQUE
    // =====================================================

    @Override
    @Transactional
    public ControlEstanqueResponseDTO crear(ControlEstanqueRequestDTO dto) {

        // Validar que existan detalles
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe registrar al menos un detalle."
            );
        }

        // Buscar estanque
        Estanque estanque = estanqueRepository
                .findById(dto.getEstanqueId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Estanque no encontrado."
                        )
                );

        // Impedir utilizar estanques inactivos
        if (!Boolean.TRUE.equals(estanque.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede registrar un control en un estanque inactivo."
            );
        }

        // Crear control
        ControlEstanque control = ControlEstanque.builder()
                .estanque(estanque)
                .fecha(dto.getFecha())
                .observaciones(dto.getObservaciones())
                .build();

        // =================================================
        // PROCESAR INSUMOS CONSUMIDOS
        // =================================================

        for (DetalleControlEstanqueRequestDTO detalleDTO : dto.getDetalles()) {

            // Validar cantidad antes de convertirla
            if (detalleDTO.getCantidad() == null ||
                    detalleDTO.getCantidad() <= 0) {

                throw new IllegalArgumentException(
                        "La cantidad consumida debe ser mayor que cero."
                );
            }

            // Buscar insumo
            Insumo insumo = insumoRepository
                    .findById(detalleDTO.getInsumoId())
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Insumo no encontrado."
                            )
                    );

            // Impedir utilizar insumos inactivos
            if (!Boolean.TRUE.equals(insumo.getActivo())) {
                throw new IllegalArgumentException(
                        "No se puede utilizar el insumo inactivo: "
                                + insumo.getNombre()
                );
            }

            // Convertir cantidad a BigDecimal
            BigDecimal cantidad = BigDecimal.valueOf(
                    detalleDTO.getCantidad()
            );

            // Validar existencias
            if (insumo.getStockActual().compareTo(cantidad) < 0) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el insumo: "
                                + insumo.getNombre()
                );
            }

            // Descontar existencias
            insumo.setStockActual(
                    insumo.getStockActual().subtract(cantidad)
            );

            insumoRepository.save(insumo);

            // Crear detalle
            DetalleControlEstanque detalle =
                    DetalleControlEstanque.builder()
                            .controlEstanque(control)
                            .insumo(insumo)
                            .cantidad(detalleDTO.getCantidad())
                            .build();

            control.getDetalles().add(detalle);
        }

        // Guardar control y detalles
        ControlEstanque guardado =
                controlEstanqueRepository.save(control);

        return toResponse(guardado);
    }

    // =====================================================
    // LISTAR CONTROLES ACTIVOS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ControlEstanqueResponseDTO> listar() {

        return controlEstanqueRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // BUSCAR CONTROL POR ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public ControlEstanqueResponseDTO buscarPorId(Long id) {

        ControlEstanque control = controlEstanqueRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Control de estanque no encontrado."
                        )
                );

        // Impedir consultar controles inactivos
        if (!Boolean.TRUE.equals(control.getActivo())) {
            throw new EntityNotFoundException(
                    "El control de estanque no se encuentra activo."
            );
        }

        return toResponse(control);
    }

    // =====================================================
    // ACTUALIZAR CONTROL
    // =====================================================

    @Override
    @Transactional
    public ControlEstanqueResponseDTO actualizar(
            Long id,
            ControlEstanqueRequestDTO dto
    ) {

        // Se conserva el comportamiento original
        throw new UnsupportedOperationException(
                "Actualizar aún no implementado."
        );
    }

    // =====================================================
    // ELIMINAR CONTROL (ELIMINACIÓN LÓGICA)
    // =====================================================

    @Override
    @Transactional
    public void eliminar(Long id) {

        ControlEstanque control = controlEstanqueRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Control de estanque no encontrado."
                        )
                );

        // Evitar eliminar dos veces el mismo control
        if (!Boolean.TRUE.equals(control.getActivo())) {
            throw new IllegalArgumentException(
                    "El control de estanque ya se encuentra inactivo."
            );
        }

        // Eliminación lógica
        // No se devuelven automáticamente los insumos consumidos
        control.setActivo(false);

        controlEstanqueRepository.save(control);
    }

    // =====================================================
    // CONVERTIR ENTIDAD A RESPONSE DTO
    // =====================================================

    private ControlEstanqueResponseDTO toResponse(ControlEstanque control) {

        List<DetalleControlEstanqueResponseDTO> detalles =
                control.getDetalles()
                        .stream()
                        .map(detalle ->
                                DetalleControlEstanqueResponseDTO.builder()
                                        .id(detalle.getId())
                                        .insumoId(detalle.getInsumo().getId())
                                        .nombreInsumo(detalle.getInsumo().getNombre())
                                        .cantidad(detalle.getCantidad())
                                        .build()
                        )
                        .toList();

        return ControlEstanqueResponseDTO.builder()
                .id(control.getId())
                .estanque(control.getEstanque().getNombre())
                .fecha(control.getFecha())
                .observaciones(control.getObservaciones())
                .detalles(detalles)
                .build();
    }
}
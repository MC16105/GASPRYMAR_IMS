package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.ControlMaquinariaRequestDTO;
import com.graspymar.ims.dto.ControlMaquinariaResponseDTO;
import com.graspymar.ims.dto.DetalleControlMaquinariaRequestDTO;
import com.graspymar.ims.dto.DetalleControlMaquinariaResponseDTO;

import com.graspymar.ims.entity.ControlMaquinaria;
import com.graspymar.ims.entity.DetalleControlMaquinaria;
import com.graspymar.ims.entity.Insumo;
import com.graspymar.ims.entity.Maquinaria;

import com.graspymar.ims.repository.ControlMaquinariaRepository;
import com.graspymar.ims.repository.InsumoRepository;
import com.graspymar.ims.repository.MaquinariaRepository;

import com.graspymar.ims.service.ControlMaquinariaService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ControlMaquinariaServiceImpl implements ControlMaquinariaService {

    private final ControlMaquinariaRepository controlMaquinariaRepository;
    private final MaquinariaRepository maquinariaRepository;
    private final InsumoRepository insumoRepository;

    // =====================================================
    // CREAR CONTROL DE MAQUINARIA
    // =====================================================

    @Override
    @Transactional
    public ControlMaquinariaResponseDTO crear(ControlMaquinariaRequestDTO dto) {

        // Verificar que existan detalles
        if (dto.getDetalles() == null || dto.getDetalles().isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe registrar al menos un detalle."
            );
        }

        Maquinaria maquinaria = maquinariaRepository
                .findById(dto.getMaquinariaId())
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Maquinaria no encontrada."
                        )
                );

        // Impedir utilizar maquinaria inactiva
        if (!Boolean.TRUE.equals(maquinaria.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede registrar un control para maquinaria inactiva."
            );
        }

        // Validar horas de uso
        if (dto.getHorasUso() == null ||
                new BigDecimal(dto.getHorasUso().toString())
                        .compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "Las horas de uso deben ser mayores que cero."
            );
        }

        ControlMaquinaria control = ControlMaquinaria.builder()
                .maquinaria(maquinaria)
                .fecha(dto.getFecha())
                .horasUso(dto.getHorasUso())
                .operador(dto.getOperador())
                .build();

        // =================================================
        // PROCESAR INSUMOS CONSUMIDOS
        // =================================================

        for (DetalleControlMaquinariaRequestDTO detalleDTO : dto.getDetalles()) {

            // Validar cantidad
            if (detalleDTO.getCantidad() == null ||
                    detalleDTO.getCantidad() <= 0) {

                throw new IllegalArgumentException(
                        "La cantidad consumida debe ser mayor que cero."
                );
            }

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

            BigDecimal cantidad = BigDecimal.valueOf(
                    detalleDTO.getCantidad()
            );

            // Verificar existencias suficientes
            if (insumo.getStockActual().compareTo(cantidad) < 0) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para el insumo: "
                                + insumo.getNombre()
                );
            }

            // Descontar insumo del inventario
            insumo.setStockActual(
                    insumo.getStockActual().subtract(cantidad)
            );

            insumoRepository.save(insumo);

            // Crear detalle
            DetalleControlMaquinaria detalle =
                    DetalleControlMaquinaria.builder()
                            .controlMaquinaria(control)
                            .insumo(insumo)
                            .cantidad(detalleDTO.getCantidad())
                            .build();

            control.getDetalles().add(detalle);
        }

        ControlMaquinaria guardado =
                controlMaquinariaRepository.save(control);

        return toResponse(guardado);
    }

    // =====================================================
    // LISTAR CONTROLES ACTIVOS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<ControlMaquinariaResponseDTO> listar() {

        return controlMaquinariaRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // BUSCAR CONTROL POR ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public ControlMaquinariaResponseDTO buscarPorId(Long id) {

        ControlMaquinaria control = controlMaquinariaRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Control de maquinaria no encontrado."
                        )
                );

        // Impedir consultar controles inactivos
        if (!Boolean.TRUE.equals(control.getActivo())) {
            throw new EntityNotFoundException(
                    "El control de maquinaria no se encuentra activo."
            );
        }

        return toResponse(control);
    }

    // =====================================================
    // ACTUALIZAR CONTROL
    // =====================================================

    @Override
    @Transactional
    public ControlMaquinariaResponseDTO actualizar(
            Long id,
            ControlMaquinariaRequestDTO dto
    ) {

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

        ControlMaquinaria control = controlMaquinariaRepository
                .findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Control de maquinaria no encontrado."
                        )
                );

        // Evitar eliminar dos veces el mismo control
        if (!Boolean.TRUE.equals(control.getActivo())) {
            throw new IllegalArgumentException(
                    "El control de maquinaria ya se encuentra inactivo."
            );
        }

        // Eliminación lógica.
        // No se devuelven automáticamente los insumos consumidos.
        control.setActivo(false);

        controlMaquinariaRepository.save(control);
    }

    // =====================================================
    // CONVERTIR ENTIDAD A RESPONSE DTO
    // =====================================================

    private ControlMaquinariaResponseDTO toResponse(
            ControlMaquinaria control
    ) {

        List<DetalleControlMaquinariaResponseDTO> detalles =
                control.getDetalles()
                        .stream()
                        .map(detalle ->
                                DetalleControlMaquinariaResponseDTO.builder()
                                        .id(detalle.getId())
                                        .insumoId(detalle.getInsumo().getId())
                                        .nombreInsumo(detalle.getInsumo().getNombre())
                                        .cantidad(detalle.getCantidad())
                                        .build()
                        )
                        .toList();

        return ControlMaquinariaResponseDTO.builder()
                .id(control.getId())
                .maquinaria(control.getMaquinaria().getNombre())
                .fecha(control.getFecha())
                .horasUso(control.getHorasUso())
                .operador(control.getOperador())
                .detalles(detalles)
                .build();
    }
}
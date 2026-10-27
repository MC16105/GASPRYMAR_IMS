package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.EstanqueRequestDTO;
import com.graspymar.ims.dto.EstanqueResponseDTO;
import com.graspymar.ims.entity.Estanque;
import com.graspymar.ims.repository.EstanqueRepository;
import com.graspymar.ims.service.EstanqueService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstanqueServiceImpl implements EstanqueService {

    private final EstanqueRepository estanqueRepository;

    // =====================================================
    // CREAR ESTANQUE
    // =====================================================

    @Override
    @Transactional
    public EstanqueResponseDTO crear(EstanqueRequestDTO dto) {

        // Verificar que el código no esté registrado
        estanqueRepository.findByCodigo(dto.getCodigo())
                .ifPresent(existente -> {
                    throw new IllegalArgumentException(
                            "Ya existe un estanque con el código: "
                                    + dto.getCodigo()
                    );
                });

        Estanque estanque = Estanque.builder()
                .nombre(dto.getNombre())
                .codigo(dto.getCodigo())
                .area(dto.getArea())
                .capacidad(dto.getCapacidad())
                .ubicacion(dto.getUbicacion())
                .estado(dto.getEstado())
                .build();

        estanque = estanqueRepository.save(estanque);

        return toResponse(estanque);
    }

    // =====================================================
    // LISTAR ESTANQUES ACTIVOS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<EstanqueResponseDTO> listar() {

        return estanqueRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // BUSCAR ESTANQUE POR ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public EstanqueResponseDTO buscarPorId(Long id) {

        Estanque estanque = estanqueRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Estanque no encontrado."
                        )
                );

        // Impedir consultar estanques inactivos
        if (!Boolean.TRUE.equals(estanque.getActivo())) {
            throw new EntityNotFoundException(
                    "El estanque no se encuentra activo."
            );
        }

        return toResponse(estanque);
    }

    // =====================================================
    // ACTUALIZAR ESTANQUE
    // =====================================================

    @Override
    @Transactional
    public EstanqueResponseDTO actualizar(
            Long id,
            EstanqueRequestDTO dto
    ) {

        Estanque estanque = estanqueRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Estanque no encontrado."
                        )
                );

        // Impedir modificar estanques inactivos
        if (!Boolean.TRUE.equals(estanque.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede actualizar un estanque inactivo."
            );
        }

        // Verificar que el código no pertenezca a otro estanque
        estanqueRepository.findByCodigo(dto.getCodigo())
                .ifPresent(existente -> {

                    if (!existente.getId().equals(id)) {
                        throw new IllegalArgumentException(
                                "El código '" + dto.getCodigo()
                                        + "' ya pertenece a otro estanque."
                        );
                    }
                });

        estanque.setNombre(dto.getNombre());
        estanque.setCodigo(dto.getCodigo());
        estanque.setArea(dto.getArea());
        estanque.setCapacidad(dto.getCapacidad());
        estanque.setUbicacion(dto.getUbicacion());
        estanque.setEstado(dto.getEstado());

        estanque = estanqueRepository.save(estanque);

        return toResponse(estanque);
    }

    // =====================================================
    // ELIMINAR ESTANQUE (ELIMINACIÓN LÓGICA)
    // =====================================================

    @Override
    @Transactional
    public void eliminar(Long id) {

        Estanque estanque = estanqueRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Estanque no encontrado."
                        )
                );

        // Evitar eliminar nuevamente un estanque inactivo
        if (!Boolean.TRUE.equals(estanque.getActivo())) {
            throw new IllegalArgumentException(
                    "El estanque ya se encuentra inactivo."
            );
        }

        // Eliminación lógica
        estanque.setActivo(false);

        estanqueRepository.save(estanque);
    }

    // =====================================================
    // CONVERTIR ENTIDAD A RESPONSE DTO
    // =====================================================

    private EstanqueResponseDTO toResponse(Estanque estanque) {

        return EstanqueResponseDTO.builder()
                .id(estanque.getId())
                .nombre(estanque.getNombre())
                .codigo(estanque.getCodigo())
                .area(estanque.getArea())
                .capacidad(estanque.getCapacidad())
                .ubicacion(estanque.getUbicacion())
                .estado(estanque.getEstado())
                .build();
    }
}
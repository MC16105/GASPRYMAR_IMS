package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.MaquinariaRequestDTO;
import com.graspymar.ims.dto.MaquinariaResponseDTO;
import com.graspymar.ims.entity.Maquinaria;
import com.graspymar.ims.repository.MaquinariaRepository;
import com.graspymar.ims.service.MaquinariaService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MaquinariaServiceImpl implements MaquinariaService {

    private final MaquinariaRepository maquinariaRepository;

    // =====================================================
    // CREAR MAQUINARIA
    // =====================================================

    @Override
    @Transactional
    public MaquinariaResponseDTO crear(MaquinariaRequestDTO dto) {

        // Verificar que el código no esté registrado
        maquinariaRepository.findByCodigo(dto.getCodigo())
                .ifPresent(existente -> {
                    throw new IllegalArgumentException(
                            "Ya existe una maquinaria con el código: "
                                    + dto.getCodigo()
                    );
                });

        Maquinaria maquinaria = Maquinaria.builder()
                .nombre(dto.getNombre())
                .codigo(dto.getCodigo())
                .tipo(dto.getTipo())
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .anio(dto.getAnio())
                .estado(dto.getEstado())
                .observaciones(dto.getObservaciones())
                .build();

        maquinaria = maquinariaRepository.save(maquinaria);

        return toResponse(maquinaria);
    }

    // =====================================================
    // LISTAR MAQUINARIAS ACTIVAS
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public List<MaquinariaResponseDTO> listar() {

        return maquinariaRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // BUSCAR MAQUINARIA POR ID
    // =====================================================

    @Override
    @Transactional(readOnly = true)
    public MaquinariaResponseDTO buscarPorId(Long id) {

        Maquinaria maquinaria = maquinariaRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Maquinaria no encontrada."
                        )
                );

        // Impedir consultar maquinaria inactiva
        if (!Boolean.TRUE.equals(maquinaria.getActivo())) {
            throw new EntityNotFoundException(
                    "La maquinaria no se encuentra activa."
            );
        }

        return toResponse(maquinaria);
    }

    // =====================================================
    // ACTUALIZAR MAQUINARIA
    // =====================================================

    @Override
    @Transactional
    public MaquinariaResponseDTO actualizar(
            Long id,
            MaquinariaRequestDTO dto
    ) {

        Maquinaria maquinaria = maquinariaRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Maquinaria no encontrada."
                        )
                );

        // Impedir modificar maquinaria inactiva
        if (!Boolean.TRUE.equals(maquinaria.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede actualizar una maquinaria inactiva."
            );
        }

        // Verificar que el código no pertenezca a otra maquinaria
        maquinariaRepository.findByCodigo(dto.getCodigo())
                .ifPresent(existente -> {

                    if (!existente.getId().equals(id)) {
                        throw new IllegalArgumentException(
                                "El código '" + dto.getCodigo()
                                        + "' ya pertenece a otra maquinaria."
                        );
                    }
                });

        maquinaria.setNombre(dto.getNombre());
        maquinaria.setCodigo(dto.getCodigo());
        maquinaria.setTipo(dto.getTipo());
        maquinaria.setMarca(dto.getMarca());
        maquinaria.setModelo(dto.getModelo());
        maquinaria.setAnio(dto.getAnio());
        maquinaria.setEstado(dto.getEstado());
        maquinaria.setObservaciones(dto.getObservaciones());

        maquinaria = maquinariaRepository.save(maquinaria);

        return toResponse(maquinaria);
    }

    // =====================================================
    // ELIMINAR MAQUINARIA (ELIMINACIÓN LÓGICA)
    // =====================================================

    @Override
    @Transactional
    public void eliminar(Long id) {

        Maquinaria maquinaria = maquinariaRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Maquinaria no encontrada."
                        )
                );

        // Evitar eliminar dos veces la misma maquinaria
        if (!Boolean.TRUE.equals(maquinaria.getActivo())) {
            throw new IllegalArgumentException(
                    "La maquinaria ya se encuentra inactiva."
            );
        }

        maquinaria.setActivo(false);

        maquinariaRepository.save(maquinaria);
    }

    // =====================================================
    // CONVERTIR ENTIDAD A RESPONSE DTO
    // =====================================================

    private MaquinariaResponseDTO toResponse(Maquinaria maquinaria) {

        return MaquinariaResponseDTO.builder()
                .id(maquinaria.getId())
                .nombre(maquinaria.getNombre())
                .codigo(maquinaria.getCodigo())
                .tipo(maquinaria.getTipo())
                .marca(maquinaria.getMarca())
                .modelo(maquinaria.getModelo())
                .anio(maquinaria.getAnio())
                .estado(maquinaria.getEstado())
                .observaciones(maquinaria.getObservaciones())
                .build();
    }
}

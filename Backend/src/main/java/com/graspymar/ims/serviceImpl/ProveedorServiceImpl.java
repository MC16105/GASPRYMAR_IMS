package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.ProveedorRequestDTO;
import com.graspymar.ims.dto.ProveedorResponseDTO;
import com.graspymar.ims.entity.Proveedor;
import com.graspymar.ims.repository.ProveedorRepository;
import com.graspymar.ims.service.ProveedorService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    // =====================================================
    // CREAR PROVEEDOR
    // =====================================================

    @Override
    @Transactional
    public ProveedorResponseDTO crear(ProveedorRequestDTO dto) {

        // Verificar que el DUI/NIT no esté registrado
        proveedorRepository.findByDuiNit(dto.getDuiNit())
                .ifPresent(existente -> {
                    throw new IllegalArgumentException(
                            "Ya existe un proveedor con el DUI/NIT: "
                                    + dto.getDuiNit()
                    );
                });

        Proveedor proveedor = Proveedor.builder()
                .nombreRazonSocial(dto.getNombreRazonSocial())
                .duiNit(dto.getDuiNit())
                .nrc(dto.getNrc())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .correo(dto.getCorreo())
                .build();

        proveedor = proveedorRepository.save(proveedor);

        return toResponse(proveedor);
    }

    // =====================================================
    // LISTAR PROVEEDORES ACTIVOS
    // =====================================================

    @Override
    @Transactional
    public List<ProveedorResponseDTO> listar() {

        return proveedorRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // BUSCAR PROVEEDOR POR ID
    // =====================================================

    @Override
    @Transactional
    public ProveedorResponseDTO buscarPorId(Long id) {

        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Proveedor no encontrado."
                        )
                );

        // Impedir consultas normales de proveedores inactivos
        if (!Boolean.TRUE.equals(proveedor.getActivo())) {
            throw new EntityNotFoundException(
                    "El proveedor no se encuentra activo."
            );
        }

        return toResponse(proveedor);
    }

    // =====================================================
    // ACTUALIZAR PROVEEDOR
    // =====================================================

    @Override
    @Transactional
    public ProveedorResponseDTO actualizar(
            Long id,
            ProveedorRequestDTO dto
    ) {

        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Proveedor no encontrado."
                        )
                );

        // Impedir modificaciones de proveedores inactivos
        if (!Boolean.TRUE.equals(proveedor.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede actualizar un proveedor inactivo."
            );
        }

        // Verificar que el DUI/NIT no pertenezca a otro proveedor
        proveedorRepository.findByDuiNit(dto.getDuiNit())
                .ifPresent(existente -> {

                    if (!existente.getId().equals(id)) {
                        throw new IllegalArgumentException(
                                "El DUI/NIT '" + dto.getDuiNit()
                                        + "' ya pertenece a otro proveedor."
                        );
                    }
                });

        proveedor.setNombreRazonSocial(dto.getNombreRazonSocial());
        proveedor.setDuiNit(dto.getDuiNit());
        proveedor.setNrc(dto.getNrc());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setCorreo(dto.getCorreo());

        proveedor = proveedorRepository.save(proveedor);

        return toResponse(proveedor);
    }

    // =====================================================
    // ELIMINAR PROVEEDOR (ELIMINACIÓN LÓGICA)
    // =====================================================

    @Override
    @Transactional
    public void eliminar(Long id) {

        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Proveedor no encontrado."
                        )
                );

        // Evitar eliminar nuevamente un proveedor inactivo
        if (!Boolean.TRUE.equals(proveedor.getActivo())) {
            throw new IllegalArgumentException(
                    "El proveedor ya se encuentra inactivo."
            );
        }

        // Eliminación lógica
        proveedor.setActivo(false);

        proveedorRepository.save(proveedor);
    }

    // =====================================================
    // CONVERTIR ENTIDAD A RESPONSE DTO
    // =====================================================

    private ProveedorResponseDTO toResponse(Proveedor proveedor) {

        return ProveedorResponseDTO.builder()
                .id(proveedor.getId())
                .nombreRazonSocial(proveedor.getNombreRazonSocial())
                .duiNit(proveedor.getDuiNit())
                .nrc(proveedor.getNrc())
                .direccion(proveedor.getDireccion())
                .telefono(proveedor.getTelefono())
                .correo(proveedor.getCorreo())
                .build();
    }
}
package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.VehiculoRequestDTO;
import com.graspymar.ims.dto.VehiculoResponseDTO;
import com.graspymar.ims.entity.Vehiculo;
import com.graspymar.ims.repository.VehiculoRepository;
import com.graspymar.ims.service.VehiculoService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    // =====================================================
    // CREAR VEHÍCULO
    // =====================================================

    @Override
    @Transactional
    public VehiculoResponseDTO crear(VehiculoRequestDTO dto) {

        // Verificar que la placa no esté registrada
        vehiculoRepository.findByPlaca(dto.getPlaca())
                .ifPresent(vehiculo -> {
                    throw new IllegalArgumentException(
                            "Ya existe un vehículo registrado con la placa: "
                                    + dto.getPlaca()
                    );
                });

        Vehiculo vehiculo = Vehiculo.builder()
                .placa(dto.getPlaca())
                .marca(dto.getMarca())
                .modelo(dto.getModelo())
                .clase(dto.getClase())
                .tipo(dto.getTipo())
                .anio(dto.getAnio())
                .estado(dto.getEstado())
                .observaciones(dto.getObservaciones())
                .build();

        vehiculo = vehiculoRepository.save(vehiculo);

        return toResponse(vehiculo);
    }

    // =====================================================
    // LISTAR VEHÍCULOS ACTIVOS
    // =====================================================

    @Override
    @Transactional
    public List<VehiculoResponseDTO> listar() {

        return vehiculoRepository.findByActivoTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    // =====================================================
    // BUSCAR VEHÍCULO POR ID
    // =====================================================

    @Override
    @Transactional
    public VehiculoResponseDTO buscarPorId(Long id) {

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Vehículo no encontrado."
                        )
                );

        if (!Boolean.TRUE.equals(vehiculo.getActivo())) {
            throw new EntityNotFoundException(
                    "El vehículo no se encuentra activo."
            );
        }

        return toResponse(vehiculo);
    }

    // =====================================================
    // ACTUALIZAR VEHÍCULO
    // =====================================================

    @Override
    @Transactional
    public VehiculoResponseDTO actualizar(Long id, VehiculoRequestDTO dto) {

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Vehículo no encontrado."
                        )
                );

        // Impedir modificaciones a vehículos inactivos
        if (!Boolean.TRUE.equals(vehiculo.getActivo())) {
            throw new IllegalArgumentException(
                    "No se puede actualizar un vehículo inactivo."
            );
        }

        // Comprobar que la placa no pertenezca a otro vehículo
        vehiculoRepository.findByPlaca(dto.getPlaca())
                .ifPresent(existente -> {

                    if (!existente.getId().equals(id)) {
                        throw new IllegalArgumentException(
                                "La placa '" + dto.getPlaca()
                                        + "' ya pertenece a otro vehículo."
                        );
                    }
                });

        vehiculo.setPlaca(dto.getPlaca());
        vehiculo.setMarca(dto.getMarca());
        vehiculo.setModelo(dto.getModelo());
        vehiculo.setClase(dto.getClase());
        vehiculo.setTipo(dto.getTipo());
        vehiculo.setAnio(dto.getAnio());
        vehiculo.setEstado(dto.getEstado());
        vehiculo.setObservaciones(dto.getObservaciones());

        vehiculo = vehiculoRepository.save(vehiculo);

        return toResponse(vehiculo);
    }

    // =====================================================
    // ELIMINAR VEHÍCULO (ELIMINACIÓN LÓGICA)
    // =====================================================

    @Override
    @Transactional
    public void eliminar(Long id) {

        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Vehículo no encontrado."
                        )
                );

        if (!Boolean.TRUE.equals(vehiculo.getActivo())) {
            throw new IllegalArgumentException(
                    "El vehículo ya se encuentra inactivo."
            );
        }

        vehiculo.setActivo(false);

        vehiculoRepository.save(vehiculo);
    }

    // =====================================================
    // CONVERTIR ENTIDAD A RESPONSE DTO
    // =====================================================

    private VehiculoResponseDTO toResponse(Vehiculo vehiculo) {

        return VehiculoResponseDTO.builder()
                .id(vehiculo.getId())
                .placa(vehiculo.getPlaca())
                .marca(vehiculo.getMarca())
                .modelo(vehiculo.getModelo())
                .clase(vehiculo.getClase())
                .tipo(vehiculo.getTipo())
                .anio(vehiculo.getAnio())
                .estado(vehiculo.getEstado())
                .observaciones(vehiculo.getObservaciones())
                .build();
    }
}
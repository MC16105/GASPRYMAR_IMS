package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.VehiculoRequestDTO;
import com.graspymar.ims.dto.VehiculoResponseDTO;
import com.graspymar.ims.entity.Vehiculo;
import com.graspymar.ims.repository.VehiculoRepository;
import com.graspymar.ims.service.VehiculoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoServiceImpl implements VehiculoService {

    private final VehiculoRepository vehiculoRepository;

    @Override
    public VehiculoResponseDTO crear(VehiculoRequestDTO dto) {
        vehiculoRepository.findByPlaca(dto.getPlaca())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Ya existe un Vehiculo con esa Placa");
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

    @Override
    public List<VehiculoResponseDTO> listar() {
        return vehiculoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public VehiculoResponseDTO buscarPorId(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehiculo no encontrado."));
        return toResponse(vehiculo);
    }

    @Override
    public VehiculoResponseDTO actualizar(Long id, VehiculoRequestDTO dto) {
       Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehiculo no encontrado."));
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

    @Override
    public void eliminar(Long id) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Vehiculo no encontrado."));
        vehiculo.setActivo(false);
        vehiculoRepository.save(vehiculo);
    }

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

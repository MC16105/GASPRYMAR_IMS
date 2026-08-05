package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.EstanqueRequestDTO;
import com.graspymar.ims.dto.EstanqueResponseDTO;
import com.graspymar.ims.entity.Estanque;
import com.graspymar.ims.repository.EstanqueRepository;
import com.graspymar.ims.service.EstanqueService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EstanqueServiceImpl implements EstanqueService {

    private final EstanqueRepository estanqueRepository;

    @Override
    public EstanqueResponseDTO crear(EstanqueRequestDTO dto) {
        estanqueRepository.findByCodigo(dto.getCodigo())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Ya existe un Estanque con ese Codigo");
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

    @Override
    public List<EstanqueResponseDTO> listar() {
        return estanqueRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public EstanqueResponseDTO buscarPorId(Long id) {
        Estanque estanque = estanqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estanque no encontrado."));
        return toResponse(estanque);
    }

    @Override
    public EstanqueResponseDTO actualizar(Long id, EstanqueRequestDTO dto) {
        Estanque estanque = estanqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estanque no encontrado."));
        estanque.setNombre(dto.getNombre());
        estanque.setCodigo(dto.getCodigo());
        estanque.setArea(dto.getArea());
        estanque.setCapacidad(dto.getCapacidad());
        estanque.setUbicacion(dto.getUbicacion());
        estanque.setEstado(dto.getEstado());
        estanque = estanqueRepository.save(estanque);
        return toResponse(estanque);
    }

    @Override
    public void eliminar(Long id) {
        Estanque estanque = estanqueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Estanque no encontrado."));
        estanque.setActivo(false);
        estanqueRepository.save(estanque);
    }

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

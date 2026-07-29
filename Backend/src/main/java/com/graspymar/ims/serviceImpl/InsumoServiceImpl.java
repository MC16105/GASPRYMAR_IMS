package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.InsumoRequestDTO;
import com.graspymar.ims.dto.InsumoResponseDTO;
import com.graspymar.ims.entity.Insumo;
import com.graspymar.ims.repository.InsumoRepository;
import com.graspymar.ims.service.InsumoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsumoServiceImpl implements InsumoService {

    private final InsumoRepository insumoRepository;

    @Override
    public InsumoResponseDTO crear(InsumoRequestDTO dto) {
        insumoRepository.findByNombre(dto.getNombre())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Ya existe un Insumo con ese Nombre.");
                });
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

    @Override
    public List<InsumoResponseDTO> listar() {
        return insumoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public InsumoResponseDTO buscarPorId(Long id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Insumo no encontrado."));
        return toResponse(insumo);
    }

    @Override
    public InsumoResponseDTO actualizar(Long id, InsumoRequestDTO dto) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Insumo no encontrado."));
        insumo.setNombre(dto.getNombre());
        insumo.setUnidadMedida(dto.getUnidadMedida());
        insumo.setStockActual(dto.getStockActual());
        insumo.setStockMinimo(dto.getStockMinimo());
        insumo.setPrecioReferencia(dto.getPrecioReferencia());
        insumo = insumoRepository.save(insumo);
        return toResponse(insumo);
    }

    @Override
    public void eliminar(Long id) {
        Insumo insumo = insumoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Insumo no encontrado."));
        insumo.setActivo(false);
        insumoRepository.save(insumo);
    }

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

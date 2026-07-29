package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.ProveedorRequestDTO;
import com.graspymar.ims.dto.ProveedorResponseDTO;
import com.graspymar.ims.entity.Proveedor;
import com.graspymar.ims.repository.ProveedorRepository;
import com.graspymar.ims.service.ProveedorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProveedorServiceImpl implements ProveedorService {

    private final ProveedorRepository proveedorRepository;

    @Override
    public ProveedorResponseDTO crear(ProveedorRequestDTO dto) {
        proveedorRepository.findByDuiNit(dto.getDuiNit())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Ya existe un proveedor con ese NIT.");
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

    @Override
    public List<ProveedorResponseDTO> listar() {
        return proveedorRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProveedorResponseDTO buscarPorId(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado."));
        return toResponse(proveedor);
    }

    @Override
    public ProveedorResponseDTO actualizar(Long id, ProveedorRequestDTO dto) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado."));
        proveedor.setNombreRazonSocial(dto.getNombreRazonSocial());
        proveedor.setDuiNit(dto.getDuiNit());
        proveedor.setNrc(dto.getNrc());
        proveedor.setDireccion(dto.getDireccion());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setCorreo(dto.getCorreo());
        proveedor = proveedorRepository.save(proveedor);
        return toResponse(proveedor);
    }

    @Override
    public void eliminar(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Proveedor no encontrado."));
        proveedor.setActivo(false);
        proveedorRepository.save(proveedor);
    }

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

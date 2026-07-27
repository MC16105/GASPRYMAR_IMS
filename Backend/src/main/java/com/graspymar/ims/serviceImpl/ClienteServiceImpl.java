package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.ClienteRequestDTO;
import com.graspymar.ims.dto.ClienteResponseDTO;
import com.graspymar.ims.entity.Cliente;
import com.graspymar.ims.repository.ClienteRepository;
import com.graspymar.ims.service.ClienteService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService{

    private final ClienteRepository clienteRepository;

    @Override
    public ClienteResponseDTO crear(ClienteRequestDTO dto) {
        clienteRepository.findByDuiNit(dto.getDuiNit())
                .ifPresent(c -> {
                    throw new IllegalArgumentException("Ya existe un cliente con ese DUI/NIT.");
                });
        Cliente cliente = Cliente.builder()
                .nombreRazonSocial(dto.getNombreRazonSocial())
                .duiNit(dto.getDuiNit())
                .nrc(dto.getNrc())
                .direccion(dto.getDireccion())
                .telefono(dto.getTelefono())
                .correo(dto.getCorreo())
                .build();
        cliente = clienteRepository.save(cliente);
        return toResponse(cliente);
    }

    @Override
    public List<ClienteResponseDTO> listar() {
        return clienteRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado."));
        return toResponse(cliente);
    }

    @Override
    public ClienteResponseDTO actualizar(Long id, ClienteRequestDTO dto) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado."));
        cliente.setNombreRazonSocial(dto.getNombreRazonSocial());
        cliente.setDuiNit(dto.getDuiNit());
        cliente.setNrc(dto.getNrc());
        cliente.setDireccion(dto.getDireccion());
        cliente.setTelefono(dto.getTelefono());
        cliente.setCorreo(dto.getCorreo());
        cliente = clienteRepository.save(cliente);
        return toResponse(cliente);
    }

    @Override
    public void eliminar(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado."));
        cliente.setActivo(false);
        clienteRepository.save(cliente);
    }

    private ClienteResponseDTO toResponse(Cliente cliente) {
        return ClienteResponseDTO.builder()
                .id(cliente.getId())
                .nombreRazonSocial(cliente.getNombreRazonSocial())
                .duiNit(cliente.getDuiNit())
                .nrc(cliente.getNrc())
                .direccion(cliente.getDireccion())
                .telefono(cliente.getTelefono())
                .correo(cliente.getCorreo())
                .build();
    }

}
